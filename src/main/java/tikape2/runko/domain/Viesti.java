/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.domain;
import java.sql.Timestamp;
import tikape2.runko.domain.Retrievable;
/**
 *
 * @author jack
 */
public class Viesti implements Retrievable<Viesti>{
    
    private int id; //PRIMARY KEY
    private int lankaid; //FOREIGN KEY VIITEAVAIN
    private Timestamp aikaleima;
    private String viesti;
    private String nimimerkki;
    

    public Viesti(int id, int lankaid, Timestamp aikaleima, String viesti, String nimimerkki) {
        this.id = id;
        this.lankaid = lankaid;
        this.aikaleima = aikaleima;
        this.viesti = viesti;
        this.nimimerkki = nimimerkki;
    }
    
    public Viesti(int lankaid, Timestamp aikaleima, String viesti, String nimimerkki) {
        this.lankaid = lankaid;
        this.aikaleima = aikaleima;
        this.viesti = viesti;
        this.nimimerkki = nimimerkki;
    }
    
    public Viesti(int id, int lankaid, String viesti, String nimimerkki) {
        this.id = id;
        this.lankaid = lankaid;
        this.viesti = viesti;
        this.nimimerkki = nimimerkki;
    }

    public int getId() {
        return id;
    }

    public int getLankaid() {
        return lankaid;
    }

    public Timestamp getAikaleima() {
        return aikaleima;
    }

    public String getViesti() {
        return viesti;
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLankaid(int lankaid) {
        this.lankaid = lankaid;
    }

    public void setAikaleima(Timestamp aikaleima) {
        this.aikaleima = aikaleima;
    }

    public void setViesti(String viesti) {
        this.viesti = viesti;
    }

    public void setNimimerkki(String nimimerkki) {
        this.nimimerkki = nimimerkki;
    }
    
    
    
    
}
