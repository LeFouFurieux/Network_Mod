package com.example.examplemod.custom_block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.small_foundry_entity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class blue_ethernet extends Block {

    public int currentDirectionIndex = 0;
    public static final DirectionProperty source1 = DirectionProperty.create("source1");
    public static final DirectionProperty dest1 = DirectionProperty.create("dest1");
    public static final BooleanProperty source1_connected = BooleanProperty.create("source1_connected");
    public static final BooleanProperty dest1_connected = BooleanProperty.create("dest1_connected");
    public static Block[] blockEthernet;
    public static Block[] blockWiredConnected;

    Direction[] possibleDirections = {
          Direction.NORTH, Direction.SOUTH,Direction.WEST,Direction.EAST
    };
    public blue_ethernet(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(source1, Direction.UP)
                .setValue(dest1, Direction.UP)
                .setValue(source1_connected,false)
                .setValue(dest1_connected,false));

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
        for( int i =0; i<possibleDirections.length-1;i++ ){
            if(possibleDirections[i] == blockPlaceContext.getHorizontalDirection().getOpposite()){
                currentDirectionIndex = i;
                break;
            }

        }
        return this.defaultBlockState()
                .setValue(source1,blockPlaceContext.getHorizontalDirection())
                .setValue(dest1,blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()){
            do{
                if(currentDirectionIndex < possibleDirections.length -1){
                    currentDirectionIndex++;
                }
                else{
                    currentDirectionIndex = 0;
                }
            }
            while(possibleDirections[currentDirectionIndex] == pState.getValue(source1));

            pLevel.setBlock(pPos,pState.setValue(dest1,possibleDirections[currentDirectionIndex]),3);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(source1);
        pBuilder.add(dest1);
        pBuilder.add(source1_connected);
        pBuilder.add(dest1_connected);
    }

}
