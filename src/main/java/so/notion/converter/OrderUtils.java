package so.notion.converter;

import so.notion.converter.Entity.Order;

public class OrderUtils {

    public static Order validate(Order order) {
        if (order.getResult() == null) {

            order.setResult("OK");
        }

        return order;
    }
}
