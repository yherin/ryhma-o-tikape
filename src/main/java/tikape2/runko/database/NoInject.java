/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.database;

/**
 *
 * @author sjack
 */
public class NoInject {
    
    
    public static String cleanHtml(String string){
        
        
        // "<" ja ">" poistetaan viestistä
        return string.replaceAll("<", " ").replaceAll(">", " ").trim();
    }
    
}
