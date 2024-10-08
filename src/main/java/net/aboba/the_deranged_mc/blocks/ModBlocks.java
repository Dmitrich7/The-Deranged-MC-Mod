package net.aboba.the_deranged_mc.blocks;

import net.aboba.the_deranged_mc.TheDerangedMC;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block CONDENSED_DIRT = register(
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.GRASS)),
            "condensed_dirt",
            true
    );

    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        Identifier id = new Identifier(TheDerangedMC.MOD_ID, name);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void registerModBlocks() {
        TheDerangedMC.LOGGER.info("Registering Mod Blocks for " + TheDerangedMC.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> {
            itemGroup.add(ModBlocks.CONDENSED_DIRT.asItem());
        });
    }

}
