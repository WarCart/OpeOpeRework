package net.warcar.ope_ope_rework.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.config.CommonConfig;
import net.warcar.ope_ope_rework.init.Projectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.projectiles.IFlexibleSizeProjectile;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class RoomProjectile extends Entity implements IFlexibleSizeProjectile {
    private static final DataParameter<Float> SIZE = EntityDataManager.defineId(RoomProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> OWNER = EntityDataManager.defineId(RoomProjectile.class, DataSerializers.INT);
    private int maxSize = -1;
    private static final DataParameter<Integer> TARGET = EntityDataManager.defineId(RoomProjectile.class, DataSerializers.INT);

    public RoomProjectile(EntityType type, World world) {
        super(type, world);
    }

    public RoomProjectile(World world, LivingEntity player) {
        super(Projectiles.ROOM.get(), world);
        this.setOwner(player);
    }

    public void setOwner(LivingEntity owner) {
        this.entityData.set(OWNER, owner.getId());
    }

    public LivingEntity getOwner() {
        return (LivingEntity) this.level.getEntity(this.entityData.get(OWNER));
    }

    public void defineSynchedData() {
        this.entityData.define(SIZE, 1f);
        this.entityData.define(TARGET, -1);
        this.entityData.define(OWNER, -1);
    }

    @Override
    public void tick() {
        if (maxSize > 0 && this.getSize() != maxSize) {
            if (this.maxSize - this.getSize() < 0) {
                this.setSize((int) this.getSize() - 1);
            } else {
                this.setSize((int) this.getSize() + 1);
            }
        }
        Entity target = this.getTarget();
        if (target != null) {
            this.teleportTo(target.getX(), target.getY() - this.getSize(), target.getZ());
        }
    }

    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }

    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    public Entity getTarget() {
        return this.level.getEntity(this.entityData.get(TARGET));
    }

    public void setTarget(Entity entity) {
        this.setTarget(entity.getId());
    }

    public void setTarget(int id) {
        this.entityData.set(TARGET, id);
    }

    public void setSize(float size) {
        this.entityData.set(SIZE, size);
    }

    public void setMaxSize(int size) {
        this.maxSize = size;
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }


    public int getMaxSize() {
        return maxSize;
    }

    public enum RoomMode {
        STATIC_ROOM,
        R_ROOM
    }
}
