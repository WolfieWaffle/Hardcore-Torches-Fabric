//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

public class FatLootNumberProvider implements LootNumberProvider {
  int[] choices;
  Random random = new Random();

  public FatLootNumberProvider(int[] choices) {
    this.choices = choices;
  }

  public float nextFloat(LootContext context) {
    return (float)this.nextInt(context);
  }

  public int nextInt(LootContext context) {
    return this.choices[this.random.nextInt(this.choices.length)];
  }

  public LootNumberProviderType getType() {
    return HCTLootNumberProviderTypes.FAT;
  }

  public static class Serializer implements JsonSerializer<FatLootNumberProvider> {
    public Serializer() {
    }

    public void toJson(JsonObject object, FatLootNumberProvider instance, JsonSerializationContext jsonSerializationContext) {
      JsonArray choices = new JsonArray();

      for(int i = 0; i < instance.choices.length; ++i) {
        choices.add(instance.choices[i]);
      }

      object.add("choices", choices);
    }

    public FatLootNumberProvider fromJson(JsonObject object, JsonDeserializationContext jsonDeserializationContext) {
      JsonArray array = JsonHelper.asArray(object, "choices");
      int[] choices = new int[array.size()];

      for(int i = 0; i < array.size(); ++i) {
        choices[i] = array.get(i).getAsInt();
      }

      return new FatLootNumberProvider(choices);
    }
  }
}
