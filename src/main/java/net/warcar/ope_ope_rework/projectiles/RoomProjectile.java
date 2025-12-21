package net.warcar.ope_ope_rework.projectiles;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.config.CommonConfig;
import net.warcar.ope_ope_rework.init.Projectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.SphereEntity;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.projectiles.IFlexibleSizeProjectile;
import xyz.pixelatedw.mineminenomi.init.ModEntities;
import xyz.pixelatedw.mineminenomi.renderers.ArenaSkybox;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class RoomProjectile extends SphereEntity implements IFlexibleSizeProjectile {
    private static final DataParameter<Integer> OWNER = EntityDataManager.defineId(RoomProjectile.class, DataSerializers.INT);
    private int maxSize = -1;
    private static final DataParameter<Float> PREVIOUS_SIZE = EntityDataManager.defineId(RoomProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TARGET = EntityDataManager.defineId(RoomProjectile.class, DataSerializers.INT);

    public RoomProjectile(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    public RoomProjectile(World world, LivingEntity player) {
        super(Projectiles.ROOM.get(), world);
        this.setColor(new Color(0, 255, 255, 50));
        this.setOwner(player);
        this.setDetailLevel(32);
        this.setAnimationSpeed(1);
    }

    public void setOwner(LivingEntity owner) {
        this.entityData.set(OWNER, owner.getId());
    }

    public LivingEntity getOwner() {
        return (LivingEntity) this.level.getEntity(this.entityData.get(OWNER));
    }

    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET, -1);
        this.entityData.define(OWNER, -1);
        this.entityData.define(PREVIOUS_SIZE, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.setPreviousSize(this.getSize());
        if (maxSize > 0 && this.getSize() != maxSize) {
            if (this.maxSize - this.getSize() < 0) {
                this.setSize((int) this.getSize() - 1);
            } else {
                this.setSize((int) this.getSize() + 1);
            }
        }
        Entity target = this.getTarget();
        if (target != null) {
            this.setPosAndOldPos(target.getX(), target.getY(), target.getZ());
            this.setPos(target.getX(), target.getY(), target.getZ());
            this.xo = target.xo;
            this.yo = target.yo;
            this.zo = target.zo;
            this.xOld = target.xOld;
            this.yOld = target.yOld;
            this.zOld = target.zOld;
        }
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

    public void setMaxSize(int size) {
        this.maxSize = size;
    }

    public float getSize() {
        return this.getRadius();
    }

    @Override
    public void setSize(float size) {
        this.setRadius(size);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ArenaSkybox getSkybox() {
        ArenaSkybox skybox = super.getSkybox();
        skybox.setRadius(MathHelper.lerp(0.05f / Minecraft.getInstance().getDeltaFrameTime(), this.getRadius(), this.getPreviousSize()));
        return skybox;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }

    public void setPreviousSize(float size) {
        this.entityData.set(PREVIOUS_SIZE, size);
    }

    public float getPreviousSize() {
        return this.entityData.get(PREVIOUS_SIZE);
    }


    public int getMaxSize() {
        return maxSize;
    }

    public enum RoomMode {
        STATIC_ROOM,
        R_ROOM
    }
}
