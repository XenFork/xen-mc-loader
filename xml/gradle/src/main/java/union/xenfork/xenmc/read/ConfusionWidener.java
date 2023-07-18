package union.xenfork.xenmc.read;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.util.XenBiMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfusionWidener {
    public XenBiMap<String, String> typeRename = new XenBiMap<>(new HashMap<>());
    public XenBiMap<String, String> typeExtends = new XenBiMap<>(new HashMap<>());
    public XenBiMap<String, String> typeName = new XenBiMap<>(new HashMap<>());
    public ConfusionWidener(@NotNull MinecraftExtension minecraft) throws Exception {
        if (minecraft.xenmc.getIsLoader()) {
            File file = new File(minecraft.xenmc.resourcesHome, "loader.confusionWidener");
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) parentFile.mkdirs();
            if (!file.exists()) file.createNewFile();

        } else {
            BufferedReader utf8Reader = ResourceUtil.getUtf8Reader("loader.confusionWidener");
            for (String s : utf8Reader.lines().toList()) {
                if (s.startsWith("#"))
                    continue;

                if (s.contains("|")) {
                    String[] split = s.split("\\|");
                    if (split.length == 4) {
                        typeRename.put(split[0], split[1]);
                        typeExtends.put(split[0], split[2]);
                        typeName.put(split[0], split[3]);
                    }
                }
            }
        }
    }
}
