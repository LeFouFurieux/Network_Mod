package com.example.examplemod.custom_block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.loading.targets.FMLClientLaunchHandler;

public class solar_panel extends HorizontalDirectionalBlock {
    public static VoxelShape SHAPE = Block.box(0.5,0,1.5,15,4,10);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;


    public solar_panel(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(FACING, Direction.EAST));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
            return Block.box(0,0,0,16,16,16);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING,blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(PART,BedPart.HEAD); //Get opposite direction of player
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING,rotation.rotate(blockState.getValue(FACING))); //Rotates block according to player facing direction
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING))); //Mirror block ?
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(PART);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        //BlockPos pos2 = new BlockPos(pos).east(1);

        if(state.getValue(PART) == BedPart.HEAD){
            switch (state.getValue(FACING)){
                case NORTH -> super.onDestroyedByPlayer(state, level, pos.west(1), player, willHarvest, fluid);
                case SOUTH -> super.onDestroyedByPlayer(state, level, pos.east(1), player, willHarvest, fluid);
                case EAST -> super.onDestroyedByPlayer(state, level, pos.north(1), player, willHarvest, fluid);
                case WEST -> super.onDestroyedByPlayer(state, level, pos.south(1), player, willHarvest, fluid);
            }
        }
        else{
            switch (state.getValue(FACING)){
                case NORTH -> super.onDestroyedByPlayer(state, level, pos.east(1), player, willHarvest, fluid);
                case SOUTH -> super.onDestroyedByPlayer(state, level, pos.west(1), player, willHarvest, fluid);
                case EAST -> super.onDestroyedByPlayer(state, level, pos.south(1), player, willHarvest, fluid);
                case WEST -> super.onDestroyedByPlayer(state, level, pos.north(1), player, willHarvest, fluid);
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}