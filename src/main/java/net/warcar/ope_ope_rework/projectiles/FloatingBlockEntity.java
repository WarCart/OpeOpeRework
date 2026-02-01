package net.warcar.ope_ope_rework.projectiles;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.warcar.ope_ope_rework.init.Projectiles;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.protection.ProtectedArea;
import xyz.pixelatedw.mineminenomi.data.world.ProtectedAreasData;

import java.util.Optional;

public class FloatingBlockEntity extends Entity {
    private static final DataParameter<Optional<BlockState>> BLOCK = EntityDataManager.defineId(FloatingBlockEntity.class, DataSerializers.BLOCK_STATE);
    private static final DataParameter<BlockPos> POS = EntityDataManager.defineId(FloatingBlockEntity.class, DataSerializers.BLOCK_POS);

    private float rotationX;
    private float rotationY;
    private float rotationZ;

    private final float rotationXTick;
    private final float rotationYTick;
    private final float rotationZTick;

    public CompoundNBT blockData;
    public boolean dropItem = true;

    public FloatingBlockEntity(EntityType type, World level) {
        super(type, level);
        this.rotationXTick = getRand() * 1.5f;
        this.rotationYTick = getRand() * 1.5f;
        this.rotationZTick = getRand() * 1.5f;
    }

    private float getRand() {
        return (random.nextFloat() - 0.5f) * 2;
    }

    public FloatingBlockEntity(World worldIn, BlockPos pos) {
        this(Projectiles.FLOATING_BLOCK.get(), worldIn);
        this.setOriginPos(pos);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.setBlockState(worldIn.getBlockState(pos));
        TileEntity entity = worldIn.getBlockEntity(pos);
        if (entity != null) {
            this.blockData = entity.serializeNBT();
        }
    }

    public boolean isAttackable() {
        return false;
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public Vector3d getDeltaMovement() {
        return super.getDeltaMovement();
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(BLOCK, Optional.empty());
        this.getEntityData().define(POS, BlockPos.ZERO);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        this.setBlockState(NBTUtil.readBlockState(nbt));
        this.setOriginPos(NBTUtil.readBlockPos(nbt));
        if (nbt.contains("blockData")) {
            this.blockData = nbt.getCompound("blockData");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.put("block", NBTUtil.writeBlockState(this.getBlockState()));
        nbt.put("pos", NBTUtil.writeBlockPos(this.getOriginPos()));
        if (blockData != null) {
            nbt.put("blockData", blockData);
        }
    }

    @Override
    public void tick() {
        if (this.getBlockState().isAir() || !this.getBlockState().getFluidState().isEmpty()) {
            this.remove();
        } else {
            Block block = this.getBlockState().getBlock();

            if (!this.isNoGravity()) {
                AbilityHelper.setDeltaMovement(this, this.getDeltaMovement().add(0.0F, -0.04, 0.0F));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            if (!this.level.isClientSide) {
                BlockPos blockpos1 = this.blockPosition();
                boolean flag = this.getBlockState().getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.level.getFluidState(blockpos1).is(FluidTags.WATER);
                double d0 = this.getDeltaMovement().lengthSqr();
                if (flag && d0 > 1) {
                    BlockRayTraceResult blockraytraceresult = this.level.clip(new RayTraceContext(new Vector3d(this.xo, this.yo, this.zo), this.position(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, this));
                    if (blockraytraceresult.getType() != RayTraceResult.Type.MISS && this.level.getFluidState(blockraytraceresult.getBlockPos()).is(FluidTags.WATER)) {
                        blockpos1 = blockraytraceresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (this.isOnGround() || flag1) {
                    BlockState blockstate = this.level.getBlockState(blockpos1);
                    AbilityHelper.setDeltaMovement(this, this.getDeltaMovement().multiply(0.7, -0.5F, 0.7));
                    if (!blockstate.is(Blocks.MOVING_PISTON)) {
                        this.remove();
                        boolean flag2 = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level, blockpos1, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                        boolean flag3 = FallingBlock.isFree(this.level.getBlockState(blockpos1.below())) && (!flag || !flag1);
                        boolean flag4 = this.getBlockState().canSurvive(this.level, blockpos1) && !flag3;
                        if (flag2 && flag4) {
                            if (this.getBlockState().hasProperty(BlockStateProperties.WATERLOGGED) && this.level.getFluidState(blockpos1).getType() == Fluids.WATER) {
                                this.setBlockState(this.getBlockState().setValue(BlockStateProperties.WATERLOGGED, true));
                            }

                            if (!this.level.setBlock(blockpos1, this.getBlockState(), 3)) {
                                if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                    this.spawnAtLocation(block);
                                }
                            } else {
                                if (this.blockData != null && this.getBlockState().hasTileEntity()) {
                                    TileEntity tileentity = this.level.getBlockEntity(blockpos1);
                                    if (tileentity != null) {
                                        CompoundNBT compoundnbt = tileentity.save(new CompoundNBT());

                                        for(String s : this.blockData.getAllKeys()) {
                                            INBT inbt = this.blockData.get(s);
                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                compoundnbt.put(s, inbt.copy());
                                            }
                                        }

                                        tileentity.load(this.getBlockState(), compoundnbt);
                                        tileentity.setChanged();
                                    }
                                }

                                ProtectedArea area = ProtectedAreasData.get(this.level).getProtectedArea(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                                if (area != null) {
                                    area.queueForRestoration(this.level, blockpos1);
                                }
                            }
                        } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(block);
                        }
                    }
                }
            }

            AbilityHelper.setDeltaMovement(this, this.getDeltaMovement().scale(0.98));
        }
        this.rotationX += this.rotationXTick;
        this.rotationY += this.rotationYTick;
        this.rotationZ += this.rotationZTick;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockState getBlockState() {
        return this.entityData.get(BLOCK).orElse(Blocks.AIR.defaultBlockState());
    }

    public void setBlockState(BlockState state) {
        this.entityData.set(BLOCK, Optional.ofNullable(state));
    }

    public BlockPos getOriginPos() {
        return this.entityData.get(POS);
    }

    public void setOriginPos(BlockPos pos) {
        this.entityData.set(POS, pos);
    }

    public float getRotationX() {
        return this.rotationX;
    }

    public float getRotationY() {
        return this.rotationY;
    }

    public float getRotationZ() {
        return this.rotationZ;
    }

    public float getRotationXTick() {
        return this.rotationXTick;
    }

    public float getRotationYTick() {
        return this.rotationYTick;
    }

    public float getRotationZTick() {
        return this.rotationZTick;
    }
}
