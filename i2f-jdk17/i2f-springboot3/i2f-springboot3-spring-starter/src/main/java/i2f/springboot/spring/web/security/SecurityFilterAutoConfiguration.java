package i2f.springboot.spring.web.security;

import i2f.web.filter.SecurityFilter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2024/10/25 8:27
 */
@ConditionalOnExpression("${i2f.security.filter.enable:true}")
@Data
@Slf4j
@NoArgsConstructor
@ConfigurationProperties("i2f.security.filter")
public class SecurityFilterAutoConfiguration {

    private String urlPatten = "/*";
    private int order = -1;

    private boolean clearDenyMethods = false;
    private List<String> additionalDenyMethods = new ArrayList<>();
    private List<String> removeDenyMethods = new ArrayList<>();

    private boolean clearAllowedContentTypes = false;
    private List<String> additionalAllowedContentTypes = new ArrayList<>();
    private List<String> removeAllowedContentTypes = new ArrayList<>();

    private boolean defaultAllowSuffix = true;

    private boolean clearAllowSuffixes = false;
    private List<String> additionalAllowSuffixes = new ArrayList<>();
    private List<String> removeAllowSuffixes = new ArrayList<>();

    private boolean clearDenySuffixes = false;
    private List<String> additionalDenySuffixes = new ArrayList<>();
    private List<String> removeDenySuffixes = new ArrayList<>();

    private boolean clearDenyUrlStrings = false;
    private List<String> additionalDenyUrlStrings = new ArrayList<>();
    private List<String> removeDenyUrlStrings = new ArrayList<>();

    private boolean strictPath = true;

    private boolean allowEmptyOrigin = true;
    private boolean allowEmptyReferer = true;

    private boolean clearAllowOriginRegexList = false;
    private List<String> additonalAllowOriginRegexList = new ArrayList<>();
    private List<String> removeAllowOriginRegexList = new ArrayList<>();

    private boolean clearAllowRefererRegexList = false;
    private List<String> additonalAllowRefererRegexList = new ArrayList<>();
    private List<String> removeAllowRefererRegexList = new ArrayList<>();

    private boolean invisibleHeaderCheck = true;
    private boolean invisibleParameterCheck = true;
    private boolean invisibleCookieCheck = true;

    private boolean sqlInjectHeaderCheck = true;
    private boolean sqlInjectParameterCheck = true;
    private boolean sqlInjectCookieCheck = true;

    private boolean remoteInvokeHeaderCheck = true;
    private boolean remoteInvokeParameterCheck = true;
    private boolean remoteInvokeCookieCheck = true;

    private boolean clearSqlInjectRegexList = false;
    private List<String> additionalSqlInjectRegexList = new ArrayList<>();
    private List<String> removeSqlInjectRegexList = new ArrayList<>();

    private boolean clearSqlInjectPatternList = false;
    private List<String> addtionalSqlInjectPatternList = new ArrayList<>();
    private List<String> removeSqlInjectPatternList = new ArrayList<>();

    private boolean clearRemoteInvokeRegexList = false;
    private List<String> additionalRemoteInvokeRegexList = new ArrayList<>();
    private List<String> removeRemoteInvokeRegexList = new ArrayList<>();

    private boolean clearRemoteInvokePatternList = false;
    private List<String> additionalRemoteInvokePatternList = new ArrayList<>();
    private List<String> removeRemoteInvokePatternList = new ArrayList<>();

    private boolean illegalFileAccessParameterCheck = true;

    private boolean clearIllegalFileAccessPaths = false;
    private List<String> additionalIllegalFileAccessPaths = new ArrayList<>();
    private List<String> removeIllegalFileAccessPaths = new ArrayList<>();

    private boolean clearIllegalFileAccessFileNames = false;
    private List<String> additionalIllegalFileAccessFileNames = new ArrayList<>();
    private List<String> removeIllegalFileAccessFileNames = new ArrayList<>();

    private boolean illegalCommandExecuteParameterCheck = true;

    private boolean clearIllegalCommandExecuteCommands = false;
    private List<String> additionalIllegalCommandExecuteCommands = new ArrayList<>();
    private List<String> removeIllegalCommandExecuteCommands = new ArrayList<>();

