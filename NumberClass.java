package com.example.ceristsmsrooter;

import android.os.Environment;
import android.telephony.SmsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//This class is used to create an object(name-number) and send it to the phone numbers
public class NumberClass {

    private String name;
    private String category;
    private String phoneNumber;

    public NumberClass(String name, String category, String phoneNumber) {
        this.category = category;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void sendMessage(String message)
    {

        String messageToSend = message;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(this.phoneNumber,null,messageToSend,null,null);
    }

    public static ArrayList<NumberClass> getPhoneNumbers()
    {
        ArrayList<NumberClass> numbersList=new ArrayList<NumberClass>();
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            File numbersFile=new File(Environment.getExternalStorageDirectory()+"/numbers.txt");
            //Getting the phone numbers
            //An alert message has been sent from the web page
            BufferedReader lecteurAvecBuffer = null;
            String line;
            NumberClass temp=null;
            String vect[]=new String[3];
            try {
                lecteurAvecBuffer=new BufferedReader(new java.io.FileReader(numbersFile));
                while ((line = lecteurAvecBuffer.readLine()) != null){
                    vect=line.split(";");
                    temp=new NumberClass(vect[0],vect[1],vect[2]);
                    numbersList.add(temp);
                }
                lecteurAvecBuffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return numbersList;
    }

    public static boolean numbersFileExistance()
    {
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File numbersFile=new File(Environment.getExternalStorageDirectory()+"/numbers.txt");
            if(numbersFile.exists())
                return true;
        }
        return false;
    }

}
