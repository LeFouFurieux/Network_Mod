package com.example.examplemod.potion;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.small_foundry_entity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ExampleMod.MODID);

    public static final RegistryObject<Potion> PHOTORESIST = POTIONS.register("photoresist", () -> new Potion(
            new MobEffectInstance(MobEffects.POISON,200,0),
            new MobEffectInstance(MobEffects.FIRE_RESISTANCE,200,0)
    ));
    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
