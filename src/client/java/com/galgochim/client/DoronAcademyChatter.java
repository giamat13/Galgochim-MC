package com.galgochim.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * Issue #6: once a minute, players whose Minecraft is set to Hebrew get a chat
 * line from Doron Fishler grumbling about the Academy of the Hebrew Language.
 *
 * <p>This is purely client side. The message is injected straight into the
 * local chat HUD rather than via a {@code /tellraw} command, so it works on any
 * world or server (including SMP) even when the player has no permissions, and
 * it is only ever shown to the player whose own client language is Hebrew.
 */
public final class DoronAcademyChatter {
    private DoronAcademyChatter() {
    }

    /** ~60 seconds at 20 ticks per second. */
    private static final int INTERVAL_TICKS = 1200;
    /** Minecraft's language code for Hebrew (matches our he_il.json). */
    private static final String HEBREW_LANGUAGE = "he_il";

    private static int ticks;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(DoronAcademyChatter::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        // Only count time while actually in a world (the chat HUD needs one).
        if (client.player == null || client.level == null) {
            ticks = 0;
            return;
        }
        if (++ticks < INTERVAL_TICKS) {
            return;
        }
        ticks = 0;

        // Show only to players whose own client is set to Hebrew.
        if (!HEBREW_LANGUAGE.equals(client.options.languageCode)) {
            return;
        }

        // On the client, LocalPlayer.sendSystemMessage routes to the local chat
        // HUD (gui.chatListener().handleSystemMessage) - no packet, no server.
        client.player.sendSystemMessage(Component.translatable("chat.galgochim.doron_academy"));
    }
}
