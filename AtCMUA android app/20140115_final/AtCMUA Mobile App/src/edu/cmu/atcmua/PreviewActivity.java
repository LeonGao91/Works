package edu.cmu.atcmua;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PreviewActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atcmua_preview);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (findViewById(R.id.fragment_preview_container) != null) {
			if ( savedInstanceState != null) {
				return;
			}
			
			ArticleFragment article = new ArticleFragment();
			article.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_preview_container, article).commit();
		}
		
		
	}
}
