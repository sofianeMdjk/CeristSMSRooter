package com.example.ceristsmsrooter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ShowPhoneNumbers extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_phone_numbers);

        String [] strings=this.convertNumberToString();
        ArrayList<NumberClass> phoneNumbers=NumberClass.getPhoneNumbers();
        ArrayAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strings);

        ListView listView=(ListView)this.findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_phone_numbers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        System.exit(1);
        return super.onOptionsItemSelected(item);
    }

    public String[] convertNumberToString()
    {

        ArrayList<NumberClass> phoneNumbers=NumberClass.getPhoneNumbers();
        String []numbers = new String[phoneNumbers.size()];
        for(int i=0;i<phoneNumbers.size();i++)
        {
            numbers[i]=phoneNumbers.get(i).getName()+"\n"+phoneNumbers.get(i).getPhoneNumber();
        }
        return numbers;
    }
}
