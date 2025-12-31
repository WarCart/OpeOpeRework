package net.warcar.ope_ope_rework.events;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.abilities.KRoomAbility;
import net.warcar.ope_ope_rework.abilities.NagiHelper;
import net.warcar.ope_ope_rework.init.Abilities;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusManager;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusOperation;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.util.TargetsPredicate;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SSyncAbilityPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.util.UUID;

@Mod.EventBusSubscriber
public class AbilitiesEvents {
    private static final UUID OPE_LONG_COOLDOWNS = UUID.fromString("76389c98-d2c1-4a9f-82c7-fa185d7d7764");
    private static final UUID OTO_DAMAGE_LOSS = UUID.fromString("a48c5928-c7c2-465b-9fca-a95620c2141d");
    @SubscribeEvent
    public static void onAbilityUsed(AbilityUseEvent.Pre event) {
        LivingEntity living = event.getEntityLiving();
        IAbilityData data = AbilityDataCapability.get(living);
        RoomAbility roomAbility = data.getEquippedAbility(RoomAbility.INSTANCE);
        IAbility eventAbility = event.getAbility();
        AbilityCore<?> core = eventAbility.getCore();
        if (core != RoomAbility.INSTANCE && core != KRoomAbility.INSTANCE && Lists.newArrayList(Abilities.REAL_OPE.getAbilities()).contains(core)) {
            eventAbility.getComponent(ModAbilityKeys.COOLDOWN).ifPresent(component -> {
                BonusManager manager = component.getBonusManager();
                manager.removeBonus(OPE_LONG_COOLDOWNS);
                if (roomAbility == null || !roomAbility.isContinuous()) {
                    manager.addBonus(OPE_LONG_COOLDOWNS, "Ope Awakening Difficulty", BonusOperation.MUL,2);
                }
            });
            WyNetwork.sendToAllTrackingAndSelf(new SSyncAbilityPacket(living.getId(), eventAbility), living);
            WyNetwork.sendToAllTrackingAndSelf(new SSyncAbilityDataPacket(living.getId(), data), living);
        } else if (Lists.newArrayList(ModAbilities.OTO_OTO_NO_MI.getAbilities()).contains(core)) {
            eventAbility.getComponent(ModAbilityKeys.DAMAGE).ifPresent(component -> component.getBonusManager().addBonus(OTO_DAMAGE_LOSS, "No Sound", BonusOperation.MUL, living.hasEffect(ModEffects.SILENT.get()) ? 0 : 1));
            eventAbility.getComponent(ModAbilityKeys.RANGE).ifPresent(component -> {
                component.setLineCheck(TargetsPredicate.DEFAULT_LINE_CHECK.testVanilla(new EntityPredicate().selector((entity) -> NagiHelper.canHear(living, entity))));
                BonusManager manager = component.getBonusManager();
                manager.removeBonus(OTO_DAMAGE_LOSS);
                manager.addBonus(OTO_DAMAGE_LOSS, "No Sound", BonusOperation.MUL, living.hasEffect(ModEffects.SILENT.get()) ? 0 : 1);
            });
        }
    }
}

