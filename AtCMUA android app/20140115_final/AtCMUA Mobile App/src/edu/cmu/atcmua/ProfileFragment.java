package edu.cmu.atcmua;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View currentView = inflater.inflate(R.layout.atcmua_profile, container, false);
		
		return currentView;
		
	}
}
