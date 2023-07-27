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

    public int currentDirectionIndexDest1 = 0;
    public int currentDirectionIndexDest2 = 0;
    public int currentWire = 0;
    public static final DirectionProperty source1 = DirectionProperty.create("source1");
    public static final DirectionProperty source2 = DirectionProperty.create("source2");
    public static final DirectionProperty dest1 = DirectionProperty.create("dest1");
    public static final DirectionProperty dest2 = DirectionProperty.create("dest2");

    Direction[] possibleDirections = {
          Direction.NORTH, Direction.SOUTH,Direction.WEST,Direction.EAST
    };
    public blue_ethernet(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(source1, Direction.UP)
                .setValue(dest1, Direction.UP)
                .setValue(source2, Direction.UP)
                .setValue(dest2, Direction.UP));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
            return Block.box(0, 0, 0, 16, 0.5, 16);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        for( int i =0; i<possibleDirections.length-1;i++ ){
            if(possibleDirections[i] == blockPlaceContext.getHorizontalDirection().getOpposite()){
                currentDirectionIndexDest1 = i;
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
            if(pState.getValue(dest2) == Direction.UP && pPlayer.getItemInHand(pHand).getItem() == ExampleMod.BLUE_ETHERNET_ITEM.get()){
                pLevel.setBlock(pPos,pState
                        .setValue(source2,pPlayer.getMotionDirection())
                        .setValue(dest2,pPlayer.getMotionDirection().getOpposite())
                        ,3);
            }

            else if(pPlayer.isShiftKeyDown()){
                if(currentWire < 1){
                    currentWire++;
                }
                else{
                    currentWire =0;
                }
            }

            else{
                do{
                    if(currentDirectionIndexDest1 < possibleDirections.length -1){
                        currentDirectionIndexDest1++;
                    }
                    else{
                        currentDirectionIndexDest1 = 0;
                    }
                }
                while(possibleDirections[currentDirectionIndexDest1] == pState.getValue(source1));

                switch(currentWire){
                    case 0 -> pLevel.setBlock(pPos,pState.setValue(dest1,possibleDirections[currentDirectionIndexDest1]),3);
                    case 1 -> pLevel.setBlock(pPos,pState.setValue(dest2,possibleDirections[currentDirectionIndexDest1]),3);
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(source1);
        pBuilder.add(dest1);
        pBuilder.add(source2);
        pBuilder.add(dest2);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        currentWire = 0;
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
