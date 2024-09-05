package net.aboba.the_deranged_mc;

import net.aboba.the_deranged_mc.items.ModItems;
import net.aboba.the_deranged_mc.blocks.ModBlocks;
import net.aboba.the_deranged_mc.states.PlayerOreBreakState;
import net.aboba.the_deranged_mc.states.PlayerSleepState;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.aboba.the_deranged_mc.items.CustomTreasureMap.giveCustomTreasureMap;

public class TheDerangedMC implements ModInitializer {
	public static final String MOD_ID = "the_deranged_mc";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier PLAYER_ORE_BREAK_STATE_ID = new Identifier(MOD_ID, "player_ore_break_state");
	public static final Identifier PLAYER_SLEEP_STATE_ID = new Identifier(MOD_ID, "sleep_with_broken_ore_state");

	@Override
	public void onInitialize() {

		LOGGER.info("Hello Fabric world!");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		PlayerBlockBreakEvents.AFTER.register(this::onPlayerBreakBlock);
		EntitySleepEvents.STOP_SLEEPING.register(this::onPlayerSleep);

	}

	private void onPlayerBreakBlock(
		World world,
		PlayerEntity player,
		BlockPos blockPos,
		BlockState state,
		BlockEntity blockEntity
	) {
		if (!(state.getBlock().equals(Blocks.DIAMOND_ORE) && player instanceof ServerPlayerEntity)){
			return;
		}

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
		if (oreBreakState.hasNotBrokenDiamondOre(playerUuid)) {
			// Mark as broken
			oreBreakState.setBrokenDiamondOre(playerUuid);

			// Notify the player
			player.sendMessage(Text.literal("You broke a Diamond Ore for the first time!"), false);

			// Save the state (it will automatically be saved because of markDirty())
			stateManager.save();
		}
	}

	// This will be called after a player sleeps
	private void onPlayerSleep(LivingEntity livingEntity, BlockPos pos) {
		// Ensure the entity is a player
		if (!(livingEntity instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) livingEntity;

		// Check that player sleep through night
		if (!(player.getWorld() instanceof ServerWorld)) {
			return;
		}
		ServerWorld serverWorld = (ServerWorld) player.getWorld();

		PersistentStateManager stateManager = serverWorld.getPersistentStateManager();
		PlayerSleepState sleepState = stateManager.getOrCreate(
				PlayerSleepState::fromNbt,
				PlayerSleepState::new,
				PLAYER_SLEEP_STATE_ID.toString()
		);
		PlayerOreBreakState oreBreakState = stateManager.getOrCreate(
				PlayerOreBreakState::fromNbt,
				PlayerOreBreakState::new,
				PLAYER_ORE_BREAK_STATE_ID.toString()
		);

		String playerUuid = player.getUuidAsString();
		// Check if player sleep first time after broke ore
		if (sleepState.hasSleepWithBrokenOre(playerUuid) || oreBreakState.hasNotBrokenDiamondOre(playerUuid)) {
			return;
		}

		long timeOfDay = serverWorld.getTimeOfDay() % 24000;

		// If it's between 0 and 1000 ticks, it's daytime, meaning they slept through the night
		if (timeOfDay >= 0 && timeOfDay < 1000) {
			giveCustomTreasureMap(player, serverWorld, 100, 100);
			player.sendMessage(Text.of("Good morning! You got something mythical!"), false);

			sleepState.setSleepWithBrokenOre(playerUuid);
			stateManager.save();
		}
	}
}