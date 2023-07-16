package union.xenfork.xenmc.read;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.util.getopt.OptionsImpl;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.printer.Printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ClassReader {
    public final Path fileJar, outDir;
    private ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();
    // 存放字节码
    private HashMap<String,byte[]> classByteMap = new HashMap<>();

    public ClassReader(Path fileJar, Path outDir) throws IOException {
        this.fileJar = fileJar;
        if (Files.isDirectory(outDir)) {
            this.outDir = outDir;
        } else if (!Files.exists(outDir)) {
            Files.createDirectories(outDir);
            this.outDir = outDir;
        } else {
            this.outDir = outDir.getParent();
        }
    }



    @SuppressWarnings("UnusedReturnValue")
    public Long cfr() {

        Long start = System.currentTimeMillis();
        // source jar
        List<String> files = new ArrayList<>();
        files.add(fileJar.toString());
        // target dir
        HashMap<String, String> outputMap = new HashMap<>();
        outputMap.put("outputdir", outDir.toString());

        OptionsImpl options = new OptionsImpl(outputMap);
        CfrDriver cfrDriver = new CfrDriver.Builder().withBuiltOptions(options).build();
        cfrDriver.analyse(files);
        Long end = System.currentTimeMillis();
        return (end - start);
    }

    @Deprecated(since = "error to decompiler", forRemoval = true)
    public Long decompiler() throws Exception {
        long start = System.currentTimeMillis();
        // 解压
        archive(fileJar.toFile());
        for (String className : classByteMap.keySet()) {
            String path = StringUtils.substringBeforeLast(className, "/");
            String name = StringUtils.substringAfterLast(className, "/");
            if (StringUtils.contains(name, "$")) {
                name = StringUtils.substringAfterLast(name, "$");
            }
            name = StringUtils.replace(name, ".class", ".java");
            decompiler.decompile(loader, printer, className);
            String context = printer.toString();

            Path targetPath = outDir.resolve(path).resolve(name);
            Path resolve = outDir.resolve(path);
            if (!Files.exists(resolve)) {
                Files.createDirectories(resolve);
            }
            Files.deleteIfExists(targetPath);
            Files.createFile(targetPath);
            Files.write(targetPath, context.getBytes());
        }
        return System.currentTimeMillis() - start;
    }
    @Deprecated(since = "error to decompiler", forRemoval = true)
    private void archive(File path) throws IOException {
        try (ZipFile archive = new JarFile(path)) {
            Enumeration<? extends ZipEntry> entries = archive.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    if (name.endsWith(".class")) {
                        byte[] bytes = null;
                        try (InputStream stream = archive.getInputStream(entry)) {
                            bytes = IOUtils.toByteArray(stream);
                        }
                        classByteMap.put(name, bytes);
                    }
                }
            }
        }
    }

    private final Printer printer = new Printer() {
        static final String TAB = "  ";
        static final String NEWLINE = "\n";
        int indentationCount = 0;
        StringBuilder sb = new StringBuilder();
        @Override public String toString() {
            String toString = sb.toString();
            sb = new StringBuilder();
            return toString;
        }
        @Override public void start(int maxLineNumber, int majorVersion, int minorVersion) {}
        @Override public void end() {}
        @Override public void printText(String text) { sb.append(text); }
        @Override public void printNumericConstant(String constant) { sb.append(constant); }
        @Override public void printStringConstant(String constant, String ownerInternalName) { sb.append(constant); }
        @Override public void printKeyword(String keyword) { sb.append(keyword); }
        @Override public void printDeclaration(int type, String internalTypeName, String name, String descriptor) { sb.append(name); }
        @Override public void printReference(int type, String internalTypeName, String name, String descriptor, String ownerInternalName) { sb.append(name); }
        @Override public void indent() { this.indentationCount++; }
        @Override public void unindent() { this.indentationCount--; }
        @Override public void startLine(int lineNumber) { for (int i=0; i<indentationCount; i++) sb.append(TAB); }
        @Override public void endLine() { sb.append(NEWLINE); }
        @Override public void extraLine(int count) { while (count-- > 0) sb.append(NEWLINE); }
        @Override public void startMarker(int type) {}
        @Override public void endMarker(int type) {}
    };

    private final Loader loader = new Loader() {
        @Override
        public byte[] load(String internalName) {
            return classByteMap.get(internalName);
        }
        @Override
        public boolean canLoad(String internalName) {
            return classByteMap.containsKey(internalName);
        }
    };
}
