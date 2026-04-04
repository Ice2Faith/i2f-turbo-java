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
        scheduler.addNode("d", () -> System.out.println("d"));
        scheduler.addNode("e", () -> System.out.println("e"));

        scheduler.addEdge("a", "b");
        scheduler.addEdge("a", "d");
        scheduler.addEdge("b", "d");
        scheduler.addEdge("d", "c");
        scheduler.addEdge("d", "e");
        scheduler.addEdge("e", "c");

        scheduler.run();
    }
}
