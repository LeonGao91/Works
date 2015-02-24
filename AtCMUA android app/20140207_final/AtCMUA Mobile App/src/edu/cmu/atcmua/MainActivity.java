package edu.cmu.atcmua;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
	ActionBar.TabListener {
	
	private atCmuaFragmentPagerAdapter mFragmentPagerAdapter;
	private ViewPager mViewPager;
	Activity currentActivity=this;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atcmua_main);
		
		mFragmentPagerAdapter = new atCmuaFragmentPagerAdapter(getSupportFragmentManager());
		final ActionBar actionBar = getActionBar();
		//currentActivity = this;
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				getActionBar().setSelectedNavigationItem(position);
			}
		});
		
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_feed).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_contacts).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_info).setTabListener(this));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main_activity, menu);
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
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {	
	}
	
	
	public class atCmuaFragmentPagerAdapter extends FragmentPagerAdapter {
		
		private FeedsFragment feedFragment = new FeedsFragment();
		private ContactsFragment contactsFragment = new ContactsFragment();
		private InfoFragment infoFragment = new InfoFragment();
		
		public atCmuaFragmentPagerAdapter (FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int num) {
			switch(num) {
			case 0:
				return feedFragment;
			case 1:
				return contactsFragment;
			case 2:
				return infoFragment;
			default:
				return null;
			}		
		}

		@Override
		public int getCount() {
			return 3;
		}
	}
	
	public static void userLogout(Activity a) {
		final Intent loginActivityIntent = new Intent(a, LoginActivity.class);
		
		LoadActivity.deleteToken();
		a.startActivity(loginActivityIntent);
		Toast.makeText(a.getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
	}

}
