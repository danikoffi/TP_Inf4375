
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.zeromq.ZMQ;
import java.util.Random;
import org.zeromq.SocketType;
import org.zeromq.ZContext;


/*  Heater: off
AC: off
Lights: off
Door lock actuator: off  */

public class SmartHouse {
	
    private static String tabActuateur[][] = {{"heater", "ac", "lights" , "door_lock"},  {" ", " ", " " , " "}};
    private float Thermostat = 0 ; 
    private static String  actionMaison = "" , heurMaison = "" , tempMaison = ""  ; 
    
    
    public float getThermo( ) {
        return this.Thermostat ; 
    }
    
    public void setThermo(float thermo ) {
         this.Thermostat =  thermo; 
    }
        public static Date convTime (String myTime) {

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            Date date = null  ;
            try {
                date = sdf.parse(myTime);
            } 
            catch (ParseException e) {
            }
            return date ;     
        }
        
        public static void setLight(ZMQ.Socket publisher , String acti , InfoMaison SmHouse ) throws ParseException{
            String action = "+" ; 
            int activConv = Integer.parseInt(acti) ; 

            if ( activConv == 0 )  
            { 
                action = "off" ;  
                if (!tabActuateur[1][2].equalsIgnoreCase(action)) {
                    SmHouse.setAct("0") ;
                    SmHouse.setLights(action);
                    System.out.println("Absence d'activité") ; 	
		} 
            } 
            
            if ( activConv == 1 )  
            { 
                action = "on" ;  
                if (!tabActuateur[1][2].equalsIgnoreCase(action)) {
                    SmHouse.setAct("1");
                    SmHouse.setLights(action);
                    System.out.println("Présence d'activité") ; 
		} 
            }   

            if (! tabActuateur[1][2].equalsIgnoreCase(action)) 
            {
                tabActuateur[1][2] = action ; 
                publissHouseStatus(publisher , tabActuateur[0][2] , tabActuateur[1][2] , SmHouse) ; 
                if (activConv == 0 ) System.out.println("Éteindre les lumières  ") ;  
                else System.out.println("Allumer les lumières ") ; 
            }      
        }

        public static void setTime(ZMQ.Socket publisher , String heur , InfoMaison SmHouse , String doorLk) throws ParseException{ 
            
            String action = doorLk ; 

            /*    
            String action = " " ; 
            Date heurConv = convTime(heur) ; 
            Date heurDeverou = convTime("23:00:00") , heurVerou = convTime("7:00:00") ; 
            SmHouse.setTime(heur);  */
            
            if (heurMaison.length() == 0 ){  
                    System.out.println("L'heure est fixée est à  :  "  +  heur );
                    heurMaison = heur  ;
                    SmHouse.setTime(heur);
            }
            else {
                if (!heur.equalsIgnoreCase(heurMaison)) {
                    System.out.println("L'heure est fixé est :  "  +  heur );
                    heurMaison = heur  ;
                    SmHouse.setTime(heur);
                }
             }
            
            //  if (heurConv.after(heurDeverou) || heurConv.before(heurVerou)) action = "on" ; 
            // else  action = "off" ; 
            if (! tabActuateur[1][3].equalsIgnoreCase(action)) 
                {
                    tabActuateur[1][3] = action ; 
                    publissHouseStatus( publisher , tabActuateur[0][3] , tabActuateur[1][3] , SmHouse) ;
                    //System.out.println(tabActuateur[0][3] + " - " + tabActuateur[1][3]);
                    if (action.equalsIgnoreCase("off") )  {
                        System.out.println("Portes déverouillées ") ;
                        SmHouse.setDoorLock(action);
                    }  
                    else {
                        System.out.println( "Les portes sont verouillées " ) ; 
                        SmHouse.setDoorLock(action);
                    }
                }   
            
        }
        
