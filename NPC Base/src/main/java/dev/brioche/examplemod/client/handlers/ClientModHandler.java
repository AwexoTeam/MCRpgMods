package dev.brioche.examplemod.client.handlers;

import dev.brioche.examplemod.NPCMod;
import dev.brioche.examplemod.client.screen.TradingScreen;
import dev.brioche.examplemod.init.MenuInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = NPCMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    //Queues up an event to which we get to register and tell Forge our screen exist.
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuInit.EXAMPLE_MENU.get(), TradingScreen::new);
        });
    }
}