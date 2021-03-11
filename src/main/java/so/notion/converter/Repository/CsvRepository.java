package so.notion.converter.Repository;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import so.notion.converter.Entity.Order;
import so.notion.converter.Exception.RepositoryException;
import so.notion.converter.Utils.OrderUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype")
public class CsvRepository extends TextFileRepository implements OrderRepository {

    /**
     * Начало отсчёта строк исходных данных
     */
    private long lineNum = 1;

    /**
     * Разделитель для CSV файла
     */
    private final String DELIMITER = ",";

    /**
     * Конструктор для создания экземпляра файлового репозитория.
     *
     * @param filePath Путь к текстовому файлу.
     */
    public CsvRepository(String filePath) {
        super(filePath);
    }

    /**
     * Получение коллекции всех {@link Order} из репозитория.
     *
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
     *
     * @param line Строка исходных данных в CSV формате.
     * @return {@link Order}
     */
    private Order parseLine(String line) {
        Order order = new Order();
        String[] row = line.split(DELIMITER);
        if (row.length == 4) {
            try {
                order.setId(Long.valueOf(row[0]));
                order.setAmount(new BigDecimal(row[1]));
                order.setCurrency(row[2]);
                order.setComment(row[3]);
            } catch (NumberFormatException e) {
                order.setResult("Incorrect data format");
            }
        } else {
            order.setResult("Incorrect column count in");
        }
        order.setFilename(getFilename());
        order.setLine(lineNum++);
        return OrderUtils.validate(order);
    }
}
