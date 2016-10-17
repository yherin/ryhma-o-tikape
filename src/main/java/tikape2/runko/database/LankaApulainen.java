/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape2.runko.domain.Alue;
import tikape2.runko.domain.Lanka;
import tikape2.runko.domain.Retrievable;
import tikape2.runko.domain.Viesti;

/**
 *
 * @author sjack
 */
public class LankaApulainen extends Apulainen<Lanka> {

    private Database database;

    public LankaApulainen(Database database) {
        this.database = database;
    }

    @Override
    public Lanka getSingle(int id) throws SQLException {
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
            statement.close();
            connection.close();
            return lanka;
        }

    }

    @Override
    public List<Lanka> getAll() throws SQLException {
        Connection connection = this.database.getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Lanka");
        ResultSet tulos = statement.executeQuery();

        List<Lanka> langat = new ArrayList<>();
        while (tulos.next()) {
            Lanka lanka = new Lanka();
            lanka.setId(tulos.getInt("id"));
            lanka.setAlueid(tulos.getInt("alueid"));
            lanka.setOtsikko(tulos.getString("otsikko"));
            langat.add(lanka);
        }
        
        statement.close();
        connection.close();
        return langat;
    }

    public List<Lanka> getLankaViesti(String key) throws SQLException { //toinen näkymä
        Connection connection = this.database.getConnection();

        String sql
                = "SELECT Lanka.id as id, Lanka.otsikko as otsikko, COUNT(Viesti.id) as lukumaara, MAX(Viesti.aikaleima) AS 'viimeisinviesti' "
                + "FROM Lanka, Alue "
                + "LEFT JOIN Viesti ON Alue.id = Lanka.alueid AND Lanka.id = Viesti.lankaid "
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

        statement.close();
        connection.close();
        
        return langat;
    }

    public List<Viesti> getKaikkiViestit(String key) throws SQLException {
        Connection connection = this.database.getConnection();

        String sql
                = "SELECT Viesti.id AS id, Viesti.teksti AS teksti, Viesti.nimimerkki AS nimimerkki "
                + "FROM Viesti, Lanka "
                + "WHERE Lanka.id = Viesti.lankaid AND Lanka.id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, key);

        ResultSet tulos = statement.executeQuery();

        List<Viesti> viestit = new ArrayList<>();

        while (tulos.next()) {
            Integer id = tulos.getInt("id");
            //  Integer alue_id = tulos.getInt("alue_id"); // EI TOIMII "GROUP BY":n kanssa
            String teksti = tulos.getString("teksti");
            String nimimerkki = tulos.getString("nimimerkki");

            Viesti v = new Viesti(id, Integer.parseInt(key), teksti, nimimerkki);
            //v.setAikaleima(tulos.getTimestamp(Viesti.aikaleima));

            viestit.add(v);
        }
        statement.close();
        connection.close();

        return viestit;
    }

    @Override
    public Lanka create(Lanka t) throws SQLException {
        Connection connection = this.database.getConnection();

        String sql = "INSERT INTO Lanka (alueid, otsikko) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, t.getAlueid());
        statement.setString(2, t.getOtsikko());

        statement.executeUpdate();

        statement.close();
        connection.close();
        
        return t;
    }
}