    private boolean clearDenyIpRegexList = false;
    private List<String> additionalDenyIpRegexList = new ArrayList<>();
    private List<String> removeDenyIpRegexList = new ArrayList<>();

    private Map<String, List<String>> servletPathRegexAllowIpRegexMap = new LinkedHashMap<>();
    private Map<String, List<String>> servletPathRegexAllowOriginRegexMap = new LinkedHashMap<>();
    private Map<String, List<String>> servletPathRegexAllowRefererRegexMap = new LinkedHashMap<>();

    private List<String> globalRequireHeadersParametersNameList = new ArrayList<>();
    private List<String> globalAllowMissingHeadersParametersNameServletPathRegexList = new ArrayList<>();

    private boolean requestBodyCheck = false;

    private boolean clearXmlXxePatternList = false;
    private List<String> additionalXmlXxePatternList = new ArrayList<>();
    private List<String> removeXmlXxePatternList = new ArrayList<>();

    private boolean clearHtmlXssPatternList = false;
    private List<String> additionalHtmlXssPatternList = new ArrayList<>();
    private List<String> removeHtmlXssPatternList = new ArrayList<>();

    private boolean clearJavaDeserializePatternList = false;
    private List<String> additionalJavaDeserializePatternList = new ArrayList<>();
    private List<String> removeJavaDeserializePatternList = new ArrayList<>();

    @Bean
    public FilterRegistrationBean<SecurityFilter> securityFilterFilterRegistrationBean() {
        SecurityFilter filter = new SecurityFilter();
        mixed(filter.getDenyMethods(), clearDenyMethods, additionalDenyMethods, removeDenyMethods);
        mixed(filter.getAllowedContentTypes(), clearAllowedContentTypes, additionalAllowedContentTypes, removeAllowedContentTypes);
        filter.getDefaultAllowSuffix().set(defaultAllowSuffix);
        mixed(filter.getAllowSuffixes(), clearAllowSuffixes, additionalAllowSuffixes, removeAllowSuffixes);
        mixed(filter.getDenySuffixes(), clearDenySuffixes, additionalDenySuffixes, removeDenySuffixes);
        mixed(filter.getDenyUrlStrings(), clearDenyUrlStrings, additionalDenyUrlStrings, removeDenyUrlStrings);
        filter.getStrictPath().set(strictPath);
        filter.getAllowEmptyOrigin().set(allowEmptyOrigin);
        filter.getAllowEmptyReferer().set(allowEmptyReferer);
        mixed(filter.getAllowOriginRegexList(), clearAllowOriginRegexList, additonalAllowOriginRegexList, removeAllowOriginRegexList);
        mixed(filter.getAllowRefererRegexList(), clearAllowRefererRegexList, additonalAllowRefererRegexList, removeAllowRefererRegexList);
        filter.getInvisibleHeaderCheck().set(invisibleHeaderCheck);
        filter.getInvisibleParameterCheck().set(invisibleParameterCheck);
        filter.getInvisibleCookieCheck().set(invisibleCookieCheck);
        filter.getSqlInjectHeaderCheck().set(sqlInjectHeaderCheck);
        filter.getSqlInjectParameterCheck().set(sqlInjectParameterCheck);
        filter.getSqlInjectCookieCheck().set(sqlInjectCookieCheck);
        filter.getRemoteInvokeHeaderCheck().set(remoteInvokeHeaderCheck);
        filter.getRemoteInvokeParameterCheck().set(remoteInvokeParameterCheck);
        filter.getRemoteInvokeCookieCheck().set(remoteInvokeCookieCheck);
        mixed(filter.getSqlInjectRegexList(), clearSqlInjectRegexList, additionalSqlInjectRegexList, removeSqlInjectRegexList);
        mixedPattern(filter.getSqlInjectPatternList(), clearSqlInjectPatternList, addtionalSqlInjectPatternList, removeSqlInjectPatternList);
        mixed(filter.getRemoteInvokeRegexList(), clearRemoteInvokeRegexList, additionalRemoteInvokeRegexList, removeRemoteInvokeRegexList);
        mixedPattern(filter.getRemoteInvokePatternList(), clearRemoteInvokePatternList, additionalRemoteInvokePatternList, removeRemoteInvokePatternList);
        filter.getIllegalFileAccessParameterCheck().set(illegalFileAccessParameterCheck);
        mixed(filter.getIllegalFileAccessPaths(), clearIllegalFileAccessPaths, additionalIllegalFileAccessPaths, removeIllegalFileAccessPaths);
        mixed(filter.getIllegalFileAccessFileNames(), clearIllegalFileAccessFileNames, additionalIllegalFileAccessFileNames, removeIllegalFileAccessFileNames);
        filter.getIllegalCommandExecuteParameterCheck().set(illegalCommandExecuteParameterCheck);
        mixed(filter.getIllegalCommandExecuteCommands(), clearIllegalCommandExecuteCommands, additionalIllegalCommandExecuteCommands, removeIllegalCommandExecuteCommands);
        mixed(filter.getDenyIpRegexList(), clearDenyIpRegexList, additionalDenyIpRegexList, removeDenyIpRegexList);
        mixedMultiValuesMap(filter.getServletPathRegexAllowIpRegexMap(), CopyOnWriteArrayList::new, servletPathRegexAllowIpRegexMap);
        mixedMultiValuesMap(filter.getServletPathRegexAllowOriginRegexMap(), CopyOnWriteArrayList::new, servletPathRegexAllowOriginRegexMap);
        mixedMultiValuesMap(filter.getServletPathRegexAllowRefererRegexMap(), CopyOnWriteArrayList::new, servletPathRegexAllowRefererRegexMap);
        filter.getGlobalRequireHeadersParametersNameList().addAll(globalRequireHeadersParametersNameList);
        filter.getGlobalAllowMissingHeadersParametersNameServletPathRegexList().addAll(globalAllowMissingHeadersParametersNameServletPathRegexList);
        filter.getRequestBodyCheck().set(requestBodyCheck);
        mixedPattern(filter.getXmlXxePatternList(), clearXmlXxePatternList, additionalXmlXxePatternList, removeXmlXxePatternList);
        mixedPattern(filter.getHtmlXssPatternList(), clearHtmlXssPatternList, additionalHtmlXssPatternList, removeHtmlXssPatternList);
        mixedPattern(filter.getJavaDeserializePatternList(), clearJavaDeserializePatternList, additionalJavaDeserializePatternList, removeJavaDeserializePatternList);

        FilterRegistrationBean<SecurityFilter> ret = new FilterRegistrationBean<>();
        ret.setFilter(filter);
        ret.addUrlPatterns(urlPatten);
        ret.setOrder(order);
        return ret;
    }

