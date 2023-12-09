package dev.brioche.examplemod.menu;

import dev.brioche.examplemod.client.screen.TradingScreen;
import dev.brioche.examplemod.entity.NPCEntity;
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

public class TradingMenu extends AbstractContainerMenu {

    private final NPCEntity blockEntity;
    private final ContainerLevelAccess levelAccess;

    // Client Constructor
    public TradingMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv, playerInv.player.level().getEntity(additionalData.getInt(0)));
    }

    // Server Constructor
    //LOOKUP: Turty wurty was very contradicting. Said they the same but different. Need more lookup
    public TradingMenu(int containerId, Inventory playerInv, Entity blockEntity) {
        super(MenuInit.EXAMPLE_MENU.get(), containerId);

        //Basic throw statement.
        if(!(blockEntity instanceof NPCEntity be)){
            throw new IllegalStateException("Incorrect block entity class (%s) passed into ExampleMenu!"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        this.blockEntity = be;
        this.levelAccess = ContainerLevelAccess.create(blockEntity.level(), blockEntity.blockPosition());

        //TODO: This should be initalized elsewhere.
        //TODO: this shouldnt be done like this but thats what my brain can atm.
        if(DatabaseContainer.tradeInventory == null)
            DatabaseContainer.tradeInventory = new SimpleContainer(9);

        if(DatabaseContainer.sellsInventory == null)
            DatabaseContainer.sellsInventory = new SimpleContainer(9);

        //Get our slots onto the UI
        createPlayerInteraction(playerInv);
        createSellInventory(be, playerInv.player);
        createBuyInventory(be, playerInv.player);
    }

    private void createSellInventory(NPCEntity be, Player player) {
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

    private void createBuyInventory(NPCEntity be, Player player) {
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

        //TODO: gotta deal with it not crashing. One bug at a time.
        if(pIndex >= TradingScreen.SLOTS_BEFORE_BUY_AREA)
            return TradingScreen.BuyItem(pPlayer, pIndex);

        if(pIndex < TradingScreen.SLOT_INVENTORY_AMOUNT){
            var pInv = pPlayer.getInventory();
            var itemStack = pInv.getItem(pIndex);
            return TradingScreen.SellItem(pPlayer, pIndex, itemStack);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

}
