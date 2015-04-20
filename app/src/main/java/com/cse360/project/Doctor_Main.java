package com.cse360.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;

public class Doctor_Main extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	SharedPreferences prefs;
	static Doctor curUser;
	static Context context;
	static List<Patient> pts;
	String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_layout);
		prefs = this.getSharedPreferences("com.cse360.project",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		username = prefs.getString("curUser", "");
		try {
			curUser = (Doctor) InternalStorage.readObject(getBaseContext(),
					username);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pts = curUser.getPts();
		context = this;
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.clear_data) {
			prefs.edit().clear().commit();
			Doctor_Main.this.finish();
			return true;
		}
		prefs.edit().clear().commit();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
            //return 3;
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			if(position==0){
			    return "Critical Patients";
			}
			else{
				//return pts.get(position).getLastName() + ", " + pts.get(position).getFirstName();
			    return "Non-critical Patients";
            }
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = null;
			Bundle args = getArguments();
			int curView = args.getInt(ARG_SECTION_NUMBER);
            Log.d("curView", Integer.toString(curView));
            if(curView>0){
                v = inflater.inflate(R.layout.patientslist, container,
                        false);
                LayoutInflater inflates=null;
                try{inflates = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);}
                catch(NullPointerException p){}
                LinearLayout pt_ll = (LinearLayout) v.findViewById(R.id.pt_ll);

                for(int i=0; i<pts.size();i++) {
                    pt_ll.addView(inflates.inflate(R.layout.pt_stub, null));
                }

                LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearSubmit);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, PrescriptionForm.class));
                    }
                });
            }
			/*if(curView>0){
				v = inflater.inflate(R.layout.pt_stub, container,
						false);
//
				TextView pname = (TextView) v.findViewById(R.id.pname);
				TextView ps1 = (TextView) v.findViewById(R.id.ps1);
				TextView ps2 = (TextView) v.findViewById(R.id.ps2);
				TextView ps3 = (TextView) v.findViewById(R.id.ps3);
				ArrayList<Integer> symptom0 = pts.get(curView-1).getSymptom0();
				ArrayList<Integer> symptom1 = pts.get(curView-1).getSymptom1();
				ArrayList<Integer> symptom2 = pts.get(curView-1).getSymptom2();

				pname.setText(pts.get(curView-1).getLastName() + ", " + pts.get(curView-1).getFirstName());
				ps1.setText(Integer.toString(symptom0.get(symptom0.size()-1)));
				ps2.setText(Integer.toString(symptom1.get(symptom1.size()-1)));
				ps3.setText(Integer.toString(symptom2.get(symptom2.size()-1)));
				stylize(ps1,symptom0);
				stylize(ps2,symptom1);
				stylize(ps3,symptom2);
			}*/
			//View rootView = inflater.inflate(R.layout.fragment_doctor__mains,
			//		container, false);
			//return rootView;
			return v;
		}
		public void stylize(TextView tx, ArrayList<Integer> ar){
			int dif = ar.get(ar.size()-2)-ar.get(ar.size()-1);
			if(dif>0){
				tx.setTextColor(getResources().getColor(R.color.green));
				if(dif>1){
					tx.setTypeface(null, Typeface.BOLD);
				}
			}
			else if(dif<0){
				tx.setTextColor(getResources().getColor(R.color.red));
				if(dif<-1){
					tx.setTypeface(null, Typeface.BOLD);
				}
			}
		}
	}

}
