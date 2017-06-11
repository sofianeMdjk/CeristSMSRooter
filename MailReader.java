package com.example.ceristsmsrooter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

/**
 * Created by Sofiane on 06/08/2016.
 */
public class MailReader extends AsyncTask {


    private String username;
    private String password;
    private String host;
    private String incomingEmail;
    private String subject;
    private Store store=null;
    private String messagesList[]=null;
    private Folder emailFolder=null;
    private MimeMessage mimeMessage;
    private MimeMultipart mimeMultipart;
    Message messages[] = null;
    int count=0;
    boolean messagesAvailable=false;
    private static boolean error=false;
    private Context context;

    public MailReader(String u,String p,String h,String im,String su,Context context)
    {
        this.host=h.replaceAll("\\s","");
        this.password=p.replaceAll("\\s","");
        this.username=u.replaceAll("\\s","");
        this.incomingEmail=im.replaceAll("\\s","");
        this.subject=su.replaceAll("\\s","");
        this.context=context;
    }
    @Override
    protected void onPreExecute() {

        //secured connexion properties

        Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.starttls.enable", "true");
        properties.put("mail.imaps.auth", "true");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.socketFactory.port", "993");
        properties.put("mail.imaps.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.imaps.socketFactory.fallback", "false");
        properties.setProperty("mail.transport.protocol", "imap");
        properties.setProperty("mail.imaps.quitwait", "false");
        Session emailSession = Session.getInstance(properties,null);
        try {
            store = emailSession.getStore("imaps");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        //this is the very connexion

        AsyncTask<Void, Void, Void> execute = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    //this is the connexion part
                    Log.d("connexion", "before connecting");
                    System.out.println(username+" "+password+" "+host);
                    store.connect(host, 993, username, password);

                    Log.d("connexion", "before connecting2");
                    // retrieve the messages from the folder
                    emailFolder = store.getFolder("INBOX");

                    emailFolder.open(Folder.READ_WRITE);

                    Log.d("connexion", "established");
                    //we only get the unread messages and each time we read a message we make it "read"
                    Flags seen = new Flags(Flags.Flag.SEEN);
                    FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
                    try {
                        messages = emailFolder.search(unseenFlagTerm);
                        messagesList=new String[messages.length];
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        System.out.println("problem");
                    }
                    if (messages.length == 0) {
                        try {
                            emailFolder.close(false);
                            store.close();
                            System.out.println("No messages found.");

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        messagesAvailable = true;
                        //there's unread messages
                        System.out.println("got here");
                        System.out.println("messages.length---" + messages.length);
                        //there's supposed to be only one message on the mail box
                    }
                } catch (MessagingException e) {
                    System.out.println("Connexion Error");
                    error=true;
                }
                return null;
            }
        };
        execute.execute();

    }

    @Override
    protected Object doInBackground(Object[] params) {
        if(!error) {
            if (messagesAvailable) {
                try {
                    //We put the content of the message in a MimeMessage(it's faster when it comes to read the content of the mail)
                    System.out.println("Before getting the actual message");
                    for (int j = 0; j < messages.length; j++) {
                        mimeMessage = new MimeMessage((MimeMessage) messages[j]);
                        mimeMessage.getContent();
                        mimeMultipart = (MimeMultipart) mimeMessage.getContent();
                        count = mimeMultipart.getCount();
                        for (int i = 0; i < count; i++) {
                            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                            if (bodyPart.isMimeType("text/plain")) {
                                messagesList[j] = new String((String) bodyPart.getContent());
                            }
                        }
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        error=true;
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        //this is the part where we send the text mail to the phone numbers
        //we'll get via the phone_numbers_file
        if (!error) {
            if (messagesAvailable) {
                for (int i = 0; i < messages.length; i++) {
                    try {
                        if (messages[i].getSubject().toString() != null) {
                            String sender =getMailAdress(InternetAddress.toString(messages[i].getFrom()));
                            if ( sender.equals(this.incomingEmail) &&
                                    messages[i].getSubject().toString().equals(this.subject) ) {
                                System.out.println("Sending SMS...");
                                ArrayList<NumberClass> numbersList = NumberClass.getPhoneNumbers();
                                SmsManager sms = SmsManager.getDefault();
                                for (int k = 0; k < numbersList.size(); k++) {
                                    System.out.print(numbersList.size());
                                    numbersList.get(k).sendMessage(messagesList[i]);
                                    System.out.println("Message sent");
                                }
                            } else
                                System.out.println("Wrong subject or wrong sender");
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private String getMailAdress(String mail)
    {
        String result="";
        Pattern pattern =Pattern.compile("(.*)<(.*)>");
        Matcher matcher = pattern.matcher(mail);
        if(matcher.matches())
            result = matcher.replaceAll("$2");
        return result;
    }


}