        public static void setDoorLock (ZMQ.Socket publisher , String doorStatus , InfoMaison SmHouse ,  String doorLk  ) 
                throws ParseException {
            String action = doorLk; 
            SmHouse.setDoorLock(doorLk);
           
         //   if (doorStatus.equalsIgnoreCase("on"))  action =  "on" ;  
         //   if (doorStatus.equalsIgnoreCase("off")) action =  "off" ; 
        //   System.out.print(" doorLk " + doorLk + " doorStatus  " +  doorStatus);
             if (! actionMaison.equalsIgnoreCase(action)) 
                {
                    tabActuateur[1][3] = action ; 
                    publissHouseStatus( publisher , tabActuateur[0][3] , tabActuateur[1][3] , SmHouse ) ;
                    SmHouse.setDoorSens(action) ; 
                    if (action.equalsIgnoreCase("off") ) System.out.println("Portes déverouillées ") ; 
                    else System.out.println( "Portes verouillées " ) ; 
                }   
        
        }
        
        
        public static void  setTemperature(ZMQ.Socket publisher , String temp , InfoMaison SmHouse , Double valTermo ) 
                throws ParseException, IOException  {
            String action = "" , valOn= "on" , valOff = "off" ; 
            double tempConv = Double.parseDouble(temp) ; 
            SmHouse.setThermo(valTermo.toString());
            
            if (tempMaison.length() == 0 ){  
                    System.out.println("La température est fixée est à  :  "  +  temp + " et le termostat est a " + valTermo );
                    tempMaison = temp  ;
                    SmHouse.setTemp(temp) ;
            }
            else {
                if (!temp.equalsIgnoreCase(tempMaison)) {
                    System.out.println("La température  est fixée est :  "  +  temp + " et le termostat est a " + valTermo  );
                    tempMaison = temp  ;
                    SmHouse.setTemp(temp) ;
                   // SmHouse.setThermo(valTermo.toString());
                }
            }
            
            // envoyer un email si la température est supérieur a 30 
            if (tempConv > 30.0 )  {
                if( SmHouse.getEnvoieEmail() == false) {
                    String fromAdr = "danikoffi@free.fr" ; 
                    String toAdr = "affianke.daniel_koffi@courrier.uqam.ca " ; //  inf4375.2018@gmail.com
                    String objEml = "Smart House alert - AFFD20098501 "  ; 
                    String msgEml = "Bonjour !! \n "
                            + "Veuillez noté que la température de la maison est au dessus de 30\n "
                            + "Actuelement la température est de : " + temp + " et la valeur du termostat est de :" +  valTermo;
                    SendMailTLS envEM = new SendMailTLS( fromAdr ,  toAdr ,  objEml ,  msgEml);
                   // envEM.SendGridMail();
                    SmHouse.setEnvoieEmail(true)  ; 
                    envEM.SendGridStatMail();
                }  

            }
            else {
                SmHouse.setEnvoieEmail(false)  ;
            }
            
            if (tempConv < valTermo - 2 ) // démarrer le chauffage 
            {
                action =  valOn ; 
                // System.out.println(" inf 2  " + (tempConv - valTermo) + "  -  " + action ) ; 
                if (! tabActuateur[1][0].equalsIgnoreCase(action)) 
                {
                    tabActuateur[1][0] = action ; 
                    tabActuateur[1][1] = valOff ; 
                    SmHouse.setThermo(temp);  // action
                    SmHouse.setAc(valOff);
                    publissHouseStatus( publisher , tabActuateur[0][0] , tabActuateur[1][0] , SmHouse) ;
                    publissHouseStatus( publisher , tabActuateur[0][1] , tabActuateur[1][1] , SmHouse) ;
                    System.out.println("Chauffage allumé \nClimatisation arrèté") ; 
                } 
                
            } 
            
            else {
            
            if ( tempConv > valTermo + 2 )  // démarré la cliamtisation et le chauffage 
            {    
                action = valOn ;  
                //  System.out.println(" sup 2  " + (tempConv - valTermo) + "  -  " + action ) ; 
                if (! tabActuateur[1][1].equalsIgnoreCase(action)) 
                {
                    if (! tabActuateur[1][0].equalsIgnoreCase(valOff)) System.out.println( "Chauffage arrèté " ) ;
                        if (! tabActuateur[1][1].equalsIgnoreCase(valOn)) System.out.println( "Climatisation démarée  " ) ;                               
                        SmHouse.setThermo(temp);  //  valOff
                        SmHouse.setAc(valOn);
                        tabActuateur[1][0] = valOff ;
                        tabActuateur[1][1] = valOn ;                                 
                        publissHouseStatus( publisher , tabActuateur[0][0] , tabActuateur[1][0] , SmHouse) ;   
                        publissHouseStatus( publisher , tabActuateur[0][1] , tabActuateur[1][1] , SmHouse) ;
                }     
            }
            
            else { 
                
            if(Math.abs(tempConv - valTermo) <= 2) {
               
                action = valOff ;  
                //  System.out.println(" entre 2  " + (tempConv - valTermo) + "  -  " + action ) ; 
                if (! tabActuateur[1][0].equalsIgnoreCase(action)) {
                    tabActuateur[1][0] = action ; 
                    System.out.println( "Chauffage arrèté " ) ;
                    publissHouseStatus( publisher , tabActuateur[0][0] , tabActuateur[1][0] , SmHouse) ;      
                }
                
                if (! tabActuateur[1][1].equalsIgnoreCase(action)) {
                    tabActuateur[1][1] = action ; 
                    System.out.println( "Climatisation arrèté " ) ;
                    publissHouseStatus( publisher , tabActuateur[0][1] , tabActuateur[1][1] , SmHouse) ;
                }
                 
            }
             }
            }
        }
    
        
        public static void publissHouseStatus( ZMQ.Socket publisher , String topic ,  String message , InfoMaison SmHouse ) 
                 {

            publisher.send(topic,ZMQ.SNDMORE)  ;
            publisher.send(message)  ; 
            SmHouse.createJsonFile() ;
        }  
        
