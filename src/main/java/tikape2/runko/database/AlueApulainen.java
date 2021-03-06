/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import sun.reflect.generics.reflectiveObjects.*;
import tikape2.runko.domain.Alue;

/**
 *
 * @author jack
 *
 * Alue taulu apulainen
 */
public class AlueApulainen extends Apulainen<Alue> {

    private Database database;

    public AlueApulainen(Database database) {
        this.database = database;
    }

    @Override
    public Alue getSingle(int id) throws SQLException {
        /*
        Hae yhden rivin Alue -taulusta
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
            statement.close();
            connection.close();
            return alue;
        }

    }

    @Override
    public List<Alue> getAll() throws SQLException {
        /*
        Hae kaikki alueet
        Ei käytössä.
        */
        Connection connection = this.database.getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Alue");
        ResultSet tulos = statement.executeQuery();

        List<Alue> alueet = new ArrayList<>();
        while (tulos.next()) {
            Alue alue = new Alue();
            alue.setId(tulos.getInt("id"));
            alue.setOtsikko(tulos.getString("otsikko"));
            alueet.add(alue);
        }

        statement.close();
        connection.close();
        return alueet;
    }

    public List<Alue> getAlueetViestitAikaLeimat() throws SQLException {
        
        /*
        Hae kaikki alueet (aakkosjärjestyksessä), viestit yhteensä ja viimeisin viesti.
        */
        
        Connection connection = this.database.getConnection();

        String sql = "SELECT Alue.id AS 'id', Alue.otsikko AS 'alue', COUNT(Viesti.id) AS 'viestit', MAX(Viesti.aikaleima) AS 'viimeisinviesti' FROM Alue, Lanka \n"
                + "LEFT JOIN Viesti ON Alue.id = Lanka.alueid AND Lanka.id = Viesti.lankaid\n"
                + "GROUP BY Alue.otsikko\n"
                + "ORDER BY Alue.otsikko ASC;";

        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet tulos = statement.executeQuery();

        List<Alue> alueet = new ArrayList<>();

        while (tulos.next()) {
            Integer id = tulos.getInt("id");
            Integer viestit = tulos.getInt("viestit");
            String otsikko = tulos.getString("alue");
            Date aikaleima = tulos.getDate("viimeisinviesti");

            Alue alue = new Alue(id, otsikko);
            alue.setViesteja_alueessa(viestit);
            alue.setViimeisinViesti(aikaleima);

            alueet.add(alue);

        }

        statement.close();
        connection.close();
        return alueet;
    }
    
    @Override
    public Alue create(Alue alue) throws SQLException {
        /*
        Luo uusi alue
        */
        Connection connection = this.database.getConnection();

        String sql = "INSERT INTO Alue (otsikko) VALUES (?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, alue.getOtsikko());

        statement.executeUpdate();

        statement.close();
        connection.close();
        return alue;
    }

}
