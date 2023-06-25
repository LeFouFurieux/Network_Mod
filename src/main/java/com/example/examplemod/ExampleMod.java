package com.example.examplemod;

import com.example.examplemod.custom_block.*;
import com.example.examplemod.item.ModCreativeModeTabs;
import com.example.examplemod.potion.ModPotions;
import com.example.examplemod.recipe.ModRecipes;
import com.example.examplemod.screen.small_foundry_screen;
import com.example.examplemod.custom_item.GunItem;
import com.example.examplemod.entity.ModBlockEntities;
import com.example.examplemod.screen.ModMenuTypes;
import com.example.examplemod.villager.ModVillagers;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.Supplier;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("smiley_block", () -> new Smiley_block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("smiley_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> small_foundry_block = BLOCKS.register("small_foundry", () -> new small_foundry(BlockBehaviour.Properties.of(Material.STONE).noOcclusion()));
    public static final RegistryObject<Item> small_foundry_item = ITEMS.register("small_foundry", () -> new BlockItem(small_foundry_block.get(), new Item.Properties()));

    public static final RegistryObject<Block> switch_block = BLOCKS.register("switch_block", () -> new switch_block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops().noOcclusion()));
    public static final RegistryObject<Item> switchItem = ITEMS.register("switch_block", () -> new BlockItem(switch_block.get(), new Item.Properties()));
    public static final RegistryObject<Item> KIM_BLOCK_ITEM = ITEMS.register("kimc_block", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).build())));
    public static final RegistryObject<Block> solar_panel_block = BLOCKS.register("solar_panel", () -> new solar_panel(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops().noOcclusion()));
    public static final RegistryObject<Item> solar_panel = ITEMS.register("solar_panel", () -> new com.example.examplemod.custom_item.solar_panel(solar_panel_block.get(), new Item.Properties()));
    public static final RegistryObject<GunItem> GUN_ITEM = ITEMS.register("gun_item", () -> new GunItem(new Item.Properties()));
    public static final RegistryObject<Item> RAW_SILICON_ITEM = ITEMS.register("raw_silicon", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILICON_CRYSTAL_ITEM = ITEMS.register("silicon_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILICON_WAFER = ITEMS.register("silicon_wafer", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SUBSTRATE = ITEMS.register("substrate", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEAT_SPREADER = ITEMS.register("heat_spreader", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILICON_WAFFLE = ITEMS.register("silicon_waffle", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).build())));
    public static final RegistryObject<Block> ETHERNET_WIRE = BLOCKS.register("ethernet_wire", () -> new ethernet_wire(BlockBehaviour.Properties.of(Material.STONE).noOcclusion()));
    public static final RegistryObject<Item> ETHERNET_WIRE_ITEM = ITEMS.register("ethernet_wire", () -> new BlockItem(ETHERNET_WIRE.get(), new Item.Properties()));
    //public static final RegistryObject<Item> PHOTORESIST = ITEMS.register("photoresist", () -> new Item(new Item.Properties()));
    public static final RegistryObject<SwordItem> PIZZA_ITEM = ITEMS.register("pizza-sword", () ->new SwordItem(new Tier() {
        @Override
        public int getUses() {
            return 0;
        }

        @Override
        public float getSpeed() {
            return 0;
        }

        @Override
        public float getAttackDamageBonus() {
            return 0;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return null;
        }
    }, 600, 2, new Item.Properties()));
    
    public ExampleMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModPotions.register(modEventBus);
        ModVillagers.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        event.enqueueWork(()->{
            ModVillagers.registerPOIs();
        });
        
    }
 

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == ModCreativeModeTabs.NETWORK_TAB) {
            event.accept(EXAMPLE_BLOCK_ITEM);
            event.accept(KIM_BLOCK_ITEM);
            event.accept(GUN_ITEM);
            event.accept(switchItem);
            event.accept(solar_panel);
            event.accept(small_foundry_item);
            event.accept(RAW_SILICON_ITEM);
            event.accept(SILICON_CRYSTAL_ITEM);
            event.accept(SILICON_WAFER);
            event.accept(SILICON_WAFFLE);
            event.accept(SUBSTRATE);
            event.accept(HEAT_SPREADER);
            event.accept(ETHERNET_WIRE_ITEM);
        }

        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
            event.accept(KIM_BLOCK_ITEM);
            event.accept(GUN_ITEM);
            event.accept(switchItem);
            event.accept(solar_panel);
            event.accept(small_foundry_item);
            event.accept(RAW_SILICON_ITEM);
            event.accept(SILICON_CRYSTAL_ITEM);
            event.accept(SILICON_WAFER);
            event.accept(SILICON_WAFFLE);
            event.accept(SUBSTRATE);
            event.accept(ETHERNET_WIRE_ITEM);
            event.accept(ETHERNET_WIRE_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            MenuScreens.register(ModMenuTypes.SMALL_FOUNDRY_MENU.get(),small_foundry_screen::new);
        }
    }
}
