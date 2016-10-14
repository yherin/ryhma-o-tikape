/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Lanka;
import tikape.runko.domain.Retrievable;

/**
 *
 * @author sjack
 */
public class LankaApulainen extends Apulainen{
    
    private Database database;

    public LankaApulainen(Database database) {
        this.database = database;
    }

    @Override
    public Retrievable getSingle(int id) throws SQLException {
        /*
        Hae yhden rivin Alue -taulusta
        EI TESTATTU
         */
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Lanka WHERE id = ?");
        statement.setObject(1, id);

        ResultSet tulos = statement.executeQuery();

        Lanka lanka = new Lanka();
        lanka.setId(tulos.getInt("id"));
        lanka.setOtsikko(tulos.getString("otsikko"));

        if (lanka == null) {
            throw new SQLException("lanka :(");
        } else {
            return lanka;
        }

    }

    @Override
    public List<Retrievable> getAll() throws SQLException {
        Connection connection = this.database.getConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Alue");
        ResultSet tulos = statement.executeQuery();
        
        List<Retrievable> alueet = new ArrayList<>();
        while (tulos.next()) {
            Alue alue = new Alue();
            alue.setId(tulos.getInt("id"));
            alue.setOtsikko(tulos.getString("otsikko"));
            alueet.add(alue);
        }
        //EI VIELÄ VALMIS
        return alueet;
    }
    
    public List<Lanka> getLankaViesti(String key) throws SQLException{
        Connection connection = this.database.getConnection();
        
        String sql = "SELECT Lanka.id AS id, Lanka.alueid AS alue_id,  Lanka.otsikko AS 'otsikko', COUNT(Viesti.id) AS lukumaara, MAX(Viesti.aikaleima) AS 'viimeisinviesti' FROM Lanka, Alue " +
        "LEFT JOIN Viesti ON Viesti.lankaid = Lanka.id " +
        "WHERE Alue.id = ? " +
        "ORDER BY lukumaara DESC " +
        "LIMIT 10;";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, key);
        
        ResultSet tulos = statement.executeQuery();
        
        List<Lanka> langat = new ArrayList<>();
        while (tulos.next()){
            
            Integer id = tulos.getInt("id");
            Integer alue_id = tulos.getInt("alue_id");
            String otsikko = tulos.getString("otsikko");
            
            
            Lanka l = new Lanka(id, alue_id, otsikko);
            l.setViesteja_langassa(tulos.getInt("lukumaara"));
            l.setViimeisinViesti(tulos.getString("viimeisinviesti"));
            
            langat.add(l);
        }
        
        return langat;
    }
}
