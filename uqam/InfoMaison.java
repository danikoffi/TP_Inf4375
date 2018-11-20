
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


 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.json.JSONObject;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import org.json.JSONArray;


public class InfoMaison {
    
    private String ac ;
    private String thermo ; 
    private String act  ; 
    private String temp  ; 
    private String doorSens ; 
    private String heater ; 
    private String time ; 
    private String doorLock ; 
    private String lights ;
    private Boolean envoieEmail ;  
    private Integer nbreEmail ; 
    //private MyHandler hsHandl ; 
    
    public InfoMaison (String ac , String thermo , String act , String temp , String doorSens , 
            String heater , String time , String doorLock , String lights   ) {
    
        JSONObject  obj = creerJsonObj ( ac , thermo , act , temp , doorSens ,  heater ,  time ,  doorLock ,  lights ) ;
    
    }
    
    
        public InfoMaison () {
    
            JSONObject  obj = creerJsonObj ( "off" , "0" , "0" , "0" , "off" ,  "off" ,  "0:00:00" ,  "off" ,  "off" ) ;
            
            this.ac = "off" ;
            this.thermo = "0";
            this.setAct("0") ;
            this.setTemp("0")  ;
            this.setDoorSens("off") ;
            this.setHeater("off") ;
            this.setTime("0:00:00") ;
            this.setDoorLock( "off") ;
            this.setLights( "off") ;
            this.envoieEmail = false ;
            this.nbreEmail = 0 ; 
    }

    
    
    public String OnOff (int val) {
        if (val==0) return "off" ; else return "on";
    }
    
    public JSONObject creerJsonObj ( String ac , String thermo , String act , String temp , String doorSens , 
                                     String heater , String time , String doorLock , String lights ) {
         
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        
            this.setAc(ac) ;
            this.setThermo(thermo);
            this.setAct(act) ;
            this.setTemp(temp)  ;
            this.setDoorSens(doorSens) ;
            this.setHeater(heater) ;
            this.setTime(time) ;
            this.setDoorLock( doorLock) ;
            this.setLights( lights) ;
        
        
	JSONObject  obj = new JSONObject();
            obj.put("ac", ac);
            obj.put("thermostat", thermo);
            obj.put("activity", act);
            obj.put("temperature", temp);
            obj.put("door_lock_sensor", doorSens);
            obj.put("heater", doorSens );
            obj.put("time", time);
            obj.put("door_lock", doorSens);
            obj.put("lights", lights);
            obj.put("dateModify", format.format(date));

        return obj ; 
        
    }   

    
    public JSONObject creerJsonObj() {
         
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        
       JSONObject  obj = new JSONObject();
            obj.put("ac", getAc());
            obj.put("thermostat", getThermo() );
            obj.put("activity", getAct() );
            obj.put("temperature", getTemp() );
            obj.put("door_lock_sensor", getDoorSens() );
            obj.put("heater", getHeater() );
            obj.put("time", getTime());
            obj.put("door_lock", getDoorLock() );
            obj.put("lights", getLights() );
            obj.put("dateModify", format.format(date));

            return obj ; 
        
    } 
    
    public JSONObject creerJsonThermoObj() {  
 
        JSONObject  obj = new JSONObject();         
        obj.put("thermostat", getThermo() );
        return obj ; 
        
    } 
    
    public JSONObject creerJsonDoorObj() {
 
        JSONObject  obj = new JSONObject();          
        obj.put("door_lock", getThermo() );            
        return obj ; 
        
    } 
    
	
        public static void readJsonFile()  {

        try {

            Object obj = new FileReader("GetInfo.json");

            JSONObject jsonObject = (JSONObject) obj;
           // System.out.println(jsonObject);

            String ac  = (String) jsonObject.get("ac");
            String thermostat = (String) jsonObject.get("thermostat");
            String activity = (String) jsonObject.get("activity");
            String temperature = (String) jsonObject.get("temperature");
            String doorSens  = (String) jsonObject.get("doorSens");  
            String heater  = (String) jsonObject.get("heater");
            String time  = (String) jsonObject.get("time");          
            String doorLock  = (String) jsonObject.get("doorLock");  
            String lights  = (String) jsonObject.get("lights");            
            DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            Date date = new Date();
            Date dateModify  = convTime((String) jsonObject.get("dateModify"));


        } catch (FileNotFoundException e) {
        }  {
        }  

    }
    
       public static String lireFile(String fileName )  {
        
        String fileContent = ""  ;
        
        try {
            File f = new File (fileName);
            FileReader fr = new FileReader (f);
            BufferedReader br = new BufferedReader (fr);
            try {
                String line = br.readLine();
                while (line != null)
                {
                    fileContent = fileContent + line ; 
                    line = br.readLine();
                }
                br.close();
                fr.close();
                }
            catch (IOException exception)
            {
                System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
            }
        }
        catch (FileNotFoundException exception)
        {
            System.out.println ("Le fichier n'a pas été trouvé");
        }  
        return fileContent ;
    }
        
        
    
        public  String readJsonPutFile(String valCherche , String fileName)  {

        String valRet = "" ; 
        
        JSONObject jsonObject = new JSONObject( lireFile(fileName) );
        valRet  = (String) jsonObject.get(valCherche);  
            return valRet ;
            
        }
        
 
     
      public static Date convTime (String myTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = null  ;
        try {
            date = sdf.parse(myTime);
        } 
        catch (java.text.ParseException e) {
        }
        return date ; 
    }
    
    
    
    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getThermo() {
        return thermo;
    }

    public void setThermo(String thermo) {
        this.thermo = thermo;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDoorSens() {
        return doorSens;
    }

    public void setDoorSens(String doorSens) {
        this.doorSens = doorSens;
    }

    public String getHeater() {
        return heater;
    }

    public void setHeater(String heater) {
        this.heater = heater;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoorLock() {
        return doorLock;
    }

    public void setDoorLock(String doorLock) {
        this.doorLock = doorLock;
    }

    public String getLights() {
        return lights;
    }

    public void setLights(String lights) {
        this.lights = lights;
    }
            
    public boolean getEnvoieEmail() {  // envoieEmail ; nbreEmail
        return this.envoieEmail;
    }

    public void setEnvoieEmail(boolean envEmail) {
        this.envoieEmail = envEmail;
    }
    
        public Integer getnbreEmail() {
        return nbreEmail;
    }

    public void setNbreEmail(Integer nbreEmail) {
        this.nbreEmail = nbreEmail;
    }
    
    
    public void  createJsonFile(){

        try (FileWriter file = new FileWriter("GetInfo.json")) {
                JSONObject  obj = creerJsonObj();
                file.write(obj.toString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
	
    public void  createJsonPutFile( String fileName){
        
        JSONObject  obj = null ; 
        try (FileWriter file = new FileWriter( fileName)) {
            if (fileName.equalsIgnoreCase("putThermo.json") ) obj = creerJsonThermoObj();
            if (fileName.equalsIgnoreCase("putDoor.json") )   obj = creerJsonDoorObj();
                file.write(obj.toString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
 
    @Override
    public String toString() {
    //  ac , thermo , act , temp , doorSens ,  heater ,  time ,  doorLock ,  lights
            return " ac : " + getAc()  +  ", \n thermo : " + getThermo() + ", \n act : "  + getAct() 
                    + ", \n temp : "  + getTemp() + ", \n doorSens : , "  + getDoorSens() + ", \n heater :  "  + getHeater() 
                    + ", \n time : "  + getTime() + ", \n doorLock : : "  + getDoorLock() + ", \n lights  : " +getLights() ; 
    }
    
}   