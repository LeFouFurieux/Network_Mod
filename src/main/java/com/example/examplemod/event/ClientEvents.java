package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents{

        @SubscribeEvent
        public static void OnKeyInput(InputEvent.Key event){
            if(KeyBinding.HACK_KEY.consumeClick()){
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Hacking in progress"));
            }
        }
    }
    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBinding.HACK_KEY);
        }
    }
}
