package dev.brioche.examplemod.events;

import dev.brioche.examplemod.NPCMod;
import dev.brioche.examplemod.entity.NPCEntity;
import dev.brioche.examplemod.init.NPCInit;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/*
* Genralized class to register that our entity exists for the system.
* This only takes care of saying that the entity exists.
* */

@Mod.EventBusSubscriber(modid = NPCMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(NPCInit.EXAMPLE_ENTITY.get(), NPCEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                NPCInit.EXAMPLE_ENTITY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                NPCEntity::canSpawn,
                SpawnPlacementRegisterEvent.Operation.OR
        );

    }
}
