package dev.brioche.examplemod;

import dev.brioche.examplemod.init.NPCInit;

import dev.brioche.examplemod.init.MenuInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NPCMod.MODID)
public class NPCMod
{
    public static final String MODID = "examplemod";

    public NPCMod()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        NPCInit.ENTITIES.register(bus);
        MenuInit.MENU_TYPES.register(bus);
    }

}
