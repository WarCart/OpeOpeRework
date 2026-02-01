package net.warcar.ope_ope_rework.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.warcar.ope_ope_rework.OpeReworkMod;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AbilityComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AbilityComponentKey;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusManager;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusOperation;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

public class SBonusManagerUpdatePacket {
    private int entityId;
    private int abilityId;
    private AbilityComponentKey<?> key;
    private Map<UUID, BonusManager.BonusValue> bonuses;

    public SBonusManagerUpdatePacket() {
    }

    public SBonusManagerUpdatePacket(LivingEntity entity, IAbility ability, AbilityComponent comp, Map<UUID, BonusManager.BonusValue> bonuses) {
        this.entityId = entity.getId();
        this.abilityId = AbilityDataCapability.get(entity).getEquippedAbilitySlot(ability);
        this.key = comp.getKey();
        this.bonuses = bonuses;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.abilityId);
        buffer.writeResourceLocation(this.key.getId());
        int size = this.bonuses.size();
        buffer.writeInt(size);
        this.bonuses.forEach((id, val) -> {
            buffer.writeUUID(id);
            int nameLen = val.getName().length();
            buffer.writeInt(nameLen);
            buffer.writeUtf(val.getName(), nameLen);
            buffer.writeInt(val.getType().ordinal());
            buffer.writeFloat(val.getValue());
        });
    }

    public static SBonusManagerUpdatePacket decode(PacketBuffer buffer) {
        SBonusManagerUpdatePacket msg = new SBonusManagerUpdatePacket();
        msg.entityId = buffer.readInt();
        msg.abilityId = buffer.readInt();
        msg.key = new AbilityComponentKey<>(buffer.readResourceLocation());
        int size = buffer.readInt();
        msg.bonuses = new HashMap<>();

        for(int i = 0; i < size; ++i) {
            UUID id = buffer.readUUID();
            int nameLen = buffer.readInt();
            String name = buffer.readUtf(nameLen);
            BonusOperation op = BonusOperation.values()[buffer.readInt()];
            float value = buffer.readFloat();
            msg.bonuses.put(id, new BonusManager.BonusValue(name, op, value));
        }

        return msg;
    }

    public static void handle(SBonusManagerUpdatePacket message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> SBonusManagerUpdatePacket.ClientHandler.handle(message));
        }

        ctx.get().setPacketHandled(true);
    }

    public static class ClientHandler {
        @OnlyIn(Dist.CLIENT)
        public static void handle(SBonusManagerUpdatePacket message) {
            Entity target = Minecraft.getInstance().level.getEntity(message.entityId);
            if (target != null && target instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity)target;
                IAbilityData props = AbilityDataCapability.get(entity);
                IAbility abl = props.getEquippedAbility(message.abilityId);
                if (abl != null) {
                    abl.getComponent(message.key).ifPresent((comp) -> comp.getBonusManagers().forEachRemaining((manager) -> manager.setBonusMap(message.bonuses)));
                }
            }
        }
    }
}
