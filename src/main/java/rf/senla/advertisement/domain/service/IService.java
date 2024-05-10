package rf.senla.advertisement.domain.service;

import rf.senla.advertisement.domain.entity.Identifiable;

import java.util.List;

/**
 * Сервис для работы с сущностями, имеющими id.
 */
public interface IService<T extends Identifiable> {
    /**
     * Сохранение пользователя сущности
     * @param entity сущность
     * @return сохраненная сущность
     */
    T save(T entity);

    /** Обновление пользователя
     * @param entity сущность
     * @return обновлённый пользователь
     */
    T update(T entity);

    /** Удаление сущности
     * @param entity сущность
     */
    void delete(T entity);

    /**
     * Получение всех сущностей
     * @return список сущностей
     */
    List<T> getAll();
}