	public static void main (String [] args) throws InterruptedException, ParseException, Exception  {
		
            ZMQ.Socket publisher;
            ZMQ.Socket subscriber;            
            InfoMaison SmHouse = new InfoMaison() ; 
            Demo objHandl = new Demo() ; 
            String doorLk = "off"  ; 
            Double valTermo = (Double)2.4 ; 
            SmHouse.createJsonFile();
           // System.out.println( SmHouse.toString()) ; 
            
            try (ZContext context = new ZContext()) {
		     
                subscriber = context.createSocket(SocketType.SUB);
                publisher = context.createSocket(SocketType.PUB);               
                
                // se connecter sur la maison intelligente 
                Thread.sleep(1000);
                subscriber.connect("tcp://localhost:5555");
                publisher.bind("tcp://*:6666");           
 
                Random rand = new Random(System.currentTimeMillis());
                String subscription = String.format("%03d", rand.nextInt(1000));
                subscriber.subscribe(subscription.getBytes(ZMQ.CHARSET));
                 
                subscriber.subscribe("temperature");
                subscriber.subscribe("activity");
                subscriber.subscribe("time");
                subscriber.subscribe("dorr_lok_sensor");

                while (true) 
                {
                    String topic = subscriber.recvStr();
                    String data = subscriber.recvStr();
                    doorLk = SmHouse.readJsonPutFile("door_lock" , "putDoor.json" ) ; 
                    String tempTermo = SmHouse.readJsonPutFile("thermostat" , "putThermo.json" ) ; 
                    if (tempTermo.length() > 0) valTermo = Double.parseDouble(tempTermo ) ; else valTermo = (double)0 ; 
                    SmHouse.setThermo(valTermo.toString());
                    SmHouse.setDoorLock(doorLk);
                    objHandl.StartHandler();
               //      System.out.println("La valeur du thermostat est :  " +  valTermo + " verro porte " + doorLk );
                    if ( topic.equalsIgnoreCase("temperature" ) )         setTemperature(publisher , data , SmHouse , valTermo ) ; 
                    if ( topic.equalsIgnoreCase("activity" ) )            setLight( publisher , data , SmHouse ) ;    // ok 
                    if ( topic.equalsIgnoreCase("time" ) )              {   if (data.length() > 5)  setTime(publisher , data , SmHouse , doorLk ) ; }   // ok 
                    if ( topic.equalsIgnoreCase("door_lock_sensor" ) )    setDoorLock(publisher , data , SmHouse  , doorLk ) ;                   
                    if (topic == null)  break;
                }
		System.out.println(" fin ") ; 
    
        }

	}
}

/* 
temperature - 21.5
activity - 0
time - 15:45:34
door_lock_sensor - off
*/     