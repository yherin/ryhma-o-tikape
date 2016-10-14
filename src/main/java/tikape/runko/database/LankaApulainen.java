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
}
