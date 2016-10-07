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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Retrievable;

/**
 *
 * @author jack
 *
 * Alue taulu apulainen
 */
public class AlueApulainen extends Apulainen {

    private Database database;

    public AlueApulainen(Database database) {
        this.database = database;
    }

    @Override
    public Retrievable getSingle(int id) throws SQLException {
        /*
        Hae yhden rivin Alue -taulusta
        EI TESTATTU
         */
        Connection connection = this.database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        statement.setObject(1, id);

        ResultSet tulos = statement.executeQuery();

        Alue alue = new Alue();
        alue.setId(tulos.getInt("id"));
        alue.setOtsikko(tulos.getString("otsikko"));

        if (alue == null) {
            throw new SQLException("alue was null :(");
        } else {
            return alue;
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
