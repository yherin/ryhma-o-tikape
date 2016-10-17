/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape2.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import tikape2.runko.domain.*;

/**
 *
 * @author miramajuri
 */
public class ViestiApulainen extends Apulainen<Viesti> {

    private Database database;

    public ViestiApulainen(Database database) {
        this.database = database;
    }

    @Override
    public Viesti getSingle(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> getAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Viesti create(Viesti viesti) throws SQLException {
        Connection connection = this.database.getConnection();

        String sql = "INSERT INTO Viesti (lankaid, aikaleima, teksti, nimimerkki) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, viesti.getLankaid());
        statement.setDate(2, viesti.getAikaleima());
        statement.setString(3, viesti.getViesti());
        statement.setString(4, viesti.getNimimerkki());

        statement.executeUpdate();
        statement.close();
        connection.close();

        return viesti;
    }

}
