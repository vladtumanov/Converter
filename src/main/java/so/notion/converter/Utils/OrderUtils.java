package so.notion.converter.Utils;

import so.notion.converter.Entity.Order;

public class OrderUtils {

    /**
     * Проверка {@link Order} на корректность исходных данных. Если данные корректны, то метод {@link Order#getResult}
     * вернёт 'OK', иначе сообщение об ошибке.
     * @param order Объект, который необходимо проверить.
     * @return Проверенный объект.
     */
    public static Order validate(Order order) {
        if (order.getResult() == null) {
            StringBuilder result = new StringBuilder();

            if (order.getId() == null)
                result.append("orderId is null;");
            if (order.getAmount() == null)
                result.append("amount is null;");
            if (order.getCurrency() == null || order.getCurrency().equals(""))
                result.append("currency is null or empty;");
            if (order.getComment() == null || order.getComment().equals(""))
                result.append("comment is null or empty;");
            order.setResult(result.length() == 0 ? "OK" : result.toString());
        }
        return order;
    }
}
