package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:15
 */
@Data
@NoArgsConstructor
@Slf4j
public class SpringJdbcProcedureNodeMapRefresher {

    protected final CopyOnWriteArraySet<String> xmlLocations=new CopyOnWriteArraySet<>();
    protected final PathMatchingResourcePatternResolver resourcePatternResolver=new PathMatchingResourcePatternResolver();

    protected final ConcurrentHashMap<String,XmlNode> nodeMap=new ConcurrentHashMap<>();

    protected volatile Thread refreshThread=null;
    protected AtomicBoolean refreshing=new AtomicBoolean(false);

    public SpringJdbcProcedureNodeMapRefresher(List<String> xmlLocations) {
        if(xmlLocations==null){
            return;
        }
        this.xmlLocations.addAll(xmlLocations);
    }

    public Map<String,XmlNode> getNodeMap(){
        if(nodeMap.isEmpty()){
            refreshNodeMap();
        }
        return new HashMap<>(nodeMap);
    }

    public void startRefreshThread(long intervalSeconds){
        if(intervalSeconds<0){
            refreshing.set(false);
            if(refreshThread!=null){
                refreshThread.interrupt();
            }
            refreshThread=null;
        }
        refreshing.set(true);
        Thread thread=new Thread(()->{
           while(refreshing.get()){
               try{
                   refreshNodeMap();
               }catch(Exception e){
               }
               try{
                   Thread.sleep(intervalSeconds*1000);
               }catch(Exception e){
               }
           }
        });
        thread.setName("procedure-xml-refresher");
        thread.setDaemon(true);
        thread.start();
        refreshThread=thread;
    }

    public void refreshNodeMap(){
        Map<String, XmlNode> map = parseResources();
        nodeMap.putAll(map);
    }

    public Map<String, XmlNode> parseResources(){
        Map<String,XmlNode> ret=new HashMap<>();
        if(xmlLocations==null || xmlLocations.isEmpty()){
            return ret;
        }
        Set<Resource> resources=new HashSet<>();
        for (String location : xmlLocations) {
            if(location==null || location.isEmpty()){
                continue;
            }
            try {
                Resource[] arr = resourcePatternResolver.getResources(location);
                if(arr!=null){
                    resources.addAll(Arrays.asList(arr));
                }
            } catch (IOException e) {

            }
        }
        for (Resource resource : resources) {
            try(InputStream is = resource.getInputStream()) {
                XmlNode node = JdbcProcedureParser.parse(is);
                String id = node.getTagAttrMap().get("id");
                if (id == null) {
                    String filename = resource.getFilename();
                    if (filename != null) {
                        id = filename;
                        int idx = filename.lastIndexOf(".");
                        if (idx >= 0) {
                            id = filename.substring(0, idx);
                        }
                    }
                }
                if (id != null) {
                    ret.put(id, node);

                    Map<String, XmlNode> next = new HashMap<>();
                    JdbcProcedureParser.resolveEmbedIdNode(node, next);
                    for (Map.Entry<String, XmlNode> entry : next.entrySet()) {
                        String childId = id + "." + entry.getKey();
                        ret.put(childId, entry.getValue());
                    }
                }
            }catch (Exception e){
                log.warn(e.getMessage(),e);
            }
        }
        return ret;
    }


}
