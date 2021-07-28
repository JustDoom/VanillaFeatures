package com.justdoom.vanillafeatures.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand extends Command {

    public TeleportCommand() {
        super("teleport");

        setCondition(Conditions::playerOnly);

        ArgumentEntity tpTo = ArgumentType.Entity("to").onlyPlayers(true).singleEntity(true);
        ArgumentEntity tpTarget = ArgumentType.Entity("target").onlyPlayers(true).singleEntity(true);

        addSyntax(this::executeSelf, tpTo);
        addSyntax(this::executeOther, tpTarget, tpTo);
    }

    private void executeSelf(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = context.get("to");
        if(!(sender instanceof Player)) {
            sender.sendMessage("A player is required to run this command here");
            return;
        }

        if(!player.hasPermission("vanillafeatures.teleport")){
            player.sendMessage("You do not have permission to run this command");
            return;
        }

        sender.asPlayer().setInstance(player.getInstance());
        sender.asPlayer().teleport(player.getPosition());
        sender.sendMessage("Teleported to " + player.getUsername());
    }

    private void executeOther(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Player player = context.get("to");
        final Player playerTarget = context.get("target");
        if(!(sender instanceof Player)) {
            sender.sendMessage("A player is required to run this command here");
            return;
        }

        if(!player.hasPermission("vanillafeatures.teleport")){
            player.sendMessage("You do not have permission to run this command");
            return;
        }

        playerTarget.setInstance(player.getInstance());
        playerTarget.teleport(player.getPosition());

        sender.sendMessage("Teleported " + playerTarget.getUsername() + " to " + player.getUsername());
    }
}