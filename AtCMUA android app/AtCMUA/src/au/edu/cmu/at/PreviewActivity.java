package au.edu.cmu.at;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

public class PreviewActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atcmua_preview);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle args = getIntent().getExtras();
		String type = args.get("type").toString();

		if (findViewById(R.id.fragment_preview_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			
			
			if (type.equals("article")) {
				BlogDetailFragment article = new BlogDetailFragment();
				article.setArguments(getIntent().getExtras());
				Log.d("TYPE: ",  ":"+ type);
				getSupportFragmentManager().beginTransaction().add(R.id.fragment_preview_container, article).commit();
			}
			/**
			 * if (type.equals("profile")) { ProfileFragment profile = new
			 * ProfileFragment(); profile.setArguments(getIntent().getExtras());
			 * getSupportFragmentManager
			 * ().beginTransaction().add(R.id.fragment_preview_container,
			 * profile).commit(); }
			 **/
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
