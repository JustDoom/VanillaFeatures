package com.justdoom.vanillafeatures.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandData;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentBlockState;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.batch.AbsoluteBlockBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.entity.EntityFinder;
import net.minestom.server.utils.location.RelativeVec;
import org.jetbrains.annotations.NotNull;

public class GameModeCommand extends Command {

    private final ArgumentEnum gamemode;
    private final ArgumentEntity other;

    public GameModeCommand() {
        super("gamemode");

        setCondition(Conditions::playerOnly);

        gamemode = ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED);
        other = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);

        addSyntax(this::executeSelf, gamemode);
        addSyntax(this::executeOther, gamemode, other);
    }

    private void executeSelf(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final GameMode gamemode = context.get("gamemode");
        assert gamemode != null;
        if(sender instanceof Player){
            ((Player) sender).setGameMode(gamemode);
            sender.sendMessage("Set own game mode to " + gamemode.name().toLowerCase() + " Mode");
        } else {
            sender.sendMessage("A player is required to run this command here");
        }
    }

    private void executeOther(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final GameMode gamemode = context.get("gamemode");
        final EntityFinder targetFinder = context.get("player");
        Player player = targetFinder.findFirstPlayer(sender);
        if(sender instanceof Player){
            player.setGameMode(gamemode);
            sender.sendMessage("Set " + player.getUsername() + "'s game mode to " + gamemode.name().toLowerCase() + " Mode");
        } else {
            sender.sendMessage("A player is required to run this command here");
        }
    }
}