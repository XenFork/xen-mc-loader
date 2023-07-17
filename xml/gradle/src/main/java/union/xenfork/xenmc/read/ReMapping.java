package union.xenfork.xenmc.read;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipOutputStream;

import static org.objectweb.asm.Opcodes.ASM9;

public class ReMapping {

    private static final ConcurrentHashMap<String, ReMapping> instanceMap = new ConcurrentHashMap<>();
    public static ReMapping getInstance(String name, Map<String, String> map) {
        if (!instanceMap.containsKey(name)) {
            instanceMap.put(name, new ReMapping(map));
        }
        return instanceMap.get(name);
    }

    private final Map<String, String> map;
    private final Map<String, Set<String>> superHashMap = new HashMap<>();
    private ReMapping(Map<String, String> map) {
        this.map = map;
    }

    public void assayJar(File inputFile) {
        try(var jarFile = new JarFile(inputFile)) {


            var entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.isDirectory())
                    continue;
                else if (!jarEntry.getName().endsWith(".class"))
                    continue;
                assay(IoUtil.readBytes(jarFile.getInputStream(jarEntry)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assay(byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(new ClassVisitor(ASM9) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                Set<String> strings = new HashSet<>();
                if (superHashMap.containsKey(name)) {
                    if (superName != null) {
                        if (!superHashMap.get(name).contains(superName)) {
                            strings.add(superName);
                        }
                    }
                    if (interfaces != null) {
                        for (String interface_ : interfaces) {
                            if (!superHashMap.get(name).contains(interface_)) {
                                strings.add(interface_);
                            }
                        }
                    }
                    superHashMap.get(name).addAll(strings);
                } else {
                    if (superName != null) {
                        strings.add(superName);
                    }
                    if (interfaces != null) {
                        Collections.addAll(strings, interfaces);
                    }
                    superHashMap.put(name, strings);
                }
                super.visit(version, access, name, signature, superName, interfaces);
            }
        }, 0);
    }

    public void remappingJar(File input, File out) throws IOException {
        assayJar(input);
        ZipOutputStream jarOutputStream = ZipUtil.getZipOutputStream(FileUtil.getOutputStream(out), StandardCharsets.UTF_8);
        try (var jarFile = new JarFile(input)) {
            var entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                var jarEntry = entries.nextElement();
                if (jarEntry.isDirectory())
                    continue;
                if (jarEntry.getName().endsWith(".class")) {
                    String name = map.get(jarEntry.getName().replace(".class", ""));
                    if (name != null)
                        name += ".class";
                    else
                        name = jarEntry.getName();
                    jarOutputStream.putNextEntry(new JarEntry(name));
                    remappingClass(jarFile.getInputStream(jarEntry), jarOutputStream);
                } else {
                    if (jarEntry.getName().endsWith("MANIFEST.MF"))
                        continue;
                    jarOutputStream.putNextEntry(new JarEntry(jarEntry.getName()));
                    IoUtil.copy(jarFile.getInputStream(jarEntry), jarOutputStream);
                }
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
        }

    }

    public void remappingClass(InputStream input, OutputStream output) throws IOException {
        output.write(remapping(IoUtil.readBytes(input)));
        output.flush();
    }

    public byte[] remapping(byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(0);
        ClassRemapper classRemapper = new ClassRemapper(new ClassVisitor(ASM9, classWriter) {},
                new SimpleRemapper(map) {
                    @Override
                    public String mapFieldName(String owner, String name, String descriptor) {
                        String remappedName = map(owner + "." + name);
                        if (remappedName == null) {
                            if (superHashMap.containsKey(owner)) {
                                for (String s : superHashMap.get(owner)) {
                                    String s1 = mapFieldName(s, name, descriptor);
                                    if (s1 != null) {
                                        return s1;
                                    }
                                }
                            }
                        }
                        return remappedName == null ? name : remappedName;
                    }

                    @Override
                    public String mapMethodName(String owner, String name, String descriptor) {
                        String remappedName = map(owner + "." + name + descriptor);

                        if (remappedName == null) {
                            if (superHashMap.containsKey(owner)) {
                                for (String s : superHashMap.get(owner)) {
                                    String s1 = mapMethodName(s, name, descriptor);
                                    if (s1 != null) {
                                        return s1;
                                    }
                                }
                            }
                        }
                        return remappedName == null ? name : remappedName;
                    }
                });
        classReader.accept(classRemapper, 0);
        return classWriter.toByteArray();
    }

}
