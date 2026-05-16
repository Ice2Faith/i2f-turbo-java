package i2f.turbo.idea.plugin.funic.lang.debugger;

/**
 * @author Ice2Faith
 * @date 2026/5/16 22:09
 * @desc
 */
public interface FunicDebugConsts {
    String SIMPLE_CLASS_NAME = FunicDebugBridgeReporter.class.getSimpleName();
    String CLASS_PATTERN = "*." + SIMPLE_CLASS_NAME;
    String METHOD_NAME = "report";
    int PARAM_COUNT = 0;
    String FILE_NAME_FIELD_NAME = "fileName";
    String LINE_NUMBER_FIELD_NAME = "lineNumber";
}
