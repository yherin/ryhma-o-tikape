/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.domain;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author jack
 */
public class Lanka {
    
    private int id;
    private int alueid;
    private List<Viesti> viestit;
    private String otsikko;
    private String teksti;
    private Viesti viesti;
    
    private String viimeisinViestiString;
    private Date viimeisinViesti;
    private Integer viesteja_langassa;
    
    private String nimimerkki;

    public Lanka(){
        
    }
    
    public Lanka(int id, int alueid, List<Viesti> viestit, String otsikko, String teksti, String nimimerkki) {
        this.id = id;
        this.alueid = alueid;
        this.viestit = viestit;
        this.otsikko = otsikko;
        this.teksti = teksti;
        this.nimimerkki = nimimerkki;
        
    }
    
    public Lanka( int alueid, String otsikko,Viesti viesti) {
        this.alueid = alueid;
        this.otsikko = otsikko;
        this.viesti = viesti;
        
    }
    
     public Lanka(int id, int alueid,  String otsikko) {
        this.id = id;
        this.alueid = alueid;
        
        this.otsikko = otsikko;
    }
     
     public Lanka(int alueid,  String otsikko) {
        this.alueid = alueid;
        this.otsikko = otsikko;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlueid() {
        return alueid;
    }

    public void setAlueid(int alueid) {
        this.alueid = alueid;
    }

    public List<Viesti> getViestit() {
        return viestit;
    }

    public void setViestit(List<Viesti> viestit) {
        this.viestit = viestit;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public Date getViimeisinViesti() {
        return viimeisinViesti;
    }

    public void setViimeisinViesti(Date viimeisinViesti) {
        this.viimeisinViesti = viimeisinViesti;
    }

    public Integer getViesteja_langassa() {
        return viesteja_langassa;
    }

    public void setViesteja_langassa(Integer viesteja_langassa) {
        this.viesteja_langassa = viesteja_langassa;
    }

    @Override
    public String toString() {
        return "Lanka{" + "id=" + id + ", alueid=" + alueid + ", viestit=" + viestit + ", otsikko=" + otsikko + ", viimeisinViesti=" + viimeisinViesti + ", viesteja_langassa=" + viesteja_langassa + '}';
    }

    public String getViimeisinViestiString() {
        try {
        String aikaleima = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(this.viimeisinViesti);
        return aikaleima;
        } catch (NullPointerException e){
            return "Ei viestejä";
        }
    }
    
    public long getViimeisinViestiAika(){
        return this.getViimeisinViesti().getTime();
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

    public Viesti getViesti() {
        return viesti;
    }

    public void setViesti(Viesti viesti) {
        this.viesti = viesti;
    }
    
    
    
}
