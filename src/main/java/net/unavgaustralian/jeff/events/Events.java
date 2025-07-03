package net.unavgaustralian.jeff.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Events {
    private static int tickCounter = 0;
    private static int tickToWait = GenerateRandomTick();

    private static int GenerateRandomTick() {
        double ranTick = Math.random();
        ranTick *= 2500;
        ranTick = Math.floor(ranTick);
        ranTick += 3250;
        return (int)ranTick;
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            System.out.println("Tick: " + tickCounter + "\n Tick to wait: " + tickToWait);
            if (tickCounter >= tickToWait) {
                tickCounter = 0;
                tickToWait = GenerateRandomTick();
                Void.replaceRandomBlock(server);
            }
        });
    }
}
