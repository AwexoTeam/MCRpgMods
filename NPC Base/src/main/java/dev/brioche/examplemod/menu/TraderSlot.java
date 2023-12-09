package dev.brioche.examplemod.menu;

import dev.brioche.examplemod.client.screen.TradingScreen;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TraderSlot extends Slot {
    public Player player;

    public TraderSlot(Player player, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack){
        return player.isCreative();
    }

    @Override
    public boolean mayPickup(Player player) {
        if(!player.isCreative())
            return  DoSurvivalPickup(player);

        return true;
    }

    public boolean DoSurvivalPickup(Player player){
        TradingScreen.BuyItem(player,index);

        return false;
    }
}
