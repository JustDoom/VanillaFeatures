package com.justdoom.vanillafeatures;

import com.justdoom.vanillafeatures.commands.GameModeCommand;
import com.justdoom.vanillafeatures.commands.SetBlockCommand;
import com.justdoom.vanillafeatures.commands.TeleportCommand;
import com.justdoom.vanillafeatures.gamedata.loottables.VanillaLootTables;
import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.*;
import net.minestom.server.utils.Position;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class VanillaFeatures extends Extension {

    private static VanillaFeatures instance;

    public static VanillaFeatures getInstance() {
        return instance;
    }

    public CommentedConfigurationNode root;

    public VanillaFeatures(){
        instance = this;
    }

    @Override
    public void initialize(){
        MinecraftServer.getCommandManager().register(new GameModeCommand());
        MinecraftServer.getCommandManager().register(new SetBlockCommand());
        MinecraftServer.getCommandManager().register(new TeleportCommand());

        VanillaLootTables.register(MinecraftServer.getLootTableManager());

        try {
            if(!FileUtil.doesFileExist("./extensions/VanillaFeatures"))
                FileUtil.createDirectory("./extensions/VanillaFeatures");

            if(!FileUtil.doesFileExist("./extensions/VanillaFeatures/config.yml")) {
                getLogger().info("Config not found, creating one now");
                FileUtil.addConfig("./extensions/VanillaFeatures/config.yml");
                getLogger().info("Config created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("./extensions/VanillaFeatures/config.yml")) // Set where we will load and save to
                .build();

        try {
            root = loader.load();
            getLogger().info("Config has been loaded");
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }

        new PlayerInit();

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        if(root.node("join-message", "enabled").getBoolean()) {
            globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
                final Player player = event.getPlayer();

                String msg = root.node("join-message", "message").getString().replaceAll("[{player}]", player.getUsername());
                for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    p.sendMessage(ColoredText.of(ChatColor.YELLOW, msg));
                }
            });
        }

        if(root.node("quit-message", "enabled").getBoolean()) {
            globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
                final Player player = event.getPlayer();

                String msg = root.node("quit-message", "message").getString().replaceAll("[{player}]", player.getUsername());
                for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    p.sendMessage(ColoredText.of(ChatColor.YELLOW, msg));
                }
            });
        }

        getLogger().info("VanillaFeatures has been started");
    }

    @Override
    public void terminate() {
        getLogger().info("VanillaFeatures has been terminated");
    }
}