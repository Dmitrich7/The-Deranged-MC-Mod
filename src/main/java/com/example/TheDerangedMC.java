package com.example;

import com.example.item.ModItems;
import com.example.block.ModBlocks;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;
import net.minecraft.world.PersistentStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheDerangedMC implements ModInitializer {
	public static final String MODID = "the_deranged_mc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final Identifier PLAYER_ORE_BREAK_STATE_ID = new Identifier(MODID, "player_ore_break_state");



	@Override
	public void onInitialize() {

		LOGGER.info("Hello Fabric world!");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			if (state.getBlock().equals(Blocks.DIAMOND_ORE) && player instanceof ServerPlayerEntity){
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
				ServerWorld serverWorld = (ServerWorld) world;

				// Access the PersistentStateManager from the world
				PersistentStateManager stateManager = serverWorld.getPersistentStateManager();

				// Load or create the PlayerOreBreakState
				PlayerOreBreakState oreBreakState = stateManager.getOrCreate(
                        PlayerOreBreakState::fromNbt,
						PlayerOreBreakState::new,
						PLAYER_ORE_BREAK_STATE_ID.toString()
				);

				String playerUuid = serverPlayer.getUuidAsString();
				if (!oreBreakState.hasBrokenDiamondOre(playerUuid)) {
					// Mark as broken
					oreBreakState.setBrokenDiamondOre(playerUuid);

					// Notify the player
					player.sendMessage(Text.literal("You broke a Diamond Ore for the first time!"), false);

					// Save the state (it will automatically be saved because of markDirty())
					stateManager.save();
				}
			}

		});

	}

}