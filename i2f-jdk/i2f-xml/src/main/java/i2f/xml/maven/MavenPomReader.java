package i2f.xml.maven;

import i2f.xml.XmlUtil;
import i2f.xml.maven.data.MavenPom;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/8/13 10:43
 * @desc
 */
public class MavenPomReader {
    public static void main(String[] args) throws Exception {
        File file = new File("./i2f-springboot/i2f-springboot-swl-starter/pom.xml");

        MavenPom pom = read(file);

        System.out.println(pom);
    }

    public static MavenPom read(File file) throws Exception {
        return readNext(new MavenPom(), file, true, true);
    }

    public static MavenPom readNext(MavenPom ret, File file, boolean resolveParent, boolean resolveDependencies) throws Exception {

        file = new File(file.getAbsolutePath());
        if (resolveParent) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                File parentDir = parentFile.getParentFile();
                if (parentDir != null) {
                    File parentPom = new File(parentDir, "pom.xml");
                    if (parentPom.exists() && parentPom.isFile()) {
                        readNext(ret, parentPom, resolveParent, false);
                    }
                }
            }
        }

        Document document = XmlUtil.parseXml(file);
        Node rootNode = XmlUtil.getRootNode(document);
        List<Node> firstNodes = XmlUtil.getChildNodes(rootNode);
        List<Node> parentNodes = new ArrayList<>();
        List<Node> propertiesNodes = new ArrayList<>();
        List<Node> dependenciesNodes = new ArrayList<>();
        List<Node> dependencyManagementNodes = new ArrayList<>();
        List<Node> dependencyManagementDependenciesNodes = new ArrayList<>();
        for (Node node : firstNodes) {
            String name = XmlUtil.getTagName(node);
            if ("parent".equals(name)) {
                parentNodes.add(node);
                dependencyManagementNodes.add(node);
            }
            if ("properties".equals(name)) {
                propertiesNodes.add(node);
            }
            if (resolveDependencies) {
                if ("dependencies".equals(name)) {
                    dependenciesNodes.add(node);
                }
            }
            if ("dependencyManagement".equals(name)) {
                dependencyManagementNodes.add(node);
                List<Node> childNodes = XmlUtil.getChildNodes(node);
                dependencyManagementDependenciesNodes.addAll(childNodes);
            }
        }
        for (Node propsNode : parentNodes) {
            readPropertiesNode(propsNode, ret.getParent());
        }

        for (Node propsNode : propertiesNodes) {
            readPropertiesNode(propsNode, ret.getProperties());
        }


        for (Node dependency : dependencyManagementDependenciesNodes) {
            readDependenciesNode(dependency, ret.getProperties(), ret.getManagementDependencies());
        }

        if (resolveDependencies) {
            for (Node dependency : dependenciesNodes) {
                readDependenciesNode(dependency, ret.getProperties(), ret.getDependencies());
            }
        }


        for (Map<String, String> item : ret.getManagementDependencies()) {
            String groupId = item.get("groupId");
            String artifactId = item.get("artifactId");
            String version = item.get("version");
            ret.getManageVersions().put(groupId + ":" + artifactId, version);
        }

        if (ret.getParent() != null) {
            String groupId = ret.getParent().get("groupId");
            String artifactId = ret.getParent().get("artifactId");
            String version = ret.getParent().get("version");
            ret.getManageVersions().put(groupId + ":" + artifactId, version);
        }

        if (resolveDependencies) {
            for (Map<String, String> dependency : ret.getDependencies()) {
                String version = dependency.get("version");
                if (version != null && !version.isEmpty()) {
                    continue;
                }
                String groupId = dependency.get("groupId");
                String artifactId = dependency.get("artifactId");
                String key = groupId + ":" + artifactId;
                String manageVersion = ret.getManageVersions().get(key);
                if (manageVersion != null) {
                    dependency.put("version", manageVersion);
                    continue;
                }

                if ("org.springframework.boot".equals(groupId)) {
                    if (artifactId.startsWith("spring-boot-")) {
                        manageVersion = ret.getManageVersions().get("org.springframework.boot:spring-boot-starter-parent");
                        if (manageVersion != null) {
                            dependency.put("version", manageVersion);
                        }
                        manageVersion = ret.getManageVersions().get("org.springframework.boot:spring-boot-dependencies");
                        if (manageVersion != null) {
                            dependency.put("version", manageVersion);
                        }
                    }
                }

                if ("org.springframework.cloud".equals(groupId)) {
                    if (artifactId.startsWith("spring-cloud-")) {
                        manageVersion = ret.getManageVersions().get("org.springframework.cloud:spring-cloud-dependencies");
                        if (manageVersion != null) {
                            dependency.put("version", manageVersion);
                        }
                    }
                }

            }
        }

        return ret;
    }


    public static void readPropertiesNode(Node propsNode, Map<String, String> properties) {
        List<Node> nodes = XmlUtil.getChildNodes(propsNode);
        for (Node node : nodes) {
            String name = XmlUtil.getTagName(node);
            if ("#text".equals(name)) {
                continue;
            }
            if ("#comment".equals(name)) {
                continue;
            }
            String content = XmlUtil.getNodeContent(node).trim();
            properties.put(name, content);
        }
    }

    public static void readDependenciesNode(Node dependency, Map<String, String> properties, List<Map<String, String>> dependencies) {
        List<Node> nodes = XmlUtil.getChildNodes(dependency);
        for (Node node : nodes) {
            String name = XmlUtil.getTagName(node);
            if (!"dependency".equals(name)) {
                continue;
            }
            List<Node> attrs = XmlUtil.getChildNodes(node);
            Map<String, String> item = new HashMap<>();
            for (Node attr : attrs) {
                String key = XmlUtil.getTagName(attr);
                if ("#text".equals(key)) {
                    continue;
                }
                if ("#comment".equals(name)) {
                    continue;
                }
                if ("exclusions".equals(key)) {
                    continue;
                }
                String content = XmlUtil.getNodeContent(attr).trim();
                if (content.startsWith("${") && content.endsWith("}")) {
                    String prop = content.substring(2, content.length() - 1).trim();
                    String value = properties.get(prop);
                    if (value != null) {
                        content = value;
                    }
                }
                item.put(key, content);
            }
            dependencies.add(item);
        }
    }

}
