/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.domain;

import java.util.List;
import tikape2.runko.domain.Retrievable;

/**
 *
 * @author jack
 */
public class Lanka implements Retrievable<Lanka> {
    
    private int id;
    private int alueid;
    private List<Viesti> viestit;
    private String otsikko;
    
    private String viimeisinViesti;
    private Integer viesteja_langassa;

    public Lanka(){
        
    }
    
    public Lanka(int id, int alueid, List<Viesti> viestit, String otsikko) {
        this.id = id;
        this.alueid = alueid;
        this.viestit = viestit;
        this.otsikko = otsikko;
    }
    
     public Lanka(int id, int alueid,  String otsikko) {
        this.id = id;
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

    public String getViimeisinViesti() {
        return viimeisinViesti;
    }

    public void setViimeisinViesti(String viimeisinViesti) {
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
    
    
    
}
