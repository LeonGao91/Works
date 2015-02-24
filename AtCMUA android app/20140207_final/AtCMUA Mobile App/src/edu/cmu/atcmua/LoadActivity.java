package edu.cmu.atcmua;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class LoadActivity extends Activity {
	
	private static SharedPreferences localStorage;
	private final static String tokenKey = "TOKEN";
	private final static String feedsKey = "FEEDS";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Intent loginActivityIntent = new Intent(this, LoginActivity.class);
		final Intent mainActivityIntent = new Intent(this, MainActivity.class);
		
		localStorage = this.getSharedPreferences("LOCAL_STORAGE", Context.MODE_MULTI_PROCESS);
		
		String item = loadToken();
		if (item.equals("noToken")) {
			startActivity(loginActivityIntent);
		} else {
			startActivity(mainActivityIntent);
		}
	}
	
	public static String loadToken() {
		
		return localStorage.getString(tokenKey, "noToken");
	}
	
	public static void saveToken(String token) {
		SharedPreferences.Editor editor = localStorage.edit();
		editor.putString(tokenKey, token);
		editor.commit();
	}
	
	public static void deleteToken() {
		SharedPreferences.Editor editor = localStorage.edit();
		editor.remove(tokenKey);
		editor.commit();
	}
	
	public static void saveFeeds(String feeds) {
		SharedPreferences.Editor editor = localStorage.edit();
		editor.putString(feedsKey, feeds);
		editor.commit();
	}
	public static void userLogout(Activity a) {
		final Intent loginActivityIntent = new Intent(a, LoginActivity.class);
		
		LoadActivity.deleteToken();
		a.startActivity(loginActivityIntent);
		Toast.makeText(a.getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
	}

}
