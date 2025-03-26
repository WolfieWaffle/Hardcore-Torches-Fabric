//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.blockentity;

import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FuelBlockEntity extends BlockEntity {
  protected int fuel;
  protected static Random random = new Random();

  public FuelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  public void writeNbt(NbtCompound tag) {
    super.writeNbt(tag);
    tag.putInt("Fuel", this.fuel);
  }

  public void readNbt(NbtCompound tag) {
    super.readNbt(tag);
    if (tag.contains("number")) {
      this.fuel = tag.getInt("number");
    } else {
      this.fuel = tag.getInt("Fuel");
    }

  }

  public int getFuel() {
    return this.fuel;
  }

  public void setFuel(int newValue) {
    this.fuel = newValue;
  }

  public void changeFuel(int increment) {
    World world = this.getWorld();
    BlockPos pos = this.getPos();
    this.fuel += increment;
    if (this.fuel <= 0) {
      this.fuel = 0;
      if (world.getBlockState(pos).getBlock() instanceof IFuelBlock) {
        IFuelBlock block = (IFuelBlock)world.getBlockState(pos).getBlock();
        block.outOfFuel(world, pos, world.getBlockState(pos), false);
      }
    }

  }
}
