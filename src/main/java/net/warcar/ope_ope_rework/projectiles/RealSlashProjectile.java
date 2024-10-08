package net.warcar.ope_ope_rework.projectiles;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.warcar.ope_ope_rework.init.Effects;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.damagesource.ModIndirectEntityDamageSource;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.projectiles.ope.OpeProjectiles;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class RealSlashProjectile extends AbilityProjectileEntity {
    public RealSlashProjectile(World world, LivingEntity thrower) {
        super(OpeProjectiles.SPATIAL_SLASH.get(), world, thrower);
        this.setDamageSource((new ModIndirectEntityDamageSource("ability_projectile", this, thrower)).setProjectile());
        this.setDamage(-1);
        //this.setPhysical();
        this.setMaxLife(200);
        this.setBlocksAffectedLimit(1024);
        this.onBlockImpactEvent = this::onBlockImpact;
        this.onEntityImpactEvent = this::onEntityImpact;
    }

    private void onEntityImpact(LivingEntity entity) {
        RoomAbility a = AbilityDataCapability.get(this.getThrower()).getEquippedAbility(RoomAbility.INSTANCE);
        if (a == null || a.isPositionInRoom(entity.blockPosition())) {
            boolean hakiCondition = (double) HakiDataCapability.get(entity).getBusoshokuHakiExp() + WyHelper.randomWithRange(0, 5) >= (double)HakiDataCapability.get(this.getThrower()).getBusoshokuHakiExp();
            if (!hakiCondition) {
                entity.addEffect(new EffectInstance(Effects.SLICED.get(), 200, 0, false, false));
                LegsEntity legs = new LegsEntity(entity.level);
                legs.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                legs.setOwner(entity);
                entity.level.addFreshEntity(legs);
            }
        }
    }

    private void onBlockImpact(BlockPos blockPos) {
        RoomAbility a = AbilityDataCapability.get(this.getThrower()).getEquippedAbility(RoomAbility.INSTANCE);
        if (a == null || a.isPositionInRoom(blockPos)) {
            this.level.removeBlock(blockPos, true);
        }
    }
}
