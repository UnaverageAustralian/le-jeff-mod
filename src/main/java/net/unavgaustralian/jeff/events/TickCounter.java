package net.unavgaustralian.jeff.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.unavgaustralian.jeff.LeJeff;

public class TickCounter extends PersistentState {
    private int tickCounter = 0;
    private int tickToWait = generateRandomTick();

    private static int generateRandomTick() {
        return (int) (Math.random() * 1500) + 3250;
    }

    public static TickCounter createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        TickCounter state = new TickCounter();
        state.tickCounter = tag.getInt("tickCounter");
        state.tickToWait = tag.getInt("tickToWait");
        return state;
    }

    public static TickCounter createNew() {
        return new TickCounter();
    }

    private static final Type<TickCounter> type = new Type<>(
            TickCounter::createNew,
            TickCounter::createFromNbt,
            null
    );

    public static TickCounter getServerState(MinecraftServer server) {
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        assert serverWorld != null;

        TickCounter state = serverWorld.getPersistentStateManager().getOrCreate(type, LeJeff.MOD_ID);

        state.markDirty();

        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt("tickCounter", this.tickCounter);
        nbt.putInt("tickToWait", this.tickToWait);
        return nbt;
    }

    public void incrementTick(ServerWorld world) {
        this.tickCounter++;
        System.out.println("Tick: " + this.tickCounter + "\n Tick to wait: " + tickToWait);
        if (this.tickCounter >= this.tickToWait) {
            this.tickCounter = 0;
            this.tickToWait = generateRandomTick();
            Void.replaceRandomBlock(world.getServer());
        }
        markDirty();
    }

    public static void registerTickEvent() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (world.getRegistryKey() == World.OVERWORLD) {
                TickCounter.getServerState(world.getServer()).incrementTick(world);
            }
        });
    }
}
