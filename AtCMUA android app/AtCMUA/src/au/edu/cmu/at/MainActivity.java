package au.edu.cmu.at;

import au.edu.cmu.at.BlogFragment;
import au.edu.cmu.at.LoadActivity;
import au.edu.cmu.at.LoginActivity;
import au.edu.cmu.at.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends FragmentActivity {
	
	private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    
    private BlogFragment blog;
    private ContactsFragment contacts;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		blog = new BlogFragment();
		contacts = new ContactsFragment();
		
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mMenuTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		if (savedInstanceState == null) {
			mDrawerList.setItemChecked(0, true);
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_activity_blog, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_logout:
			userLogout(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public static void userLogout(Activity a) {
		final Intent loginActivityIntent = new Intent(a, LoginActivity.class);
		
		LoadActivity.deleteToken();
		a.startActivity(loginActivityIntent);
		Toast.makeText(a.getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	mDrawerList.setItemChecked(position, true);
	    	selectItem(position);
	        mDrawerLayout.closeDrawer(mDrawerList);
	    }
	}
	
	private void selectItem(int position) {
		switch (position) {
    	case 0:
    		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, blog).commit();
    		break;
    	case 1:
    		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, contacts).commit();
    		break;
    	case 2:
    		userLogout(this);
    		break;
    	}
	}

}
