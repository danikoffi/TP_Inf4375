
/*

Cours   : Inf4375
Session : Automne 2018
Étape 1 : Communication avec des capteurs et actuateurs
Étape 2 : API WEB pour contrôler certains aspectsde l’application. 
Autheur : 
    AFFIANKÉ Koffi Daniel    	(AFFD20098501)
    FALL Abdoulaye              (FALA08077608)
Date    : 9  Novembre 2018

*/

package ca.uqam;

/**
 *
 * @author danikoffi
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
//import org.json.simple.JSONObject;




public class Demo {
    
    //  
    public static void StartHandler() throws Exception {
        try {
            /**
             * On utilise le port 8000 en localhost.
             * 0 par défaut pour le socket backlog, nul besoin de le changer dans le cadre du TP.
             */
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            /** On définit un endpoint "/test" ainsi qu'un gestionnaire qui va effectuer le traitement associé. */
            server.createContext("/thermostat", new Demo.MyHandler("thermostat" , "thermostat"));
            server.createContext("/temperature", new Demo.MyHandler("temperature" , "temperature"));
            server.createContext("/door_lock", new Demo.MyHandler("door_lock" , "door_lock"));
            server.createContext("/presence", new Demo.MyHandler("presence" , "activity"));
        
            /** 
             * On utilise un executeur qui prend en charge un ensemble de threads fixe, pour le besoin 15 sera suffisant,
             * si votre machine possède moins de ressource, vous pouvez descendre jusqu'à 5. 
             */
            server.setExecutor(Executors.newFixedThreadPool(15));
            /** On démarre le serveur. */
            server.start();
        } catch (Exception ex) {
            /** Traitement d'erreur... */
        }
    }

    /**
     * Gestionnaire de endpoint.
     */
    static class MyHandler implements HttpHandler {
        
        private String  endpoint  ; 
        private String message ; 
        
        MyHandler (String endp , String msg )  {
            endpoint =  endp ; 
            message = msg ; 
        }
        
        public void setEndpoint(String topic) {
              this.endpoint = topic ; 
        }
        
        public void setMessage(String message) {
              this.message = message ; 
        }
        
         public String getEndpoint() {
             return this.endpoint  ; 
        }
        
        public String getMessage() {
             return  this.message  ; 
        }
        
        @Override
        public void handle(HttpExchange he  ) throws IOException {
            /** Récupère le type de méthode de la requête : GET, PUT, ... */
            String requestMethod = he.getRequestMethod();
            String response = "" ;
            /** Traitement dépendant de la méthode de la requête, agit comme un controleur. */
            if (requestMethod.equalsIgnoreCase("GET")) {
                try {
                        response = readJsonFile( this.getMessage() ); 
                   
                } catch (Exception ex) {
                    Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
                }
                addResponseHeader(he);
                he.sendResponseHeaders(200, response.length());
                try (OutputStream os = he.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } 
            
            
            if (requestMethod.equalsIgnoreCase("PUT")) {
                try {
                        
                    String valPut = he.getRequestURI().toString().substring(he.getRequestURI().toString().indexOf("?=") + 2, he.getRequestURI().toString().length());
                    if (this.getMessage().equalsIgnoreCase("thermostat") )  {
                        createJsonPutFile ("putThermo.json" , valPut ) ; 
                        response = "La valeur du thermostat a été modifié avec succès " ; 
                    } 
                    if (this.getMessage().equalsIgnoreCase("door_lock")  ) {
                        createJsonPutFile ("putDoor.json" , valPut )  ;
                        response = "La valeur du door lock a été modifié avec succès " ; 
                    }

                } catch (Exception ex) {
                    Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
                }
                addResponseHeader(he);
                he.sendResponseHeaders(200, response.length());
                try (OutputStream os = he.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } 

            
        }
    }
    
      public static JSONObject creerJsonThermoObj(String valObj) {  
 
        JSONObject  obj = new JSONObject();        
        obj.put("thermostat",  valObj );
        return obj ; 
        
    } 
    
    public static JSONObject creerJsonDoorObj(String valObj) {
 
        JSONObject  obj = new JSONObject();          
        obj.put("door_lock",  valObj );            
        return obj ; 
        
    } 


    public static void  createJsonPutFile( String fileName , String jsonObj){
        
        JSONObject  obj = null ; 
        try (FileWriter file = new FileWriter( fileName)) {
            if (fileName.equalsIgnoreCase("putThermo.json") ) obj = creerJsonThermoObj(jsonObj);
            if (fileName.equalsIgnoreCase("putDoor.json") )   obj = creerJsonDoorObj(jsonObj);
                file.write(obj.toString());
                file.flush();
            } catch (IOException e) {
        }
    }
    
    
     private static void addResponseHeader(HttpExchange httpExchange) {
        List<String> contentTypeValue = new ArrayList<>();
        contentTypeValue.add("application/json");
        httpExchange.getResponseHeaders().put("Content-Type", contentTypeValue);
    }
     
     // lire le fichier Json 
     
        public static String  readJsonFile( String valCherche ) throws ParseException {

            Object parser = new Object();
            String valRet = "" ; 
            try {
                
                Object obj = new FileReader("GetInfo.json");
                JSONObject jsonObject = (JSONObject) obj;
                valRet  = (String) jsonObject.get(valCherche);//valCherche
                 
            } catch (FileNotFoundException e) {
            }  {
            }  
             return valRet ; 
        }

 
         static class ThreadManager {
 
            Thread subThread;
 
            public ThreadManager(Runnable subRunnable) {
                this.subThread = new Thread(subRunnable);
            }
         }

         
}
