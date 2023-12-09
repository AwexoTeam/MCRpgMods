package dev.brioche.examplemod.menu;

import dev.brioche.examplemod.client.screen.ExampleMenuScreen;
import dev.brioche.examplemod.entity.ExampleEntity;
import dev.brioche.examplemod.init.MenuInit;
import dev.brioche.examplemod.mod.DatabaseContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExampleMenu extends AbstractContainerMenu {

    private final ExampleEntity blockEntity;
    private final ContainerLevelAccess levelAccess;

    // Client Constructor
    public ExampleMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv, playerInv.player.level().getEntity(additionalData.getInt(0)));
    }

    // Server Constructor
    public ExampleMenu(int containerId, Inventory playerInv, Entity blockEntity) {
        super(MenuInit.EXAMPLE_MENU.get(), containerId);
        if(blockEntity instanceof ExampleEntity be) {
            this.blockEntity = be;
        } else {
            throw new IllegalStateException("Incorrect block entity class (%s) passed into ExampleMenu!"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        this.levelAccess = ContainerLevelAccess.create(blockEntity.level(), blockEntity.blockPosition());

        if(DatabaseContainer.tradeInventory == null)
            DatabaseContainer.tradeInventory = new SimpleContainer(9);

        if(DatabaseContainer.sellsInventory == null)
            DatabaseContainer.sellsInventory = new SimpleContainer(9);

        createPlayerInteraction(playerInv);
        createSellInventory(be, playerInv.player);
        createBuyInventory(be, playerInv.player);
    }

    private void createSellInventory(ExampleEntity be, Player player) {
        int index = 0;
        int xo = 8;
        int yo = 18;

        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 3; column++){
                int x = (column*18) + xo;
                int y = (row*18)+yo;

                SellerSlot slot = new SellerSlot(player, DatabaseContainer.tradeInventory,index, x,y);
                addSlot(slot);

                index++;
            }
        }
    }

    private void createBuyInventory(ExampleEntity be, Player player) {
        int index = 0;
        int xo = 116;
        int yo = 18;

        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 3; column++){
                int x = (column*18) + xo;
                int y = (row*18)+yo;

                TraderSlot slot = new TraderSlot(player, DatabaseContainer.sellsInventory,index, x,y);
                addSlot(slot);

                index++;
            }
        }
    }

    private void createPlayerInteraction(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv,
                    column,
                    8 + (column * 18),
                    142));
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv,
                        9 + column + (row * 9),
                        8 + (column * 18),
                        84 + (row * 18)));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {

        if(pIndex >= ExampleMenuScreen.SLOTS_BEFORE_BUY_AREA)
            return ExampleMenuScreen.BuyItem(pPlayer, pIndex);

        if(pIndex < ExampleMenuScreen.SLOT_INVENTORY_AMOUNT){
            var pInv = pPlayer.getInventory();
            var itemStack = pInv.getItem(pIndex);
            return ExampleMenuScreen.SellItem(pPlayer, pIndex, itemStack);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

}
