package com.github.wolfiewaffle.hardcore_torches.block;

import net.minecraft.block.Waterloggable;

import java.util.function.IntSupplier;

public class LanternBlock extends AbstractLanternBlock implements Waterloggable {

    public LanternBlock(Settings settings, boolean isLit, IntSupplier maxFuel) {
        super(settings, isLit, maxFuel);
        this.setDefaultState(this.stateManager.getDefaultState().with(HANGING, false).with(WATERLOGGED, false));
    }
}
