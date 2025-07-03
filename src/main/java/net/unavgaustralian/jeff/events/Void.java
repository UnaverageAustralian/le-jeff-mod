package net.unavgaustralian.jeff.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.unavgaustralian.jeff.block.ModBlocks;

import java.util.Objects;
import java.util.Random;

public class Void {
    public static void replaceRandomBlock(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        Random random = new Random();

        BlockPos center = BlockPos.ofFloored(Objects.requireNonNull(world.getRandomAlivePlayer()).getPos());

        for (int attempts = 0; attempts < 1000; attempts++) {
            int dx = random.nextInt(201) - 100;
            int dy = random.nextInt(201) - 100;
            int dz = random.nextInt(201) - 100;
            BlockPos pos = center.add(dx, dy, dz);

            BlockState state = world.getBlockState(pos);
            if (state.isAir() || !state.isOpaque()) {
                continue;
            }

            boolean adjacentToAir = false;
            for (Direction direction : Direction.values()) {
                BlockState adjacentState = world.getBlockState(pos.offset(direction));
                if (adjacentState.isAir()) {
                    adjacentToAir = true;
                    if (adjacentState.getBlock() == Blocks.CAVE_AIR || adjacentState.getBlock() == Blocks.VOID_AIR) {
                        adjacentToAir = false;
                        break;
                    }
                }
            }

            if (adjacentToAir) {
                world.setBlockState(pos, ModBlocks.VOID_BLOCK.getDefaultState());
                System.out.println("Replaced block at: " + pos);
                break;
            }
        }
    }
}
