package net.aboba.the_deranged_mc.states;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class PlayerSleepState extends PersistentState {

    private static final String SLEEP_WITH_BROKEN_ORE_KEY = "SleepWithBrokenOre";
    private final NbtCompound playerData = new NbtCompound();

    public boolean hasSleepWithBrokenOre(String playerUuid) {
        return playerData.getBoolean(playerUuid);
    }

    public void setSleepWithBrokenOre(String playerUuid) {
        playerData.putBoolean(playerUuid, true);
        markDirty(); // Marks the data as dirty so that it will be saved
    }

    public static PlayerSleepState fromNbt(NbtCompound nbt) {
        PlayerSleepState state = new PlayerSleepState();
        state.readNbt(nbt);
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put(SLEEP_WITH_BROKEN_ORE_KEY, playerData);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains(SLEEP_WITH_BROKEN_ORE_KEY)) {
            playerData.copyFrom(nbt.getCompound(SLEEP_WITH_BROKEN_ORE_KEY));
        }
    }

    // Ensure that this method returns a unique name for your persistent data
    @Override
    public String toString() {
        return "sleep_with_broken_ore_state";
    }
}
