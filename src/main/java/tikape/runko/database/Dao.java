package tikape.runko.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    //EI SAA KÄYTTÄÄ
     
    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    void delete(K key) throws SQLException;
}
