package com.troblecodings.signals.tileentitys;

import com.troblecodings.guilib.ecs.interfaces.ISyncable;
import com.troblecodings.linkableapi.ILinkableTile;
import com.troblecodings.signals.OpenSignalsMain;
import com.troblecodings.signals.blocks.ComputerInterfaceBlock;
import com.troblecodings.signals.blocks.Signal;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class ComputerinterfaceEntity extends SyncableTileEntity implements ISyncable, ILinkableTile {

    private BlockPos linkedSignalPosition = null;
    private Signal linkedSignal = null;
    private static final String LINKED_POS_X = "linkedPosX";
    private static final String LINKED_POS_Y = "linkedPosY";
    private static final String LINKED_POS_Z = "linkedPosZ";
    private static final String LINKED_SIGNAL = "linkedSignal";

    public ComputerinterfaceEntity() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        // Read linked position if it exists
        if (compound.hasKey(LINKED_POS_X) && compound.hasKey(LINKED_POS_Y) && compound.hasKey(LINKED_POS_Z)) {
            int x = compound.getInteger(LINKED_POS_X);
            int y = compound.getInteger(LINKED_POS_Y);
            int z = compound.getInteger(LINKED_POS_Z);
            linkedSignalPosition = new BlockPos(x, y, z);
            
            // Read linked signal type if it exists
            if (compound.hasKey(LINKED_SIGNAL)) {
                String signalName = compound.getString(LINKED_SIGNAL);
                Block block = Block.REGISTRY.getObject(new ResourceLocation(OpenSignalsMain.MODID, signalName));
                if (block instanceof Signal) {
                    linkedSignal = (Signal) block;
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        
        // Save linked position if it exists
        if (linkedSignalPosition != null) {
            compound.setInteger(LINKED_POS_X, linkedSignalPosition.getX());
            compound.setInteger(LINKED_POS_Y, linkedSignalPosition.getY());
            compound.setInteger(LINKED_POS_Z, linkedSignalPosition.getZ());
            
            // Save linked signal type if it exists
            if (linkedSignal != null) {
                ResourceLocation registryName = linkedSignal.getRegistryName();
                if (registryName != null) {
                    compound.setString(LINKED_SIGNAL, registryName.getResourcePath());
                }
            }
        }
        
        return compound;
    }

    @Override
    public boolean hasLink() {
        return linkedSignalPosition != null;
    }

    @Override
    public boolean unlink() {
        linkedSignal = null;
        linkedSignalPosition = null;
        markDirty(); // Mark the tile entity as dirty so it will be saved
        return true;
    }

    @Override
    public boolean isValid(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean link(final BlockPos pos, final NBTTagCompound tag) {
        final Block block = Block.REGISTRY.getObject(
                new ResourceLocation(OpenSignalsMain.MODID, tag.getString(pos.toString())));
        if (block != null && block instanceof Signal) {
            unlink();
            linkedSignalPosition = pos;
            linkedSignal = (Signal) block;
            markDirty(); // Mark the tile entity as dirty so it will be saved
            onLoad();
            return true;
        } else if (block instanceof ComputerInterfaceBlock) {
            loadChunkAndGetTile(RedstoneIOTileEntity.class, world, pos,
                    (tile, _u) -> tile.linkController(getPos()));
            return true;
        }
        return false;
    }

    public Signal getLinkedSignal() {
        return linkedSignal;
    }

    public BlockPos getLinkedPosition() {
        return linkedSignalPosition;
    }
}
