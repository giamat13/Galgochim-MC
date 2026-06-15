package com.galgochim.registry;

import java.util.function.Function;

import com.galgochim.Galgochim;
import com.galgochim.block.WashingMachineBlock;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {
    private ModBlocks() {
    }

    /** Doron Fishler's workstation - a microphone on a stand. */
    public static final Block MICROPHONE = register("microphone",
            Block::new, MapColor.METAL, SoundType.METAL, 1.0f);

    /** A washing machine; full of laundry powder when placed. */
    public static final Block WASHING_MACHINE = register("washing_machine",
            WashingMachineBlock::new, MapColor.SNOW, SoundType.METAL, 2.0f);

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                  MapColor color, SoundType sound, float strength) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Galgochim.id(name));
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(color).sound(sound).strength(strength).setId(blockKey);
        Block block = factory.apply(props);
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Galgochim.id(name));
        Item blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        return block;
    }

    /** Triggers static init (registration) and adds the block items to a creative tab. */
    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
            output.accept(MICROPHONE);
            output.accept(WASHING_MACHINE);
        });
    }
}
