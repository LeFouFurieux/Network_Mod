package com.example.examplemod.integration;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.recipe.small_foundry_recipe;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class small_foundry_recipe_category implements IRecipeCategory<small_foundry_recipe> {

    public final static ResourceLocation UID = new ResourceLocation(ExampleMod.MODID, "small_foundry");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(ExampleMod.MODID, "textures/gui/small_foundry_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public small_foundry_recipe_category(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ExampleMod.small_foundry_block.get())); //Tab icon
    }

    @Override
    public RecipeType<small_foundry_recipe> getRecipeType() {
        return JIEPlugin.INFUSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Small Foundry");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, small_foundry_recipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 86, 15).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 86, 60).addItemStack(recipe.getResultItem());
    }
}
