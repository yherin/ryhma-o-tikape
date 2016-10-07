package tikape.runko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueApulainen;
import tikape.runko.database.Database;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.domain.Alue;

public class Main {

    public static void main(String[] args) throws Exception {

        Database database = new Database("jdbc:sqlite:foorumi.db");

        Connection conn = DriverManager.getConnection("jdbc:sqlite:foorumi.db");

        Statement statement = conn.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM Alue");

        
        //  database.init(); db on jo olemmasa

        // OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);   //VANHA
        AlueApulainen alueapulainen = new AlueApulainen(database);  //MEIDÄN TOTEUTUS mutta ei toimi vielä

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");
            map.put("alueet", alueapulainen.getAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        /* EI KÄYTETÄ
        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
         */
    }

}
