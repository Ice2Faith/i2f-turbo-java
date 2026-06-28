package i2f.design.pattern.concurrency.producerConsumer.product;

import lombok.Data;

/**
 * 生产者-消费者模式 —— 菜品（Product：Dish）
 *
 * <p><b>角色：</b>产品（Product）</p>
 *
 * <p><b>模式说明：</b>生产者和消费者之间传递的数据对象。
 * 在生产者-消费者模式中，缓冲区存储的就是这类产品对象。</p>
 *
 * <p><b>命名立意：</b>以"餐厅菜品"为场景——厨师制作各种菜品（产品），
 * 服务员将菜品送给顾客。菜品是生产与消费的核心载体。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
@Data
public class Dish {

    /**
     * 菜品名称。
     */
    private String name;

    /**
     * 菜品类型（如：主菜、配菜、甜点等）。
     */
    private String type;

    /**
     * 制作时间（秒）。
     */
    private int cookTime;

    public Dish() {
    }

    public Dish(String name, String type, int cookTime) {
        this.name = name;
        this.type = type;
        this.cookTime = cookTime;
    }

    /**
     * 获取菜品描述。
     *
     * @return 菜品描述字符串
     */
    public String getDescription() {
        return name + " [" + type + ", 制作耗时: " + cookTime + "秒]";
    }

    @Override
    public String toString() {
        return "Dish{name='" + name + "', type='" + type + "'}";
    }
}
