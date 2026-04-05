package i2f.workflow.rag;

/**
 * @author Ice2Faith
 * @date 2026/4/4 22:10
 * @desc
 */
public class TestDagScheduler {
    public static void main(String[] args) {
        DagScheduler scheduler = new DagScheduler();

        /**
         * a->b,a->d,b->d,d->c,d->e,e->c
         * 执行顺序：abdec
         */
        scheduler.addNode("a", () -> System.out.println("a"));
        scheduler.addNode("b", () -> System.out.println("b"));
        scheduler.addNode("c", () -> System.out.println("c"));
        scheduler.addNode("d", () -> System.out.println("d"), DagNodeType.HUMAN);
        scheduler.addNode("e", () -> System.out.println("e"));

        scheduler.addEdge("a", "b");
        scheduler.addEdge("a", "d");
        scheduler.addEdge("b", "d");
        scheduler.addEdge("d", "c");
        scheduler.addEdge("d", "e");
        scheduler.addEdge("e", "c");

        /**
         * 运行，此时 d 是 HUMAN 节点，需要外部介入
         * 因此只会执行：ab
         */
        scheduler.run();

        /**
         * 外部处理节点 d
         */
        DagNode d = scheduler.getNodes().get("d");
        if (d.getStatus() == DagNodeStatus.WAITING) {
            /**
             * 情况1
             * 外部处理任务，也就是 d 任务不在调度器中执行处理，外部直接改变状态
             * 也就是直接改为成功或失败，这种情况，失败也在外部处理
             * 最终输出序列：abec
             * 因为 d 不在调度器中
             */
            //d.setStatus(DagNodeStatus.SUCCESS);

            /**
             * 情况2
             * 外部判断后，继续恢复执行，也就是 d 任务在调度器中继续执行处理
             * 最终输出序列：abdec
             * 因为 d 还在调度器中执行
             */
            d.setStatus(DagNodeStatus.CONTINUE);
        }

        scheduler.run();
    }
}
