package dev.brioche.examplemod.menu;

import dev.brioche.examplemod.client.screen.ExampleMenuScreen;
import dev.brioche.examplemod.mod.DatabaseContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class SellerSlot extends Slot {
    private Player player;

    public SellerSlot(Player player, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        ItemStack result = ExampleMenuScreen.SellItem(player, index, stack);
        return result == ItemStack.EMPTY;
    }
}