    public void mixed(Collection<String> dst, boolean clear, Collection<String> additional, Collection<String> remove) {
        if (clear) {
            dst.clear();
        }
        if (remove != null) {
            for (String item : remove) {
                dst.remove(item);
            }
        }
        if (additional != null) {
            dst.addAll(additional);
        }
    }

    public void mixedPattern(Collection<Pattern> dst, boolean clear, Collection<String> additional, Collection<String> remove) {
        if (clear) {
            dst.clear();
        }
        if (remove != null) {
            List<Pattern> removeList = new ArrayList<>();
            for (Pattern pattern : dst) {
                if (pattern == null) {
                    continue;
                }
                if (remove.contains(pattern.pattern())) {
                    removeList.add(pattern);
                }
            }
            if (!removeList.isEmpty()) {
                for (Pattern pattern : removeList) {
                    dst.remove(pattern);
                }
            }
        }
        if (additional != null) {
            for (String str : additional) {
                if (str == null || str.isEmpty()) {
                    continue;
                }
                dst.add(Pattern.compile(str));
            }
        }
    }

    public <L extends List<String>> void mixedMultiValuesMap(Map<String, L> dst, Supplier<L> multiValueSupplier, Map<String, ? extends Collection<String>> src) {
        if (src == null) {
            return;
        }
        for (Map.Entry<String, ? extends Collection<String>> entry : src.entrySet()) {
            if (!dst.containsKey(entry.getKey())) {
                dst.put(entry.getKey(), multiValueSupplier.get());
            }
            Collection<String> vals = entry.getValue();
            if (vals == null || vals.isEmpty()) {
                continue;
            }
            dst.get(entry.getKey()).addAll(vals);
        }
    }
}
