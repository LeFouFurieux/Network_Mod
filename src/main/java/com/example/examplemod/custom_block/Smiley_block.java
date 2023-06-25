package com.example.examplemod.custom_block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class Smiley_block extends Block {
    public Smiley_block(Properties properties) {
        super(properties);
    }

    public static  final BooleanProperty Happy = BooleanProperty.create("happy");

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if(!level.isClientSide() && interactionHand == interactionHand.MAIN_HAND && player.getHealth() > 9 ){
            if(!level.getBlockState(blockPos).getValue(Happy)){
                level.setBlock(blockPos, blockState.setValue(Happy, true),3);
                player.sendSystemMessage(Component.literal("Changed block mood to happy"));
            }

            int randomNumber = (int) Math.floor(Math.random() * (6 - 0 + 1) + 0);

            switch (randomNumber) {
                case 0:
                    player.sendSystemMessage(Component.literal("The ability to mine does make you intelligent - Qui Gon Kim"));
                    break;
                case 1:
                    player.sendSystemMessage(Component.literal("Villagers feel safe around you"));
                    break;
                case 2:
                    player.sendSystemMessage(Component.literal("How many crops do you need, geez!"));
                    break;
                case 3:
                    player.sendSystemMessage(Component.literal("The dragon is scared"));
                    break;
                case 4:
                    player.sendSystemMessage(Component.literal("You play like Notch!"));
                    break;
                case 5:
                    player.sendSystemMessage(Component.literal("You are such a good steve"));
                    break;
                case 6:
                    player.sendSystemMessage(Component.literal("Creepers run away from you!"));
                    break;
                default:
                    player.sendSystemMessage(Component.literal("Error generating the compliment" + randomNumber));
            }

        }

        else if(!level.isClientSide() && interactionHand == interactionHand.MAIN_HAND && player.getHealth() < 10 ){
            if(level.getBlockState(blockPos).getValue(Happy)){
                level.setBlock(blockPos, blockState.setValue(Happy, false),3);
                player.sendSystemMessage(Component.literal("Changed block mood to sad"));
            }
            int randomNumber = (int) Math.floor(Math.random() * (6 - 0 + 1) + 0);

            switch (randomNumber) {
                case 0:
                    player.sendSystemMessage(Component.literal("Need a little help there ? "));
                    break;
                case 1:
                    player.sendSystemMessage(Component.literal("I strongly recommended switching to easy difficulty"));
                    break;
                case 2:
                    player.sendSystemMessage(Component.literal("Life is tough, isnt it ?"));
                    break;
                case 3:
                    player.sendSystemMessage(Component.literal("Don't you have diamonds yet ? "));
                    break;
                case 4:
                    player.sendSystemMessage(Component.literal("Go back to Roblox"));
                    break;
                case 5:
                    player.sendSystemMessage(Component.literal("Sheeps are faster than you"));
                    break;
                case 6:
                    player.sendSystemMessage(Component.literal("I believe it's called a noob"));
                    break;
                default:
                    player.sendSystemMessage(Component.literal("Error generating the compliment" + randomNumber));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(Happy);
    }
}
