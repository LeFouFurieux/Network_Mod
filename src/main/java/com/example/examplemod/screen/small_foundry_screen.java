package com.example.examplemod.screen;

import com.example.examplemod.ExampleMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class small_foundry_screen extends AbstractContainerScreen<small_foundry_menu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ExampleMod.MODID,"textures/gui/small_foundry_gui.png");
    public small_foundry_screen(small_foundry_menu smallFoundryMenu, Inventory inventory, Component component) {
        super(smallFoundryMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        //Sets up everything
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //Renders texture
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack, x, y);
        renderFuelStored(pPoseStack,x,y);
        if(menu.FireOn()){
            renderFire(pPoseStack,x,y);
        }
    }

    private void renderFire(PoseStack pPoseStack, int x, int y) {
            blit(pPoseStack,x+51,y+45,180,18,14,14);
    }

    private void renderFuelStored(PoseStack pPoseStack, int x, int y) {
        blit(pPoseStack,x+13,y+46 - menu.getScaledFuelStored(),180,65-menu.getScaledFuelStored(),15,menu.getScaledFuelStored());
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()){ //If not crafting, don't render arrow
            blit(pPoseStack, x+76, y+32, 178, 0, menu.getScaledProgress(), 13); //Rendering progress arrow #22 37:31
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}
