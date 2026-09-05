package com.colombiando.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica que define las operaciones CRUD básicas
 * que deben implementar todos los DAOs del sistema.
 *
 * @param <T>  tipo de entidad
 * @param <ID> tipo del identificador primario
 */
public interface GenericoDAO<T, ID> {

    /**
     * Inserta un nuevo registro en la base de datos.
     *
     * @param entidad objeto a persistir
     * @return {@code true} si se insertó correctamente
     * @throws SQLException si ocurre un error en la BD
     */
    boolean insertar(T entidad) throws SQLException;

    /**
     * Actualiza un registro existente en la base de datos.
     *
     * @param entidad objeto con los datos actualizados
     * @return {@code true} si se actualizó correctamente
     * @throws SQLException si ocurre un error en la BD
     */
    boolean actualizar(T entidad) throws SQLException;

    /**
     * Elimina un registro por su identificador.
     *
     * @param id identificador del registro
     * @return {@code true} si se eliminó correctamente
     * @throws SQLException si ocurre un error en la BD
     */
    boolean eliminar(ID id) throws SQLException;

    /**
     * Busca un registro por su identificador.
     *
     * @param id identificador del registro
     * @return Optional con la entidad encontrada, o vacío si no existe
     * @throws SQLException si ocurre un error en la BD
     */
    Optional<T> buscarPorId(ID id) throws SQLException;

    /**
     * Retorna todos los registros de la entidad.
     *
     * @return lista de entidades; vacía si no hay registros
     * @throws SQLException si ocurre un error en la BD
     */
    List<T> listarTodos() throws SQLException;
}
