package i2f.turbo.idea.plugin.inject.data;

/**
 * @author Ice2Faith
 * @date 2026/4/17 15:02
 * @desc
 */
public class LanguageInjectJavaMetadata {
    protected String className;
    protected String methodName;

    public String getClassName() {
        return className;
    }

    public LanguageInjectJavaMetadata className(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public LanguageInjectJavaMetadata methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }
}
