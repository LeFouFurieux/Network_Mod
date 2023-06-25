package com.example.examplemod.custom_block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class switch_block extends HorizontalDirectionalBlock {
    public static VoxelShape SHAPE = Block.box(0.5,0,1.5,15,4,10);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    //public static  final BooleanProperty isEthernet = BooleanProperty.create("is_ethernet");

    public static IntegerProperty wiredConnected = IntegerProperty.create("wired_connected",0,3);

    public switch_block(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(FACING)){
            case NORTH -> Block.box(0.5,0,1.5,15.5,4,12);
            case SOUTH -> Block.box( 0.5,0,4,15.5,4,14.5);
            case WEST ->  Block.box(1.5,0,0.5,12,4,15.5);
            case EAST ->  Block.box( 4,0,0.5,14.5,4,15.5);
            default -> Block.box(1.5,0,0.5,12,4,15.5);
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING,blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(wiredConnected,0); //Get opposite direction of player
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING,rotation.rotate(blockState.getValue(FACING)));  //Rotates block according to player facing direction
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));  //Mirror block ?
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(wiredConnected);
    }
}
