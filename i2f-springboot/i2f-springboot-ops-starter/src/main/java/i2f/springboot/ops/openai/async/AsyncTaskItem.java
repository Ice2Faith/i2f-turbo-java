package i2f.springboot.ops.openai.async;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/1 14:18
 * @desc
 */
@Data
@NoArgsConstructor
public class AsyncTaskItem {
    public static interface ResultTypes {
        String IMAGE = "image";
        String VIDEO = "video";
        String AUDIO = "audio";
        String FILE = "file";
        String FILE_LIST = "file_list";
        String IMAGE_LIST = "image_list";
    }

    public static interface Status {
        String PENDING = "pending";
        String RUNNING = "running";
        String SUCCESS = "success";
        String FAILURE = "failure";
    }

    protected String description;
    protected String type;
    protected String taskId;
    protected Map<String, Object> taskParameters;
    protected String status;
    protected String resultType;
    protected Object result;
    protected Object rawResult;
    protected String error;

    public boolean isFinished() {
        return Status.SUCCESS.equals(status) || Status.FAILURE.equals(status);
    }
}
