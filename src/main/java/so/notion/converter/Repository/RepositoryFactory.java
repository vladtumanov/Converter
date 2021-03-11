package so.notion.converter.Repository;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import so.notion.converter.Exception.RepositoryException;

/**
 * Singleton. Класс фабрика, для получения данных из файлов.
 *
 * @author Vladislav Tumanov
 */
@Component
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RepositoryFactory {

    private final ApplicationContext context;

    public RepositoryFactory(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Получение объекта репозитория для доступа к его данным.
     * @param fileName Имя файла репозитория.
     * @return {@link OrderRepository}
     * @throws RepositoryException если возникла ошибка доступа.
     */
    public OrderRepository getRepository(String fileName) throws RepositoryException {
        String extension = getFileExtension(fileName);

        switch (extension) {
            case "csv" : return context.getBean(CsvRepository.class, fileName);
            case "json" : return context.getBean(JsonRepository.class, fileName);
            default: throw new RepositoryException("File extension not support " + fileName);
        }
    }

    private String getFileExtension(String fileName) throws RepositoryException {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1 || lastIndex == 0)
            throw new RepositoryException("File extension not found" + fileName);
        return fileName.substring(lastIndex + 1).toLowerCase();
    }
}
