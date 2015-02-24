package edu.cmu.atcmua;

import edu.cmu.atcmua.toolkit.HttpTools;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment {
	private String token;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		
		token = LoadActivity.loadToken();
		System.out.println("token in info: "+token);
		
		Thread getInfoThread= new Thread(new Runnable(){
			public void run(){
				try{
					//String s = URLEncoder.encode("printer", "UTF-8");
					String s="printer";
					String info=HttpTools.getInfo(token,s);
					System.out.println("INFO:"+info);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
			
		});
		

		getInfoThread.start();

		
		return view;
	}
}
