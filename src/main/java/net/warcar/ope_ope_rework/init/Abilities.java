package net.warcar.ope_ope_rework.init;

import net.MrMagicalCart.cartaddon.abilities.opeextra.*;
import net.MrMagicalCart.cartaddon.init.ReworkAbilities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.abilities.*;
import xyz.pixelatedw.mineminenomi.abilities.ope.*;
import xyz.pixelatedw.mineminenomi.api.ModRegistries;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityPool2;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

import java.util.Arrays;
import java.util.Objects;

public class Abilities {
    public static final DeferredRegister<AbilityCore<?>> ABILITIES = DeferredRegister.create(ModRegistries.ABILITIES, OpeReworkMod.MOD_ID);

    public static final AbilityPool2 ROOMS = new AbilityPool2().addFlag("ignore_cooldowns", true);
    public static final AkumaNoMiItem REAL_NAGI = registerFruit(new AkumaNoMiItem("Nagi Nagi no Mi", 1,FruitType.PARAMECIA, SilentAbility.INSTANCE, CalmAbility.INSTANCE));
    public static final AkumaNoMiItem REAL_OPE = registerFruit(new AkumaNoMiItem("Ope Ope no Mi", 2, FruitType.PARAMECIA, opeAbilities()));
    public static void reg(IEventBus bus) {
        ABILITIES.register(bus);
    }

    private static AbilityCore<?>[] opeAbilities() {
        if (OpeReworkMod.isCartAddonLoaded()) {
            if (WyHelper.isAprilFirst()) {
                return new AbilityCore[]{RoomAbility.INSTANCE, ReworkedShamblesAbility.INSTANCE, ReworkedTaktAbility.INSTANCE, ReworkedInjectionShotAbility.INSTANCE, ReworkedCounterShockAbility.INSTANCE, ReworkedMesAbility.INSTANCE, ReworkedGammaKnifeAbility.INSTANCE, ReworkedAmputateAbility.INSTANCE, TaktEmergenceAbility.INSTANCE, FuroShujutsuAbility.INSTANCE, RadioKnifeAbility.INSTANCE, CurtainAbility.INSTANCE, TaktTossAbility.INSTANCE, JinkakuIshokuShujutsuAbility.INSTANCE, KRoomAbility.INSTANCE};
            } else {
                return new AbilityCore[]{RoomAbility.INSTANCE, ReworkedShamblesAbility.INSTANCE, ReworkedTaktAbility.INSTANCE, ReworkedInjectionShotAbility.INSTANCE, ReworkedCounterShockAbility.INSTANCE, ReworkedMesAbility.INSTANCE, ReworkedGammaKnifeAbility.INSTANCE, ReworkedAmputateAbility.INSTANCE, TaktEmergenceAbility.INSTANCE, PerennialYouthOperationAbility.INSTANCE, RadioKnifeAbility.INSTANCE, CurtainAbility.INSTANCE, TaktTossAbility.INSTANCE, JinkakuIshokuShujutsuAbility.INSTANCE, KRoomAbility.INSTANCE};
            }
        } else {
            if (WyHelper.isAprilFirst()) {
                return new AbilityCore[]{RoomAbility.INSTANCE, KRoomAbility.INSTANCE, ShamblesAbility.INSTANCE, TaktAbility.INSTANCE, InjectionShotAbility.INSTANCE, CounterShockAbility.INSTANCE, MesAbility.INSTANCE, GammaKnifeAbility.INSTANCE, FuroShujutsuAbility.INSTANCE};
            } else {
                return new AbilityCore[]{RoomAbility.INSTANCE, KRoomAbility.INSTANCE, ShamblesAbility.INSTANCE, TaktAbility.INSTANCE, InjectionShotAbility.INSTANCE, CounterShockAbility.INSTANCE, MesAbility.INSTANCE, GammaKnifeAbility.INSTANCE};
            }
        }
    }

    private static <T extends AkumaNoMiItem> AkumaNoMiItem registerFruit(T fruit) {
        String resourceName = WyHelper.getResourceName(fruit.getDevilFruitName());
        WyRegistry.getLangMap().put("item.mineminenomi." + resourceName, fruit.getDevilFruitName());
        WyRegistry.registerItem(fruit.getDevilFruitName(), () -> fruit);
        if (fruit.getAbilities() != null && fruit.getAbilities().length > 0) {
            registerAbilities(fruit.getAbilities());
        }

        return fruit;
    }

    private static void registerAbilities(AbilityCore[] abilities) {
        Arrays.stream(abilities).filter(Objects::nonNull).forEach(Abilities::registerAbility);
    }



    public static <T extends IAbility> AbilityCore<T> registerAbility(AbilityCore<T> core) {
        String resourceName = WyHelper.getResourceName(core.getId());
        ResourceLocation key = new ResourceLocation("mineminenomi", resourceName);
        RegistryObject<AbilityCore<?>> ret = RegistryObject.of(key, ModRegistries.ABILITIES);
        if (!ABILITIES.getEntries().contains(ret)) {
            ABILITIES.register(resourceName, () -> core);
            if (core.getIcon() == null) {
                core.setIcon(new ResourceLocation(key.getNamespace(), "textures/abilities/" + key.getPath() + ".png"));
            }
        }
        return core;
    }
}
