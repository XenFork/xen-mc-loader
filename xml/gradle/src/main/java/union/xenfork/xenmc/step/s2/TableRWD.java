package union.xenfork.xenmc.step.s2;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpUtil;
import org.gradle.api.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;
import union.xenfork.xenmc.read.ClassReader;
import union.xenfork.xenmc.read.MappingTable;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.io.File.separator;

public class TableRWD implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        if (minecraft.xenmc.remapTypesDir == null) {
            minecraft.xenmc.remapTypesDir = new File(minecraft.xenmc.cacheHome, "remapof" + separator + minecraft.version);
        }
        Utils.setupMessagePrefix(project, minecraft);
//        new ClassReader(MinecraftExtension.clientGame.toPath(), new File(MinecraftExtension.game, "client").toPath()).cfr();
        if (minecraft.xenmc.remapTypesOf.equals("xml")) {
            File file = new File(minecraft.xenmc.remapTypesDir, "xenmc.xml");
            if (minecraft.xenmc.getIsLoader()) {
                if (!file.exists()) {
                    Document xml = XmlUtil.createXml();
                    Element element = xml.createElement("data");
                    element.setAttribute("version", minecraft.version + "-" + LocalDateTimeUtil.now());
                    Element element1 = xml.createElement("packages");
                    element1.setAttribute("name", "a");
                    element.appendChild(element1);
                    xml.appendChild(element);
                    XmlUtil.toFile(xml, String.valueOf(file));
                }
                FileUtil.copyFile(file, new File(minecraft.xenmc.resourcesHome, "xenmc.xml"), StandardCopyOption.REPLACE_EXISTING);
            } else {
                BufferedInputStream in = new BufferedInputStream(Objects.requireNonNull(this.getClass().getResourceAsStream("xenmc.xml")));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
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
