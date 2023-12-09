package dev.brioche.examplemod.menu;

import dev.brioche.examplemod.client.screen.TradingScreen;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SellerSlot extends Slot {
    private Player player;

    public SellerSlot(Player player, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        ItemStack result = TradingScreen.SellItem(player, index, stack);
        return result == ItemStack.EMPTY;
    }
}
