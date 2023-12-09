package dev.brioche.examplemod.init;

import dev.brioche.examplemod.menu.ExampleMenu;
import dev.brioche.examplemod.ExampleMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.*;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExampleMod.MODID);

    public static final RegistryObject<MenuType<ExampleMenu>> EXAMPLE_MENU = MENU_TYPES.register("example_menu",
            () -> IForgeMenuType.create(ExampleMenu::new));
}
