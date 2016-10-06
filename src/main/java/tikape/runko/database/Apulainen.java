/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;
import java.sql.SQLException;
import java.util.List;
import tikape.runko.domain.Retrievable;

        

/**
 *
 * @author jack
 */
public abstract class Apulainen {
    
    public abstract Retrievable getSingle(int id) throws SQLException;
    //hae tietokannasta yhden rivin id:n avulla
    
    
    public abstract List<Retrievable> getAll() throws SQLException; 
    //not yet implemented, hae kaikki taulun rivin
            
    
}
