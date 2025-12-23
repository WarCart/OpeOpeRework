package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedOpeHelper;
import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedRoomAbility;
import net.MrMagicalCart.cartaddon.abilities.opeextra.TaktEmergenceAbility;
import net.MrMagicalCart.cartaddon.init.CartParticleEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

@Mixin(TaktEmergenceAbility.class)
public abstract class TaktEmergenceMixin implements IAbility {
    @Shadow private Vector3d targetPos;

    @Shadow @Final private ChargeComponent chargeComponent;

    @Inject(method = "duringChargeEvent", at = @At("HEAD"), cancellable = true, remap = false)
    public void duringChargeEvent(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        if (!entity.level.isClientSide && ReworkedOpeHelper.hasRoomActive(entity, this).isFail()) {
            this.chargeComponent.stopCharging(entity);
        }

        RoomAbility abl = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        if (this.targetPos == null) {
            int roomSize = abl == null ? 100 : abl.getROOMSize();
            RayTraceResult mop = WyHelper.rayTraceBlocksAndEntities(entity, roomSize);
            double i = mop.getLocation().x;
            double k = mop.getLocation().z;
            int y = entity.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int)i, (int)k);
            this.targetPos = new Vector3d(i, y, k);
        }

        if (!entity.level.isClientSide) {
            WyHelper.spawnParticleEffect(CartParticleEffects.TAKT_EMERGENCE_IDLE.get(), entity, this.targetPos.x, this.targetPos.y, this.targetPos.z);
        }

        entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 2, 1, false, false));
    }
}
