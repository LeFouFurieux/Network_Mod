package com.example.examplemod.integration;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.recipe.small_foundry_recipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JIEPlugin implements IModPlugin {
    public static RecipeType<small_foundry_recipe> INFUSION_TYPE =
            new RecipeType<>(small_foundry_recipe_category.UID, small_foundry_recipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExampleMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                small_foundry_recipe_category(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<small_foundry_recipe> recipesInfusing = rm.getAllRecipesFor(small_foundry_recipe.Type.INSTANCE);
        registration.addRecipes(INFUSION_TYPE, recipesInfusing);
    }
}
