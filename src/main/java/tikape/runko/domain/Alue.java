/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.sql.Timestamp;

/**
 *
 * @author jack
 */
public class Alue implements Retrievable{
    
    private int id;
    private String otsikko;

   
    private Timestamp viimeisinViesti;
    private Integer viesteja_alueessa;
    
    public Alue(){};

    public Alue(int id, String otsikko) {
        this.id = id;
        this.otsikko = otsikko;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }
    
     public Timestamp getViimeisinViesti() {
        return viimeisinViesti;
    }

    public void setViimeisinViesti(Timestamp viimeisinViesti) {
        this.viimeisinViesti = viimeisinViesti;
    }

    public Integer getViesteja_alueessa() {
        return viesteja_alueessa;
    }

    public void setViesteja_alueessa(Integer viesteja_alueessa) {
        this.viesteja_alueessa = viesteja_alueessa;
    }
    
    
    
}
