package so.notion.converter.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import so.notion.converter.Entity.Order;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Класс, выполняющий конвертацию данных.
 */
@Service
@Scope("prototype")
public class ConverterService implements Runnable {

    private final BlockingQueue<Order> sharedOrders;
    private final OutputStream stream;
    private final ObjectMapper mapper;

    public ConverterService(BlockingQueue<Order> sharedOrders, OutputStream stream) {
        this.sharedOrders = sharedOrders;
        this.stream = stream;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            Order order;
            while ((order = sharedOrders.poll(1, TimeUnit.SECONDS)) != null) {
                try {
                    stream.write(mapper.writeValueAsBytes(order));
                    stream.write(new byte[]{'\n'});
                } catch (IOException e) {
                    Order temp = new Order();
                    temp.setResult(e.getMessage());
                    mapper.writeValue(stream, temp);
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
