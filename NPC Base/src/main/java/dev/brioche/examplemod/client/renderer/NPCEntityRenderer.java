package dev.brioche.examplemod.client.renderer;

import dev.brioche.examplemod.NPCMod;
import dev.brioche.examplemod.client.model.NPCModel;
import dev.brioche.examplemod.entity.NPCEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class NPCEntityRenderer extends LivingEntityRenderer<NPCEntity, NPCModel<NPCEntity>> {
    //TODO: Want this to be dynamic?
    //Base path for all our textures of entities.
    public static String path = "textures/entity/";

    //Constructor overload where we just use the models layer location
    public NPCEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new NPCModel<>(ctx.bakeLayer(NPCModel.LAYER_LOCATION)), 1.0f);
    }

    @Override
    //TODO: Consider making this private so you dont call new constantly. It aiiint good.
    public ResourceLocation getTextureLocation(NPCEntity entity) {
        var rtn = new ResourceLocation(NPCMod.MODID,path+entity.skin);
        return rtn;
    }
}
