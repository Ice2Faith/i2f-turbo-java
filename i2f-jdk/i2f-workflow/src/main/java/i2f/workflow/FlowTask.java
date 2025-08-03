package i2f.workflow;

/**
 * @author Ice2Faith
 * @date 2024/3/22 15:57
 * @desc
 */
public interface FlowTask {
    default boolean trigger(FlowNode node) {
        if(node.getPrev().isEmpty()){
            return true;
        }
        return node.getPrev().size()==node.getDone().size();
    }

    void run(FlowNode node) throws Throwable;
}
