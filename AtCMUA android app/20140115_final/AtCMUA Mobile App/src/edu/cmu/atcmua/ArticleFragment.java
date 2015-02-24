package edu.cmu.atcmua;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArticleFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_article, container, false);
		TextView content = (TextView) view.findViewById(R.id.article_content);
		
		content.setText(getArguments().getString("blog_guid"));
		System.out.println("fragment start");
		
		return view;
	}
}
