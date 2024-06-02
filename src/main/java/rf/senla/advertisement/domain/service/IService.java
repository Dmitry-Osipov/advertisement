package rf.senla.advertisement.domain.service;

import java.util.List;

/**
 * Сервис для работы с сущностями.
 */
public interface IService<T> {
    /**
     * Сохранение сущности
     * @param entity сущность
     * @return сохранённая сущность
     */
    T save(T entity);

    /** Обновление сущности
     * @param entity сущность
     * @return обновлённая сущность
     */
    T update(T entity);

    /** Удаление сущности
     * @param entity сущность
     */
    void delete(T entity);

    /**
     * Получение списка сущностей
     * @return список сущностей
     */
    List<T> getAll();
}
