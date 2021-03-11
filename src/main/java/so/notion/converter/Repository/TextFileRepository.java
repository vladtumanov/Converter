package so.notion.converter.Repository;

import so.notion.converter.Exception.RepositoryException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Абстрактный класс репозитория с доступом к текстовым файлам.
 * <p>Примечание: для корректной работы, текст в файле должен быть
 * в UTF-8 кодировке.
 *
 * @author Vladislav Tumanov
 */
public abstract class TextFileRepository {

    /** Полне с информацией о пути к файлу. */
    private final File file;

    /**
     * Конструктор для создания экземпляра файлового репозитория.
     * @param filePath Путь к текстовому файлу.
     */
    public TextFileRepository(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Метод, возвращающий все данные из репозитория. Если произошла ошибка доступа
     * к файлу репозитория, то в стандартный поток вывода ошибок отправляется сообщение
     * с описанием ошибки.
     * @return Список строк из репозитория. Если данных нет, то возвращается пустой список.
     * @throws RepositoryException если возникли проблемы с доступом.
     */
    protected final List<String> readAllLines() throws RepositoryException {
        try {
            if (!file.exists())
                throw new RepositoryException("File not found " + file.getName());
            return Files.lines(file.toPath(), StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    /**
     * Метод, возвращающий имя файла.
     * @return Имя файла.
     */
    public String getFilename() {
        return file.getName();
    }
}
