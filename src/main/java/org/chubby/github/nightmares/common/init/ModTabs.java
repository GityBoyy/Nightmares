package org.chubby.github.nightmares.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.units.qual.C;
import org.chubby.github.nightmares.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModTabs
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.
            create(Registries.CREATIVE_MODE_TAB,Constants.MODID);

    public static final List<Supplier<? extends Item>> ITEM_LIST = new ArrayList<>();
    public static final List<Supplier<? extends Block>> BLOCK_LIST = new ArrayList<>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEM_TAB = TABS.register("item_tab",()->CreativeModeTab.builder()
            .title(Component.translatable("tab.nightmares.item_tab"))
            .icon(()-> ItemStack.EMPTY)
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((param,output)->{
                ITEM_LIST.forEach(obj -> output.accept(obj.get()));
            })
            .build()
    );
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCK_TAB = TABS.register("block_tab",()->CreativeModeTab.builder()
            .title(Component.translatable("tab.nightmares.block_tab"))
            .icon(()-> ItemStack.EMPTY)
            .withTabsBefore(ITEM_TAB.getKey())
            .displayItems((param,output)->{
                BLOCK_LIST.forEach(obj -> output.accept(obj.get()));
            })
            .build()
    );

    public static void register(IEventBus eventBus)
    {
        TABS.register(eventBus);
    }
}
