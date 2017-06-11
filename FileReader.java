package com.example.ceristsmsrooter;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/*The role of this class is to get the data from the numbers and message file IF IT EXISTS
and send it as an SMS to the phone numbers available in the numbers file
 */

public class FileReader  extends AsyncTask {
    //this is the paths where we'll find the numbers and message file(in the phone storage)
    private String messageFilePath= Environment.getExternalStorageDirectory()+"/alertMessage.txt";
    private boolean messageAvailable=false;
    private String alertMessage;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        //we test if we have Acces to the external storage
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File messageFile=new File(messageFilePath);
            if(messageFile.exists())
            {
                //reading the Alert message
                System.out.println("Alert message Arrived !");
                BufferedReader reading = null;
                try
                {
                    reading =new BufferedReader(new java.io.FileReader(messageFile));
                    String line ;
                    while((line=reading.readLine())!=null)
                    {
                        alertMessage=line;
                    }

                    System.out.println("Message Read");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                messageAvailable=true;
                //we delete the file no need to keep it on the phone it takes memory
                //the alert messages should be saved in the dataBase
                messageFile.delete();
            }
            else
            {
                System.out.println("No message yet !");
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        //Sending the messages via SMS
        if(messageAvailable) {
            ArrayList<NumberClass> numbersList=NumberClass.getPhoneNumbers();
            System.out.println("sending message");
            for (int i = 0; i <numbersList.size(); i++) {
                numbersList.get(i).sendMessage(alertMessage);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
