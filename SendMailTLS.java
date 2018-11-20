/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uqam;

/**
 *
 * @author danikoffi
 */



import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;



public class SendMailTLS {
    
        public String fromAdress ; 
        public String toAdress ; 
        public String objectEmail ; 
        public String msgEmail ; 

    public String getFromAdress() {
        return fromAdress;
    }

    public void setFromAdress(String fromAdres) {
        this.fromAdress = fromAdres;
    }

    public String getToAdress()  {
        return toAdress;
    }

    public void setToAdress(String toAdress) {
        this.toAdress = toAdress;
    }

    public String getObjectEmail() {
        return objectEmail;
    }

    public void setObjectEmail(String objectEmail) {
        this.objectEmail = objectEmail;
    }

    public String getMsgEmail() {
        return msgEmail;
    }

    public void setMsgEmail(String msgEmail) {
        this.msgEmail = msgEmail;
    }
        
    SendMailTLS () {
        setFromAdress("danikoffi@free.fr"); 
        setToAdress("affianke.daniel_koffi@courrier.uqam.ca");
        setObjectEmail("alert email") ;
    }    
    
    SendMailTLS ( String fromAdr , String toAdr , String objEml , String msgEml ) {
        setFromAdress(fromAdr); 
        setToAdress(toAdr);
        setObjectEmail(objEml) ;
        setMsgEmail(msgEml) ; 
    } 
    
    
        

        
    public  void SendGridMail() throws IOException {
            Email from = new Email(this.getFromAdress());
            Email to = new Email(this.getToAdress());  
            Content content = new Content("text/plain", this.getMsgEmail());
            Mail mail = new Mail(from, this.getObjectEmail(), to, content);

            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            
            try {
                    request.setMethod(Method.POST);
                    request.setEndpoint("mail/send");
                    request.setBody(mail.build());
                    Response response = sg.api(request);
                    System.out.println(response.getStatusCode());
                    System.out.println(response.getBody());
                            System.out.println(response.getHeaders());
            } catch (IOException ex) {
              throw ex;
            }
    }
    
        public  void SendGridStatMail() throws IOException {
            
            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            
            String NbreEmail = "0" , msg = "" ;  
            Integer req = 0 , deliv = 0 ; 
            try {
                    request.setMethod(Method.GET);
                    request.setEndpoint("stats");  
                    request.addQueryParam("start_date", "2018-11-01");
                    request.addQueryParam("aggregated_by", "month");
                    Response response = sg.api(request);
                    msg = response.getBody() ; 
                    req =  msg.indexOf("requests") + 10 ; 
                    deliv = msg.indexOf("spam_report_drops") -2 ;        
                    //  System.out.println("Le nombre d'email envoyé est : " + msg.substring(req, deliv )) ;
                    
                    int emlReq , emlDelvr ;                    
                    JSONObject jsonObject = new JSONObject ((new JSONObject (( new JSONArray( (new JSONObject ( (String)((JSONArray) new JSONArray( msg )).get(0).toString())).get("stats").toString() )  ) .get(0).toString())).get("metrics" ).toString()) ;
                    emlReq =  (int )jsonObject.get("requests" ) ; 
                    emlDelvr =  (int )jsonObject.get("processed" ) ;
                    System.out.println("Emails Requested " + emlReq + " emails Delivered " + emlDelvr );

            } catch (IOException ex) {
              throw ex;
            }
    }

}


