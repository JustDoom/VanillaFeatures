package com.justdoom.vanillafeatures;

public class PlayerInit {

    public PlayerInit(){

        /**ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
        connectionManager.addPlayerInitialization(player -> {
            if(VanillaFeatures.getInstance().root.node("block-drops", "enabled").getBoolean()) {
                player.addEventCallback(PlayerBlockBreakEvent.class, event -> {
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        VanillaBlocks.dropOnBreak(player.getInstance(), event.getBlockPosition());
                    }
                });
            }

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
        });**/
    }
}
