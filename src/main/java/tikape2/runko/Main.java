package tikape2.runko;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape2.runko.database.AlueApulainen;
import tikape2.runko.database.Database;
import tikape2.runko.database.LankaApulainen;
import tikape2.runko.database.OpiskelijaDao;
import tikape2.runko.database.ViestiApulainen;
import tikape2.runko.domain.Alue;
import tikape2.runko.domain.Lanka;
import tikape2.runko.domain.Viesti;

public class Main {

    public static void main(String[] args) throws Exception {
        port(getHerokuAssignedPort());
        
        String dbOsoite = "jdbc:sqlite:foorumi.db";
        
         if (System.getenv("DATABASE_URL") != null) {
            dbOsoite = System.getenv("DATABASE_URL");
        } 

        
        Database database = new Database(dbOsoite);


        AlueApulainen alueapulainen = new AlueApulainen(database);  //MEIDÄN TOTEUTUS mutta ei toimi vielä
        LankaApulainen lankaapulainen = new LankaApulainen(database);
        ViestiApulainen viestiapulainen = new ViestiApulainen(database);

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
        
        post("/alueet/:id", (req, res) -> {
            int alueid = Integer.parseInt(req.params(":id"));
            String otsikko = req.queryParams("lanka");
            Lanka lanka = new Lanka(alueid, otsikko);
            lankaapulainen.create(lanka);
            
            res.redirect("/alueet/" + alueid);
            
            return "";
        });

        post("/alueet", (req, res) -> {
            HashMap map = new HashMap<>();
            String otsikko = req.queryParams("alue");
            Alue alue = new Alue(otsikko);

            alueapulainen.create(alue);
            res.redirect("/alueet");
            return "";
        });

        get("/langat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            String id = req.params(":id");
            Lanka lanka = (Lanka) lankaapulainen.getSingle(Integer.parseInt(id));
            List<Viesti> viestit = lankaapulainen.getKaikkiViestit(id);
            double sivut = sivumaara(viestit.size());
            System.out.println(viestit);

            map.put("viestit", viestit);
            map.put("lanka", lanka);

            return new ModelAndView(map, "lanka");
        }, new ThymeleafTemplateEngine());

        post("/langat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int lankaid = Integer.parseInt(req.params(":id"));
            Timestamp time = new Timestamp(System.currentTimeMillis());
            Date aika = new Date(time.getTime());
            String viesti = req.queryParams("viesti");
            String nimimerkki = req.queryParams("nimimerkki");
            Viesti v = new Viesti(lankaid, aika, viesti, nimimerkki);

            viestiapulainen.create(v);
            res.redirect("/langat/" + lankaid);
            return "";
        });
        
        
    }
    
    static int sivumaara(int size){
            
            double i =  size / 10;
             i = Math.round(i);
             
             return (int) i;
        }
    
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null ) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

}
