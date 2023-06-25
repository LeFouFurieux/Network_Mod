package com.example.examplemod.custom_item;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

import static com.example.examplemod.custom_block.solar_panel.FACING;

public class solar_panel extends BlockItem {

    public solar_panel(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext blockPlaceContext) {
        boolean HasThePlace = false;

        BlockState pState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        Player player = blockPlaceContext.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);

        if(blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos()).canBeReplaced() && !player.blockPosition().equals(blockPlaceContext.getClickedPos())){
            BlockPos pos;
            switch(blockPlaceContext.getHorizontalDirection()){
                case NORTH:
                    pos = new BlockPos(blockPlaceContext.getClickedPos().east(1));
                    if (blockPlaceContext.getLevel().getBlockState(pos).canBeReplaced()){
                        HasThePlace = true;
                        blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos().east(1),ExampleMod.solar_panel_block.get().defaultBlockState().setValue(FACING,Direction.SOUTH), 3 );
                    }
                    break;
                case SOUTH:
                    pos = new BlockPos(blockPlaceContext.getClickedPos().west(1));
                    if (blockPlaceContext.getLevel().getBlockState(pos).canBeReplaced()){
                        HasThePlace = true;
                        blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos().west(1),ExampleMod.solar_panel_block.get().defaultBlockState().setValue(FACING,Direction.NORTH), 3 );
                    }
                    break;
                case WEST:
                    pos = new BlockPos(blockPlaceContext.getClickedPos().north(1));
                    if (blockPlaceContext.getLevel().getBlockState(pos).canBeReplaced()){
                        HasThePlace = true;
                        blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos().north(1),ExampleMod.solar_panel_block.get().defaultBlockState().setValue(FACING,Direction.EAST), 3 );
                    }
                    break;
                case EAST:
                    pos = new BlockPos(blockPlaceContext.getClickedPos().south(1));
                    if (blockPlaceContext.getLevel().getBlockState(pos).canBeReplaced()){
                        HasThePlace = true;
                        blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos().south(1),ExampleMod.solar_panel_block.get().defaultBlockState().setValue(FACING,Direction.WEST), 3 );
                    }
                    break;
            }
        }
        if(HasThePlace){
            return super.place(blockPlaceContext);
        }
        return InteractionResult.PASS;
    }
}
