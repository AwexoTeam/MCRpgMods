package dev.brioche.examplemod.init;

import dev.brioche.examplemod.NPCMod;
import dev.brioche.examplemod.entity.NPCEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NPCInit {
    //LOOKUP: Read up on:
    // DefferedRegister, ?, Registry OBject, () -> notation, Can you make it into a function so this isnt as ugly.
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NPCMod.MODID);

    public static final RegistryObject<EntityType<NPCEntity>> EXAMPLE_ENTITY = ENTITIES.register("example_entity",
            () -> EntityType.Builder.<NPCEntity>of(NPCEntity::new, MobCategory.MISC)
                    .sized(1.0f, 1.0f)
                    .build(new ResourceLocation(NPCMod.MODID, "example_entity").toString())
    );
}
