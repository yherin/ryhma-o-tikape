package tikape2.runko;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape2.runko.database.AlueApulainen;
import tikape2.runko.database.Database;
import tikape2.runko.database.LankaApulainen;
import tikape2.runko.database.NoInject;
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

        

        Spark.staticFileLocation("/styles");

        //oliot
        Database database = new Database(dbOsoite);
        AlueApulainen alueapulainen = new AlueApulainen(database);  
        LankaApulainen lankaapulainen = new LankaApulainen(database);
        ViestiApulainen viestiapulainen = new ViestiApulainen(database);

        
        //etusivu redirect
        get("/", (req, res) -> {

            res.redirect("/alueet");
            return "";
        });
        
        //kaikki alueet - eli etusivu
        get("/alueet", (req, res) -> {
            HashMap map = new HashMap<>();

            List<Alue> alueet = alueapulainen.getAlueetViestitAikaLeimat();

            map.put("alueet", alueet);

            System.out.println(alueet);

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());
        
        //luo uusi alue
        post("/alueet", (req, res) -> {
            HashMap map = new HashMap<>();
            String otsikko =  NoInject.cleanHtml(req.queryParams("alue"));
            Alue alue = new Alue(otsikko);

            alueapulainen.create(alue);
            res.redirect("/alueet");
            return "";
        });
        
        //näytä langat yhdessä alueessa
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

        //luo uusi lanka ja sen ensimäinen viesti
        post("/alueet/:id", (req, res) -> {
            int alueid = Integer.parseInt(req.params(":id"));
            String otsikko = NoInject.cleanHtml(req.queryParams("lanka"));
            String nimimerkki =  NoInject.cleanHtml(req.queryParams("nimimerkki"));
            String teksti =  NoInject.cleanHtml(req.queryParams("viesti"));
            
              Timestamp time = new Timestamp(System.currentTimeMillis());

              
            //herokun palvelin on irlannisa.  aikavyöhyke korjaus
            Date aika = new Date(time.getTime() + TimeZone.getTimeZone("Europe/Helsinki").getOffset(time.getTime()));
            
            Viesti viesti = new Viesti(aika, teksti, nimimerkki);
            Lanka lanka = new Lanka(alueid, otsikko, viesti);
            
            
            lankaapulainen.create(lanka);
      
            res.redirect("/alueet/" + alueid);

            return "";
        });
        
        
        //näytä 10 viestia käyttäjä
        get("/langat/:id/:sivu", (req, res) -> {  
            HashMap map = new HashMap<>();
            String id = req.params(":id");
            int sivu = Integer.parseInt(req.params(":sivu"));
            
            Lanka lanka = (Lanka) lankaapulainen.getSingle(Integer.parseInt(id));
            Alue alue = (Alue) alueapulainen.getSingle(lanka.getAlueid());
            List<Viesti> viestit = lankaapulainen.getKaikkiViestit(id, sivu);
            int sivumaara = lankaapulainen.getSivujenMaaraLangassa(id);
            
            System.out.println(viestit);

            SimpleDateFormat dateformat = new SimpleDateFormat();
            
            map.put("viestit", viestit);
            map.put("lanka", lanka);
            map.put("alue", alue);
            map.put("sivu", sivu);
            map.put("sivumaara", sivumaara);

            return new ModelAndView(map, "lanka");
        }, new ThymeleafTemplateEngine());
        
        
        //luo uusi viesti
        post("/langat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int lankaid = Integer.parseInt(req.params(":id"));
            Timestamp time = new Timestamp(System.currentTimeMillis());

            Date aika = new Date(time.getTime() + TimeZone.getTimeZone("Europe/Helsinki").getOffset(time.getTime()));

            String viesti =  NoInject.cleanHtml(req.queryParams("viesti"));
            String nimimerkki =  NoInject.cleanHtml(req.queryParams("nimimerkki"));
            Viesti v = new Viesti(lankaid, aika, viesti, nimimerkki);

            viestiapulainen.create(v);
            res.redirect("/langat/" + lankaid + "/1");
            return "";
        });

    }

    //heroku juttu
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

}
