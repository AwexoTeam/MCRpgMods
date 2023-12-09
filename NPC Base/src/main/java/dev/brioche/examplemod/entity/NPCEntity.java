package dev.brioche.examplemod.entity;

import dev.brioche.examplemod.menu.TradingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NPCEntity extends Mob implements MenuProvider {

    //Default skin to use.
    public String skin = "test.png";

    public NPCEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        refreshDimensions();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0,new LookAtPlayerGoal(this, Player.class, 6.0f));
    }

    @Override
    public EntityDimensions getDimensions(Pose p_21047_) {
        return new EntityDimensions(0.8f,2f, true);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 pos, InteractionHand hand) {

        //LOOKUP: Why do we deny clientside?
        if(level().isClientSide)
            return InteractionResult.PASS;

        //TODO: Should be static somewhere
        Item itemTag = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft","name_tag"));

        if(player.getItemInHand(hand).getItem().equals(itemTag)){
            DoSkinChange();
            return InteractionResult.PASS;
        }

        //Taken from Forge documentation.
        if(player instanceof ServerPlayer sPlayer){
            NetworkHooks.openScreen(sPlayer, this, buffer -> buffer.writeInt(this.getId()));
        }

        return InteractionResult.CONSUME;
    }

    //TODO: Give actual attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes();
    }

    //We spawn em manually so answer is just yes
    public static boolean canSpawn(EntityType<NPCEntity> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos position, RandomSource random) {
        return true;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, Player pPlayer) {
        return new TradingMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Skin",this.skin);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        //TODO: Figure out why this doesnt redirect to skin variable.
        this.skin = tag.getString("Skin");
        System.out.println("Skin tag: " + tag.getString("Skin"));
        System.out.println("Skin variable: " + this.skin);
    }

    public void DoSkinChange() {

        //No need to continue if it didnt have custom name.
        if (!this.hasCustomName())
            return;

        //Get Name and check if syntax right
        String name = this.getCustomName().getString().toLowerCase();
        if (!name.contains("skin:"))
            return;

        //Remove "skin:"
        name = name.substring(5, name.length());
        skin = name;

        //A way to tell player that its done.
        this.setCustomName(Component.literal("Done!"));
    }
}
