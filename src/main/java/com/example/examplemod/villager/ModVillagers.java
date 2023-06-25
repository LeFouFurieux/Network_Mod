package com.example.examplemod.villager;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.potion.ModPotions;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.InvocationTargetException;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPE = DeferredRegister.create(ForgeRegistries.POI_TYPES, ExampleMod.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ExampleMod.MODID);

    //Block to get profession, range is how many villagers can get this profession
    public static final RegistryObject<PoiType> PHOTORESIST_POI = POI_TYPE.register("photoresist_poi",
            ()-> new PoiType(ImmutableSet.copyOf(ExampleMod.switch_block.get().getStateDefinition().getPossibleStates()), 1, 1));
    public static final RegistryObject<VillagerProfession> NETWORK_MASTER = VILLAGER_PROFESSION.register("network_master",
            ()-> new VillagerProfession("network_master",x -> x.get() == PHOTORESIST_POI.get(),
                    x->x.get() ==PHOTORESIST_POI.get(), ImmutableSet.of(),ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_ARMORER)); //The predicates determine the primary and secondary work site
    public static void register(IEventBus eventBus){
        POI_TYPE.register(eventBus);
        VILLAGER_PROFESSION.register(eventBus);
    }
    public static void registerPOIs(){
        try{
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockstates",PoiType.class).invoke(null,PHOTORESIST_POI.get());
        }catch (InvocationTargetException | IllegalAccessException exception){
            exception.printStackTrace();
        }
    }
}
