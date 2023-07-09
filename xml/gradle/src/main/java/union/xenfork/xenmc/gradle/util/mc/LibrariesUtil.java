package union.xenfork.xenmc.gradle.util.mc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import union.xenfork.xenmc.gradle.XenMcGradleExtension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static union.xenfork.xenmc.gradle.util.Other.gson;
import static union.xenfork.xenmc.gradle.util.mc.MinecraftImpl.getJson;

public class LibrariesUtil {
    public static List<String> getLibraries(XenMcGradleExtension extension) {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        for (JsonElement jsonElement : gson.fromJson(getJson(extension), JsonObject.class).get("libraries").getAsJsonArray()) {
            if (jsonElement.getAsJsonObject().has("natives")) {
                continue;
            }
            String name = jsonElement.getAsJsonObject().get("name").getAsString();
            list.put(name.substring(0, name.lastIndexOf(":")), name.substring(name.lastIndexOf(":")));
        }
        List<String> libraries = new ArrayList<>();
        for (var entry : list.entrySet()) {
            libraries.add(entry.getKey() + entry.getValue());
        }
        return libraries;
    }
}
