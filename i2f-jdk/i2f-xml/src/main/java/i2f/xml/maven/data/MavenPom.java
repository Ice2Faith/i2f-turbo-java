package i2f.xml.maven.data;


import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/8/13 10:46
 * @desc
 */
@Data
public class MavenPom {
    protected Map<String, String> parent = new LinkedHashMap<>();

    protected Map<String, String> properties = new LinkedHashMap<>();

    protected Map<String, String> manageVersions = new LinkedHashMap<>();

    protected List<Map<String, String>> dependencies = new ArrayList<>();

    protected List<Map<String, String>> managementDependencies = new ArrayList<>();

}
