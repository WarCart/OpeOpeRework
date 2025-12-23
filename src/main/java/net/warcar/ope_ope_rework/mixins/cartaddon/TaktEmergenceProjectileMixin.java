package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedRoomAbility;
import net.MrMagicalCart.cartaddon.entities.projectiles.opeextra.TaktEmergenceProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

@Mixin(TaktEmergenceProjectile.class)
public abstract class TaktEmergenceProjectileMixin extends AbilityProjectileEntity {
    private TaktEmergenceProjectileMixin(EntityType type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getThrower() != null && this.tickCount > 0 && this.getThrower().tickCount > 0) {
            if (DevilFruitCapability.get(this.getThrower()).hasAwakenedFruit()) {
                return;
            }
            RoomAbility a = AbilityDataCapability.get(this.getThrower()).getEquippedAbility(RoomAbility.INSTANCE);
            if (a == null) {
                this.remove();
                return;
            }

            if (!a.isPositionInRoom(this.blockPosition())) {
                this.remove();
            }
        }
    }
}
