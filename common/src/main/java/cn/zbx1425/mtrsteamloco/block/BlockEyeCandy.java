package cn.zbx1425.mtrsteamloco.block;

import cn.zbx1425.mtrsteamloco.Main;
import cn.zbx1425.mtrsteamloco.network.PacketScreen;
import cn.zbx1425.mtrsteamloco.render.rail.RailRenderDispatcher;
import cn.zbx1425.mtrsteamloco.render.scripting.eyecandy.EyeCandyScriptContext;
import cn.zbx1425.sowcer.math.Vector3f;
import mtr.block.IBlock;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.registry.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class BlockEyeCandy extends BlockDirectionalMapper implements EntityBlockMapper {

    public BlockEyeCandy() {
        super(BlockBehaviour.Properties.of().strength(2).noCollission());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        if (!RailRenderDispatcher.isHoldingBrush) {
            BlockEntity blockEntity = blockGetter.getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityEyeCandy blockEntityEyeCandy) {
                VoxelShape outlineShape = blockEntityEyeCandy.scriptContext.getOutlineShape();
                if (outlineShape != null) {
                    return outlineShape;
                }
            }
        }
        return super.getShape(state, blockGetter, pos, collisionContext);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        if (!RailRenderDispatcher.isHoldingBrush) {
            BlockEntity blockEntity = blockGetter.getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityEyeCandy blockEntityEyeCandy) {
                VoxelShape collisionShape = blockEntityEyeCandy.scriptContext.getCollisionShape();
                if (collisionShape != null) {
                    return collisionShape;
                }
            }
        }
        return Shapes.empty();
    }

    @Override
    public InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (player.getMainHandItem().is(Items.BRUSH.get())) {
            if (!level.isClientSide) {
                PacketScreen.sendScreenBlockS2C((ServerPlayer) player, "eye_candy", pos);
            }
            return InteractionResult.SUCCESS;
        } else {
            if (level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BlockEntityEyeCandy blockEntityEyeCandy) {
                    blockEntityEyeCandy.scriptContext.events().triggerOnBlockUse();
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityEyeCandy(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public static class BlockEntityEyeCandy extends BlockEntityClientSerializableMapper {

        public String prefabId = null;

        public float translateX = 0, translateY = 0, translateZ = 0;
        public float rotateX = 0, rotateY = 0, rotateZ = 0;

        public boolean fullLight = false;

        public EyeCandyScriptContext scriptContext = new EyeCandyScriptContext(this);

        public BlockEntityEyeCandy(BlockPos pos, BlockState state) {
            super(Main.BLOCK_ENTITY_TYPE_EYE_CANDY.get(), pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            prefabId = compoundTag.getString("prefabId");
            if (StringUtils.isEmpty(prefabId)) prefabId = null;
            fullLight = compoundTag.getBoolean("fullLight");

            translateX = compoundTag.contains("translateX") ? compoundTag.getFloat("translateX") : 0;
            translateY = compoundTag.contains("translateY") ? compoundTag.getFloat("translateY") : 0;
            translateZ = compoundTag.contains("translateZ") ? compoundTag.getFloat("translateZ") : 0;
            rotateX = compoundTag.contains("rotateX") ? compoundTag.getFloat("rotateX") : 0;
            rotateY = compoundTag.contains("rotateY") ? compoundTag.getFloat("rotateY") : 0;
            rotateZ = compoundTag.contains("rotateZ") ? compoundTag.getFloat("rotateZ") : 0;
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putString("prefabId", prefabId == null ? "" : prefabId);
            compoundTag.putBoolean("fullLight", fullLight);
            
            compoundTag.putFloat("translateX", translateX);
            compoundTag.putFloat("translateY", translateY);
            compoundTag.putFloat("translateZ", translateZ);
            compoundTag.putFloat("rotateX", rotateX);
            compoundTag.putFloat("rotateY", rotateY);
            compoundTag.putFloat("rotateZ", rotateZ);
        }

        public BlockPos getWorldPos() {
            return this.worldPosition;
        }

        public Vector3f getWorldPosVector3f() {
            return new Vector3f(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());
        }

        public Direction facing() {
            return IBlock.getStatePropertySafe(this.getBlockState(), BlockEyeCandy.FACING);
        }

        public int redstoneLevel() {
            Level level = getLevel();
            if (level == null) {
                return 0;
            }
            int highestRedstoneLevel = 0;

            for (Direction direction : Direction.values()) {
                highestRedstoneLevel = Math.max(highestRedstoneLevel, level.hasSignal(getBlockPos(), direction) ? 15 : 0);
            }
            return highestRedstoneLevel;
        }
    }
}
