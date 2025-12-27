package i2f.springboot.ops.elasticsearch.controller;

import i2f.extension.elasticsearch.EsManager;
import i2f.page.Page;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.elasticsearch.data.ElasticSearchOperateDto;
import i2f.springboot.ops.elasticsearch.helper.ElasticSearchOpsHelper;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/27 11:15
 */
@ConditionalOnClass(RestHighLevelClient.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/elasticsearch")
public class ElasticSearchOpsController implements IOpsProvider {

    @Autowired
    protected ElasticSearchOpsHelper elasticSearchOpsHelper;

    @Autowired
    protected OpsSecureTransfer transfer;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("ElasticSearch")
                .subTitle("ElasticSearch 搜索引擎管理")
                .icon("el-icon-search")
                .href("./elasticsearch/index.html")
                .disabled(true)
        );
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    @PostMapping("/index/list")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> indexList(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            ElasticSearchOperateDto req = transfer.recv(reqDto, ElasticSearchOperateDto.class);
            RestHighLevelClient client = null;
            try {
                client = elasticSearchOpsHelper.getClient(req);
                EsManager esManager = new EsManager(client);

                String pattern = req.getPattern();
                if (pattern == null || pattern.isEmpty()) {
                    pattern = "*";
                }

                List<String> resp = esManager.indexListByPattern(pattern);

                return transfer.success(resp);
            } finally {
                if (req.isUseCustomMeta()) {
                    if (client != null) {
                        client.close();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/index/add")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> indexAdd(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            ElasticSearchOperateDto req = transfer.recv(reqDto, ElasticSearchOperateDto.class);
            RestHighLevelClient client = null;
            try {
                client = elasticSearchOpsHelper.getClient(req);
                EsManager esManager = new EsManager(client);


                boolean resp = esManager.indexCreate(req.getIndexName(), req.getMappingJson(), req.getSettingJson());

                return transfer.success(resp);
            } finally {
                if (req.isUseCustomMeta()) {
                    if (client != null) {
                        client.close();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/index/delete")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> indexDelete(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            ElasticSearchOperateDto req = transfer.recv(reqDto, ElasticSearchOperateDto.class);
            RestHighLevelClient client = null;
            try {
                client = elasticSearchOpsHelper.getClient(req);
                EsManager esManager = new EsManager(client);

                boolean resp = esManager.indexDelete(req.getIndexName());

                return transfer.success(resp);
            } finally {
                if (req.isUseCustomMeta()) {
                    if (client != null) {
                        client.close();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/document/add")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> documentAdd(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            ElasticSearchOperateDto req = transfer.recv(reqDto, ElasticSearchOperateDto.class);
            RestHighLevelClient client = null;
            try {
                client = elasticSearchOpsHelper.getClient(req);
                EsManager esManager = new EsManager(client);

                boolean resp = esManager.recordsInsert(req.getIndexName(), req.getId(), req.getJsonDsl());

                return transfer.success(resp);
            } finally {
                if (req.isUseCustomMeta()) {
                    if (client != null) {
                        client.close();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/document/update")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> documentUpdate(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            ElasticSearchOperateDto req = transfer.recv(reqDto, ElasticSearchOperateDto.class);
            RestHighLevelClient client = null;
            try {
                client = elasticSearchOpsHelper.getClient(req);
                EsManager esManager = new EsManager(client);

                boolean resp = esManager.recordsUpdate(req.getIndexName(), req.getId(), req.getJsonDsl());

                return transfer.success(resp);
            } finally {
                if (req.isUseCustomMeta()) {
                    if (client != null) {
                        client.close();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/document/delete")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> documentDelete(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            ElasticSearchOperateDto req = transfer.recv(reqDto, ElasticSearchOperateDto.class);
            RestHighLevelClient client = null;
            try {
                client = elasticSearchOpsHelper.getClient(req);
                EsManager esManager = new EsManager(client);

                boolean resp = esManager.recordsDelete(req.getIndexName(), req.getId());

                return transfer.success(resp);
            } finally {
                if (req.isUseCustomMeta()) {
                    if (client != null) {
                        client.close();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }


    @PostMapping("/document/search")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> documentSearch(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            ElasticSearchOperateDto req = transfer.recv(reqDto, ElasticSearchOperateDto.class);
            RestHighLevelClient client = null;
            try {
                client = elasticSearchOpsHelper.getClient(req);
                EsManager esManager = new EsManager(client);

                SearchSourceBuilder builder = EsManager.searchSourceBuilderFromJsonDsl(req.getJsonDsl());
                if (req.getMaxCount() != null && req.getMaxCount() >= 0) {
                    builder.from(0);
                    builder.size(req.getMaxCount());
                }
                Page<Map<String, Object>> resp = esManager.searchAsMap(req.getIndexName(), builder);

                return transfer.success(resp);
            } finally {
                if (req.isUseCustomMeta()) {
                    if (client != null) {
                        client.close();
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }
}
