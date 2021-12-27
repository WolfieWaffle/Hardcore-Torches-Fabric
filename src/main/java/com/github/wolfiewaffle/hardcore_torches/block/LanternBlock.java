package com.github.wolfiewaffle.hardcore_torches.block;

import net.minecraft.block.Waterloggable;

public class LanternBlock extends AbstractLanternBlock implements Waterloggable {

    public LanternBlock(Settings settings, boolean isLit) {
        super(settings, isLit);
        this.setDefaultState(this.stateManager.getDefaultState().with(HANGING, false).with(WATERLOGGED, false));
    }
}
