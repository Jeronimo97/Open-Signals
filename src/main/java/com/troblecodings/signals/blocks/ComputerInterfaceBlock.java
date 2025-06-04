package com.troblecodings.signals.blocks;

import java.util.Optional;

import com.troblecodings.core.WriteBuffer;
import com.troblecodings.signals.OpenSignalsMain;
import com.troblecodings.signals.core.TileEntitySupplierWrapper;
import com.troblecodings.signals.init.OSItems;
import com.troblecodings.signals.tileentitys.ComputerinterfaceEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ComputerInterfaceBlock extends BasicBlock {

    public static final TileEntitySupplierWrapper SUPPLIER = ComputerinterfaceEntity::new;

    public ComputerInterfaceBlock() {
        super(Material.IRON);
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos,
            final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final Item item = playerIn.getHeldItemMainhand().getItem();
        if (!(item.equals(OSItems.LINKING_TOOL) || item.equals(OSItems.MULTI_LINKING_TOOL))) {
            if (worldIn.isRemote)
                return true;
            return true;
        }
        return false;
    }

    @Override
    public Optional<TileEntitySupplierWrapper> getSupplierWrapper() {
        return Optional.of(SUPPLIER);
    }

    @Override
    public Optional<String> getSupplierWrapperName() {
        return Optional.of("controller");
    }

    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new ComputerinterfaceEntity();
    }

    public void message(final EntityPlayer player, final String text, final Object... obj) {
        player.sendMessage(getComponent(text, obj));
    }

    public ITextComponent getComponent(final String text, final Object... obj) {
        return new TextComponentTranslation(text, obj);
    }
}
