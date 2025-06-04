package com.troblecodings.signals.cctweaked;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.troblecodings.signals.blocks.ComputerInterfaceBlock;
import com.troblecodings.signals.tileentitys.ComputerinterfaceEntity;

public class ComputerInterfacePeripheralProvider implements IPeripheralProvider {

    @Override
    @Nullable
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        if (!(world.getBlockState(pos).getBlock() instanceof ComputerInterfaceBlock))
            return null;

        ComputerinterfaceEntity tileEntity = (ComputerinterfaceEntity) world.getTileEntity(pos);
        if (tileEntity == null)
            return null;

        return new ComputerInterfacePeripheral(tileEntity);
    }
}