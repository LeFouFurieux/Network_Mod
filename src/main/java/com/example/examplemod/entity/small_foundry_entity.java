package com.example.examplemod.entity;

import com.example.examplemod.recipe.small_foundry_recipe;
import com.example.examplemod.recipe.small_foundry_recipe_alt_one;
import com.example.examplemod.screen.small_foundry_menu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

import static net.minecraft.client.gui.GuiComponent.blit;

public class small_foundry_entity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;
    private int fuelStored = 0;
    private int maxFuelStored = 16;
    private int fuelTransformProgress = 0;
    private int fuelTransformMaxProgress = 200;
    private int fuelConsommationProgress = 0;
    private int FireOn = 0;
    private final ItemStackHandler itemHandler = new ItemStackHandler(6){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    public small_foundry_entity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_FOUNDRY.get(),pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> small_foundry_entity.this.progress;
                    case 1 -> small_foundry_entity.this.maxProgress;
                    case 2 -> small_foundry_entity.this.fuelStored;
                    case 3 -> small_foundry_entity.this.maxFuelStored;
                    case 4 -> small_foundry_entity.this.FireOn;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                 switch (index){
                    case 0 -> small_foundry_entity.this.progress = value;
                    case 1 -> small_foundry_entity.this.maxProgress  = value;
                    case 2 -> small_foundry_entity.this.fuelStored = value;
                    case 3 -> small_foundry_entity.this.maxFuelStored  = value;
                    case 4 -> small_foundry_entity.this.FireOn = value;
                };
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Small Foundry");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new small_foundry_menu(id,inventory,this,this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()->itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.put("inventory", itemHandler.serializeNBT()); //save entity inventory
        compoundTag.putInt("small_foundry_entity.progress",this.progress);
        compoundTag.putInt("small_foundry_entity.fuelStored",this.fuelStored);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        itemHandler.deserializeNBT(compoundTag.getCompound("inventory")); //Load inventory of entity
        progress = compoundTag.getInt("small_foundry_entity.progress");
        fuelStored = compoundTag.getInt("small_foundry_entity.fuelStored");
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i =0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i,itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level,this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, small_foundry_entity pEntity) {
        if(level.isClientSide()){
            return;
        }

        if(hasFuelRecipe(pEntity) && pEntity.fuelStored < pEntity.maxFuelStored){
            pEntity.fuelTransformProgress+=2;
            if( pEntity.fuelTransformProgress >= pEntity.fuelTransformMaxProgress){
                pEntity.fuelStored++;
                pEntity.itemHandler.extractItem(0,1,false);
                pEntity.resetFuelTransformProgress();
            }
        }
        else{
            pEntity.resetFuelTransformProgress();
            pEntity.fuelConsommationProgress++;
            if(pEntity.fuelConsommationProgress >= pEntity.fuelTransformMaxProgress){
                pEntity.fuelStored--;
                pEntity.fuelConsommationProgress = 0;
            }
        }

        if(pEntity.fuelStored >= pEntity.maxFuelStored/2){
            pEntity.FireOn = 1;
            if(hasRecipe(pEntity,0) != -1){
                pEntity.progress++;
                setChanged(level,blockPos,blockState);
                if(pEntity.progress >= pEntity.maxProgress){
                    craftItem(pEntity);
                }
            } else{
                pEntity.resetProgress();
                setChanged(level,blockPos,blockState);
            }
        }
        else{
            pEntity.FireOn = 0;
        }
    }

    private void resetFuelTransformProgress() {
        this.fuelTransformProgress =0;
    }

    private static boolean hasFuelRecipe(small_foundry_entity pEntity) {
        if (pEntity.itemHandler.getStackInSlot(0).getItem() == Items.COAL){
            return true;
        }

        return false;

    }
    private void resetProgress() {
        this.progress =0;
    }

    private static void craftItem(small_foundry_entity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for(int i = 0; i< pEntity.itemHandler.getSlots();i++){
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<small_foundry_recipe> recipeCheck = level.getRecipeManager().getRecipeFor(small_foundry_recipe.Type.INSTANCE,inventory,level);
        Optional<small_foundry_recipe_alt_one> recipeCheck1 = level.getRecipeManager().getRecipeFor(small_foundry_recipe_alt_one.Type.INSTANCE,inventory,level);
        int numberRandom;
        if(recipeCheck1.isPresent()){
            numberRandom = new Random().nextInt(100);
        }
        else{
            numberRandom = 0;
        }
        int outputIndex;
        if(numberRandom <=50){
            outputIndex = hasRecipe(pEntity, 0);
            if(outputIndex != -1){
                pEntity.itemHandler.extractItem(1,1,false);
                pEntity.itemHandler.setStackInSlot(outputIndex,new ItemStack(recipeCheck.get().getResultItem().getItem(),
                        pEntity.itemHandler.getStackInSlot(outputIndex).getCount()+1));
            }

        }
        else{
            outputIndex = hasRecipe(pEntity, 1);
            if(outputIndex != -1){
                pEntity.itemHandler.extractItem(1,1,false);
                pEntity.itemHandler.setStackInSlot(outputIndex,new ItemStack(recipeCheck1.get().getResultItem().getItem(),
                        pEntity.itemHandler.getStackInSlot(outputIndex).getCount()+1));
            }
        }
        pEntity.resetProgress();

    }

    private static int hasRecipe(small_foundry_entity pEntity, int recipe_alt) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for(int i = 0; i< pEntity.itemHandler.getSlots();i++){
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }
        switch (recipe_alt){
            case 0:
                Optional<small_foundry_recipe> recipe = level.getRecipeManager().getRecipeFor(small_foundry_recipe.Type.INSTANCE,inventory,level);
                if(recipe.isPresent()){
                    int outputSlotIndex = canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());
                    if (recipe.isPresent() && outputSlotIndex != -1 && canInsertAmountIntoOutputSlot(inventory,outputSlotIndex)){
                        return outputSlotIndex;
                    }
                }
            case 1:
                Optional<small_foundry_recipe_alt_one> recipe_alt_one = level.getRecipeManager().getRecipeFor(small_foundry_recipe_alt_one.Type.INSTANCE,inventory,level);
                if(recipe_alt_one.isPresent()){
                    int outputSlotIndex = canInsertItemIntoOutputSlot(inventory, recipe_alt_one.get().getResultItem());
                    if (recipe_alt_one.isPresent() && outputSlotIndex != -1 && canInsertAmountIntoOutputSlot(inventory,outputSlotIndex)){
                        return outputSlotIndex;
                    }
                }
        }


        return -1;
    }

    private static int canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        if(inventory.getItem(2).isEmpty() || inventory.getItem(2).getItem() == itemStack.getItem()){
            return 2;
        }
        else if(inventory.getItem(3).isEmpty()|| inventory.getItem(3).getItem() == itemStack.getItem()){
            return 3;
        }
        else if(inventory.getItem(4).isEmpty()|| inventory.getItem(4).getItem() == itemStack.getItem()){
            return 4;
        }
        else if(inventory.getItem(5).isEmpty()|| inventory.getItem(5).getItem() == itemStack.getItem()){
            return 5;
        }
        return -1;
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory, int index) {
        return inventory.getItem(index).getMaxStackSize() > inventory.getItem(index).getCount();
    }
}
