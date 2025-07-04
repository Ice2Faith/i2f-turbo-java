package i2f.springboot.jdbc.bql.procedure.impl;

import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.DefaultXProc4jEventHandler;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.provider.types.xml.impl.AbstractJdbcProcedureXmlNodeMetaCacheProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:15
 */
@Data
@NoArgsConstructor
@Slf4j
public class SpringJdbcProcedureXmlNodeMetaCacheProvider extends AbstractJdbcProcedureXmlNodeMetaCacheProvider implements ApplicationRunner {

    protected final CopyOnWriteArraySet<String> xmlLocations = new CopyOnWriteArraySet<>();
    protected final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private final ExecutorService pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
    protected volatile XProc4jEventHandler eventHandler = new DefaultXProc4jEventHandler();

    public SpringJdbcProcedureXmlNodeMetaCacheProvider(List<String> xmlLocations) {
        if (xmlLocations == null) {
            return;
        }
        this.xmlLocations.addAll(xmlLocations);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            try {
                parseResources();
            } catch (Exception e) {

            }
        }).start();
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        Map<String, XmlNode> ret = new ConcurrentHashMap<>();
        if (xmlLocations == null || xmlLocations.isEmpty()) {
            return ret;
        }
        long beginTs = System.currentTimeMillis();
        log.info("procedure resource finding ...");
        Set<Resource> resources = new HashSet<>();
        for (String location : xmlLocations) {
            if (location == null || location.isEmpty()) {
                continue;
            }
            log.info("procedure scan resources: " + location);
            try {
                Resource[] arr = resourcePatternResolver.getResources(location);
                if (arr != null) {
                    resources.addAll(Arrays.asList(arr));
                }
            } catch (IOException e) {

            }
        }
        int resoucesCnt = resources.size();
        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger nodeCnt = new AtomicInteger(0);
        AtomicInteger failureCnt = new AtomicInteger(0);
        log.info("procedure resource found " + resoucesCnt + ".");
        log.info("procedure resource parsing ...");

        CountDownLatch latch = new CountDownLatch(resoucesCnt);
        for (Resource resource : resources) {
            pool.submit(() -> {
                try (InputStream is = resource.getInputStream()) {
                    XmlNode node = JdbcProcedureParser.parse(resource.getFilename(), is);
                    String id = node.getTagAttrMap().get("id");
                    String filename = resource.getFilename();
                    String fileNameId = filename;
                    if (filename != null) {
                        int idx = filename.lastIndexOf(".");
                        if (idx >= 0) {
                            fileNameId = filename.substring(0, idx);
                        }
                    }
                    if (id != null && fileNameId != null) {
                        if (!id.equals(fileNameId)) {
                            log.warn("resource filename not equal id, filename=" + filename + ", id=" + id);
                        }
                    }
                    if (id == null) {
                        if (filename != null) {
                            id = fileNameId;
                        }
                    }
                    if (id != null) {
                        ret.put(id, node);
                        successCnt.incrementAndGet();
                        nodeCnt.incrementAndGet();
                        log.debug("load procedure resource: id=" + id + ", filename=" + filename);

                        Map<String, XmlNode> next = new HashMap<>();
                        JdbcProcedureParser.resolveEmbedIdNode(node, next);
                        for (Map.Entry<String, XmlNode> entry : next.entrySet()) {
                            if (!entry.getKey().equals(id)) {
                                nodeCnt.incrementAndGet();
                                String childId = id + "." + entry.getKey();
                                log.debug("load procedure child node resource: id=" + childId + ", filename=" + filename);
                                ret.put(childId, entry.getValue());
                            }
                        }
                    }
                } catch (Exception e) {
                    failureCnt.incrementAndGet();
                    log.warn(resource.getFilename() + " parse error: " + e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {

        }
        long useTs = System.currentTimeMillis() - beginTs;
        log.info("procedure resource parsed"
                + ", use seconds=" + Math.round(useTs / 1000.0)
                + ", resource count=" + resoucesCnt
                + ", success count=" + successCnt
                + ", failure count=" + failureCnt
                + ", node count=" + nodeCnt
                + ", result count=" + ret.size());
        return ret;
    }


}
