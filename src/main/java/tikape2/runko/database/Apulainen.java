/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.database;
import java.sql.SQLException;
import java.util.List;

        

/**
 *
 * @author jack
 */
public abstract class Apulainen<T> {
    
    public abstract T getSingle(int id) throws SQLException;
    //hae tietokannasta yhden rivin id:n avulla
    
    
    public abstract List<T> getAll() throws SQLException; 
    //not yet implemented, hae kaikki taulun rivin
            
    public abstract T create(T t) throws SQLException; 
    
}
