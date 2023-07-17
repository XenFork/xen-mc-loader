package union.xenfork.xenmc.step.s2;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.XmlUtil;
import org.gradle.api.Project;
import org.w3c.dom.*;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.util.Utils;
import union.xenfork.xenmc.read.MappingTableToXml;
import union.xenfork.xenmc.util.XenBiMap;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.io.File.separator;

public class TableRWD implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        if (minecraft.xenmc.remapTypesDir == null) {
            minecraft.xenmc.remapTypesDir = new File(minecraft.xenmc.cacheHome, "remapof" + separator + minecraft.version);
        }
        String NAME_LINE = "^.+:";
        Utils.setupMessagePrefix(project, minecraft);
//        new ClassReader(MinecraftExtension.clientGame.toPath(), new File(MinecraftExtension.game, "client").toPath()).cfr();
        MappingTableToXml mappingTableToXml = new MappingTableToXml();
        if (minecraft.xenmc.remapTypesOf.equals("xml")) {
            File file = new File(minecraft.xenmc.resourcesHome, "xenmc.xml");
            File dest = new File(minecraft.xenmc.remapTypesDir, "xenmc.xml");
            List<String> lines = FileUtil.readLines(MinecraftExtension.clientMapping, StandardCharsets.UTF_8);
            if (minecraft.xenmc.getIsLoader()) {
                if (!file.exists()) {
                    Document xml = XmlUtil.createXml();
                    Element data = xml.createElement("data");
                    data.setAttribute("version", minecraft.version + "-" + LocalDateTimeUtil.now());

                    List<String> packages_ = mappingTableToXml.packages();

                    Element packages = xml.createElement("packages");
                    for (String pkg : packages_) {
                        Element name = xml.createElement("name");
                        name.setAttribute("id", pkg);
                        packages.appendChild(name);
                    }
                    data.appendChild(packages);
                    Element classes = xml.createElement("classes");

                    for (Map.Entry<String, String> stringStringEntry : mappingTableToXml.getSrgMap()) {
                        Element name = xml.createElement("name");
                        name.setAttribute("id", stringStringEntry.getValue());
                        name.setAttribute("nature", stringStringEntry.getKey());

                        Element field = xml.createElement("field");
                        XenBiMap<String, XenBiMap<String, String>> entries = mappingTableToXml.fieldSrgMap().containsKey(stringStringEntry.getKey()) ? mappingTableToXml.fieldSrgMap().get(stringStringEntry.getKey()) : new XenBiMap<>(new HashMap<>());
                        for (var entry1 : entries) {
                            for (Map.Entry<String, String> entry2 : entry1.getValue()) {
                                Element name1 = xml.createElement("name");
                                name1.setAttribute("id", entry1.getKey());
                                name1.setAttribute("nature", entry2.getKey());
                                name1.setAttribute("type", entry2.getValue());
                                field.appendChild(name1);
                            }
                        }
                        Element method = xml.createElement("method");
                        XenBiMap<String, XenBiMap<String, XenBiMap<String, ArrayList<String>>>> entries1 = mappingTableToXml.methodSrgMap().containsKey(stringStringEntry.getKey()) ? mappingTableToXml.methodSrgMap().get(stringStringEntry.getKey()) : new XenBiMap<>(new HashMap<>());
                        for (Map.Entry<String, XenBiMap<String, XenBiMap<String, ArrayList<String>>>> entries2 : entries1) {
                            for (Map.Entry<String, XenBiMap<String, ArrayList<String>>> entries3 : entries2.getValue()) {
                                for (Map.Entry<String, ArrayList<String>> entries4 : entries3.getValue()) {
                                    Element name1 = xml.createElement("name");
                                    name1.setAttribute("id", entries2.getKey());
                                    name1.setAttribute("nature", entries3.getKey());
                                    name1.setAttribute("type", entries4.getKey());
                                    for (String s : entries4.getValue()) {
                                        Element args = xml.createElement("args");
                                        args.setAttribute("id", s);
                                        name1.appendChild(args);
                                    }
                                    method.appendChild(name1);
//                                    System.out.printf("%s:%s:%s:%n", entries2.getKey(), entries3.getKey(), entries4.getKey());
                                }
                            }
                        }
                        name.appendChild(field);
                        name.appendChild(method);
                        classes.appendChild(name);
                    }

                    data.appendChild(classes);
                    xml.appendChild(data);


                    XmlUtil.toFile(xml, String.valueOf(file));
                }

                FileUtil.copyFile(file, dest, StandardCopyOption.REPLACE_EXISTING);
            } else {
                BufferedInputStream in = new BufferedInputStream(Objects.requireNonNull(this.getClass().getResourceAsStream("xenmc.xml")));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
                out.write(in.readAllBytes());
                out.close();
                in.close();
            }
        }



//        if (minecraft.xenmc.remapTypesOf.equals("csv")) {
//
//        }

    }
}
