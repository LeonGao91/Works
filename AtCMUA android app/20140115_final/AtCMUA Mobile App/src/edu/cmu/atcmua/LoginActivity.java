package edu.cmu.atcmua;


import edu.cmu.atcmua.toolkit.HttpTools;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText username;
	private EditText password;
	private String token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atcmua_login);
		
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//			StrictMode.setThreadPolicy(policy);
//		}
		
		username = (EditText) findViewById(R.id.input_username);
		password = (EditText) findViewById(R.id.input_password);

		username.setText("yomin");
		password.setText("8628416583");

		final Button loginButton = (Button) findViewById(R.id.button_login);
		final Intent mainActivityIntent = new Intent(this, MainActivity.class);
		//final Activity currentActivity = this;

		loginButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

//				Intent tokenServiceIntent = new Intent(currentActivity,HttpIntentService.class);
//				tokenServiceIntent.setData(Uri.parse(passwordString));
//				currentActivity.startService(tokenServiceIntent);
				
				Thread getTokenThread = new Thread(new Runnable(){
					public void run(){
						try{
							String usernameString = username.getText().toString();
							String passwordString = password.getText().toString();
							token = HttpTools.getToken(usernameString, passwordString);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
				getTokenThread.start();
				try {
					getTokenThread.join();
					if(token.equals("invalid")){
						Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
						password.setText("");
					} else{
						startActivity(mainActivityIntent);
						System.out.println("[LOGIN ACTIVITY] The Token is: " + token);
					}
					LoadActivity.saveToken(token);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
