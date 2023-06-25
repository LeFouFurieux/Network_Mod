package com.example.examplemod.recipe;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExampleMod.MODID);

    public static final RegistryObject<RecipeSerializer<small_foundry_recipe>> SMALL_FOUNDRY_SERIALIZER =
        SERIALIZERS.register("small_foundry", () -> small_foundry_recipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<small_foundry_recipe_alt_one>> SMALL_FOUNDRY_SERIALIZER_ALT_ONE =
            SERIALIZERS.register("small_foundry_alt_one", () -> small_foundry_recipe_alt_one.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
