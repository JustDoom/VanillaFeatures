package com.justdoom.vanillafeatures;

import com.justdoom.vanillafeatures.blocks.VanillaBlocks;
import com.justdoom.vanillafeatures.commands.GameModeCommand;
import com.justdoom.vanillafeatures.commands.SetBlockCommand;
import com.justdoom.vanillafeatures.gamedata.loottables.VanillaLootTables;
import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.*;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.Vector;
import net.minestom.server.utils.time.TimeUnit;
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

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        try {
            if(!FileUtil.doesFileExist("./extensions/VanillaFeatures"))
                FileUtil.createDirectory("./extensions/VanillaFeatures");

            if(!FileUtil.doesFileExist("/extensions./VanillaFeatures/config.yml"))
                FileUtil.addConfig("./extensions/VanillaFeatures/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("./extensions/VanillaFeatures/config.yml")) // Set where we will load and save to
                .build();

        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }

        System.out.println("VanillaFeatures has been started");

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();

            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Position(0, 42, 0));
            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                p.sendMessage(ColoredText.of(ChatColor.YELLOW, player.getUsername() + " has joined the game"));
            }
        });

        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                p.sendMessage(ColoredText.of(ChatColor.YELLOW, player.getUsername() + " has left the game"));
            }
        });

        ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
        connectionManager.addPlayerInitialization(player -> {
            player.addEventCallback(PlayerBlockBreakEvent.class, event -> {
                if(player.getGameMode() != GameMode.CREATIVE){
                    VanillaBlocks.dropOnBreak(player.getInstance(), event.getBlockPosition());
                }
            });

            player.addEventCallback(PickupItemEvent.class, event -> {
                boolean couldAdd = player.getInventory().addItemStack(event.getItemStack());
                event.setCancelled(!couldAdd); // Cancel event if player does not have enough inventory space
            });

            player.addEventCallback(ItemDropEvent.class, event -> {
                ItemStack droppedItem = event.getItemStack();

                ItemEntity itemEntity = new ItemEntity(droppedItem, player.getPosition().clone().add(0, 1.5f, 0));
                itemEntity.setPickupDelay(500, TimeUnit.MILLISECOND);
                itemEntity.setInstance(player.getInstance());
                Vector velocity = player.getPosition().clone().getDirection().multiply(6);
                itemEntity.setVelocity(velocity);
            });
        });

        VanillaLootTables.register(MinecraftServer.getLootTableManager());
    }

    @Override
    public void terminate() {
        System.out.println("VanillaFeatures has been terminated");
    }
}