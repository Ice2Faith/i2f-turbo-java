package i2f.springboot.ops.elasticsearch.helper;

import i2f.extension.elasticsearch.EsManager;
import i2f.springboot.ops.elasticsearch.data.ElasticSearchOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/11/27 11:20
 */
@Data
@NoArgsConstructor
@Component
public class ElasticSearchOpsHelper {
    @Autowired
    protected ApplicationContext applicationContext;

    protected AtomicReference<RestHighLevelClient> clientHolder=new AtomicReference<>();

    public RestHighLevelClient getClient(ElasticSearchOperateDto dto) throws IOException {
        if(dto.isUseCustomMeta()){
            return EsManager.getClient(dto.getMeta());
        }
        RestHighLevelClient ret = clientHolder.get();
        if(ret==null){
            try{
                String[] names = applicationContext.getBeanNamesForType(RestHighLevelClient.class);
                for (String name : names) {
                    ret = applicationContext.getBean(name, RestHighLevelClient.class);
                    break;
                }
            }catch(Exception e){

            }
        }
        clientHolder.set(ret);
        return ret;
    }
}
