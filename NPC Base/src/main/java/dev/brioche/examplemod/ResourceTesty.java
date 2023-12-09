package dev.brioche.examplemod;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ResourceTesty extends ResourceLocation {
    String path = "";

    public ResourceTesty(String namespace, String path, @Nullable ResourceLocation.Dummy locationDummy) {
        super(namespace, path, locationDummy);
        this.path = path;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
