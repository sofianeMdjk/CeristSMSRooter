package com.example.ceristsmsrooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	//Initialization the buttons of the main activity
	Button emailsButton=null;
	Button physicalConnexionButton=null;
	Button phoneButton=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		emailsButton=(Button)this.findViewById(R.id.emailButton);
		physicalConnexionButton=(Button)this.findViewById(R.id.filesButton);
		phoneButton=(Button)this.findViewById(R.id.phoneButton);
		//Making the buttons listen to the touch
		physicalConnexionButton.setOnClickListener(this);
		phoneButton.setOnClickListener(this);
		emailsButton.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		switch (item.getItemId())
		{
			case R.id.action_settings:
			{
				Toast.makeText(this,"blabla",Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	//the onClick method make the buttons do actions when we click (interraction system)
	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.emailButton:
			{
				//Email fetching
				if(!isOnline())
				{
					Toast.makeText(this,"No internet connexion make sure you are connected",Toast.LENGTH_LONG).show();
				}
				else {
					if(!NumberClass.numbersFileExistance())
						Toast.makeText(this,"No phone numbers yet make sure put the phone numbers File",Toast.LENGTH_LONG).show();
					else
					{
						Intent intent = new Intent(this, FormActivity.class);
						this.startActivity(intent);
					}
				}

			}break;
			case R.id.filesButton:
			{
				//Physical Connexion
				Intent intent=new Intent(this,FileFetching.class);
				this.startActivity(intent);

			}break;
			case R.id.phoneButton:
			{
				if(NumberClass.numbersFileExistance()) {
					Intent intent = new Intent(this, ShowPhoneNumbers.class);
					this.startActivity(intent);
				}
				else
					Toast.makeText(this,"Numbers File missing make sure you load it",Toast.LENGTH_LONG).show();
			}break;
		}

	}

	/*this method let us take the phone numbers from the file when needed
	 */


	public boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
