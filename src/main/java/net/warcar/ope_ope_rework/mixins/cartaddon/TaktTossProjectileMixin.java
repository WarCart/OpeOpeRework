package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.entities.projectiles.opeextra.TaktEmergenceProjectile;
import net.MrMagicalCart.cartaddon.entities.projectiles.opeextra.TaktTossProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

@Mixin(TaktTossProjectile.class)
public abstract class TaktTossProjectileMixin extends AbilityProjectileEntity {
    @Shadow private boolean tossed;

    private TaktTossProjectileMixin(EntityType type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (!this.tossed) {
                if (this.getThrower() != null && this.tickCount > 0 && this.getThrower().tickCount > 0) {
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
            } else {
                this.setGravity(0.025F);
            }
        }
    }
}
