package so.notion.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import so.notion.converter.Entity.Order;
import so.notion.converter.Service.ConverterService;

import java.io.*;
import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConverterApplicationTests {

    @Qualifier("converterService")
    private static ConverterService converterService;

    private static OutputStream stream;

    private static final Order order = new Order();
    private static final BlockingQueue<Order> sharedQueue = new LinkedBlockingQueue<>();

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void init() {
        stream = Mockito.mock(OutputStream.class);
        converterService = new ConverterService(sharedQueue, stream);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        order.setId(1L);
        order.setAmount(BigDecimal.valueOf(1.11d));
        order.setCurrency("TST");
        order.setComment("test order");
        order.setFilename("test.tst");
        order.setLine(1L);
        order.setResult("OK");
        sharedQueue.clear();
        sharedQueue.put(order);
    }

    @Test
    void contextLoads() {
        assertThat(converterService).isNotNull();
        assertThat(stream).isNotNull();
        assertThat(order).isNotNull();
        assertThat(sharedQueue).isNotNull();
    }

    @Test
    void outputTest() throws IOException {
        converterService.run();
        Mockito.verify(stream).write(mapper.writeValueAsBytes(order));
    }
}
