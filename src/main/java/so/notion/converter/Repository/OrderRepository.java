package so.notion.converter.Repository;

import so.notion.converter.Entity.Order;
import so.notion.converter.Exception.RepositoryException;

import java.util.List;

public interface OrderRepository {

    /**
     * Получение коллекции всех {@link Order} из репозитория.
     *
     * @return Коллекция {@link Order}.
     * @throws RepositoryException
     */
    List<Order> getOrders() throws RepositoryException;
}
