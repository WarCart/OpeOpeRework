package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.entities.projectiles.opeextra.ReworkedSpatialSlashProjectile;
import net.MrMagicalCart.cartaddon.entities.projectiles.opeextra.TaktEmergenceProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

@Mixin(ReworkedSpatialSlashProjectile.class)
public abstract class ReworkedSpatialSlashProjectileMixin extends AbilityProjectileEntity {
    private ReworkedSpatialSlashProjectileMixin(EntityType type, World world) {
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

    @Inject(method = "onBlockImpactEvent", at = @At("HEAD"), remap = false, cancellable = true)
    public void onBlockImpactEvent(CallbackInfo ci) {
        RoomAbility a = AbilityDataCapability.get(this.getThrower()).getEquippedAbility(RoomAbility.INSTANCE);
        if (a != null && !a.isPositionInRoom(this.blockPosition())) {
            ci.cancel();
        }
    }

    @Inject(method = "onEntityImpact", at = @At("HEAD"), remap = false, cancellable = true)
    public void onEntityImpactEvent(CallbackInfo ci) {
        RoomAbility a = AbilityDataCapability.get(this.getThrower()).getEquippedAbility(RoomAbility.INSTANCE);
        if (a != null && !a.isPositionInRoom(this.blockPosition())) {
            ci.cancel();
        }
    }
}
