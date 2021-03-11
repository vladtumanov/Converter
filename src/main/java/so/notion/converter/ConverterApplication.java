package so.notion.converter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import so.notion.converter.Entity.Order;
import so.notion.converter.Service.ConverterService;
import so.notion.converter.Service.ParseService;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
public class ConverterApplication implements CommandLineRunner {

    private final ApplicationContext applicationContext;
    private final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static String[] filesName;

    public ConverterApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        filesName = args;
        SpringApplication.run(ConverterApplication.class, args);
    }

    @Override
    public void run(String... args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.execute(applicationContext.getBean(ParseService.class));
        executorService.execute(applicationContext.getBean(ConverterService.class));
        executorService.shutdown();
    }

    @Bean
    @Scope("singleton")
    public BlockingQueue<Order> blockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    @Scope("singleton")
    public String[] strings() {
        return filesName == null ? new String[0] : filesName;
    }

    @Bean
    @Scope("singleton")
    public OutputStream outputStream() {
        return System.out;
    }
}
