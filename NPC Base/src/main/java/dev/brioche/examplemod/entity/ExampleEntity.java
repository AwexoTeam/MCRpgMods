package dev.brioche.examplemod.entity;

import dev.brioche.examplemod.menu.ExampleMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class ExampleEntity extends Mob implements MenuProvider {

    public String skin = "test.png";

    public ExampleEntity(EntityType<? extends Mob> type, Level level) {
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
        if(level().isClientSide)
            return InteractionResult.PASS;

        Item itemTag = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft","name_tag"));
        if(player.getItemInHand(hand).getItem().equals(itemTag)){
            System.out.println("You're right clicking with Name Tag!");
            return InteractionResult.PASS;
        }

        if(player instanceof ServerPlayer sPlayer){
            NetworkHooks.openScreen(sPlayer, this, buffer -> buffer.writeInt(this.getId()));
        }

        return InteractionResult.CONSUME;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes();
    }

    public static boolean canSpawn(EntityType<ExampleEntity> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos position, RandomSource random) {
        return true;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, Player pPlayer) {
        return new ExampleMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Skin",this.skin);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.skin = tag.getString("Skin");
        System.out.println("Skin tag: " + tag.getString("Skin"));
        System.out.println("Skin variable: " + this.skin);
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.hasCustomName())
            return;

        String name = this.getCustomName().getString().toLowerCase();
        if(!name.contains("skin:"))
            return;

        if(!name.contains("www.")){
            System.out.println("Name contained skin. Changing to: " + name);
            name = name.substring(5,name.length());
            skin = name;

            this.setCustomName(Component.literal("Done!"));
            return;
        }

        //DownloadImage(name);
        this.setCustomName(Component.literal("Done!"));
    }

    public static void DownloadImage(String address) {

        URL url = null;
        try {
            url = new URL(address);
            InputStream in = new BufferedInputStream(url.openStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Brioche\\Desktop\\skin.png"));

            for (int i; (i = in.read()) != -1; ) {
                out.write(i);
            }
            in.close();
            out.close();

        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
