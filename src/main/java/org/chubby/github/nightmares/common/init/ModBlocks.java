package org.chubby.github.nightmares.common.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.nightmares.Constants;

import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MODID);

    public static final DeferredBlock<Block> CRACKED_STEM = registerBlock("cracked_stem",
            ()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STEM).sound(SoundType.STEM)));

    public static<T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> supplier)
    {
        DeferredBlock<T> regObj = BLOCKS.register(name,supplier);
        ModItems.registerItem(name,()-> new BlockItem(regObj.get(),new Item.Properties()),false);
        ModTabs.BLOCK_LIST.add(regObj);
        return  regObj;
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
