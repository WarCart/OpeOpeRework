package net.warcar.ope_ope_rework.init;

import net.warcar.ope_ope_rework.abilities.CalmAbility;
import net.warcar.ope_ope_rework.abilities.KRoomAbility;
import net.warcar.ope_ope_rework.abilities.SilentAbility;
import net.warcar.ope_ope_rework.abilities.TrueAmputateAbility;
import xyz.pixelatedw.mineminenomi.abilities.ope.*;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityPool2;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

import java.util.Arrays;
import java.util.Objects;

public class Abilities {
    public static final AbilityPool2 ROOMS = new AbilityPool2().addFlag("ignore_cooldowns", true);
    public static final AkumaNoMiItem REAL_NAGI = registerFruit(new AkumaNoMiItem("Nagi Nagi no Mi", 1,FruitType.PARAMECIA, SilentAbility.INSTANCE, CalmAbility.INSTANCE));
    public static final AkumaNoMiItem REAL_OPE = registerFruit(new AkumaNoMiItem("Ope Ope no Mi", 2,FruitType.PARAMECIA, RoomAbility.INSTANCE, KRoomAbility.INSTANCE, TrueAmputateAbility.INSTANCE, ShamblesAbility.INSTANCE, TaktAbility.INSTANCE, InjectionShotAbility.INSTANCE, CounterShockAbility.INSTANCE, MesAbility.INSTANCE, GammaKnifeAbility.INSTANCE));
    public static void reg() {
    }

    private static <T extends AkumaNoMiItem> T registerFruit(T fruit) {
        String resourceName = WyHelper.getResourceName(fruit.getDevilFruitName());
        WyRegistry.getLangMap().put("item.mineminenomi." + resourceName, fruit.getDevilFruitName());
        WyRegistry.registerItem(fruit.getDevilFruitName(), () -> fruit);
        if (fruit.getAbilities() != null && fruit.getAbilities().length > 0) {
            registerAbilities(fruit.getAbilities());
        }

        return fruit;
    }

    private static void registerAbilities(AbilityCore[] abilities) {
        Arrays.stream(abilities).filter(Objects::nonNull).forEach(WyRegistry::registerAbility);
    }
}
