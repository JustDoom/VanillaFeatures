package com.justdoom.vanillafeatures.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandData;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentBlockState;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.batch.AbsoluteBlockBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.location.RelativeVec;
import org.jetbrains.annotations.NotNull;

public class SetBlockCommand extends Command {

    private final ArgumentBlockState blockState;
    private final ArgumentRelativeVec3 pos;

    public SetBlockCommand() {
        super("setblock");

        setCondition(Conditions::playerOnly);

        blockState = new ArgumentBlockState("block");
        pos = ArgumentType.RelativeVec3("pos");

        addSyntax(this::execute, blockState, pos);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final RelativeVec relativeVec = context.get(pos);
        final Position position = relativeVec.from((Entity) sender).toPosition();

        final short blockId = context.get(blockState).getBlockId();
        if(sender instanceof Player){
            AbsoluteBlockBatch batch = new AbsoluteBlockBatch();
            batch.setBlock(position.toBlockPosition(), Block.fromStateId(blockId));

            batch.apply(((Player) sender).getInstance(), null);

            context.setReturnData(new CommandData().set("value", "number"));
        } else {
            sender.sendMessage("A player is required to run this command here");
        }
    }
}