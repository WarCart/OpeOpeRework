package net.warcar.ope_ope_rework.events;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityPredicate;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.abilities.KRoomAbility;
import net.warcar.ope_ope_rework.abilities.NagiHelper;
import net.warcar.ope_ope_rework.init.Abilities;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusOperation;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.util.TargetsPredicate;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = OpeReworkMod.MOD_ID)
public class AbilitiesEvents {
    private static final UUID OPE_LONG_COOLDOWNS = UUID.fromString("76389c98-d2c1-4a9f-82c7-fa185d7d7764");
    private static final UUID OTO_DAMAGE_LOSS = UUID.fromString("a48c5928-c7c2-465b-9fca-a95620c2141d");
    @SubscribeEvent
    public static void onAbilityUsed(AbilityUseEvent.Pre event) {
        RoomAbility ability = AbilityDataCapability.get(event.getEntityLiving()).getEquippedAbility(RoomAbility.INSTANCE);
        AbilityCore<?> core = event.getAbility().getCore();
        if (core != RoomAbility.INSTANCE && core != KRoomAbility.INSTANCE && Lists.newArrayList(Abilities.REAL_OPE.getAbilities()).contains(core)) {
            event.getAbility().getComponent(ModAbilityKeys.COOLDOWN).ifPresent(component -> component.getBonusManager().addBonus(OPE_LONG_COOLDOWNS, "Ope Awakening Difficulty", BonusOperation.MUL, ability != null && ability.isContinuous() ? 1 : 2));
        } else if (Lists.newArrayList(ModAbilities.OTO_OTO_NO_MI.getAbilities()).contains(core)) {
            event.getAbility().getComponent(ModAbilityKeys.DAMAGE).ifPresent(component -> component.getBonusManager().addBonus(OTO_DAMAGE_LOSS, "No Sound", BonusOperation.MUL, event.getEntityLiving().hasEffect(ModEffects.SILENT.get()) ? 0 : 1));
            event.getAbility().getComponent(ModAbilityKeys.RANGE).ifPresent(component -> {
                component.setLineCheck(TargetsPredicate.DEFAULT_LINE_CHECK.testVanilla(new EntityPredicate().selector((entity) -> NagiHelper.canHear(event.getEntityLiving(), entity))));
                component.getBonusManager().addBonus(OTO_DAMAGE_LOSS, "No Sound", BonusOperation.MUL, event.getEntityLiving().hasEffect(ModEffects.SILENT.get()) ? 0 : 1);
            });
        }
    }
}

