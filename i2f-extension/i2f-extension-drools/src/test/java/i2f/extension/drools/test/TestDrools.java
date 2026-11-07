package i2f.extension.drools.test;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Ice2Faith
 * @date 2026/11/7 15:30
 * @desc
 */
public class TestDrools {

    public static void main(String[] args) {
        testHello();
    }

    public static void testHello() {
        // 获取服务
        KieServices kieServices = KieServices.Factory.get();

        // 获取容器
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        // 获取会话
        KieSession kieSession = kieContainer.newKieSession();

        // 创建事实对象
        Order order = new Order();
        order.setAmount(102);

        // 添加到会话中
        kieSession.insert(order);

        // 触发规则引擎执行
        kieSession.fireAllRules();

        // 也可以单独触发某个规则执行
        // kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("hello_lt_100"));

        // 通过事实对象获取结果
        System.out.println("result score: " + order.getScore());

        // 销毁会话
        kieSession.dispose();
    }


    public static void testContains() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        // 创建事实对象
        Order order = new Order();
        order.setAmount(67);

        // 添加到会话中
        kieSession.insert(order);

        Customer customer = new Customer();
        customer.setName("Jake");
        customer.setOrders(new ArrayList<>());
        customer.getOrders().add(order);

        kieSession.insert(customer);

        kieSession.fireAllRules();

        System.out.println("result score: " + order.getScore());

        kieSession.dispose();
    }

    public static void testEffectExpire() {
        // 设置生失效的时间格式，替换默认的 dd-MM-yyyy 格式
        System.setProperty("drools.dateformat", "yyyy-MM-dd");

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        Order order = new Order();
        order.setAmount(67);

        kieSession.insert(order);

        kieSession.fireAllRules();

        System.out.println("result score: " + order.getScore());

        kieSession.dispose();
    }

    public static void testAgentGroup() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        // 设置添加对应议程的焦点，纳入规则执行
        kieSession.getAgenda().getAgendaGroup("simple").setFocus();

        Order order = new Order();
        order.setAmount(67);

        kieSession.insert(order);

        kieSession.fireAllRules();

        System.out.println("result score: " + order.getScore());

        kieSession.dispose();
    }

    public static void testTimerInterval() throws Throwable {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        Order order = new Order();
        order.setAmount(67);

        kieSession.insert(order);

        new Thread(() -> {
            // 执行指导停止
            kieSession.fireUntilHalt(new RuleNameEqualsAgendaFilter("grammar_timer_interval"));
        }).start();

        // 等待一段时间停止
        Thread.sleep(10 * 1000);
        kieSession.halt();

        System.out.println("result score: " + order.getScore());

        kieSession.dispose();
    }

    public static void testTimerCron() throws Throwable {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        Order order = new Order();
        order.setAmount(67);

        kieSession.insert(order);

        new Thread(() -> {
            // 执行指导停止
            kieSession.fireUntilHalt(new RuleNameEqualsAgendaFilter("grammar_timer_cron"));
        }).start();

        // 等待一段时间停止
        Thread.sleep(10 * 1000);
        kieSession.halt();

        System.out.println("result score: " + order.getScore());

        kieSession.dispose();
    }


    public static void testGlobalVariable() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();


        List<Order> globalOrders = new ArrayList<>();
        kieSession.setGlobal("g_orders", globalOrders);

        Order order = new Order();
        order.setAmount(67);

        kieSession.insert(order);

        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("grammar_global"));

        System.out.println("result score: " + order.getScore());

        System.out.println("global_orders: " + globalOrders);

        kieSession.dispose();
    }

    public static void testQuery() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();


        Order order = new Order();
        order.setAmount(67);

        kieSession.insert(order);

        kieSession.fireAllRules();

        System.out.println("result score: " + order.getScore());

        // 获取工作内存里面的对象
        QueryResults results = kieSession.getQueryResults("grammar_query");
        for (QueryResultsRow result : results) {
            Order item = (Order) result.get("$order");
            System.out.println(item);
        }

        kieSession.dispose();
    }

    public static void testQueryArgs() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();


        Order order = new Order();
        order.setAmount(67);

        kieSession.insert(order);

        kieSession.fireAllRules();

        System.out.println("result score: " + order.getScore());

        // 获取工作内存里面的对象
        QueryResults results = kieSession.getQueryResults("grammar_query_args", 50);
        for (QueryResultsRow result : results) {
            Order item = (Order) result.get("$order");
            System.out.println(item);
        }

        kieSession.dispose();
    }
}
