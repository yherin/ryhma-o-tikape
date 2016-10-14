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
public class LankaApulainen extends Apulainen {

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
        return alueet;
    }

    public List<Lanka> getLankaViesti(String key) throws SQLException { //toinen näkymä
        Connection connection = this.database.getConnection();
        
        /*
        Jos alueessa on 2 tai enemmän lankaa, jossa on sama COUNT(Viesti.id) lukumaara tämä kysely ei toimii oikean.
        Tämä tapahtuu kun alueid = 2 tai 3.
        Kun alueid on 1 tai 4, kysely toimii hyvin.
        */
        
        /*
        If the number of messages in 2 or more threads is the same, the query will join them together and not work:
        
        If alueid = 2
        
        lankaid|lukumaara|viimeisinviesti
        3|4|2016-10-04 20:02:00
        
        If alueid = 1
        
        lankaid|lukumaara|viimeisinviesti
        1|2|2016-10-04 08:54:00
        2|3|2016-10-04 20:00:00

        
        */

        String sql
                = "SELECT Lanka.id as id, Lanka.otsikko as otsikko, COUNT(Viesti.id) as lukumaara, MAX(Viesti.aikaleima) AS 'viimeisinviesti' "
                + "FROM Lanka, Alue "
                + "JOIN Viesti ON Alue.id = Lanka.alueid AND Lanka.id = Viesti.lankaid "
                + "WHERE Lanka.alueid = ? "
                + "GROUP BY Lanka.id;";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, key);

        ResultSet tulos = statement.executeQuery();

        List<Lanka> langat = new ArrayList<>();
        while (tulos.next()) {

            Integer id = tulos.getInt("id");
          //  Integer alue_id = tulos.getInt("alue_id"); // EI TOIMII "GROUP BY":n kanssa
            String otsikko = tulos.getString("otsikko");

            Lanka l = new Lanka(id, Integer.parseInt(key), otsikko);
            l.setViesteja_langassa(tulos.getInt("lukumaara"));
            l.setViimeisinViesti(tulos.getString("viimeisinviesti"));

            langat.add(l);
        }

        return langat;
    }
}
