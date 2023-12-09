package dev.brioche.examplemod.client.renderer;

import dev.brioche.examplemod.ExampleMod;
import dev.brioche.examplemod.ResourceTesty;
import dev.brioche.examplemod.client.model.ExampleEntityModel;
import dev.brioche.examplemod.entity.ExampleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExampleEntityRenderer extends LivingEntityRenderer<ExampleEntity, ExampleEntityModel<ExampleEntity>> {
    //public static String path = "textures/entity/";
    public static String path = "textures/entity/";

    public ExampleEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ExampleEntityModel<>(ctx.bakeLayer(ExampleEntityModel.LAYER_LOCATION)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(ExampleEntity entity) {
        var rtn = new ResourceLocation(ExampleMod.MODID,path+entity.skin);
        return rtn;
    }
}
