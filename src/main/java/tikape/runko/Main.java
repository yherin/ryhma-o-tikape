package tikape.runko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueApulainen;
import tikape.runko.database.Database;
import tikape.runko.database.LankaApulainen;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Lanka;

public class Main {

    public static void main(String[] args) throws Exception {

        Database database = new Database("jdbc:sqlite:foorumi.db");

        Connection conn = DriverManager.getConnection("jdbc:sqlite:foorumi.db");

        Statement statement = conn.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM Alue");

        
        //  database.init(); db on jo olemmasa

        // OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);   //VANHA
        AlueApulainen alueapulainen = new AlueApulainen(database);  //MEIDÄN TOTEUTUS mutta ei toimi vielä
        LankaApulainen lankaapulainen = new LankaApulainen(database);
        
        get("/", (req, res) -> {

            res.redirect("/alueet");
            return "";
        });
        
        get("/alueet", (req, res) -> {
        HashMap map = new HashMap<>();
        
        List<Alue> alueet = alueapulainen.getAlueetViestitAikaLeimat();
        
        map.put("alueet", alueet);
        
            System.out.println(alueet);
        
        return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());
        
        get("/alueet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            String id = req.params(":id");
            Alue alue = (Alue) alueapulainen.getSingle(Integer.parseInt(id));
            List<Lanka> langat = lankaapulainen.getLankaViesti(id);
            System.out.println(langat);
            
            map.put("langat", langat);
            map.put("alue", alue);
            
        return new ModelAndView(map, "alue");
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
