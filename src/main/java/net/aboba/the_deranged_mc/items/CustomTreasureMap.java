package net.aboba.the_deranged_mc.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class CustomTreasureMap {
    public static void giveCustomTreasureMap(PlayerEntity player, ServerWorld world, int x, int z) {
        ItemStack mapStack = FilledMapItem.createMap(world, x, z, (byte) 2, true, true);
        MapState mapState = FilledMapItem.getMapState(mapStack, world);

        BlockPos pos = new BlockPos(x, 0, z);
        BlockState bannerState = Blocks.RED_BANNER.getDefaultState();
        world.setBlockState(pos, bannerState);

        if (mapState != null) {
            mapState.addBanner(world, pos);
            mapState.markDirty();
            player.giveItemStack(mapStack);
        }
    }
}
