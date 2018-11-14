package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SandLayerBlock extends Block implements IBucketPickupHandler, ILiquidContainer {
    private static final VoxelShape[] VOXEL_SHAPES = {
        ShapeUtils.empty(),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
        Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public SandLayerBlock(final Block material) {
        super(Builder.from(material));
        this.setDefaultState();
    }

    @Override
    public Fluid pickupFluid(final IWorld world, final BlockPos position, final IBlockState state) {
        if (state.get(BlockStateProperties.WATERLOGGED)) {
            world.setBlockState(position, state.with(BlockStateProperties.WATERLOGGED, false), 3);
            return Fluids.WATER;
        }

        return Fluids.EMPTY;
    }

    @Override
    public boolean canContainFluid(final IBlockReader reader, final BlockPos position, final IBlockState state, final Fluid fluid) {
        return Fluids.WATER == fluid && !state.get(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public boolean receiveFluid(final IWorld world, final BlockPos position, final IBlockState state, final IFluidState fluidState) {
        if (Fluids.WATER == fluidState.getFluid() && !state.get(BlockStateProperties.WATERLOGGED)) {
            if (!world.isRemote()) {
                world.setBlockState(position, state.with(BlockStateProperties.WATERLOGGED, true), 3);
                world.getPendingFluidTicks().scheduleTick(position, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }

            return true;
        }

        return false;
    }

    @Override
    @Deprecated
    public IBlockState updatePostPlacement(final IBlockState state, final EnumFacing face, final IBlockState other, final IWorld world, final BlockPos position, final BlockPos offset) {
        if (state.isValidPosition(world, position)) {
            if (state.get(BlockStateProperties.WATERLOGGED)) {
                world.getPendingFluidTicks().scheduleTick(position, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }

            return super.updatePostPlacement(state, face, other, world, position, offset);
        }

        return Blocks.AIR.getDefaultState();
    }

    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return 8 == state.get(BlockStateProperties.LAYERS_1_8);
    }

    @Override
    @Deprecated
    public boolean allowsMovement(final IBlockState state, final IBlockReader reader, final BlockPos position, final PathType pathType) {
        return PathType.LAND == pathType && state.get(BlockStateProperties.LAYERS_1_8) < 5;
    }

    @Override
    @Deprecated
    public boolean isReplaceable(final IBlockState state, final BlockItemUseContext context) {
        final int layers = state.get(BlockStateProperties.LAYERS_1_8);

        if (this.asItem() != context.getItem().getItem() || 8 <= layers) {
            return 1 == layers;
        }

        if (context.func_196012_c()) {
            return EnumFacing.UP == context.getFace();
        }

        return true;
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockReader reader, final IBlockState state, final BlockPos position, final EnumFacing face) {
        return EnumFacing.DOWN == face ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    @Deprecated
    public VoxelShape getShape(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        return SandLayerBlock.VOXEL_SHAPES[state.get(BlockStateProperties.LAYERS_1_8)];
    }

    @Override
    @Deprecated
    public VoxelShape getCollisionShape(final IBlockState state, final IBlockReader reader, final BlockPos position) {
        return SandLayerBlock.VOXEL_SHAPES[state.get(BlockStateProperties.LAYERS_1_8) - 1];
    }

    @Override
    public IItemProvider getItemDropped(final IBlockState state, final World world, final BlockPos position, final int fortune) {
        return Items.AIR;
    }

    @Override
    @Deprecated
    public boolean isValidPosition(final IBlockState state, final IWorldReaderBase reader, final BlockPos position) {
        final BlockPos down = position.down();
        final IBlockState below = reader.getBlockState(down);
        final BlockFaceShape shape = below.getBlockFaceShape(reader, down, EnumFacing.UP);

        if (BlockFaceShape.SOLID == shape) {
            return true;
        }

        if (this == below.getBlock()) {
            return below.get(BlockStateProperties.LAYERS_1_8) == 8;
        }

        return false;
    }

    @Override
    @Nullable
    public IBlockState getStateForPlacement(final BlockItemUseContext context) {
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        final IBlockState state = world.getBlockState(pos);

        if (this == state.getBlock()) {
            final int oldLayers = state.get(BlockStateProperties.LAYERS_1_8);
            final int newLayers = Math.min(8, oldLayers + 1);

            return state.with(BlockStateProperties.LAYERS_1_8, newLayers);
        }

        final IFluidState fluidState = world.getFluidState(pos);
        final boolean waterlogged = Fluids.WATER == fluidState.getFluid();

        return this.getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    @Override
    public void harvestBlock(final World world, final EntityPlayer player, final BlockPos position, final IBlockState state, @Nullable final TileEntity entity, final ItemStack stack) {
        player.addStat(StatList.BLOCK_MINED.get(this));
        player.addExhaustion(0.005F);

        final int layers = state.get(BlockStateProperties.LAYERS_1_8);

        if (8 == layers) {
            Block.spawnAsEntity(world, position, new ItemStack(Blocks.SAND));
            return;
        }

        for (int layer = 0; layer < layers; ++layer) {
            Block.spawnAsEntity(world, position, this.getSilkTouchDrop(state));
        }
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    @Deprecated
    public IFluidState getFluidState(final IBlockState state) {
        if (state.get(BlockStateProperties.WATERLOGGED)) {
            return Fluids.WATER.getStillFluidState(false);
        }

        return super.getFluidState(state);
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(BlockStateProperties.LAYERS_1_8);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    private void setDefaultState() {
        IBlockState state = this.getStateContainer().getBaseState();

        state = state.with(BlockStateProperties.LAYERS_1_8, 1);
        state = state.with(BlockStateProperties.WATERLOGGED, false);

        this.setDefaultState(state);
    }
}
