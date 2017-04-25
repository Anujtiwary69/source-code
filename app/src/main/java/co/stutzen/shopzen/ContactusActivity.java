package co.stutzen.shopzen;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.database.DBController;

public class ContactusActivity extends AppCompatActivity {

    private DBController dbCon;
    EditText textname;
    EditText textEmail;
    EditText textMsg;
    private TextView send;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        LinearLayout back = (LinearLayout)findViewById(R.id.backclick);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dbCon=new DBController(this);
        send = (TextView)findViewById(R.id.send);
        textname = (EditText)findViewById(R.id.name);
        textEmail = (EditText)findViewById(R.id.email);
        textMsg = (EditText)findViewById(R.id.message);
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onPause", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume","onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("onRestart", "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onStop","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy","onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("onRestoreInstanceState","onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("onSaveInstanceState","onSaveInstanceState");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void handleClick(){
        if(send.getText().toString().equalsIgnoreCase("submit")) {
            if (textname.getText().toString().trim().length() > 0 && textEmail.getText().toString().trim().length() > 0 && textMsg.getText().toString().trim().length() > 0) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(textEmail.getText().toString()).matches()) {
                    send.setText("Processing...");
                    SendMailTask task = new SendMailTask();
                    task.execute(textname.getText().toString(), textEmail.getText().toString(), textMsg.getText().toString());
                } else {
                    textEmail.setError("Enter valid email.");
                }
            } else {
                textname.setError("Field must be filled.");
                textEmail.setError("Field must be filled.");
                textMsg.setError("Field must be filled.");
            }
        }else{
            Toast.makeText(getApplicationContext(), "Form submitting. Please be patient.", Toast.LENGTH_SHORT).show();
        }
    }

    class SendMailTask extends AsyncTask<String,Void,Boolean> {
        String msg = null;

        protected Boolean doInBackground(String... param) {
            boolean resp = false;
            try {
                String email = AppConstants.receipient;
                MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
                mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
                mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
                mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
                mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
                mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
                CommandMap.setDefaultCommandMap(mc);
                Message message = new MimeMessage(getMailSession());
                Multipart multipart = new MimeMultipart();
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(getHtmlBody(param[0], param[1], param[2]), "text/html");
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);
                message.setFrom(new InternetAddress(AppConstants.fromemail));
                String[] emailarray = email.split(",");
                InternetAddress[] intnt = new InternetAddress[emailarray.length];
                for(int i=0; i < emailarray.length; i++)
                    intnt[i] = new InternetAddress(emailarray[i]);
                message.addRecipients(Message.RecipientType.TO, intnt);
                message.setSubject("Shopzen - Customer support");
                Transport.send(message);
                resp=true;
            }
            catch (MessagingException e) {
                e.printStackTrace();
                msg = e.getMessage();
            }
            catch (Exception e) {
                e.printStackTrace();
                msg = e.getMessage();
            }
            return resp;
        }

        protected void onPostExecute(Boolean feed) {
            send.setText("Submit");
            if(feed == true){
                textname.setText("");
                textEmail.setText("");
                textMsg.setText("");
                Toast.makeText(getApplicationContext(), "Your form submitted successfully.", Toast.LENGTH_LONG).show();
            }else{
                if(msg == null || (msg != null && msg.contains("host")))
                    Toast.makeText(getApplicationContext(), "We unable to submit your feedback. Please check internet connection.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }

    }

    public Session getMailSession(){
        Session session = null;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "25");
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AppConstants.fromemail, AppConstants.password);
            }
        });
        return session;

    }


    public static String getHtmlBody(String name1, String email1, String comments){
        StringBuffer body = new StringBuffer();
        body.append("<body bgcolor=\"#E1E1E1\" leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\">");
        body.append("<center style=\"background-color:#E1E1E1;\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\" style=\"table-layout: fixed;max-width:100% !important;width: 100% !important;min-width: 100% !important;\">");
        body.append("<tr><td align=\"center\" valign=\"top\" id=\"bodyCell\">");
        body.append("<table bgcolor=\"#E1E1E1\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"500\" id=\"emailHeader\">");
        body.append("<tr><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
        body.append("<tr><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"500\" class=\"flexibleContainer\">");
        body.append("<tr><td valign=\"top\" width=\"500\" class=\"flexibleContainerCell\">");
        body.append("<table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
        body.append("<tr><td align=\"left\" valign=\"middle\" id=\"invisibleIntroduction\" class=\"flexibleContainerBox\" style=\"display:none !important; mso-hide:all;\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:100%;\">");
        body.append("<tr><td align=\"left\" class=\"textContent\">");
        body.append("<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:13px;color:#828282;text-align:center;line-height:120%;\">");
        body.append("Request form submitted.");
        body.append("</div></td></tr></table></td>");
        body.append("<td align=\"right\" valign=\"middle\" class=\"flexibleContainerBox\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:100%;\">");
        body.append("<tr><td align=\"left\" class=\"textContent\">");
        body.append("<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:13px;color:#828282;text-align:center;line-height:120%;\">");
        body.append("&nbsp;");
        body.append("</div></td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table>");
        body.append("<table bgcolor=\"#FFFFFF\"  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"500\" id=\"emailBody\">");
        body.append("<tr><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"color:#FFFFFF;\" bgcolor=\"#3e4653\">");
        body.append("<tr><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"500\" class=\"flexibleContainer\">");
        body.append("<tr><td align=\"center\" valign=\"top\" width=\"500\" class=\"flexibleContainerCell\">");
        body.append("<table border=\"0\" cellpadding=\"15\" cellspacing=\"0\" width=\"100%\">");
        body.append("<tr><td align=\"center\" valign=\"top\" class=\"textContent\">");
        body.append("<h1 style=\"color:#FFFFFF;line-height:100%;font-family:Helvetica,Arial,sans-serif;font-size:25px;font-weight:normal;margin-bottom: 0px;text-align: left;\">Shopzen Customer Feedback</h1>");
        body.append("</td></tr></table></td></tr></table></td></tr></table></td></tr>");
        body.append("<tr mc:hideable><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
        body.append("<tr><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"500\" class=\"flexibleContainer\">");
        body.append("<tr><td valign=\"top\" width=\"500\" class=\"flexibleContainerCell\">");
        body.append("<table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
        body.append("<tr><td align=\"left\" colspan=\"2\" valign=\"top\" class=\"flexibleContainerBox\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 100%; margin-bottom: 20px;\">");
        body.append("<tr><td align=\"left\" class=\"textContent\">");
        body.append("<div style=\"text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;\">");
        body.append("Date & Time: "+formateDate(new Date(), "dd-MM-yyyy hh:mm aa"));
        body.append("</div></td></tr></table></td></tr><tr><td align=\"left\" valign=\"top\" class=\"flexibleContainerBox\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"210\" style=\"max-width: 100%;\">");
        body.append("<tr><td align=\"left\" class=\"textContent\">");
        body.append("<h3 style=\"color:#454545;line-height:125%;font-family:Helvetica,Arial,sans-serif;font-size:15px;font-weight:bold;margin-top:0;margin-bottom:10px;text-align:left;\">");
        body.append("Customer Name");
        body.append("</h3><div style=\"text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;\">");
        body.append(name1.replace("\n", "<br />"));
        body.append("</div></td></tr></table></td><td align=\"right\" valign=\"top\" class=\"flexibleContainerBox\">");
        body.append("<table class=\"flexibleContainerBoxNext\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"210\" style=\"max-width: 100%;\">");
        body.append("<tr><td align=\"left\" class=\"textContent\">");
        body.append("<h3 style=\"color:#454545;line-height:125%;font-family:Helvetica,Arial,sans-serif;font-size:15px;font-weight:bold;margin-top:0;margin-bottom:10px;text-align:left;\">Customer Email</h3>");
        body.append("<div style=\"text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;\">");
        body.append(email1);
        body.append("</div></td></tr></table></td></tr><tr>");
        body.append("<tr><td align=\"left\" colspan=\"2\" valign=\"top\" class=\"flexibleContainerBox\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 100%; margin-bottom: 20px;\">");
        body.append("<tr><td align=\"left\" class=\"textContent\">");
        body.append("<div style=\"text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;\">");
        body.append("</div></td></tr></table></td></tr><tr><td align=\"left\" valign=\"top\" class=\"flexibleContainerBox\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"210\" style=\"max-width: 100%;\">");
        body.append("<tr><td align=\"left\" class=\"textContent\">");
        body.append("<h3 style=\"color:#454545;line-height:125%;font-family:Helvetica,Arial,sans-serif;font-size:15px;font-weight:bold;margin-top:0;margin-bottom:10px;text-align:left;\">");
        body.append("Message");
        body.append("</h3><div style=\"text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;\">");
        body.append(comments.replace("\n", "<br />"));
        body.append("</div></td></tr></table></td></tr></table></td></tr><tr>");
        body.append("</table></td></tr></table></td></tr></table></td></tr></table>");
        body.append("<table bgcolor=\"#E1E1E1\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"500\" id=\"emailFooter\">");
        body.append("<tr><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
        body.append("<tr><td align=\"center\" valign=\"top\">");
        body.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"500\" class=\"flexibleContainer\">");
        body.append("<tr><td align=\"center\" valign=\"top\" width=\"500\" class=\"flexibleContainerCell\">");
        body.append("<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">");
        body.append("<tr><td valign=\"top\" bgcolor=\"#E1E1E1\">");
        body.append("<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:13px;color:#828282;text-align:center;line-height:120%;\">");
        body.append("<div></div>");
        body.append("</div></td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></center></body>");
        return body.toString();
    }

    public static String formateDate(Date date,String pattern) {
        String d=null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            d =	formatter.format(date);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return d;
    }
}

