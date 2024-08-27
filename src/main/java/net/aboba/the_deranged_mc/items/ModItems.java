package net.aboba.the_deranged_mc.items;

import net.aboba.the_deranged_mc.TheDerangedMC;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item TEST_ITEM = register(
            new Item(new FabricItemSettings()),
            "test_item"
    );

    public static Item register(Item item, String id) {
        Identifier itemID = new Identifier(TheDerangedMC.MOD_ID, id);
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
        return registeredItem;
    }

    public static void registerModItems() {
        TheDerangedMC.LOGGER.info("Registering Mod Items for " + TheDerangedMC.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.TEST_ITEM));
    }

}