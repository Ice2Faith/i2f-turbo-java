package i2f.maven.plugin.spi;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maven plugin mojo that automatically scans compiled classes for {@code @Spi} annotations
 * and generates corresponding {@code META-INF/services/} descriptor files for Java SPI.
 *
 * <p>This eliminates the need to manually maintain service provider configuration files.
 * When a class is annotated with {@code @Spi}, this plugin extracts the declared SPI interfaces
 * and registers the class as a service provider for each interface.</p>
 *
 * <p>The plugin supports merging with existing service files — manually created entries
 * are preserved and combined with auto-discovered ones.</p>
 *
 * @author Ice2Faith
 * @date 2026/9/9 16:20
 */
@Mojo(
        name = "spi",
        defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyResolution = ResolutionScope.COMPILE
)
public class SpiComponentScanMojo extends AbstractMojo {

    private static final String SERVICES_PATH = "META-INF/services/";
    private static final String CLASS_FILE_SUFFIX = ".class";
    private static final String VALUE_ATTRIBUTE = "value";

    /**
     * Fully qualified name of the SPI annotation to scan for.
     * Can be overridden via the {@code spi.annotation} system property.
     */
    @Parameter(property = "spi.annotation", defaultValue = "i2f.spi.annotations.Spi")
    private String spiAnnotation;

