package com.curiophil.javalearn.json.extract;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DagTaskScheduler {

    private DirectedAcyclicGraph<TaskNode, DefaultEdge> dag;
    private ExecutorService executorService;

    public DagTaskScheduler() {
        this.dag = new DirectedAcyclicGraph<>(DefaultEdge.class);
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    // 添加任务节点
    public void addTask(TaskNode task) {
        dag.addVertex(task);
    }

    // 添加任务依赖（边）
    public void addDependency(TaskNode from, TaskNode to) {
        dag.addEdge(from, to);
    }

    // 执行DAG任务调度
    public void execute() {
        List<TaskNode> sortedTasks = new ArrayList<>(dag.vertexSet());
        Collections.sort(sortedTasks);

        Map<TaskNode, CompletableFuture<String>> taskFutures = new HashMap<>();

        // 遍历每个任务节点
        for (TaskNode task : sortedTasks) {
            List<CompletableFuture<String>> dependencies = dag.incomingEdgesOf(task).stream()
                    .map(edge -> taskFutures.get(dag.getEdgeSource(edge)))
                    .collect(Collectors.toList());

            // 收集所有前驱任务的输出
            CompletableFuture<String> future = CompletableFuture.allOf(dependencies.toArray(new CompletableFuture[0]))
                    .thenApplyAsync(ignored -> {
                        // 获取前驱任务的输出
                        List<String> results = dependencies.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList());

                        // 将前驱任务的输出传递给当前任务
                        return task.execute(results);
                    }, executorService);

            taskFutures.put(task, future);
        }

        // 等待所有任务完成
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(taskFutures.values().toArray(new CompletableFuture[0]));
        allTasks.join();
        executorService.shutdown();
    }

    // 任务节点类
    public static class TaskNode implements Comparable<TaskNode> {
        private final String name;

        public TaskNode(String name) {
            this.name = name;
        }

        // 任务执行方法，接收前驱任务的输出列表作为参数
        public String execute(List<String> inputs) {
            String inputSummary = String.join(", ", inputs);
            System.out.println("Executing task: " + name + " with inputs: " + inputSummary);
            String output = name + " result";  // 生成任务的输出
            // 模拟任务执行时间
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task " + name + " completed with output: " + output);
            return output;
        }

        @Override
        public int compareTo(TaskNode o) {
            return this.name.compareTo(o.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) throws Exception {
        DagTaskScheduler scheduler = new DagTaskScheduler();

        // 创建任务节点
        TaskNode taskA = new TaskNode("Task A");
        TaskNode taskB = new TaskNode("Task B");
        TaskNode taskC = new TaskNode("Task C");
        TaskNode taskD = new TaskNode("Task D");

        // 构建DAG任务依赖关系
        scheduler.addTask(taskA);
        scheduler.addTask(taskB);
        scheduler.addTask(taskC);
        scheduler.addTask(taskD);

        scheduler.addDependency(taskA, taskB);
        scheduler.addDependency(taskA, taskC);
        scheduler.addDependency(taskB, taskD);
        scheduler.addDependency(taskC, taskD);

        // 执行任务调度
        scheduler.execute();
    }
}
