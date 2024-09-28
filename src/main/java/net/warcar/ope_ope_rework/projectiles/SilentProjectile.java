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
import net.minecraftforge.fml.network.NetworkHooks;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.init.Projectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.projectiles.IFlexibleSizeProjectile;

public class SilentProjectile extends Entity implements IFlexibleSizeProjectile {
    private static final DataParameter<Float> SIZE = EntityDataManager.defineId(SilentProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> OWNER = EntityDataManager.defineId(SilentProjectile.class, DataSerializers.INT);

    public SilentProjectile(EntityType type, World world) {
        super(type, world);
    }

    public SilentProjectile(World world, LivingEntity player) {
        super(Projectiles.SILENT.get(), world);
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
        this.entityData.define(OWNER, -1);
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    @Override
    public void tick() {
    }

    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
    }

    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setSize(float size) {
        this.entityData.set(SIZE, size);
    }
}
