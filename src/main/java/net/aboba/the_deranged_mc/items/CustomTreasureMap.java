package net.aboba.the_deranged_mc.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;

public class CustomTreasureMap {
    public static void giveCustomTreasureMap(PlayerEntity player, ServerWorld world, int x, int z) {
        ItemStack mapStack = FilledMapItem.createMap(world, x, z, (byte) 2, true, true);
        MapState mapState = FilledMapItem.getMapState(mapStack, world);

        if (mapState != null) {
            // Update the map with the new coordinates
            mapState.markDirty();
            player.giveItemStack(mapStack);
        }
    }
}
