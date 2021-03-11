package so.notion.converter.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import so.notion.converter.Entity.Order;
import so.notion.converter.Exception.RepositoryException;
import so.notion.converter.Utils.OrderUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype")
public class JsonRepository extends TextFileRepository implements OrderRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Начало отсчёта строк исходных данных
     */
    private long lineNum = 1;

    /**
     * Конструктор для создания экземпляра файлового репозитория.
     *
     * @param filePath Путь к текстовому файлу.
     */
    public JsonRepository(String filePath) {
        super(filePath);
    }

    /**
     * Получение коллекции всех {@link Order} из репозитория.
     * @return Коллекция {@link Order}.
     * @throws RepositoryException
     */
    @Override
    public List<Order> getOrders() throws RepositoryException {
        return readAllLines()
                .stream()
                .map(this::parseLine)
                .collect(Collectors.toList());
    }

    /**
     * Метод преобразующий исходные данные в объект {@link Order}.
     * @param line Строка исходных данных в JSON формате.
     * @return {@link Order}
     */
    private Order parseLine(String line) {
        Order order;
        try {
            order = mapper.readValue(line, Order.class);
            order.setFilename(getFilename());
            order.setLine(lineNum);
        } catch (JsonProcessingException e) {
            order = new Order();
            order.setFilename(getFilename());
            order.setLine(lineNum);
            order.setResult(e.getMessage());
        }
        lineNum++;
        return OrderUtils.validate(order);
    }
}
