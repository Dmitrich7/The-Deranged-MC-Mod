package com.example.item;

import com.example.TheDerangedMC;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

//    public static final Item TEST_ITEM = registerItem("test_item", new Item(new FabricItemSettings()));

    public static final Item TEST_ITEM = register(
            // Ignore the food component for now, we'll cover it later in the food section.
            new Item(new FabricItemSettings()),
            "test_item"
    );

    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = new Identifier(TheDerangedMC.MODID, id);

        // Register the item.
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

        // Return the registered item!
        return registeredItem;
    }

    public static void registerModItems() {
        TheDerangedMC.LOGGER.info("Registering Mod Items for " + TheDerangedMC.MODID);

        // Get the event for modifying entries in the ingredients group.
// And register an event handler that adds our suspicious item to the ingredients group.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.TEST_ITEM));
    }

}