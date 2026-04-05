package i2f.workflow.rag;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/4/4 21:03
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class DagScheduler implements Runnable {
    // key: nodeId, value:node
    protected Map<String, DagNode> nodes = new ConcurrentHashMap<>();
    // key: fromId,value:List<toIdNode>
    protected Map<String, List<DagEdge>> edges = new ConcurrentHashMap<>();
    protected ExecutorService pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);


    @Override
    public void run() {
        initDependencies();

        // 1. 获取分层后的任务列表 (例如: [[A, E], [B], [C], [D]])
        List<List<String>> layers = topologicalSort();

        // 2. 逐层执行
        for (List<String> layer : layers) {
            // 为当前层创建一个 Latch，数量等于当前层的任务数
            boolean hasWaiting = false;
            Set<String> pendingList = new LinkedHashSet<>(layer);
            while (!pendingList.isEmpty()) {

                List<String> removeList = new ArrayList<>();
                List<Map.Entry<DagNode, List<DagInboundNode>>> taskList = new ArrayList<>();

                // 3. 提交当前层的所有任务到线程池
                for (String taskId : pendingList) {
                    DagNode node = nodes.get(taskId);
                    if (node.getStatus() != DagNodeStatus.PENDING
                            && node.getStatus() != DagNodeStatus.CONTINUE) {
                        removeList.add(taskId);
                        continue;
                    }
                    DagInboundGateway inboundGateway = node.getInboundGateway();
                    List<DagInboundNode> inboundNodes = new ArrayList<>();
                    Set<String> dependencies = node.getDependencies();
                    for (String dependTaskId : dependencies) {
                        DagNode dependNode = nodes.get(dependTaskId);
                        DagNodeStatus dependNodeStatus = dependNode.getStatus();
                        DagEdgeStatus edgeStatus = DagEdgeStatus.of(dependNodeStatus);
                        if (dependNodeStatus == DagNodeStatus.SUCCESS) {
                            // 如果执行成功，则查找边，判断边上的条件是否满足，不满足的话这条边就应该变为 SKIP 跳过
                            List<DagEdge> outboundEdges = edges.get(dependTaskId);
                            for (DagEdge outboundEdge : outboundEdges) {
                                if (outboundEdge.getToId().equals(taskId)) {
                                    boolean ok = outboundEdge.getCondition().test(dependNode.getResult());
                                    if (!ok) {
                                        edgeStatus = DagEdgeStatus.SKIP;
                                    }
                                    break;
                                }
                            }
                        }

                        DagInboundNode inboundNode = new DagInboundNode();
                        inboundNode.setNode(dependNode);
                        inboundNode.setEdgeStatus(edgeStatus);
                        inboundNodes.add(inboundNode);
                    }
                    DagNodeStatus status = inboundGateway.test(inboundNodes);
                    if (status != DagNodeStatus.SUCCESS) {
                        removeList.add(taskId);
                    }
                    if (status == DagNodeStatus.FAILURE) {
                        throw new IllegalStateException("task [" + taskId + "] inbound check failed！");
                    } else if (status == DagNodeStatus.SUCCESS) {
                        if (node.getType() == DagNodeType.HUMAN
                                && node.getStatus() == DagNodeStatus.PENDING) {
                            hasWaiting = true;
                            node.setStatus(DagNodeStatus.WAITING);
                        } else {
                            taskList.add(new AbstractMap.SimpleEntry<>(node, inboundNodes));
                        }
                    } else {
                        node.setStatus(status);
                    }

                }

                CountDownLatch latch = new CountDownLatch(taskList.size());
                for (Map.Entry<DagNode, List<DagInboundNode>> entry : taskList) {
                    pool.submit(() -> {
                        DagNode node = entry.getKey();
                        List<DagInboundNode> inboundList = entry.getValue();
                        DagTask<?> task = node.getTask();
                        try {
                            Object result = task.call(inboundList);
                            node.setResult(result);
                            node.setStatus(DagNodeStatus.SUCCESS);
                        } catch (Throwable e) {
                            node.setError(e);
                            node.setStatus(DagNodeStatus.FAILURE);
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                // 4. 主线程阻塞，等待当前层所有任务执行完毕
                // 这里可以设置超时时间，防止某个任务卡死导致整个系统挂起
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // 移除已运行的
                for (String id : removeList) {
                    pendingList.remove(id);
                }

            }
            if (hasWaiting) {
                break;
            }
        }
    }

    public DagScheduler addNode(String id, Runnable task) {
        return addNode(id, task, null, null);
    }

    public DagScheduler addNode(String id, Runnable task, DagNodeType type) {
        return addNode(id, task, type, null);
    }

    public DagScheduler addNode(String id, Runnable task, DagNodeType type, DagInboundGateway inboundGateway) {
        return addNode(id, () -> {
            task.run();
            return null;
        }, type, inboundGateway);
    }

    public DagScheduler addNode(String id, Callable<?> task) {
        return addNode(id, task, null, null);
    }

    public DagScheduler addNode(String id, Callable<?> task, DagNodeType type) {
        return addNode(id, task, type, null);
    }

    public DagScheduler addNode(String id, Callable<?> task, DagNodeType type, DagInboundGateway inboundGateway) {
        return addNode(id, (list) -> {
            return task.call();
        }, type, inboundGateway);
    }

    public DagScheduler addNode(String id, DagTask<?> task) {
        return addNode(id, task, null, null);
    }

    public DagScheduler addNode(String id, DagTask<?> task, DagNodeType type) {
        return addNode(id, task, type, null);
    }

    public DagScheduler addNode(String id, DagTask<?> task, DagNodeType type, DagInboundGateway inboundGateway) {
        DagNode node = new DagNode();
        node.setId(id);
        node.setTask(task);
        node.setInboundGateway(inboundGateway == null ? DagInboundGateways::allSuccess : inboundGateway);
        node.setType(type == null ? DagNodeType.AUTO : type);
        node.setStatus(DagNodeStatus.PENDING);
        nodes.put(id, node);
        return this;
    }

    public DagScheduler addEdge(String fromId, String toId) {
        return addEdge(fromId, toId, null);
    }

    public DagScheduler addEdge(String fromId, String toId, Predicate<Object> condition) {
        List<DagEdge> toEdges = edges.computeIfAbsent(fromId, k -> new CopyOnWriteArrayList<>());
        DagEdge edge = new DagEdge();
        edge.setFromId(fromId);
        edge.setToId(toId);
        edge.setCondition(condition == null ? DagEdge::always : condition);
        toEdges.add(edge);
        return this;
    }

    protected void initDependencies() {
        // 依赖
        Map<String, Set<String>> dependenciesMap = new HashMap<>();
        for (Map.Entry<String, List<DagEdge>> entry : edges.entrySet()) {
            List<DagEdge> toEdges = entry.getValue();
            for (DagEdge edge : toEdges) {
                Set<String> dependencies = dependenciesMap.computeIfAbsent(edge.getToId(), k -> new LinkedHashSet<>());
                dependencies.add(edge.getFromId());
            }

        }
        // 应用依赖关系
        for (Map.Entry<String, DagNode> entry : nodes.entrySet()) {
            DagNode node = entry.getValue();
            if (dependenciesMap.containsKey(node.getId())) {
                node.setDependencies(dependenciesMap.get(node.getId()));
            } else {
                node.setDependencies(new LinkedHashSet<>());
            }
        }
    }

    /**
     * Kahn 算法进行拓扑排序
     */
    protected List<List<String>> topologicalSort() {

        // 计算入度
        Map<String, Integer> inDegreeMap = new HashMap<>();
        for (DagNode node : nodes.values()) {
            inDegreeMap.put(node.getId(), node.getDependencies().size());
        }

        List<List<String>> result = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();

        // 将入度为 0 的节点加入队列
        for (Map.Entry<String, Integer> entry : inDegreeMap.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            // 这一层有多少个任务可以并行
            int currentLayerSize = queue.size();
            List<String> currentLayer = new ArrayList<>();

            for (int i = 0; i < currentLayerSize; i++) {
                String currentTaskId = queue.poll();
                currentLayer.add(currentTaskId);

                // 寻找后继节点：遍历所有节点，看谁依赖了 currentTaskId
                for (DagNode node : nodes.values()) {
                    if (node.getDependencies().contains(currentTaskId)) {
                        int newInDegree = inDegreeMap.get(node.getId()) - 1;
                        inDegreeMap.put(node.getId(), newInDegree);
                        if (newInDegree == 0) {
                            queue.offer(node.getId());
                        }
                    }
                }
            }
            result.add(currentLayer);
        }

        // 检查是否存在循环依赖
        if (result.stream().mapToInt(List::size).sum() != nodes.size()) {
            throw new RuntimeException("dag contains circle!");
        }

        return result;
    }
}
