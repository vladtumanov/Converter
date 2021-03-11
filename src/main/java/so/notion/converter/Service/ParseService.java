package so.notion.converter.Service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import so.notion.converter.Entity.Order;
import so.notion.converter.Exception.RepositoryException;
import so.notion.converter.Repository.OrderRepository;
import so.notion.converter.Repository.RepositoryFactory;
import so.notion.converter.Utils.OrderUtils;

import java.util.concurrent.*;

/**
 * Класс, выполняющий парсинг исходных данных.
 */
@Service
@Scope("prototype")
public class ParseService implements Runnable {

    private RepositoryFactory repositoryFactory;
    private final BlockingQueue<Order> sharedOrders;
    private final String[] filesRepo;

    public ParseService(BlockingQueue<Order> sharedOrders, String[] filesRepo) {
        this.sharedOrders = sharedOrders;
        this.filesRepo = filesRepo;
    }

    //@Autowired
    public void setRepositoryFactory(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void run() {
        for (String fileName : filesRepo) {
            try {
                OrderRepository repository = repositoryFactory.getRepository(fileName);
                for (Order order : repository.getOrders()) {
                    sharedOrders.put(OrderUtils.validate(order));
                }
            } catch (InterruptedException | RepositoryException e) {
                e.printStackTrace();
            }
        }
    }
}