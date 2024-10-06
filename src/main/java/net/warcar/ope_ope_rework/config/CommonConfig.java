package net.warcar.ope_ope_rework.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.warcar.ope_ope_rework.OpeReworkMod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = OpeReworkMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonConfig {
    public static final CommonConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;
    private final ForgeConfigSpec.BooleanValue abilitiesOutside;
    private final ForgeConfigSpec.IntValue maxRoomSize;
    private final ForgeConfigSpec.BooleanValue visibleRoom;
    private final ForgeConfigSpec.BooleanValue visibleSilent;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        this.abilitiesOutside = builder.comment("Allows to use ope abilities outside of room after awakening \nDefault: true").define("Outside Ope abilities", true);
        this.maxRoomSize = builder.comment("Defines Maximum size room can get \nDefault: 45").defineInRange("Max Room Size", 45, 8, 250);
        this.visibleRoom = builder.comment("Only Ope User can see own room \nDefault: false").define("Invisible Room", false);
        this.visibleSilent = builder.comment("Only Nagi User can see own silent area \nDefault: true").define("Invisible Silent", true);
    }

    public boolean isOutsideAbilities() {
        return this.abilitiesOutside.get();
    }

    static {
        Pair<CommonConfig, ForgeConfigSpec> pair = (new ForgeConfigSpec.Builder()).configure(CommonConfig::new);
        SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public boolean getInvisibleRoom() {
        return visibleRoom.get();
    }

    public boolean getInvisibleSilent() {
        return visibleSilent.get();
    }

    public int getMaxRoomSize() {
        return maxRoomSize.get();
    }
}
