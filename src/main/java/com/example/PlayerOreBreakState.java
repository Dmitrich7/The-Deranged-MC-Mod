package com.example;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class PlayerOreBreakState extends PersistentState {

    private static final String DIAMOND_ORE_BROKEN_KEY = "diamondOreBroken";
    private final NbtCompound playerData = new NbtCompound();

    public boolean hasBrokenDiamondOre(String playerUuid) {
        return playerData.getBoolean(playerUuid);
    }

    public void setBrokenDiamondOre(String playerUuid) {
        playerData.putBoolean(playerUuid, true);
        markDirty(); // Marks the data as dirty so that it will be saved
    }

    public static PlayerOreBreakState fromNbt(NbtCompound nbt) {
        PlayerOreBreakState state = new PlayerOreBreakState();
        state.readNbt(nbt);
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put(DIAMOND_ORE_BROKEN_KEY, playerData);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains(DIAMOND_ORE_BROKEN_KEY)) {
            playerData.copyFrom(nbt.getCompound(DIAMOND_ORE_BROKEN_KEY));
        }
    }

    // Ensure that this method returns a unique name for your persistent data
    @Override
    public String toString() {
        return "player_ore_break_state";
    }
}
