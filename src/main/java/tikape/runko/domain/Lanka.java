/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.List;

/**
 *
 * @author jack
 */
public class Lanka implements Retrievable {
    
    private int id;
    private int alueid;
    private List<Viesti> viestit;
    private String otsikko;

    public Lanka(int id, int alueid, List<Viesti> viestit, String otsikko) {
        this.id = id;
        this.alueid = alueid;
        this.viestit = viestit;
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
    
    
    
}