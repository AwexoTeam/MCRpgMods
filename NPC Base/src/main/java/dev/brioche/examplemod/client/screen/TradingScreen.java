package dev.brioche.examplemod.client.screen;

import dev.brioche.examplemod.NPCMod;
import dev.brioche.examplemod.menu.TradingMenu;
import dev.brioche.examplemod.mod.DatabaseContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class TradingScreen extends AbstractContainerScreen<TradingMenu> {
    public  static final int SLOTS_BEFORE_BUY_AREA = 45;
    public  static final int SLOT_INVENTORY_AMOUNT = 36;
    public TradingMenu menu;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(NPCMod.MODID, "textures/gui/example_menu.png");

    public TradingScreen(TradingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        //CONSIDER: Better way than hardcoding this?
        this.menu = pMenu;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pGuiGraphics);

        //LOOKUP: Blit? It renders UI
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        //TODO: No magic numbers. Though for quick editing this is nice.
        float x = this.leftPos + 70;
        float y = this.topPos + 54;

        if(this.menu.getCarried() == null || !this.menu.getCarried().isEmpty())
            return;

        if(this.hoveredSlot == null || !this.hoveredSlot.hasItem())
            return;

        int index = hoveredSlot.index;
        boolean isInBuyers = index < SLOTS_BEFORE_BUY_AREA;

        ItemStack itemstack = this.hoveredSlot.getItem();
        String itemName = itemstack.getDisplayName().getString();
        if(!DatabaseContainer.shopPrices.containsKey(itemName))
            return;

        int price = DatabaseContainer.shopPrices.get(itemName);
        var toolTip = this.getTooltipFromContainerItem(itemstack);
        toolTip.add(Component.empty().append("$" + price));

        //Add item cost to tooltip!
        pGuiGraphics.renderTooltip(this.font, toolTip, itemstack.getTooltipImage(), itemstack, pMouseX, pMouseY);
    }

    public static ItemStack BuyItem(Player player, int index){
        //TODO: DEFINITELY NEEDS STATIC
        Item currency = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft","emerald"));
        ItemStack draggedStack = DatabaseContainer.tradeInventory.getItem(index - TradingScreen.SLOTS_BEFORE_BUY_AREA);
        String draggedName = draggedStack.getDisplayName().getString();

        Inventory pInv = player.getInventory();

        //Safety checks.
        if(draggedStack.isEmpty() || draggedName.equals("[Air]")){
            return ItemStack.EMPTY;
        }

        if(pInv.isEmpty()){
            System.out.println("Inventory Empty");
            return ItemStack.EMPTY;
        }

        if(!DatabaseContainer.shopPrices.containsKey(draggedName)){
            return ItemStack.EMPTY;
        }

        //Consider making this a function.
        int cost = DatabaseContainer.shopPrices.get(draggedName);
        if(pInv.countItem(currency) < cost){
            //TODO: Tell player ingame
            System.out.println("Inventory Doesnt have enough of currency");
            return ItemStack.EMPTY;
        }

        int stackValue = 0;

        //Loop through our inventory untill we taken the right amount.
        for (int i = 0; i < cost; i += stackValue) {
            int slotToTakeFrom = pInv.findSlotMatchingItem(new ItemStack(currency,1));
            ItemStack stackToTakeFrom = pInv.getItem(slotToTakeFrom);

            stackValue = stackToTakeFrom.getCount();
            int amountLeft = cost - i;
            int amount = Math.min(stackValue, amountLeft);

            pInv.removeItem(slotToTakeFrom, amount);
        }

        //Add the required item!
        pInv.add(new ItemStack(draggedStack.getItem(),1));
        return new ItemStack(draggedStack.getItem(),1);

    }

    public  static ItemStack SellItem(Player player, int index, ItemStack stack){
        String itemName = stack.getDisplayName().getString();

        if(!DatabaseContainer.shopPrices.containsKey(itemName))
            return ItemStack.EMPTY;

        int cost = DatabaseContainer.shopPrices.get(itemName);
        Item currency = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "emerald"));
        player.addItem(new ItemStack(currency, cost * stack.getCount()));

        DatabaseContainer.sellsInventory.removeItemType(stack.getItem(), stack.getCount());
        player.getInventory().removeItem(stack);
        return stack;
    }
}
