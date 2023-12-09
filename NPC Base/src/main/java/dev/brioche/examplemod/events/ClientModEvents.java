package dev.brioche.examplemod.events;

import dev.brioche.examplemod.ExampleMod;
import dev.brioche.examplemod.client.model.ExampleEntityModel;
import dev.brioche.examplemod.client.renderer.ExampleEntityRenderer;
import dev.brioche.examplemod.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityInit.EXAMPLE_ENTITY.get(), ExampleEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinations(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ExampleEntityModel.LAYER_LOCATION, ExampleEntityModel::createBodyLayer);
    }
}
