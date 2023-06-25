package com.example.examplemod.custom_block;

import com.example.examplemod.ExampleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ethernet_wire extends Block {
    public static final DirectionProperty source1 = DirectionProperty.create("source1");
    public static final DirectionProperty dest1 = DirectionProperty.create("dest1");

    public static Block[] blockEthernet;
    public static Block[] blockWiredConnected;
    public static boolean hasSource1 = false;
    public static boolean hasDest1 = false;
    public ethernet_wire(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(source1, Direction.UP).setValue(dest1, Direction.UP));
        blockEthernet = new Block[]{this, ExampleMod.switch_block.get()};
        blockWiredConnected = new Block[]{ExampleMod.switch_block.get()};
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(pState.getValue(dest1)==Direction.UP){
            return switch (pState.getValue(source1) ){
                case NORTH -> Block.box(7,0,0,9,1,12);
                case SOUTH -> Block.box(7,0,4,9,1,16);
                case WEST ->  Block.box(0,0,7,12,1,9);
                case EAST ->  Block.box(4,0,7,16,1,9);
                default -> Block.box(7,0,1,9,1,15);
            };
        }
        else{
            if((pState.getValue(source1) == Direction.NORTH && pState.getValue(dest1) == Direction.SOUTH) ||(pState.getValue(source1) == Direction.SOUTH && pState.getValue(dest1) == Direction.NORTH)) {
                return Block.box(7,0,0,9,1,16);
            }
            else if((pState.getValue(source1) == Direction.WEST && pState.getValue(dest1) == Direction.EAST) || (pState.getValue(source1) == Direction.EAST && pState.getValue(dest1) == Direction.WEST)){
                return Block.box(0,0,7,16,1,9);
            }

            switch (pState.getValue(source1)){
                case NORTH:
                    if(pState.getValue(dest1) == Direction.WEST){
                        return Shapes.join(Block.box(7, 0, 0, 9, 0.5, 7.75), Block.box(0, 0, 7, 9, 0.5, 9), BooleanOp.OR);
                    }
                    else{
                        return Shapes.join(Block.box(7, 0, 0, 9, 0.5, 7.75), Block.box(7, 0, 7, 16, 0.5, 9), BooleanOp.OR);
                    }
                case SOUTH:
                    if(pState.getValue(dest1) == Direction.WEST){
                        return Shapes.join(Block.box(7, 0, 7.75, 9, 0.5, 16), Block.box(0, 0, 7, 9, 0.5, 9), BooleanOp.OR);
                    }
                    else{
                        return Shapes.join(Block.box(7, 0, 7, 9, 0.5, 16), Block.box(7, 0, 7, 16, 0.5, 9), BooleanOp.OR);
                    }
            }
            return Block.box(0, 0, 0, 16, 0.5, 16);
        }

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {

        if(blockPlaceContext.getPlayer().blockPosition().equals(blockPlaceContext.getClickedPos())){
            return null;
        }
        else{
            return getBlockStateToSet(blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos());
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(source1);
        pBuilder.add(dest1);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        Boolean tempBool = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        BlockState neighbour1 =level.getBlockState(pos.north(1));
        BlockState neighbour2 = level.getBlockState(pos.south(1));
        BlockState neighbour3 = level.getBlockState(pos.west(1));
        BlockState neighbour4 = level.getBlockState(pos.east(1));

        if(hasWiredConnectedProperty(neighbour1.getBlock()) && switchFacingIsCorrect(neighbour1,Direction.SOUTH)){
            level.setBlock(pos.north(1),neighbour1.setValue(switch_block.wiredConnected,0),3);
        }
        if(hasWiredConnectedProperty(neighbour2.getBlock())  && switchFacingIsCorrect(neighbour2,Direction.NORTH)){
            level.setBlock(pos.south(1),neighbour2.setValue(switch_block.wiredConnected,0),3);
        }

        if(hasWiredConnectedProperty(neighbour3.getBlock())  && switchFacingIsCorrect(neighbour3,Direction.EAST)){
            level.setBlock(pos.west(1),neighbour3.setValue(switch_block.wiredConnected,0),3);
        }

        if(hasWiredConnectedProperty(neighbour4.getBlock())  && switchFacingIsCorrect(neighbour4,Direction.WEST)){
            level.setBlock(pos.east(1),neighbour4.setValue(switch_block.wiredConnected,0),3);
        }

        return tempBool;
    }

    //onNeighborChange is not called since 1.11 so using this deprecated method for now
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        pLevel.setBlock(pPos,getBlockStateToSet(pLevel,pPos),3);
    }

    public BlockState getBlockStateToSet(Level level, BlockPos position){

        BlockState neighbour1 = level.getBlockState(position.north(1));
        BlockState neighbour2 = level.getBlockState(position.south(1));
        BlockState neighbour3 = level.getBlockState(position.west(1));
        BlockState neighbour4 = level.getBlockState(position.east(1));

        //Reset
        hasSource1 =false;
        hasDest1 = false;

        //Direction tempSource1 = blockPlaceContext.getHorizontalDirection();
        Direction tempSource1 = Direction.UP;
        Direction tempDest1 = Direction.UP;


        if(isEthernet(neighbour1.getBlock()) && switchFacingIsCorrect(neighbour1,Direction.SOUTH)){
            if(hasWiredConnectedProperty(neighbour1.getBlock())){
                level.setBlock(position.north(1),neighbour1.setValue(switch_block.wiredConnected,1),3);
            }
            hasSource1 = true;
            tempSource1 = Direction.NORTH;
        }
        if(isEthernet(neighbour2.getBlock()) && switchFacingIsCorrect(neighbour2,Direction.NORTH)){
            if(hasWiredConnectedProperty(neighbour2.getBlock())){
                level.setBlock(position.south(1),neighbour2.setValue(switch_block.wiredConnected,1),3);
            }
            if(hasSource1 && !hasDest1){
                hasDest1 = true;
                tempDest1 = Direction.SOUTH;
            }
            else{
                hasSource1 = true;
                tempSource1 = Direction.SOUTH;
            }
        }

        if(isEthernet(neighbour3.getBlock()) && switchFacingIsCorrect(neighbour3,Direction.EAST)){
            if(!hasSource1 || !hasDest1){
                if(hasWiredConnectedProperty(neighbour3.getBlock())){
                    level.setBlock(position.west(1),neighbour3.setValue(switch_block.wiredConnected,1),3);
                }
                if (hasSource1 && !hasDest1) {
                    hasDest1 = true;
                    tempDest1 = Direction.WEST;
                } else {
                    hasSource1 = true;
                    tempSource1 = Direction.WEST;
                }
            }

            else{
                if(hasWiredConnectedProperty(neighbour3.getBlock())){
                    level.setBlock(position.west(1),neighbour3.setValue(switch_block.wiredConnected,0),3);
                }
            }


        }

        if(isEthernet(neighbour4.getBlock())  && switchFacingIsCorrect(neighbour4,Direction.WEST)){
            if(!hasSource1 || !hasDest1){
                if(hasWiredConnectedProperty(neighbour4.getBlock())){
                    level.setBlock(position.east(1),neighbour4.setValue(switch_block.wiredConnected,1),3);
                }
                if (hasSource1 && !hasDest1) {
                    hasDest1 = true;
                    tempDest1 = Direction.EAST;
                } else {
                    hasSource1 = true;
                    tempSource1 = Direction.EAST;
                }
            }
            else{
                if(hasWiredConnectedProperty(neighbour4.getBlock())){
                    level.setBlock(position.east(1),neighbour4.setValue(switch_block.wiredConnected,0),3);
                }
            }
        }

        if(hasDest1){
            return this.defaultBlockState().setValue(source1,tempSource1).setValue(dest1,tempDest1);
        }
        else if(hasSource1){
            return this.defaultBlockState().setValue(source1,tempSource1).setValue(dest1,tempDest1);
        }
        else{
            return this.defaultBlockState().setValue(source1,tempSource1).setValue(dest1,tempDest1);
        }
    }

    public Boolean isEthernet(Block block){
        for (Object obj: blockEthernet) {
            if(obj == block){
                return true;
            }
        }
        return false;
    }

    public boolean hasWiredConnectedProperty(Block block){
        for (Object obj: blockWiredConnected) {
            if(obj == block){
                return true;
            }
        }
        return false;
    }

    public Boolean switchFacingIsCorrect(@NotNull BlockState blockstate, Direction direction){
        if(blockstate.getBlock() == ExampleMod.switch_block.get() && blockstate.getValue(switch_block.FACING) != direction){
            return false;
        }
        return true;
    }
}
