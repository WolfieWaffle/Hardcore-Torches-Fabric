//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.block;

import java.util.function.IntSupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;

public class LanternBlock extends AbstractLanternBlock implements Waterloggable {
  public LanternBlock(AbstractBlock.Settings settings, boolean isLit, IntSupplier maxFuel) {
    super(settings, isLit, maxFuel);
    this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(HANGING, false)).with(WATERLOGGED, false));
  }
}
