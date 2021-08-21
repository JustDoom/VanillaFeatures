/**package com.justdoom.vanillafeatures.entity;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.CustomBlock;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Position;

import java.util.Random;

public class FallingBlockEntity extends LivingEntity {
    private final Block baseBlock;
    private final CustomBlock toPlace;

    public FallingBlockEntity(Block baseBlock, CustomBlock toPlace, Position initialPosition) {
        super(EntityType.FALLING_BLOCK, initialPosition);
        this.baseBlock = baseBlock;
        this.toPlace = toPlace;
        //setGravity(0.025f, getGravityAcceleration());
        setBoundingBox(0.98f, 0.98f, 0.98f);
    }

    @Override
    public void update(long time) {
        if(isOnGround()) {
            BlockPosition position = getPosition().toBlockPosition().subtract(0, 1, 0);
            if(instance.getBlockStateId(position) != Block.AIR.getBlockId()) {
                // landed on non-full block, break into item
                Material correspondingItem = Material.valueOf(baseBlock.name()); // TODO: ugly way of finding corresponding item, change
                ItemStack stack = ItemStack.of(correspondingItem, (byte) 1);
                ItemEntity itemForm = new ItemEntity(stack, new Position(position.getX()+0.5f, position.getY(), position.getZ()+0.5f));

                Random rng = new Random();
                itemForm.getVelocity().setX((float) rng.nextGaussian()*2f);
                itemForm.getVelocity().setY(rng.nextFloat()*2.5f+2.5f);
                itemForm.getVelocity().setZ((float) rng.nextGaussian()*2f);

                itemForm.setInstance(instance);
            } else {
                if(toPlace != null) {
                    instance.setSeparateBlocks(position.getX(), position.getY(), position.getZ(), baseBlock.getBlockId(), toPlace.getCustomBlockId());
                } else {
                    instance.setBlock(getPosition().toBlockPosition(), baseBlock);
                }
            }
            remove();
        }
    }
}**/