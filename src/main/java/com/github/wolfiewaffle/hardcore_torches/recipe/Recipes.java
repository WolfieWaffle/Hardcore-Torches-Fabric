//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import net.minecraft.util.Identifier;

public class Recipes {
  public Recipes() {
  }

  public static JsonObject createShapedRecipeJson(ArrayList<Character> keys, ArrayList<Identifier> items, ArrayList<String> type, ArrayList<String> pattern, Identifier output, int count) {
    JsonObject json = new JsonObject();
    json.addProperty("type", "minecraft:crafting_shaped");
    JsonArray jsonArray = new JsonArray();
    jsonArray.add((String)pattern.get(0));
    if (pattern.size() > 1) {
      jsonArray.add((String)pattern.get(1));
    }

    if (pattern.size() > 2) {
      jsonArray.add((String)pattern.get(2));
    }

    json.add("pattern", jsonArray);
    JsonObject keyList = new JsonObject();

    for(int i = 0; i < keys.size(); ++i) {
      JsonObject individualKey = new JsonObject();
      individualKey.addProperty((String)type.get(i), ((Identifier)items.get(i)).toString());
      keyList.add("" + keys.get(i), individualKey);
    }

    json.add("key", keyList);
    JsonObject result = new JsonObject();
    result.addProperty("item", output.toString());
    result.addProperty("count", count);
    json.add("result", result);
    return json;
  }
}
