package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus =  Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab NETWORK_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        NETWORK_TAB = event.registerCreativeModeTab(new ResourceLocation(ExampleMod.MODID, "tutorial_tab"),
                builder -> builder.icon(() -> new ItemStack(ExampleMod.switch_block.get()))
                        .title(Component.translatable("creativemodetab.Network_tab")));
    }
}
