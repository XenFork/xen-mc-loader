package union.xenfork.xenmc.gradle.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import union.xenfork.xenmc.gradle.XenMcGradleExtension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static union.xenfork.xenmc.gradle.util.MinecraftImpl.getJson;

public class LibrariesUtil {
    public static List<String> getLibraries(XenMcGradleExtension extension) {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        for (JsonElement jsonElement : new Gson().fromJson(getJson(extension), JsonObject.class).get("libraries").getAsJsonArray()) {
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
