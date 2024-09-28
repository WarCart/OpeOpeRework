package net.warcar.ope_ope_rework.events;

import com.google.common.collect.Lists;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.abilities.KRoomAbility;
import net.warcar.ope_ope_rework.init.Abilities;
import xyz.pixelatedw.mineminenomi.abilities.ope.OpeHelper;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusManager;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusOperation;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = OpeReworkMod.MOD_ID)
public class LongerCooldownsEvents {
    private static final UUID uuid = UUID.fromString("dd80c59d-6748-47ff-a5c9-6c2476c6102c");
    @SubscribeEvent
    public static void onAbilityUsed(AbilityUseEvent event) {
        RoomAbility ability = AbilityDataCapability.get(event.getEntityLiving()).getEquippedAbility(RoomAbility.INSTANCE);
        AbilityCore<?> core = event.getAbility().getCore();
        if (core != RoomAbility.INSTANCE && core != KRoomAbility.INSTANCE && Lists.newArrayList(Abilities.REAL_OPE.getAbilities()).contains(core)) {
            event.getAbility().getComponent(ModAbilityKeys.COOLDOWN).ifPresent(component -> component.getBonusManager().addBonus(uuid, "Ope Awakening Difficulty", BonusOperation.MUL, ability != null && ability.isContinuous() ? 1 : 2));
        }
    }
}
