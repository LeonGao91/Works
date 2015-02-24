package edu.cmu.atcmua;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		
		TextView content = (TextView) view.findViewById(R.id.profile_content);
		content.setText(getArguments().getString("username"));
		
		return view;
	}
}
