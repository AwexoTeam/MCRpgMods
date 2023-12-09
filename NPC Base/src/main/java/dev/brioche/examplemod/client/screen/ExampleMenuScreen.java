package dev.brioche.examplemod.client.screen;

import dev.brioche.examplemod.ExampleMod;
import dev.brioche.examplemod.menu.ExampleMenu;
import dev.brioche.examplemod.mod.DatabaseContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ExampleMenuScreen extends AbstractContainerScreen<ExampleMenu> {
    public  static final int SLOTS_BEFORE_BUY_AREA = 45;
    public  static final int SLOT_INVENTORY_AMOUNT = 36;
    public ExampleMenu menu;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ExampleMod.MODID, "textures/gui/example_menu.png");

    public ExampleMenuScreen(ExampleMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.menu = pMenu;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
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

        pGuiGraphics.renderTooltip(this.font, toolTip, itemstack.getTooltipImage(), itemstack, pMouseX, pMouseY);
    }

    public static ItemStack BuyItem(Player player, int index){
        Item currency = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft","emerald"));
        ItemStack draggedStack = DatabaseContainer.sellsInventory.getItem(index - ExampleMenuScreen.SLOTS_BEFORE_BUY_AREA);
        String draggedName = draggedStack.getDisplayName().getString();

        Inventory pInv = player.getInventory();

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

        int cost = DatabaseContainer.shopPrices.get(draggedName);
        if(pInv.countItem(currency) < cost){
            System.out.println("Inventory Doesnt have enough of currency");
            return ItemStack.EMPTY;
        }

        int stackValue = 0;
        for (int i = 0; i < cost; i += stackValue) {
            int slotToYoink = pInv.findSlotMatchingItem(new ItemStack(currency,1));
            ItemStack stackYoink = pInv.getItem(slotToYoink);

            stackValue = stackYoink.getCount();
            int amountLeft = cost - i;
            int amount = Math.min(stackValue, amountLeft);

            System.out.println("Removing from slot" + slotToYoink + " and taking " + amount + " there is now " + (cost-i) + "/" + cost);
            pInv.removeItem(slotToYoink, amount);
        }

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
        return stack;
    }
}
