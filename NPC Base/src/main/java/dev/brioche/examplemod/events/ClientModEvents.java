package dev.brioche.examplemod.events;

import dev.brioche.examplemod.NPCMod;
import dev.brioche.examplemod.client.model.NPCModel;
import dev.brioche.examplemod.client.renderer.NPCEntityRenderer;
import dev.brioche.examplemod.init.NPCInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/*
* Generalized class to register the different renders and layer definations that we use.
* For this it should only be one. But is good practice to know I can do it in one class.
* This only takes care of telling how the entity should be rendered.
* */
@Mod.EventBusSubscriber(modid = NPCMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(NPCInit.EXAMPLE_ENTITY.get(), NPCEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinations(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(NPCModel.LAYER_LOCATION, NPCModel::createBodyLayer);
    }
}
