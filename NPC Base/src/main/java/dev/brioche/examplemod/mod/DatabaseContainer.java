package dev.brioche.examplemod.mod;

import net.minecraft.world.SimpleContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DatabaseContainer {
    public static final int CURRENCY_ID = 388;

    public static SimpleContainer tradeInventory = new SimpleContainer(9);
    public static SimpleContainer sellsInventory = new SimpleContainer(9);
    public static Map<String, Integer> shopPrices = new HashMap<String, Integer>(){{
        put( "[White Wool]",10);
        put( "[Oak Log]",100);
    }};
}
