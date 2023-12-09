package dev.brioche.examplemod.init;

import dev.brioche.examplemod.menu.TradingMenu;
import dev.brioche.examplemod.NPCMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.*;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, NPCMod.MODID);

    public static final RegistryObject<MenuType<TradingMenu>> EXAMPLE_MENU = MENU_TYPES.register("example_menu",
            () -> IForgeMenuType.create(TradingMenu::new));
}
