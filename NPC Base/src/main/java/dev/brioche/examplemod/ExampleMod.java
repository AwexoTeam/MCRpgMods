package dev.brioche.examplemod;

import dev.brioche.examplemod.init.EntityInit;

import dev.brioche.examplemod.init.MenuInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    public static final String MODID = "examplemod";

    public ExampleMod()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EntityInit.ENTITIES.register(bus);
        MenuInit.MENU_TYPES.register(bus);
    }

}
