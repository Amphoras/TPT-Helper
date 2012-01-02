package com.amphoras.tpthelper;

/*  
TPT Helper  Copyright (C) 2011  David Phillips

This file is part of TPT Helper.

TPT Helper is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

TPT Helper is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with TPT Helper.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Locale;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends ListActivity {
	SharedPreferences preferences;
	private static ProgressDialog dialog;
	private final int SHOW_DISCLAIMER = 1;
	private final int POWER_OFF = 2;
	private final int DELETE_IMAGE = 3;
	private final int IMAGE_DELETED = 4;
	private final int BATTERY_LOW = 5;
	private final int START_TPT = 6;
	private final int TPT_FAILED = 7;
	private final int CHANGE_LOCALE = 8;
	private final int CHECK_TYPE = 9;
	private final int CHECK_GEN = 10;
	private final int MANUAL_TYPE = 11;
	private final int UNKNOWN_TYPE = 12;
	private final int CHECK_BOARD = 13;
	private final int MANUAL_BOARD = 14;
	private final int CHECK_BLADE2 = 15;
	private final int UNSUPPORTED = 16;
	private final int GEN_1 = 17;
	private final int GEN_2 = 18;
	
	private BroadcastReceiver BatInfoReceiver = new BroadcastReceiver(){

	    @Override
	    public void onReceive(Context arg0, Intent intent) {
	        int level = intent.getIntExtra("level", 0);
	        Editor edit = preferences.edit();
	        edit.putInt("batterylevel", level);
	        edit.commit();
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    Locale currentlocale = Locale.getDefault();
	    String defaultlocale = currentlocale.toString();
	    String setlocale = preferences.getString("locale", defaultlocale);
	    Locale locale = new Locale(setlocale); 
	    if (!(currentlocale.equals(locale))) {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
        Intent i = new Intent(HomeActivity.this, HomeActivity.class);
	    startActivity(i);
        HomeActivity.this.finish();
	    }
	    this.registerReceiver(this.BatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
          // show disclaimer on first start only
        boolean firststart = preferences.getBoolean("first_start", true);
        boolean allchecked = preferences.getBoolean("allchecked", false);
        if (firststart == true) {
      	    showDialog(SHOW_DISCLAIMER);
        } else {
        	if (allchecked == false) {
        		showDialog(CHECK_BOARD);
        	}
        }
	    String[] options = getResources().getStringArray(R.array.options);
	    setListAdapter(new ArrayAdapter<String>(this, R.layout.home_list_item, options));

	    ListView listview = getListView();
	    listview.setTextFilterEnabled(true);
	    
	    listview.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	int board = preferences.getInt("board", 1);
		    	int gen = preferences.getInt("gen", 1);
		    	String blade2 = preferences.getString("blade2","SF2");
		    	switch (position) {
			      case 0:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, AllInOne.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, AllInOneGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, AllInOneGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, AllInOneSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, AllInOneSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, AllInOneVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 1:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, DirectDownloader.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, Downloader.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, DownloaderGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, DownloaderSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, DownloaderSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, DownloaderVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 2:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFile.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, PickFileGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, PickFileSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, PickFileSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, PickFileVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 3:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFileUnzip.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileUnzipGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, PickFileUnzipGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, PickFileUnzipSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, PickFileUnzipSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, PickFileUnzipVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 4:
			    	  Intent m = new Intent(HomeActivity.this, VerifyImage.class);
		        	  startActivity(m);
					  break;
			      case 5:
			    	  showDialog(START_TPT);
					  break;
			      case 6:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, CustomTPT.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, CustomTPTGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, CustomTPTGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, CustomTPTSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  //Intent sf2 = new Intent(HomeActivity.this, CustomTPTSF2.class);
				    		  //startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  //Intent tmv = new Intent(HomeActivity.this, CustomTPTVivacity.class);
					    		  //startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 7:
			    	  showDialog(DELETE_IMAGE);
					  break;
			      case 8:
			    	  showDialog(POWER_OFF);
			    	  break;
			      /*case 9:
			    	  
			    	  break;*/
			    }
		    }
	    });
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.main);
	    
  	    String[] options = getResources().getStringArray(R.array.options);
	    setListAdapter(new ArrayAdapter<String>(this, R.layout.home_list_item, options));

	    ListView listview = getListView();
	    listview.setTextFilterEnabled(true);

	    listview.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	int board = preferences.getInt("board", 1);
		    	int gen = preferences.getInt("gen", 1);
		    	String blade2 = preferences.getString("blade2", "SF2");
		    	switch (position) {
			      case 0:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, AllInOne.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, AllInOneGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, AllInOneGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, AllInOneSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, AllInOneSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, AllInOneVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 1:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, DirectDownloader.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, Downloader.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, DownloaderGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, DownloaderSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, DownloaderSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, DownloaderVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 2:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFile.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, PickFileGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, PickFileSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, PickFileSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, PickFileVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 3:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFileUnzip.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileUnzipGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, PickFileUnzipGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, PickFileUnzipSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  Intent sf2 = new Intent(HomeActivity.this, PickFileUnzipSF2.class);
				    		  startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  Intent tmv = new Intent(HomeActivity.this, PickFileUnzipVivacity.class);
					    		  startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 4:
			    	  Intent m = new Intent(HomeActivity.this, VerifyImage.class);
		        	  startActivity(m);
					  break;
			      case 5:
			    	  showDialog(START_TPT);
					  break;
			      case 6:
			    	  switch (board) {
			    	  case 1:
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, CustomTPT.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, CustomTPTGen2.class);
					          startActivity(gen_2);
					          break;
			    		  case 3:
			    			  //Intent gen_3 = new Intent(HomeActivity.this, CustomTPTGen3.class);
					          //startActivity(gen_3);
					          break;
			    		  }
			    		  break;
			    	  case 2:
			    		  Intent skate = new Intent(HomeActivity.this, CustomTPTSkate.class);
			    		  startActivity(skate);
			    		  break;
			    	  case 3:
			    		  if (blade2.equals("SF2")) {
			    			  //Intent sf2 = new Intent(HomeActivity.this, CustomTPTSF2.class);
				    		  //startActivity(sf2);
			    		  } else {
			    			  if (blade2.equals("TMV")) {
			    				  //Intent tmv = new Intent(HomeActivity.this, CustomTPTVivacity.class);
					    		  //startActivity(tmv);
			    			  }
			    		  }
			    		  break;
			    	  }
					  break;
			      case 7:
			    	  showDialog(DELETE_IMAGE);
					  break;
			      case 8:
			    	  showDialog(POWER_OFF);
			    	  break;
			      /*case 9:
			    	  
			    	  break;*/
			    }
		    }
		});
	}
    
    @Override
	protected Dialog onCreateDialog(int id) {
    	CharSequence unknown = getText(R.string.unknown);
    	CharSequence other = getText(R.string.other);
    	CharSequence board_blade = getText(R.string.board_blade);
    	CharSequence board_skate = getText(R.string.board_skate);
    	CharSequence board_blade2 = getText(R.string.board_blade2);
        switch (id) {
        case SHOW_DISCLAIMER:
        	  // show disclaimer
            Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle(R.string.disclaimer_heading);
            builder.setMessage(R.string.disclaimer);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Editor edit = preferences.edit();
        		    edit.putBoolean("first_start", false);
        	        edit.commit();
        	        final String board = Build.BOARD;
        	        if (board.equals("blade")) {
        	        	showDialog(CHECK_BOARD);
        	        } else {
        	        	if (board.equals("skate")) {
        	        		showDialog(CHECK_BOARD);
        	        	} else {
        	            	if (board.equals("blade2")) {
        	            		showDialog(CHECK_BOARD);
        	            	} else {
        	            		showDialog(MANUAL_BOARD);
        	            	}
        	            }
        	        }
                }
            });
            return builder.create();
        case POWER_OFF:
          Builder poweroffbuilder = new AlertDialog.Builder(HomeActivity.this);
          poweroffbuilder.setTitle(R.string.power_off);
          poweroffbuilder.setMessage(R.string.start_tpt);
          poweroffbuilder.setCancelable(false);
          poweroffbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  int level = preferences.getInt("batterylevel", 0);
            	  if (level < 25) {
            		  showDialog(BATTERY_LOW);
            	  } else {
                	  try {
    					Runtime.getRuntime().exec(new String[]{"/system/xbin/su","-c","reboot -p"});
    				  } catch (IOException e) {
    					  try {
    					      Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c","reboot -p"});
    					  } catch (IOException e1) {
    					  
    					  }
    				  }
            	  }
              }
          });
          poweroffbuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  // Do nothing
              }
          });
          return poweroffbuilder.create();
        case DELETE_IMAGE:
        	  // delete the image folder
            Builder deleteimagebuilder = new AlertDialog.Builder(HomeActivity.this);
            deleteimagebuilder.setTitle(R.string.delete_image_heading);
            deleteimagebuilder.setMessage(R.string.delete_image);
            deleteimagebuilder.setCancelable(false);
            deleteimagebuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
        	        deleteimage();
                }
            });
            deleteimagebuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return deleteimagebuilder.create();
        case IMAGE_DELETED:
      	    // confirm image folder is deleted
          Builder imagedeletedbuilder = new AlertDialog.Builder(HomeActivity.this);
          imagedeletedbuilder.setTitle(R.string.delete_image_heading);
          imagedeletedbuilder.setMessage(R.string.image_deleted);
          imagedeletedbuilder.setCancelable(false);
          imagedeletedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
      	          // Do nothing
              }
          });
          return imagedeletedbuilder.create();
        case BATTERY_LOW:
        	  // warn about low battery level
            Builder lowbatbuilder = new AlertDialog.Builder(HomeActivity.this);
            lowbatbuilder.setTitle(R.string.low_battery_heading);
            lowbatbuilder.setMessage(R.string.low_battery);
            lowbatbuilder.setCancelable(false);
            lowbatbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Boolean xbin = preferences.getBoolean("xbin", true);
              	    try {
  					  if (xbin == true) {
  						  Runtime.getRuntime().exec(new String[]{"/system/xbin/su","-c","reboot -p"});
  					  } else {
  						  Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c","reboot -p"});
  					  }
  				    } catch (IOException e) {
  				    	
  				    }
                }
            });
            lowbatbuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
        	          // Do nothing
                }
            });
            return lowbatbuilder.create();
        case START_TPT:
      	    // warn before starting TPT
          Builder autotptbuilder = new AlertDialog.Builder(HomeActivity.this);
          autotptbuilder.setTitle(R.string.auto_tpt_heading);
          autotptbuilder.setMessage(R.string.auto_tpt);
          autotptbuilder.setCancelable(false);
          autotptbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  Intent i = new Intent();
		          ComponentName component = new ComponentName("com.android.settings", "com.android.settings.deviceinfo.SDRise");
		          i.setComponent(component);
		          try {
		        	  HomeActivity.this.startActivity(i);
			          HomeActivity.this.finish();
		          } catch (ActivityNotFoundException e) {
		        	  showDialog(TPT_FAILED);
		          } 
              }
          });
          autotptbuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
      	          // Do nothing
              }
          });
          return autotptbuilder.create();
        case TPT_FAILED:
      	    // tell that auto tpt not supported on current rom
          Builder autotptfailbuilder = new AlertDialog.Builder(HomeActivity.this);
          autotptfailbuilder.setTitle(R.string.auto_tpt_heading);
          autotptfailbuilder.setMessage(R.string.auto_tpt_fail);
          autotptfailbuilder.setCancelable(false);
          autotptfailbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
      	          // Do nothing
              }
          });
          return autotptfailbuilder.create();
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(HomeActivity.this);
          localebuilder.setTitle(R.string.change_locale_heading);
          localebuilder.setCancelable(false);
          CharSequence english = getText(R.string.english);
          CharSequence french = getText(R.string.french);
          CharSequence german = getText(R.string.german);
          CharSequence russian = getText(R.string.russian);
          CharSequence chinese = getText(R.string.chinese);
          CharSequence portuguese = getText(R.string.portuguese);
          CharSequence spanish = getText(R.string.spanish);
          CharSequence serbian = getText(R.string.serbian);
          CharSequence cancel = getText(R.string.cancel);
          final CharSequence[] locales = {english, french, german, russian, chinese, portuguese, spanish, serbian, cancel};
      	  localebuilder.setItems(locales, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor editlocale = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		editlocale.putString("locale", "en");
      	    		editlocale.commit();
      	    		Intent i = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 8:
      	    		// Do nothing
      	    		break;
      	    	}
      	      }
      	  });
          return localebuilder.create();
        case CHECK_TYPE:
      	    // check what type of blade user has
          String type = "Unknown Blade";
          try {
      	      File iomem = new File("/proc/iomem");
      	      FileInputStream fis = new FileInputStream(iomem);
      	      InputStreamReader isr = new InputStreamReader(fis);
      	      BufferedReader br = new BufferedReader(isr);
      	      StringBuffer buffer = new StringBuffer();
      	      StringBuffer buffer2 = new StringBuffer();
      	      for (int i = 1; i < 7; i++) {
      		      String s = br.readLine();
      		      if (s != null) {
      		    	  if (i == 5) {
      		    		  for (int j = 20; j < s.length(); j++) {
      		    		      buffer.append(s.charAt(j));
      		    	      }
      		    		  String check = buffer.toString();
      		        	  if (check.equals("System RAM")) {
      		    		      if (s.charAt(0) == '2') {
      				    	      type = "European Blade";
      				          } 
      		        	  } else {
      				    	  if (s.charAt(0) == '9') {
      				    		  type = "Chinese Blade";
      				          }
      				      }
      		    	  }
      		    	  if (i == 6) {
      	    			  for (int j = 20; j < s.length(); j++) {
      		    		      buffer2.append(s.charAt(j));
      		    	      }
      	    			  String check = buffer2.toString();
      		        	  if (check.equals("System RAM")) {
      		        		  if (s.charAt(0) == '2') {
      					    	  type = "European Blade";
      					      }
      		        	  } else {
      		        		  
      		        	  }
      	    		  }
      		      }
      	      }
      	  } catch (FileNotFoundException e) {
      	    	
      	  } catch (IOException e) {

      	  }
      	  final String bladetype = type;
          Builder checktypebuilder = new AlertDialog.Builder(HomeActivity.this);
          checktypebuilder.setTitle(R.string.check_type_heading);
          CharSequence checktype = getText(R.string.check_type);
          checktypebuilder.setMessage(type + " " + checktype);
          checktypebuilder.setCancelable(false);
          checktypebuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
      	          Editor edit = preferences.edit();
      	          edit.putString("blade", bladetype);
      	          edit.commit();
      	          String gen = "";
                  try {
              	    File iomem = new File("/proc/iomem");
              	    FileInputStream fis = new FileInputStream(iomem);
              	    InputStreamReader isr = new InputStreamReader(fis);
              	    BufferedReader br = new BufferedReader(isr);
              	    String s = br.readLine();
                      if (s != null) {
                  	      if (s.charAt(2) == '5') {
              			      gen = "Gen 2";
              		      } else {
              			      if (s.charAt(2) == '9') {
              			          gen = "Gen 1";
              		          }
              		      }
            	      }   
                  } catch (FileNotFoundException e) {
              	    	
                  } catch (IOException e) {

                  }
                  if (gen.equals("Gen 1")) {
                	  showDialog(GEN_1);
                  } else {
                	  if (gen.equals("Gen 2")) {
                    	  showDialog(GEN_2);
                      } else {
                    	  showDialog(CHECK_GEN);
                      }
                  }
              }
          });
          checktypebuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
    	          showDialog(MANUAL_TYPE);
              }
          });
          checktypebuilder.setNeutralButton(R.string.unknown, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
      	          Editor edit = preferences.edit();
      	          edit.putString("blade", bladetype);
      	          edit.commit();
    	          showDialog(UNKNOWN_TYPE);
              }
          });
          return checktypebuilder.create();
        case CHECK_GEN:
      	    // check what gen of blade user has
          Builder checkgenbuilder = new AlertDialog.Builder(HomeActivity.this);
          checkgenbuilder.setTitle(R.string.check_gen_heading);
          checkgenbuilder.setCancelable(false);
          final CharSequence[] gens = {"Gen 1", "Stock Gen 2", "TPT upgraded Gen 2", "Windows upgraded Gen 2", "Stock Gen 3", "TPT upgraded Gen 3", "Windows upgraded Gen 3", unknown};
          checkgenbuilder.setItems(gens, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor editgen = preferences.edit();
      	    	Editor edit2 = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		editgen.putInt("gen", 1);
      	    		editgen.commit();
      	            edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 1:
      	    		editgen.putInt("gen", 2);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 2:
      	    		editgen.putInt("gen", 1);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 3:
      	    		editgen.putInt("gen", 2);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 4:
      	    		editgen.putInt("gen", 3);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 5:
      	    		editgen.putInt("gen", 3);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 6:
      	    		editgen.putInt("gen", 3);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 7:
      	    		editgen.putInt("gen", 0);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	}
      	      }
      	  });
          return checkgenbuilder.create();
        case MANUAL_TYPE:
      	    // let user enter type and set it
          Builder manualtypebuilder = new AlertDialog.Builder(HomeActivity.this);
          manualtypebuilder.setTitle(R.string.manual_type_heading);
          manualtypebuilder.setCancelable(false);
          final CharSequence[] types = {"European", "Chinese", unknown};
          manualtypebuilder.setItems(types, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor edit = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		edit.putString("blade", "European Blade");
        	        edit.commit();
        	        showDialog(CHECK_GEN);
      	    		break;
      	    	case 1:
      	    		edit.putString("blade", "Chinese Blade");
        	        edit.commit();
        	        showDialog(CHECK_GEN);
      	    		break;
      	    	case 2:
      	    		edit.putString("blade", "Unknown Blade");
        	        edit.commit();
        	        showDialog(CHECK_GEN);
      	    		break;
      	    	}
      	      }
      	  });
          return manualtypebuilder.create();
        case UNKNOWN_TYPE:
      	    // tell the user that detected type is being set
          Builder unknowntypebuilder = new AlertDialog.Builder(HomeActivity.this);
          unknowntypebuilder.setTitle(R.string.setting_type_heading);
          String unknowntype = preferences.getString("blade", "Unknown Blade");
          CharSequence setting_type = getText(R.string.setting_type);
          unknowntypebuilder.setMessage(setting_type + " " + unknowntype);
          unknowntypebuilder.setCancelable(false);
          unknowntypebuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
    	          showDialog(CHECK_GEN);
              }
          });
          return unknowntypebuilder.create();
        case CHECK_BOARD:
      	    // check which phone board the user has
          Builder checkboardbuilder = new AlertDialog.Builder(HomeActivity.this);
          checkboardbuilder.setTitle(R.string.check_board_heading);
          final String board = Build.BOARD;
          CharSequence correct = getText(R.string.correct);
          if (board.equals("blade")) {
        	  checkboardbuilder.setMessage(board_blade + " " + correct);
          } else {
        	  if (board.equals("skate")) {
        		  checkboardbuilder.setMessage(board_skate + " " + correct);
        	  } else {
            	  if (board.equals("blade2")) {
            		  checkboardbuilder.setMessage(board_blade2 + " " + correct);
            	  }
              }
          }
          checkboardbuilder.setCancelable(false);
          checkboardbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  Editor edit = preferences.edit();
            	  Editor edit2 = preferences.edit();
            	  if (board.equals("blade")) {
                	  edit.putInt("board", 1);
                	  edit.commit();
                	  showDialog(CHECK_TYPE);
                  } else {
                	  if (board.equals("skate")) {
                		  edit.putInt("board", 2);
                    	  edit.commit();
                    	  edit2.putBoolean("allchecked", true);
                    	  edit2.commit();
                	  } else {
                    	  if (board.equals("blade2")) {
                    		  edit.putInt("board", 3);
                        	  edit.commit();
                        	  showDialog(CHECK_BLADE2);
                    	  }
                      }
                  }
              }
          });
          checkboardbuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  showDialog(MANUAL_BOARD);
              }
          });
          return checkboardbuilder.create();
        case MANUAL_BOARD:
      	    // let user enter board and set it
          Builder manualboardbuilder = new AlertDialog.Builder(HomeActivity.this);
          manualboardbuilder.setTitle(R.string.phone_type_heading);
          manualboardbuilder.setCancelable(false);
          final CharSequence[] boards = {board_blade, board_skate, board_blade2, other};
          manualboardbuilder.setItems(boards, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor edit = preferences.edit();
      	    	Editor edit2 = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		edit.putInt("board", 1);
        	        edit.commit();
        	        showDialog(CHECK_TYPE);
      	    		break;
      	    	case 1:
      	    		edit.putInt("board", 2);
        	        edit.commit();
        	        edit2.putBoolean("allchecked", true);
              	    edit2.commit();
      	    		break;
      	    	case 2:
      	    		edit.putInt("board", 3);
        	        edit.commit();
        	        showDialog(CHECK_BLADE2);
      	    		break;
      	    	case 3:
        	        showDialog(UNSUPPORTED);
      	    		break;
      	    	}
      	      }
      	  });
          return manualboardbuilder.create();
        case CHECK_BLADE2:
      	    // check which blade2 user has
          Builder checkblade2builder = new AlertDialog.Builder(HomeActivity.this);
          checkblade2builder.setTitle(R.string.phone_type_heading);
          checkblade2builder.setCancelable(false);
          CharSequence san_francisco_ii = getText(R.string.san_francisco_ii);
          CharSequence vivacity = getText(R.string.vivacity);
          final CharSequence[] blade2s = {san_francisco_ii, vivacity, other};
          checkblade2builder.setItems(blade2s, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor edit = preferences.edit();
      	    	Editor edit2 = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		edit.putString("blade2", "SF2");
        	        edit.commit();
        	        edit2.putBoolean("allchecked", true);
              	    edit2.commit();
      	    		break;
      	    	case 1:
      	    		edit.putString("blade2", "TMV");
        	        edit.commit();
        	        edit2.putBoolean("allchecked", true);
              	    edit2.commit();
      	    		break;
      	    	case 2:
        	        showDialog(UNSUPPORTED);
      	    		break;
      	    	}
      	      }
      	  });
          return checkblade2builder.create();
        case UNSUPPORTED:
      	    // tell user phone model is not supported
          Builder unsupportedbuilder = new AlertDialog.Builder(HomeActivity.this);
          unsupportedbuilder.setTitle(R.string.unsupported_heading);
          unsupportedbuilder.setMessage(R.string.unsupported);
          unsupportedbuilder.setCancelable(false);
          unsupportedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
    	          HomeActivity.this.finish();
              }
          });
          return unsupportedbuilder.create();
        case GEN_1:
      	    // tell user phone model is not supported
          Builder gen1builder = new AlertDialog.Builder(HomeActivity.this);
          gen1builder.setTitle(R.string.check_gen_heading);
          gen1builder.setMessage("Gen 1");
          gen1builder.setCancelable(false);
          gen1builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  Editor editgen = preferences.edit();
        	      Editor edit2 = preferences.edit();
        	      editgen.putInt("gen", 1);
        	      editgen.commit();
        	      edit2.putBoolean("allchecked", true);
        	      edit2.commit();
              }
          });
          gen1builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
    	          showDialog(CHECK_GEN);
              }
          });
          return gen1builder.create();
        case GEN_2:
      	    // tell user phone model is not supported
          Builder gen2builder = new AlertDialog.Builder(HomeActivity.this);
          gen2builder.setTitle(R.string.check_gen_heading);
          gen2builder.setCancelable(false);
          final CharSequence[] gens2 = {"Stock Gen 2", "Stock Gen 3", other, unknown};
          gen2builder.setItems(gens2, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor editgen = preferences.edit();
      	    	Editor edit2 = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		editgen.putInt("gen", 2);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 1:
      	    		editgen.putInt("gen", 3);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 2:
      	    		showDialog(CHECK_GEN);
      	    		break;
      	    	case 3:
      	    		editgen.putInt("gen", 0);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	}
      	      }
      	  });
          return gen2builder.create();
        }
        return super.onCreateDialog(id);
    }
    
    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
        	  // get list of files in the directory
            File[] files = path.listFiles();
              // for each file
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                	  // if this is also a directory, run the delete method on it
                    deleteDirectory(files[i]);
                } else {
                	  // if its a file, delete it
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflator = getMenuInflater();
    	inflator.inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.support:
    		Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tpthelper@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
            startActivity(emailIntent);
    		break;
    	/* case R.id.troubleshooting:
    		Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
    		startActivity(j);
    		break; */
    	case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
    	case R.id.about:
			Intent j = new Intent(HomeActivity.this, About.class);
			startActivity(j);
			break;
    	case R.id.instructions:
    		Intent k = new Intent(HomeActivity.this, Instructions.class);
    		startActivity(k);
    		break;
    	case R.id.show_changelog:
    		Intent l = new Intent(HomeActivity.this, Changelog.class);
    		startActivity(l);
    		break;
    	case R.id.preferences:
    		Intent m = new Intent(HomeActivity.this, Preferences.class);
    		startActivity(m);
    		break;
    	case R.id.license:
			Intent n = new Intent(HomeActivity.this, License.class);
			startActivity(n);
			break;
    	}
    	return true;
    }
    
    private class DeleteImageTask extends AsyncTask<File, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence deleting = getText(R.string.deleting);
			dialog = ProgressDialog.show(HomeActivity.this, "", deleting, true);
		}
		
		@Override
		protected String doInBackground(File... paths) {
			String response = "";
			for (File path : paths) {
				deleteDirectory(path);
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			showDialog(IMAGE_DELETED);
		}
	}
    
    public void deleteimage() {
		DeleteImageTask task = new DeleteImageTask();
		File dir = Environment.getExternalStorageDirectory();
	    File file = new File(dir, "/image");
		task.execute(new File[] {file});
	}
}