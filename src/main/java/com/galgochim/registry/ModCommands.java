package com.galgochim.registry;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Registers the joke commands from issue #1. The spellings are intentionally
 * kept as written in the issue (/tooglehgit, /tooglealians).
 */
public final class ModCommands {
    private ModCommands() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("tooglehgit").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                ModEvents.hagitEnabled = !ModEvents.hagitEnabled;
                ModEvents.summonHagit(player.serverLevel(), player);
                boolean on = ModEvents.hagitEnabled;
                ctx.getSource().sendSuccess(
                        () -> Component.literal("Hagit fly-overs: " + (on ? "ON" : "OFF")), false);
                return Command.SINGLE_SUCCESS;
            }));

            dispatcher.register(Commands.literal("tooglealians").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                ModEvents.aliensEnabled = !ModEvents.aliensEnabled;
                ModEvents.summonAlien(player.serverLevel(), player);
                boolean on = ModEvents.aliensEnabled;
                ctx.getSource().sendSuccess(
                        () -> Component.literal("Alien fly-overs: " + (on ? "ON" : "OFF")), false);
                return Command.SINGLE_SUCCESS;
            }));
        });
    }
}