    /**
     * Compiled classes output directory (typically {@code target/classes}).
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", readonly = true)
    private File classesDirectory;

    @Override
    public void execute() throws MojoExecutionException {
        if (!classesDirectory.exists()) {
            getLog().warn("Classes directory does not exist: " + classesDirectory);
            return;
        }

        String annotationDescriptor = toAnnotationDescriptor(spiAnnotation);
        Map<String, List<String>> spiMappings = scanForSpiImplementations(annotationDescriptor);

        if (spiMappings.isEmpty()) {
            getLog().info("No @Spi annotated classes found, skipping service file generation.");
            return;
        }

        writeServiceFiles(spiMappings);
        getLog().info("SPI auto-registration completed. Total interfaces: " + spiMappings.size());
    }

    // ========================================================================
    // Scanning
    // ========================================================================

    /**
     * Walks the classes directory and scans all {@code .class} files for {@code @Spi} annotations.
     *
     * @param annotationDescriptor ASM descriptor of the target annotation (e.g. {@code Li2f/spi/annotations/Spi;})
     * @return mapping from SPI interface name to list of implementation class names
     * @throws MojoExecutionException if an I/O error occurs during scanning
     */
    private Map<String, List<String>> scanForSpiImplementations(String annotationDescriptor)
            throws MojoExecutionException {
        Map<String, List<String>> spiMappings = new LinkedHashMap<>();

        try {
            Files.walkFileTree(classesDirectory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (isClassFile(file)) {
                        scanClassFile(file, annotationDescriptor, spiMappings);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to scan classes directory: " + classesDirectory, e);
        }

        return spiMappings;
    }

    /**
     * Scans a single class file for {@code @Spi} annotation and registers any discovered
     * SPI interface-to-implementation mappings.
     */
    private void scanClassFile(Path classFile, String annotationDescriptor,
                               Map<String, List<String>> spiMappings) throws IOException {
        SpiAnnotationInfo info = extractSpiInfo(classFile, annotationDescriptor);
        if (info == null) {
            return;
        }

        for (String spiInterface : info.interfaces) {
            spiMappings.computeIfAbsent(spiInterface, k -> new ArrayList<>())
                    .add(info.className);
        }
    }

    private boolean isClassFile(Path file) {
        return file.getFileName() != null && file.toString().endsWith(CLASS_FILE_SUFFIX);
    }

    // ========================================================================
    // Service File Generation
    // ========================================================================

    /**
     * Writes {@code META-INF/services/} files for all discovered SPI mappings,
     * merging with any pre-existing entries.
     */
    private void writeServiceFiles(Map<String, List<String>> spiMappings) throws MojoExecutionException {
        Path servicesDir = classesDirectory.toPath().resolve(SERVICES_PATH);

        for (Map.Entry<String, List<String>> entry : spiMappings.entrySet()) {
            String interfaceName = entry.getKey();
            List<String> scannedImpls = entry.getValue();

            Path serviceFile = servicesDir.resolve(interfaceName);
            Set<String> mergedImpls = mergeWithExistingEntries(serviceFile, scannedImpls);
            writeServiceFile(serviceFile, mergedImpls);
        }
    }

    /**
     * Merges existing service file entries (if any) with newly scanned implementations.
     * Uses a {@link LinkedHashSet} to preserve insertion order and eliminate duplicates.
     */
    private Set<String> mergeWithExistingEntries(Path serviceFile, List<String> scannedImpls)
            throws MojoExecutionException {
        Set<String> merged = new LinkedHashSet<>();

        if (Files.exists(serviceFile)) {
            merged.addAll(readExistingEntries(serviceFile));
        }
        merged.addAll(scannedImpls);
        return merged;
    }

    private List<String> readExistingEntries(Path serviceFile) throws MojoExecutionException {
        List<String> entries = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(serviceFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    entries.add(trimmed);
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read existing service file: " + serviceFile, e);
        }
        return entries;
    }

    private void writeServiceFile(Path serviceFile, Set<String> implementations)
            throws MojoExecutionException {
        try {
            Files.createDirectories(serviceFile.getParent());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create service directory: " + serviceFile.getParent(), e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(serviceFile, StandardCharsets.UTF_8)) {
            for (String impl : implementations) {
                writer.write(impl);
                writer.newLine();
                getLog().info("Registered SPI: " + impl);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write service file: " + serviceFile, e);
        }
    }

    // ========================================================================
    // ASM Bytecode Analysis
    // ========================================================================

    /**
     * Converts a fully qualified annotation class name to its ASM descriptor form.
     * Example: {@code "i2f.spi.annotations.Spi"} → {@code "Li2f/spi/annotations/Spi;"}
     */
    private static String toAnnotationDescriptor(String className) {
        return "L" + className.replace('.', '/') + ";";
    }

    /**
     * Extracts SPI annotation info from a class file using ASM bytecode analysis.
     *
     * @return the extracted SPI info, or {@code null} if the class is not annotated
     */
    private SpiAnnotationInfo extractSpiInfo(Path classFile, String annotationDescriptor) throws IOException {
        try (InputStream is = Files.newInputStream(classFile)) {
            ClassReader reader = new ClassReader(is);
            SpiClassVisitor visitor = new SpiClassVisitor(annotationDescriptor);
            reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            return visitor.toSpiInfo();
        }
    }

    /**
     * Holds the result of SPI annotation analysis for a single class.
     */
    private static final class SpiAnnotationInfo {
        final String className;
        final List<String> interfaces;

        SpiAnnotationInfo(String className, List<String> interfaces) {
            this.className = className;
            this.interfaces = interfaces;
        }
    }

    /**
     * ASM {@link ClassVisitor} that detects the {@code @Spi} annotation on a class
     * and extracts the SPI interface names declared in its {@code value} attribute.
     *
     * <p>Supports both single-value and array-value forms:</p>
     * <ul>
     *   <li>{@code @Spi(SomeInterface.class)}</li>
     *   <li>{@code @Spi({InterfaceA.class, InterfaceB.class})}</li>
     * </ul>
     */
    private static final class SpiClassVisitor extends ClassVisitor {

        private final String targetAnnotation;
        private String internalClassName;
        private final List<String> spiInterfaces = new ArrayList<>();
        private boolean annotated;

        SpiClassVisitor(String targetAnnotation) {
            super(Opcodes.ASM9);
            this.targetAnnotation = targetAnnotation;
        }

        @Override
        public void visit(int version, int access, String name, String signature,
                          String superName, String[] interfaces) {
            this.internalClassName = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (!targetAnnotation.equals(descriptor)) {
                return null;
            }
            this.annotated = true;
            return new SpiAnnotationValueCollector();
        }

        /**
         * Builds an {@link SpiAnnotationInfo} from the visited data.
         *
         * @return the SPI info if the class is annotated and has interfaces, otherwise {@code null}
         */
        SpiAnnotationInfo toSpiInfo() {
            if (!annotated || spiInterfaces.isEmpty()) {
                return null;
            }
            String dotClassName = internalClassName.replace('/', '.');
            List<String> dotInterfaces = new ArrayList<>(spiInterfaces.size());
            for (String iface : spiInterfaces) {
                dotInterfaces.add(iface.replace('/', '.'));
            }
            return new SpiAnnotationInfo(dotClassName, dotInterfaces);
        }

        /**
         * Collects the {@code value} attribute from the {@code @Spi} annotation,
         * handling both single class references and arrays of class references.
         */
        private final class SpiAnnotationValueCollector extends AnnotationVisitor {

            SpiAnnotationValueCollector() {
                super(Opcodes.ASM9);
            }

            @Override
            public void visit(String name, Object value) {
                if (VALUE_ATTRIBUTE.equals(name) && value instanceof Type) {
                    spiInterfaces.add(((Type) value).getInternalName());
                }
            }

            @Override
            public AnnotationVisitor visitArray(String name) {
                if (!VALUE_ATTRIBUTE.equals(name)) {
                    return null;
                }
                return new AnnotationVisitor(Opcodes.ASM9) {
                    @Override
                    public void visit(String name, Object value) {
                        if (value instanceof Type) {
                            spiInterfaces.add(((Type) value).getInternalName());
                        }
                    }
                };
            }
        }
    }
}
