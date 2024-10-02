package net.warcar.ope_ope_rework.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.warcar.ope_ope_rework.init.Effects;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.entities.BottomHalfBodyEntity;
import xyz.pixelatedw.mineminenomi.entities.mobs.OPEntity;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEntities;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class LegsEntity extends OPEntity {
    private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.defineId(LegsEntity.class, DataSerializers.OPTIONAL_UUID);

    public LegsEntity(EntityType type, World world) {
        super(type, world);
    }

    public LegsEntity(World world) {
        this(ModEntities.BOTTOM_HALF_BODY.get(), world);
    }

    public void setOwner(LivingEntity owner) {
        this.entityData.set(OWNER, Optional.of(owner.getUUID()));
        EntityStatsCapability.get(this).setFaction(EntityStatsCapability.get(owner).getFaction());
        EntityStatsCapability.get(this).setRace(EntityStatsCapability.get(owner).getRace());
        ForgeRegistries.ATTRIBUTES.forEach((attr) -> {
            if (owner.getAttribute(attr) != null && this.getAttribute(attr) != null) {
                double val = owner.getAttribute(attr).getBaseValue();
                this.getAttribute(attr).setBaseValue(val);
            }

        });
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896);
        this.setHealth(owner.getHealth());
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.getEntityData().get(OWNER).isPresent() ? (UUID)((Optional)this.getEntityData().get(OWNER)).get() : null;
    }

    public LivingEntity getOwner() {
        if (this.level.isClientSide) {
            return null;
        } else {
            Optional<UUID> ownerUUID = this.getEntityData().get(OWNER);
            if (ownerUUID.isPresent()) {
                PlayerEntity playerOwner = this.level.getPlayerByUUID(ownerUUID.get());
                if (playerOwner != null) {
                    return playerOwner;
                }

                Entity entityOwner = ((ServerWorld)this.level).getEntity(ownerUUID.get());
                if (entityOwner != null && entityOwner instanceof LivingEntity) {
                    return (LivingEntity)entityOwner;
                }
            }

            return null;
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        LivingEntity owner = this.getOwner();
        if (owner == null) {
            return false;
        } else {
            owner.hurt(ModDamageSource.OUT_OF_BODY, amount);
            this.setHealth(owner.getHealth());
            return false;
        }
    }

    public void die(DamageSource cause) {
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            owner.hurt(ModDamageSource.OUT_OF_BODY, owner.getMaxHealth());
        }
    }

    public void tick() {
        if (!this.level.isClientSide) {
            LivingEntity owner = this.getOwner();
            if (owner == null || !owner.isAlive() || !owner.hasEffect(Effects.SLICED.get())) {
                this.remove();
                return;
            }
        }
        super.tick();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(OWNER, Optional.empty());
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return OPEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.30000001192092896);
    }

    public boolean save(CompoundNBT compound) {
        if (this.getEntityData().get(OWNER).isPresent()) {
            compound.putString("OwnerUUID", ((Optional)this.getEntityData().get(OWNER)).get().toString());
        }

        return super.save(compound);
    }

    public void load(CompoundNBT compound) {
        super.load(compound);
        this.getEntityData().set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
    }

}
