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
import java.text.SimpleDateFormat;
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
        lanka.setAlueid(tulos.getInt("alueid"));
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

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Lanka JOIN Viesti ON Viesti.lankaid = Lanka.id ORDER BY"
                + " MAX(Viesti.aikaleima DESC LIMIT 10;");
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
                + "GROUP BY Lanka.id ORDER BY viimeisinviesti DESC LIMIT 10;";

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
            l.setViimeisinViesti(tulos.getDate("viimeisinviesti"));

            langat.add(l);
        }

        statement.close();
        connection.close();

        return langat;
    }

    public List<Viesti> getKaikkiViestit(String key) throws SQLException {
        Connection connection = this.database.getConnection();
            String sql
                = "SELECT Viesti.id AS id, Viesti.teksti AS teksti, Viesti.nimimerkki AS nimimerkki, Viesti.aikaleima AS aikaleima "
                + "FROM Viesti, Lanka "
                + "WHERE Lanka.id = Viesti.lankaid AND Lanka.id = ? ;";

//        String sql
//                = "SELECT Viesti.id AS id, Viesti.teksti AS teksti, Viesti.nimimerkki AS nimimerkki, Viesti.aikaleima AS aikaleima, MAX(Viesti.aikaleima) as viimeisin "
//                + "FROM Viesti, Lanka "
//                + "WHERE Lanka.id = Viesti.lankaid AND Lanka.id = ? "
//                + "ORDER BY viimeisin DESC LIMIT 10";

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

            //new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(this.viimeisinViesti);

            v.setAikaleima(tulos.getDate("aikaleima"));
            //v.setAikaleima(tulos.getTimestamp(Viesti.aikaleima));

            viestit.add(v);
        }
        statement.close();
        connection.close();

        return viestit;
    }

    public List<Viesti> getKaikkiViestit(String key, int sivu) throws SQLException {
        Connection connection = this.database.getConnection();
        ResultSet tulos = null;
        int offset = (sivu -1) * 10;
        

            String sql
                    = "SELECT Viesti.id AS id, Viesti.teksti AS teksti, Viesti.nimimerkki AS nimimerkki, Viesti.aikaleima AS aikaleima "
                    + "FROM Viesti, Lanka "
                    + "WHERE Lanka.id = Viesti.lankaid AND Lanka.id = ? "
                    + "ORDER BY aikaleima ASC LIMIT 10 OFFSET ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, key);

            statement.setObject(2, offset);
            tulos = statement.executeQuery();

        List<Viesti> viestit = new ArrayList<>();

        while (tulos.next()) {
            Integer id = tulos.getInt("id");
            //  Integer alue_id = tulos.getInt("alue_id"); // EI TOIMII "GROUP BY":n kanssa
            String teksti = tulos.getString("teksti");
            String nimimerkki = tulos.getString("nimimerkki");

            Viesti v = new Viesti(id, Integer.parseInt(key), teksti, nimimerkki);

            //new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(this.viimeisinViesti);
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");

            v.setAikaleima(tulos.getDate("aikaleima"));
            //v.setAikaleima(tulos.getTimestamp(Viesti.aikaleima));

            viestit.add(v);
        }
        System.out.println("viestit: " + viestit);
        connection.close();

        return viestit;
    }
    
    public int getSivujenMaaraLangassa(String key) throws SQLException {
        Connection connection = this.database.getConnection();
            String sql
                = "SELECT COUNT(Viesti.id) AS lukumaara "
                + "FROM Viesti, Lanka "
                + "WHERE Lanka.id = Viesti.lankaid AND Lanka.id = ? ;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, key);

        ResultSet tulos = statement.executeQuery();
        Integer lkm = 0;
        
        while (tulos.next()) {
            lkm = tulos.getInt("lukumaara");
        }
        statement.close();
        connection.close();

        return (int) Math.ceil(1.0 * lkm/10);       
    }

    @Override
    public Lanka create(Lanka t) throws SQLException {
        Connection connection = this.database.getConnection();

        String sql_lanka = "INSERT INTO Lanka (alueid, otsikko) VALUES (?, ?)";
        String sql_hae_lankaid = "SELECT Lanka.id as id FROM Lanka ORDER BY Lanka.id DESC LIMIT 1";
        String sql_viesti = "INSERT INTO Viesti (lankaid, aikaleima, teksti, nimimerkki) VALUES (?, ?, ?, ?)";

        
        //Lanka luodaan
        PreparedStatement statement_lanka = connection.prepareStatement(sql_lanka);
        statement_lanka.setInt(1, t.getAlueid());
        statement_lanka.setString(2, t.getOtsikko());

        statement_lanka.executeUpdate();
         statement_lanka.close();
        
        //Lanka.id haetaan
        PreparedStatement statement_hae_lankaid = connection.prepareStatement(sql_hae_lankaid);
        ResultSet tulos = statement_hae_lankaid.executeQuery();
        int lankaid = tulos.getInt("id");
        statement_hae_lankaid.close();
         
        //Viesti luodaan
        PreparedStatement statement_viesti = connection.prepareStatement(sql_viesti);
        statement_viesti.setObject(1, lankaid);
        statement_viesti.setObject(2, t.getViesti().getAikaleima());
        statement_viesti.setObject(3, t.getViesti().getViesti());
        statement_viesti.setObject(4, t.getViesti().getNimimerkki());
        statement_viesti.execute();
        statement_viesti.close();
        
        
        connection.close();

        return t;
    }
}
