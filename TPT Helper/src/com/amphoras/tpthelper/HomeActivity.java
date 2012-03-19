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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
	private final int GEN_2_TPT = 18;
	private final int GEN_2_STOCK = 19;
	private final int GEN_3 = 20;
	String TAG = "HomeActivity";
	
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
		    	String type = preferences.getString("blade", "European Blade");
		    	switch (position) {
			      case 0:
			    	  switch (board) {
			    	  case 1:
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, AllInOne.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, AllInOneGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, DirectDownloader.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, DownloaderGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFile.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFileUnzip.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileUnzipGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, CustomTPT.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, CustomTPTGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
		    	String blade2 = preferences.getString("blade2","SF2");
		    	String type = preferences.getString("blade", "European Blade");
		    	switch (position) {
			      case 0:
			    	  switch (board) {
			    	  case 1:
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, AllInOne.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, AllInOneGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, DirectDownloader.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, DownloaderGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFile.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, PickFileUnzip.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, PickFileUnzipGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
			    		  if (type.equals("European Blade")) {
			    		  switch (gen) {
			    		  case 1:
			    			  Intent gen_1 = new Intent(HomeActivity.this, CustomTPT.class);
					          startActivity(gen_1);
					          break;
			    		  case 2:
			    			  Intent gen_2 = new Intent(HomeActivity.this, CustomTPTGen2Gen3.class);
					          startActivity(gen_2);
					          break;
			    		  }
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
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	HomeActivity.this.finish();
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
                	try {
    					Runtime.getRuntime().exec(new String[]{"/system/xbin/su","-c","reboot -p"});
    				} catch (IOException e) {
    					try {
    					    Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c","reboot -p"});
    					} catch (IOException e1) {
    					  
    					}
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
          CharSequence czech = getText(R.string.czech);
          CharSequence polish = getText(R.string.polish);
          CharSequence hungarian = getText(R.string.hungarian);
          CharSequence swedish = getText(R.string.swedish);
          CharSequence italian = getText(R.string.italian);
          CharSequence dutch_be = getText(R.string.dutch_be);
          CharSequence portuguese_br = getText(R.string.portuguese_br);
          CharSequence greek = getText(R.string.greek);
          CharSequence cancel = getText(R.string.cancel);
          final CharSequence[] locales = {english, french, german, russian, chinese, portuguese, spanish, serbian, czech, polish, hungarian, swedish, italian, dutch_be, portuguese_br, greek, cancel};
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
      	    		editlocale.putString("locale", "cs");
      	    		editlocale.commit();
      	    		Intent q = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(q);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 9:
      	    		editlocale.putString("locale", "pl");
      	    		editlocale.commit();
      	    		Intent r = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(r);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 10:
      	    		editlocale.putString("locale", "hu");
      	    		editlocale.commit();
      	    		Intent s = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(s);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 11:
      	    		editlocale.putString("locale", "sv");
      	    		editlocale.commit();
      	    		Intent t = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(t);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 12:
      	    		editlocale.putString("locale", "it");
      	    		editlocale.commit();
      	    		Intent u = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(u);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 13:
      	    		editlocale.putString("locale", "nl");
      	    		editlocale.commit();
      	    		Intent v = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(v);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 14:
      	    		editlocale.putString("locale", "pt_BR");
      	    		editlocale.commit();
      	    		Intent w = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(w);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 15:
      	    		editlocale.putString("locale", "el");
      	    		editlocale.commit();
      	    		Intent x = new Intent(HomeActivity.this, HomeActivity.class);
      	    	    startActivity(x);
      	    	    HomeActivity.this.finish();
      	    		break;
      	    	case 16:
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
      	      checked_type: {
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
      				    	      break checked_type;
      				          } 
      		        	  } else {
      				    	  if (s.charAt(0) == '9') {
      				    		  type = "Chinese Blade";
      				    		  break checked_type;
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
      					    	  break checked_type;
      					      }
      		        	  } else {
    				    	  if (s.charAt(0) == '9') {
    				    		  type = "Chinese Blade";
    				    		  break checked_type;
    				          }
    				      }
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
      	          if (bladetype.equals("European Blade")) {
                      try {
                	      File iomem = new File("/proc/iomem");
                	      FileInputStream fis = new FileInputStream(iomem);
                	      InputStreamReader isr = new InputStreamReader(fis);
                	      BufferedReader br = new BufferedReader(isr);
                	      String s1 = br.readLine();
                          if (s1 != null) {
                    	      if (s1.charAt(2) == '5') {
                    	    	File lib = new File("/system/lib/libloc.so");
                    	    	StringBuffer buffer = new StringBuffer();
                    	  	    StringBuffer recovery = new StringBuffer();
                    	  	    StringBuffer boot = new StringBuffer();
                    	  	    StringBuffer splash = new StringBuffer();
                    	  	    StringBuffer misc = new StringBuffer();
                    	  	    StringBuffer cache = new StringBuffer();
                    	  	    StringBuffer system = new StringBuffer();
                    	  	    StringBuffer userdata = new StringBuffer();
                    	  	    StringBuffer oem = new StringBuffer();
                    	  	    StringBuffer persist = new StringBuffer();
                    	  		try {
                    	  	    	File partitions = new File("/proc/partitions");
                    	  	    	FileInputStream fis2 = new FileInputStream(partitions);
                    	  		    InputStreamReader isr2 = new InputStreamReader(fis2);
                    	  		    BufferedReader br2 = new BufferedReader(isr2);
                    	  		    for (int i = 1; i < 50; i++) {
                    	  			    String s = br2.readLine();
                    	  			    if (i > 2) {
                    	  			      if (s != null) {
                    	  			    	  String b = s.charAt(3) + "";
                    	  			    	  if (b.equals("1")) {
                    	  			    		  String a = s.charAt(2) + "";
                    	  			    		  if (a.equals("3")) {
                    	  			    			  String c = s.charAt(12) + "";
                    	  					    	  if (c.equals("0")) {
                    	  						    	  buffer.append("\nRecovery: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      recovery.append(s.charAt(23));
                    	  							      int recovery1 = Integer.valueOf(s.charAt(22));
                    	  							      if (recovery1 != 32) {
                    	  							    	  recovery.append(s.charAt(22));
                    	  							      }
                    	  							      int recovery2 = Integer.valueOf(s.charAt(21));
                    	  							      if (recovery2 != 32) {
                    	  							    	  recovery.append(s.charAt(21));
                    	  							      }
                    	  							      int recovery3 = Integer.valueOf(s.charAt(20));
                    	  							      if (recovery3 != 32) {
                    	  							    	  recovery.append(s.charAt(20));
                    	  							      }
                    	  							      int recovery4 = Integer.valueOf(s.charAt(19));
                    	  							      if (recovery4 != 32) {
                    	  							    	  recovery.append(s.charAt(19));
                    	  							      }
                    	  							      int recovery5 = Integer.valueOf(s.charAt(18));
                    	  							      if (recovery5 != 32) {
                    	  							    	  recovery.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; recovery.length() - counter > -1; counter++) {
                    	  							    	  size.append(recovery.charAt(recovery.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("recovery", Integer.parseInt(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("recovery_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("1")) {
                    	  						    	  buffer.append("\nBoot: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      boot.append(s.charAt(23));
                    	  							      int boot1 = Integer.valueOf(s.charAt(22));
                    	  							      if (boot1 != 32) {
                    	  							    	  boot.append(s.charAt(22));
                    	  							      }
                    	  							      int boot2 = Integer.valueOf(s.charAt(21));
                    	  							      if (boot2 != 32) {
                    	  							    	  boot.append(s.charAt(21));
                    	  							      }
                    	  							      int boot3 = Integer.valueOf(s.charAt(20));
                    	  							      if (boot3 != 32) {
                    	  							    	  boot.append(s.charAt(20));
                    	  							      }
                    	  							      int boot4 = Integer.valueOf(s.charAt(19));
                    	  							      if (boot4 != 32) {
                    	  							    	  boot.append(s.charAt(19));
                    	  							      }
                    	  							      int boot5 = Integer.valueOf(s.charAt(18));
                    	  							      if (boot5 != 32) {
                    	  							    	  boot.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; boot.length() - counter > -1; counter++) {
                    	  							    	  size.append(boot.charAt(boot.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("boot", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("boot_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("2")) {
                    	  						    	  buffer.append("\nSplash: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      splash.append(s.charAt(23));
                    	  							      int splash1 = Integer.valueOf(s.charAt(22));
                    	  							      if (splash1 != 32) {
                    	  							    	  splash.append(s.charAt(22));
                    	  							      }
                    	  							      int splash2 = Integer.valueOf(s.charAt(21));
                    	  							      if (splash2 != 32) {
                    	  							    	  splash.append(s.charAt(21));
                    	  							      }
                    	  							      int splash3 = Integer.valueOf(s.charAt(20));
                    	  							      if (splash3 != 32) {
                    	  							    	  splash.append(s.charAt(20));
                    	  							      }
                    	  							      int splash4 = Integer.valueOf(s.charAt(19));
                    	  							      if (splash4 != 32) {
                    	  							    	  splash.append(s.charAt(19));
                    	  							      }
                    	  							      int splash5 = Integer.valueOf(s.charAt(18));
                    	  							      if (splash5 != 32) {
                    	  							    	  splash.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; splash.length() - counter > -1; counter++) {
                    	  							    	  size.append(splash.charAt(splash.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("splash", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("splash_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("3")) {
                    	  						    	  buffer.append("\nMisc: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      misc.append(s.charAt(23));
                    	  							      int misc1 = Integer.valueOf(s.charAt(22));
                    	  							      if (misc1 != 32) {
                    	  							    	  misc.append(s.charAt(22));
                    	  							      }
                    	  							      int misc2 = Integer.valueOf(s.charAt(21));
                    	  							      if (misc2 != 32) {
                    	  							    	  misc.append(s.charAt(21));
                    	  							      }
                    	  							      int misc3 = Integer.valueOf(s.charAt(20));
                    	  							      if (misc3 != 32) {
                    	  							    	  misc.append(s.charAt(20));
                    	  							      }
                    	  							      int misc4 = Integer.valueOf(s.charAt(19));
                    	  							      if (misc4 != 32) {
                    	  							    	  misc.append(s.charAt(19));
                    	  							      }
                    	  							      int misc5 = Integer.valueOf(s.charAt(18));
                    	  							      if (misc5 != 32) {
                    	  							    	  misc.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; misc.length() - counter > -1; counter++) {
                    	  							    	  size.append(misc.charAt(misc.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("misc", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("misc_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("4")) {
                    	  						    	  buffer.append("\nCache: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      cache.append(s.charAt(23));
                    	  							      int cache1 = Integer.valueOf(s.charAt(22));
                    	  							      if (cache1 != 32) {
                    	  							    	  cache.append(s.charAt(22));
                    	  							      }
                    	  							      int cache2 = Integer.valueOf(s.charAt(21));
                    	  							      if (cache2 != 32) {
                    	  							    	  cache.append(s.charAt(21));
                    	  							      }
                    	  							      int cache3 = Integer.valueOf(s.charAt(20));
                    	  							      if (cache3 != 32) {
                    	  							    	  cache.append(s.charAt(20));
                    	  							      }
                    	  							      int cache4 = Integer.valueOf(s.charAt(19));
                    	  							      if (cache4 != 32) {
                    	  							    	  cache.append(s.charAt(19));
                    	  							      }
                    	  							      int cache5 = Integer.valueOf(s.charAt(18));
                    	  							      if (cache5 != 32) {
                    	  							    	  cache.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; cache.length() - counter > -1; counter++) {
                    	  							    	  size.append(cache.charAt(cache.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("cache", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("cache_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("5")) {
                    	  						    	  buffer.append("\nSystem: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      system.append(s.charAt(23));
                    	  							      int system1 = Integer.valueOf(s.charAt(22));
                    	  							      if (system1 != 32) {
                    	  							    	  system.append(s.charAt(22));
                    	  							      }
                    	  							      int system2 = Integer.valueOf(s.charAt(21));
                    	  							      if (system2 != 32) {
                    	  							    	  system.append(s.charAt(21));
                    	  							      }
                    	  							      int system3 = Integer.valueOf(s.charAt(20));
                    	  							      if (system3 != 32) {
                    	  							    	  system.append(s.charAt(20));
                    	  							      }
                    	  							      int system4 = Integer.valueOf(s.charAt(19));
                    	  							      if (system4 != 32) {
                    	  							    	  system.append(s.charAt(19));
                    	  							      }
                    	  							      int system5 = Integer.valueOf(s.charAt(18));
                    	  							      if (system5 != 32) {
                    	  							    	  system.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; system.length() - counter > -1; counter++) {
                    	  							    	  size.append(system.charAt(system.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("system", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("system_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("6")) {
                    	  						    	  buffer.append("\nUserdata: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      userdata.append(s.charAt(23));
                    	  							      int userdata1 = Integer.valueOf(s.charAt(22));
                    	  							      if (userdata1 != 32) {
                    	  							    	  userdata.append(s.charAt(22));
                    	  							      }
                    	  							      int userdata2 = Integer.valueOf(s.charAt(21));
                    	  							      if (userdata2 != 32) {
                    	  							    	  userdata.append(s.charAt(21));
                    	  							      }
                    	  							      int userdata3 = Integer.valueOf(s.charAt(20));
                    	  							      if (userdata3 != 32) {
                    	  							    	  userdata.append(s.charAt(20));
                    	  							      }
                    	  							      int userdata4 = Integer.valueOf(s.charAt(19));
                    	  							      if (userdata4 != 32) {
                    	  							    	  userdata.append(s.charAt(19));
                    	  							      }
                    	  							      int userdata5 = Integer.valueOf(s.charAt(18));
                    	  							      if (userdata5 != 32) {
                    	  							    	  userdata.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; userdata.length() - counter > -1; counter++) {
                    	  							    	  size.append(userdata.charAt(userdata.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("userdata", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("userdata_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("7")) {
                    	  						    	  buffer.append("\nOEM: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      oem.append(s.charAt(23));
                    	  							      int oem1 = Integer.valueOf(s.charAt(22));
                    	  							      if (oem1 != 32) {
                    	  							    	  oem.append(s.charAt(22));
                    	  							      }
                    	  							      int oem2 = Integer.valueOf(s.charAt(21));
                    	  							      if (oem2 != 32) {
                    	  							    	  oem.append(s.charAt(21));
                    	  							      }
                    	  							      int oem3 = Integer.valueOf(s.charAt(20));
                    	  							      if (oem3 != 32) {
                    	  							    	  oem.append(s.charAt(20));
                    	  							      }
                    	  							      int oem4 = Integer.valueOf(s.charAt(19));
                    	  							      if (oem4 != 32) {
                    	  							    	  oem.append(s.charAt(19));
                    	  							      }
                    	  							      int oem5 = Integer.valueOf(s.charAt(18));
                    	  							      if (oem5 != 32) {
                    	  							    	  oem.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; oem.length() - counter > -1; counter++) {
                    	  							    	  size.append(oem.charAt(oem.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("oem", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("oem_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("8")) {
                    	  						    	  buffer.append("\nPersist: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      persist.append(s.charAt(23));
                    	  							      int persist1 = Integer.valueOf(s.charAt(22));
                    	  							      if (persist1 != 32) {
                    	  							    	  persist.append(s.charAt(22));
                    	  							      }
                    	  							      int persist2 = Integer.valueOf(s.charAt(21));
                    	  							      if (persist2 != 32) {
                    	  							    	  persist.append(s.charAt(21));
                    	  							      }
                    	  							      int persist3 = Integer.valueOf(s.charAt(20));
                    	  							      if (persist3 != 32) {
                    	  							    	  persist.append(s.charAt(20));
                    	  							      }
                    	  							      int persist4 = Integer.valueOf(s.charAt(19));
                    	  							      if (persist4 != 32) {
                    	  							    	  persist.append(s.charAt(19));
                    	  							      }
                    	  							      int persist5 = Integer.valueOf(s.charAt(18));
                    	  							      if (persist5 != 32) {
                    	  							    	  persist.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; persist.length() - counter > -1; counter++) {
                    	  							    	  size.append(persist.charAt(persist.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("persist", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("persist_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					      }
                    	  			    	  }
                    	  			      }
                    	  			    }
                    	  		    }
                    	  		    int recoveryint = preferences.getInt("recovery", 0);
                    	  		    int bootint = preferences.getInt("boot", 0);
                    	  		    int splashint = preferences.getInt("splash", 0);
                    	  		    int miscint = preferences.getInt("misc", 0);
                    	  		    int cacheint = preferences.getInt("cache", 0);
                    	  		    int systemint = preferences.getInt("system", 0);
                    	  		    int userdataint = preferences.getInt("userdata", 0);
                    	  		    int oemint = preferences.getInt("oem", 0);
                    	  		    int persistint = preferences.getInt("persist", 0);
                    	      	    int total = ((recoveryint + bootint + splashint + miscint + cacheint + systemint + userdataint + oemint + persistint)/1024);
                    	      		if (total > 468) {
                    	      			if (lib.canRead()) {
                    	      				showDialog(GEN_2_TPT);
                    	        		    } else {
                    	        		    	showDialog(CHECK_GEN);
                    	        		    }
                    	      		} else {
                    	      			if (lib.canRead()) {
                    	      				showDialog(GEN_2_STOCK);
                    	        		    } else {
                    	        		    	showDialog(GEN_3);
                    	        		    }
                    	      		}
                    	  	    } catch (IOException e) {
                    	  	    	Log.i(TAG, "" + e);
                    	  	    } catch (Exception e) {
                    	  	    	Log.i(TAG, "" + e);
                    	  	    }
                		      } else {
                			      if (s1.charAt(2) == '9') {
                			    	  showDialog(GEN_1);
                		          }
                		      }
              	      }   
                      } catch (FileNotFoundException e) {
                	    	
                      } catch (IOException e) {

                      }
      	          } else {
      	        	  showDialog(UNSUPPORTED);
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
      	    		editgen.putInt("gen", 2);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 5:
      	    		editgen.putInt("gen", 2);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 6:
      	    		editgen.putInt("gen", 2);
      	    		editgen.commit();
      	    		edit2.putBoolean("allchecked", true);
      	            edit2.commit();
      	    		break;
      	    	case 7:
      	    		showDialog(UNSUPPORTED);
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
        	        try {
              	      File iomem = new File("/proc/iomem");
              	      FileInputStream fis = new FileInputStream(iomem);
              	      InputStreamReader isr = new InputStreamReader(fis);
              	      BufferedReader br = new BufferedReader(isr);
              	      String s1 = br.readLine();
                        if (s1 != null) {
                  	      if (s1.charAt(2) == '5') {
                  	    	File lib = new File("/system/lib/libloc.so");
                  	    	StringBuffer buffer = new StringBuffer();
                  	  	    StringBuffer recovery = new StringBuffer();
                  	  	    StringBuffer boot = new StringBuffer();
                  	  	    StringBuffer splash = new StringBuffer();
                  	  	    StringBuffer misc = new StringBuffer();
                  	  	    StringBuffer cache = new StringBuffer();
                  	  	    StringBuffer system = new StringBuffer();
                  	  	    StringBuffer userdata = new StringBuffer();
                  	  	    StringBuffer oem = new StringBuffer();
                  	  	    StringBuffer persist = new StringBuffer();
                  	  		try {
                  	  	    	File partitions = new File("/proc/partitions");
                  	  	    	FileInputStream fis2 = new FileInputStream(partitions);
                  	  		    InputStreamReader isr2 = new InputStreamReader(fis2);
                  	  		    BufferedReader br2 = new BufferedReader(isr2);
                  	  		    for (int i = 1; i < 50; i++) {
                  	  			    String s = br2.readLine();
                  	  			    if (i > 2) {
                  	  			      if (s != null) {
                  	  			    	  String b = s.charAt(3) + "";
                  	  			    	  if (b.equals("1")) {
                  	  			    		  String a = s.charAt(2) + "";
                  	  			    		  if (a.equals("3")) {
                  	  			    			  String c = s.charAt(12) + "";
                  	  					    	  if (c.equals("0")) {
                  	  						    	  buffer.append("\nRecovery: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      recovery.append(s.charAt(23));
                  	  							      int recovery1 = Integer.valueOf(s.charAt(22));
                  	  							      if (recovery1 != 32) {
                  	  							    	  recovery.append(s.charAt(22));
                  	  							      }
                  	  							      int recovery2 = Integer.valueOf(s.charAt(21));
                  	  							      if (recovery2 != 32) {
                  	  							    	  recovery.append(s.charAt(21));
                  	  							      }
                  	  							      int recovery3 = Integer.valueOf(s.charAt(20));
                  	  							      if (recovery3 != 32) {
                  	  							    	  recovery.append(s.charAt(20));
                  	  							      }
                  	  							      int recovery4 = Integer.valueOf(s.charAt(19));
                  	  							      if (recovery4 != 32) {
                  	  							    	  recovery.append(s.charAt(19));
                  	  							      }
                  	  							      int recovery5 = Integer.valueOf(s.charAt(18));
                  	  							      if (recovery5 != 32) {
                  	  							    	  recovery.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; recovery.length() - counter > -1; counter++) {
                  	  							    	  size.append(recovery.charAt(recovery.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("recovery", Integer.parseInt(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("recovery_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("1")) {
                  	  						    	  buffer.append("\nBoot: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      boot.append(s.charAt(23));
                  	  							      int boot1 = Integer.valueOf(s.charAt(22));
                  	  							      if (boot1 != 32) {
                  	  							    	  boot.append(s.charAt(22));
                  	  							      }
                  	  							      int boot2 = Integer.valueOf(s.charAt(21));
                  	  							      if (boot2 != 32) {
                  	  							    	  boot.append(s.charAt(21));
                  	  							      }
                  	  							      int boot3 = Integer.valueOf(s.charAt(20));
                  	  							      if (boot3 != 32) {
                  	  							    	  boot.append(s.charAt(20));
                  	  							      }
                  	  							      int boot4 = Integer.valueOf(s.charAt(19));
                  	  							      if (boot4 != 32) {
                  	  							    	  boot.append(s.charAt(19));
                  	  							      }
                  	  							      int boot5 = Integer.valueOf(s.charAt(18));
                  	  							      if (boot5 != 32) {
                  	  							    	  boot.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; boot.length() - counter > -1; counter++) {
                  	  							    	  size.append(boot.charAt(boot.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("boot", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("boot_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("2")) {
                  	  						    	  buffer.append("\nSplash: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      splash.append(s.charAt(23));
                  	  							      int splash1 = Integer.valueOf(s.charAt(22));
                  	  							      if (splash1 != 32) {
                  	  							    	  splash.append(s.charAt(22));
                  	  							      }
                  	  							      int splash2 = Integer.valueOf(s.charAt(21));
                  	  							      if (splash2 != 32) {
                  	  							    	  splash.append(s.charAt(21));
                  	  							      }
                  	  							      int splash3 = Integer.valueOf(s.charAt(20));
                  	  							      if (splash3 != 32) {
                  	  							    	  splash.append(s.charAt(20));
                  	  							      }
                  	  							      int splash4 = Integer.valueOf(s.charAt(19));
                  	  							      if (splash4 != 32) {
                  	  							    	  splash.append(s.charAt(19));
                  	  							      }
                  	  							      int splash5 = Integer.valueOf(s.charAt(18));
                  	  							      if (splash5 != 32) {
                  	  							    	  splash.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; splash.length() - counter > -1; counter++) {
                  	  							    	  size.append(splash.charAt(splash.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("splash", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("splash_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("3")) {
                  	  						    	  buffer.append("\nMisc: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      misc.append(s.charAt(23));
                  	  							      int misc1 = Integer.valueOf(s.charAt(22));
                  	  							      if (misc1 != 32) {
                  	  							    	  misc.append(s.charAt(22));
                  	  							      }
                  	  							      int misc2 = Integer.valueOf(s.charAt(21));
                  	  							      if (misc2 != 32) {
                  	  							    	  misc.append(s.charAt(21));
                  	  							      }
                  	  							      int misc3 = Integer.valueOf(s.charAt(20));
                  	  							      if (misc3 != 32) {
                  	  							    	  misc.append(s.charAt(20));
                  	  							      }
                  	  							      int misc4 = Integer.valueOf(s.charAt(19));
                  	  							      if (misc4 != 32) {
                  	  							    	  misc.append(s.charAt(19));
                  	  							      }
                  	  							      int misc5 = Integer.valueOf(s.charAt(18));
                  	  							      if (misc5 != 32) {
                  	  							    	  misc.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; misc.length() - counter > -1; counter++) {
                  	  							    	  size.append(misc.charAt(misc.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("misc", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("misc_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("4")) {
                  	  						    	  buffer.append("\nCache: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      cache.append(s.charAt(23));
                  	  							      int cache1 = Integer.valueOf(s.charAt(22));
                  	  							      if (cache1 != 32) {
                  	  							    	  cache.append(s.charAt(22));
                  	  							      }
                  	  							      int cache2 = Integer.valueOf(s.charAt(21));
                  	  							      if (cache2 != 32) {
                  	  							    	  cache.append(s.charAt(21));
                  	  							      }
                  	  							      int cache3 = Integer.valueOf(s.charAt(20));
                  	  							      if (cache3 != 32) {
                  	  							    	  cache.append(s.charAt(20));
                  	  							      }
                  	  							      int cache4 = Integer.valueOf(s.charAt(19));
                  	  							      if (cache4 != 32) {
                  	  							    	  cache.append(s.charAt(19));
                  	  							      }
                  	  							      int cache5 = Integer.valueOf(s.charAt(18));
                  	  							      if (cache5 != 32) {
                  	  							    	  cache.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; cache.length() - counter > -1; counter++) {
                  	  							    	  size.append(cache.charAt(cache.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("cache", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("cache_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("5")) {
                  	  						    	  buffer.append("\nSystem: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      system.append(s.charAt(23));
                  	  							      int system1 = Integer.valueOf(s.charAt(22));
                  	  							      if (system1 != 32) {
                  	  							    	  system.append(s.charAt(22));
                  	  							      }
                  	  							      int system2 = Integer.valueOf(s.charAt(21));
                  	  							      if (system2 != 32) {
                  	  							    	  system.append(s.charAt(21));
                  	  							      }
                  	  							      int system3 = Integer.valueOf(s.charAt(20));
                  	  							      if (system3 != 32) {
                  	  							    	  system.append(s.charAt(20));
                  	  							      }
                  	  							      int system4 = Integer.valueOf(s.charAt(19));
                  	  							      if (system4 != 32) {
                  	  							    	  system.append(s.charAt(19));
                  	  							      }
                  	  							      int system5 = Integer.valueOf(s.charAt(18));
                  	  							      if (system5 != 32) {
                  	  							    	  system.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; system.length() - counter > -1; counter++) {
                  	  							    	  size.append(system.charAt(system.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("system", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("system_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("6")) {
                  	  						    	  buffer.append("\nUserdata: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      userdata.append(s.charAt(23));
                  	  							      int userdata1 = Integer.valueOf(s.charAt(22));
                  	  							      if (userdata1 != 32) {
                  	  							    	  userdata.append(s.charAt(22));
                  	  							      }
                  	  							      int userdata2 = Integer.valueOf(s.charAt(21));
                  	  							      if (userdata2 != 32) {
                  	  							    	  userdata.append(s.charAt(21));
                  	  							      }
                  	  							      int userdata3 = Integer.valueOf(s.charAt(20));
                  	  							      if (userdata3 != 32) {
                  	  							    	  userdata.append(s.charAt(20));
                  	  							      }
                  	  							      int userdata4 = Integer.valueOf(s.charAt(19));
                  	  							      if (userdata4 != 32) {
                  	  							    	  userdata.append(s.charAt(19));
                  	  							      }
                  	  							      int userdata5 = Integer.valueOf(s.charAt(18));
                  	  							      if (userdata5 != 32) {
                  	  							    	  userdata.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; userdata.length() - counter > -1; counter++) {
                  	  							    	  size.append(userdata.charAt(userdata.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("userdata", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("userdata_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("7")) {
                  	  						    	  buffer.append("\nOEM: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      oem.append(s.charAt(23));
                  	  							      int oem1 = Integer.valueOf(s.charAt(22));
                  	  							      if (oem1 != 32) {
                  	  							    	  oem.append(s.charAt(22));
                  	  							      }
                  	  							      int oem2 = Integer.valueOf(s.charAt(21));
                  	  							      if (oem2 != 32) {
                  	  							    	  oem.append(s.charAt(21));
                  	  							      }
                  	  							      int oem3 = Integer.valueOf(s.charAt(20));
                  	  							      if (oem3 != 32) {
                  	  							    	  oem.append(s.charAt(20));
                  	  							      }
                  	  							      int oem4 = Integer.valueOf(s.charAt(19));
                  	  							      if (oem4 != 32) {
                  	  							    	  oem.append(s.charAt(19));
                  	  							      }
                  	  							      int oem5 = Integer.valueOf(s.charAt(18));
                  	  							      if (oem5 != 32) {
                  	  							    	  oem.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; oem.length() - counter > -1; counter++) {
                  	  							    	  size.append(oem.charAt(oem.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("oem", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("oem_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					    	  if (c.equals("8")) {
                  	  						    	  buffer.append("\nPersist: ");
                  	  						          buffer.append(s.charAt(18));
                  	  							      buffer.append(s.charAt(19));
                  	  							      buffer.append(s.charAt(20));
                  	  							      buffer.append(s.charAt(21));
                  	  							      buffer.append(s.charAt(22));
                  	  							      buffer.append(s.charAt(23));
                  	  							      persist.append(s.charAt(23));
                  	  							      int persist1 = Integer.valueOf(s.charAt(22));
                  	  							      if (persist1 != 32) {
                  	  							    	  persist.append(s.charAt(22));
                  	  							      }
                  	  							      int persist2 = Integer.valueOf(s.charAt(21));
                  	  							      if (persist2 != 32) {
                  	  							    	  persist.append(s.charAt(21));
                  	  							      }
                  	  							      int persist3 = Integer.valueOf(s.charAt(20));
                  	  							      if (persist3 != 32) {
                  	  							    	  persist.append(s.charAt(20));
                  	  							      }
                  	  							      int persist4 = Integer.valueOf(s.charAt(19));
                  	  							      if (persist4 != 32) {
                  	  							    	  persist.append(s.charAt(19));
                  	  							      }
                  	  							      int persist5 = Integer.valueOf(s.charAt(18));
                  	  							      if (persist5 != 32) {
                  	  							    	  persist.append(s.charAt(18));
                  	  							      }
                  	  							      StringBuffer size = new StringBuffer();
                  	  							      for (int counter = 1; persist.length() - counter > -1; counter++) {
                  	  							    	  size.append(persist.charAt(persist.length() - counter));
                  	  							    	  Editor edit2 = preferences.edit();
                  	  							    	  edit2.putInt("persist", Integer.valueOf(size.toString()));
                  	  							    	  edit2.commit();
                  	  							      }
                  	  							      Editor edit3 = preferences.edit();
                  	  							      edit3.putBoolean("persist_done", true);
                  	  							      edit3.commit();
                  	  						      }
                  	  					      }
                  	  			    	  }
                  	  			      }
                  	  			    }
                  	  		    }
                  	  		    int recoveryint = preferences.getInt("recovery", 0);
                  	  		    int bootint = preferences.getInt("boot", 0);
                  	  		    int splashint = preferences.getInt("splash", 0);
                  	  		    int miscint = preferences.getInt("misc", 0);
                  	  		    int cacheint = preferences.getInt("cache", 0);
                  	  		    int systemint = preferences.getInt("system", 0);
                  	  		    int userdataint = preferences.getInt("userdata", 0);
                  	  		    int oemint = preferences.getInt("oem", 0);
                  	  		    int persistint = preferences.getInt("persist", 0);
                  	      	    int total = ((recoveryint + bootint + splashint + miscint + cacheint + systemint + userdataint + oemint + persistint)/1024);
                  	      		if (total > 468) {
                  	      			if (lib.canRead()) {
                  	      				showDialog(GEN_2_TPT);
                  	        		    } else {
                  	        		    	showDialog(CHECK_GEN);
                  	        		    }
                  	      		} else {
                  	      			if (lib.canRead()) {
                  	      				showDialog(GEN_2_STOCK);
                  	        		    } else {
                  	        		    	showDialog(GEN_3);
                  	        		    }
                  	      		}
                  	  	    } catch (IOException e) {
                  	  	    	Log.i(TAG, "" + e);
                  	  	    } catch (Exception e) {
                  	  	    	Log.i(TAG, "" + e);
                  	  	    }
              		      } else {
              			      if (s1.charAt(2) == '9') {
              			    	  showDialog(GEN_1);
              		          }
              		      }
            	      }   
                    } catch (FileNotFoundException e) {
              	    	
                    } catch (IOException e) {

                    }
      	    		break;
      	    	case 1:
      	    		edit.putString("blade", "Chinese Blade");
        	        edit.commit();
        	        showDialog(UNSUPPORTED);
      	    		break;
      	    	case 2:
      	    		edit.putString("blade", "Unknown Blade");
        	        edit.commit();
        	        showDialog(UNSUPPORTED);
      	    		break;
      	    	}
      	      }
      	  });
          return manualtypebuilder.create();
        case UNKNOWN_TYPE:
      	    // tell the user that detected type is being set
          Builder unknowntypebuilder = new AlertDialog.Builder(HomeActivity.this);
          unknowntypebuilder.setTitle(R.string.setting_type_heading);
          final String unknowntype = preferences.getString("blade", "Unknown Blade");
          CharSequence setting_type = getText(R.string.setting_type);
          unknowntypebuilder.setMessage(setting_type + " " + unknowntype);
          unknowntypebuilder.setCancelable(false);
          unknowntypebuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  if (unknowntype.equals("European Blade")) {
            		  try {
                	      File iomem = new File("/proc/iomem");
                	      FileInputStream fis = new FileInputStream(iomem);
                	      InputStreamReader isr = new InputStreamReader(fis);
                	      BufferedReader br = new BufferedReader(isr);
                	      String s1 = br.readLine();
                          if (s1 != null) {
                    	      if (s1.charAt(2) == '5') {
                    	    	File lib = new File("/system/lib/libloc.so");
                    	    	StringBuffer buffer = new StringBuffer();
                    	  	    StringBuffer recovery = new StringBuffer();
                    	  	    StringBuffer boot = new StringBuffer();
                    	  	    StringBuffer splash = new StringBuffer();
                    	  	    StringBuffer misc = new StringBuffer();
                    	  	    StringBuffer cache = new StringBuffer();
                    	  	    StringBuffer system = new StringBuffer();
                    	  	    StringBuffer userdata = new StringBuffer();
                    	  	    StringBuffer oem = new StringBuffer();
                    	  	    StringBuffer persist = new StringBuffer();
                    	  		try {
                    	  	    	File partitions = new File("/proc/partitions");
                    	  	    	FileInputStream fis2 = new FileInputStream(partitions);
                    	  		    InputStreamReader isr2 = new InputStreamReader(fis2);
                    	  		    BufferedReader br2 = new BufferedReader(isr2);
                    	  		    for (int i = 1; i < 50; i++) {
                    	  			    String s = br2.readLine();
                    	  			    if (i > 2) {
                    	  			      if (s != null) {
                    	  			    	  String b = s.charAt(3) + "";
                    	  			    	  if (b.equals("1")) {
                    	  			    		  String a = s.charAt(2) + "";
                    	  			    		  if (a.equals("3")) {
                    	  			    			  String c = s.charAt(12) + "";
                    	  					    	  if (c.equals("0")) {
                    	  						    	  buffer.append("\nRecovery: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      recovery.append(s.charAt(23));
                    	  							      int recovery1 = Integer.valueOf(s.charAt(22));
                    	  							      if (recovery1 != 32) {
                    	  							    	  recovery.append(s.charAt(22));
                    	  							      }
                    	  							      int recovery2 = Integer.valueOf(s.charAt(21));
                    	  							      if (recovery2 != 32) {
                    	  							    	  recovery.append(s.charAt(21));
                    	  							      }
                    	  							      int recovery3 = Integer.valueOf(s.charAt(20));
                    	  							      if (recovery3 != 32) {
                    	  							    	  recovery.append(s.charAt(20));
                    	  							      }
                    	  							      int recovery4 = Integer.valueOf(s.charAt(19));
                    	  							      if (recovery4 != 32) {
                    	  							    	  recovery.append(s.charAt(19));
                    	  							      }
                    	  							      int recovery5 = Integer.valueOf(s.charAt(18));
                    	  							      if (recovery5 != 32) {
                    	  							    	  recovery.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; recovery.length() - counter > -1; counter++) {
                    	  							    	  size.append(recovery.charAt(recovery.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("recovery", Integer.parseInt(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("recovery_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("1")) {
                    	  						    	  buffer.append("\nBoot: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      boot.append(s.charAt(23));
                    	  							      int boot1 = Integer.valueOf(s.charAt(22));
                    	  							      if (boot1 != 32) {
                    	  							    	  boot.append(s.charAt(22));
                    	  							      }
                    	  							      int boot2 = Integer.valueOf(s.charAt(21));
                    	  							      if (boot2 != 32) {
                    	  							    	  boot.append(s.charAt(21));
                    	  							      }
                    	  							      int boot3 = Integer.valueOf(s.charAt(20));
                    	  							      if (boot3 != 32) {
                    	  							    	  boot.append(s.charAt(20));
                    	  							      }
                    	  							      int boot4 = Integer.valueOf(s.charAt(19));
                    	  							      if (boot4 != 32) {
                    	  							    	  boot.append(s.charAt(19));
                    	  							      }
                    	  							      int boot5 = Integer.valueOf(s.charAt(18));
                    	  							      if (boot5 != 32) {
                    	  							    	  boot.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; boot.length() - counter > -1; counter++) {
                    	  							    	  size.append(boot.charAt(boot.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("boot", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("boot_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("2")) {
                    	  						    	  buffer.append("\nSplash: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      splash.append(s.charAt(23));
                    	  							      int splash1 = Integer.valueOf(s.charAt(22));
                    	  							      if (splash1 != 32) {
                    	  							    	  splash.append(s.charAt(22));
                    	  							      }
                    	  							      int splash2 = Integer.valueOf(s.charAt(21));
                    	  							      if (splash2 != 32) {
                    	  							    	  splash.append(s.charAt(21));
                    	  							      }
                    	  							      int splash3 = Integer.valueOf(s.charAt(20));
                    	  							      if (splash3 != 32) {
                    	  							    	  splash.append(s.charAt(20));
                    	  							      }
                    	  							      int splash4 = Integer.valueOf(s.charAt(19));
                    	  							      if (splash4 != 32) {
                    	  							    	  splash.append(s.charAt(19));
                    	  							      }
                    	  							      int splash5 = Integer.valueOf(s.charAt(18));
                    	  							      if (splash5 != 32) {
                    	  							    	  splash.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; splash.length() - counter > -1; counter++) {
                    	  							    	  size.append(splash.charAt(splash.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("splash", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("splash_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("3")) {
                    	  						    	  buffer.append("\nMisc: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      misc.append(s.charAt(23));
                    	  							      int misc1 = Integer.valueOf(s.charAt(22));
                    	  							      if (misc1 != 32) {
                    	  							    	  misc.append(s.charAt(22));
                    	  							      }
                    	  							      int misc2 = Integer.valueOf(s.charAt(21));
                    	  							      if (misc2 != 32) {
                    	  							    	  misc.append(s.charAt(21));
                    	  							      }
                    	  							      int misc3 = Integer.valueOf(s.charAt(20));
                    	  							      if (misc3 != 32) {
                    	  							    	  misc.append(s.charAt(20));
                    	  							      }
                    	  							      int misc4 = Integer.valueOf(s.charAt(19));
                    	  							      if (misc4 != 32) {
                    	  							    	  misc.append(s.charAt(19));
                    	  							      }
                    	  							      int misc5 = Integer.valueOf(s.charAt(18));
                    	  							      if (misc5 != 32) {
                    	  							    	  misc.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; misc.length() - counter > -1; counter++) {
                    	  							    	  size.append(misc.charAt(misc.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("misc", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("misc_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("4")) {
                    	  						    	  buffer.append("\nCache: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      cache.append(s.charAt(23));
                    	  							      int cache1 = Integer.valueOf(s.charAt(22));
                    	  							      if (cache1 != 32) {
                    	  							    	  cache.append(s.charAt(22));
                    	  							      }
                    	  							      int cache2 = Integer.valueOf(s.charAt(21));
                    	  							      if (cache2 != 32) {
                    	  							    	  cache.append(s.charAt(21));
                    	  							      }
                    	  							      int cache3 = Integer.valueOf(s.charAt(20));
                    	  							      if (cache3 != 32) {
                    	  							    	  cache.append(s.charAt(20));
                    	  							      }
                    	  							      int cache4 = Integer.valueOf(s.charAt(19));
                    	  							      if (cache4 != 32) {
                    	  							    	  cache.append(s.charAt(19));
                    	  							      }
                    	  							      int cache5 = Integer.valueOf(s.charAt(18));
                    	  							      if (cache5 != 32) {
                    	  							    	  cache.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; cache.length() - counter > -1; counter++) {
                    	  							    	  size.append(cache.charAt(cache.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("cache", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("cache_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("5")) {
                    	  						    	  buffer.append("\nSystem: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      system.append(s.charAt(23));
                    	  							      int system1 = Integer.valueOf(s.charAt(22));
                    	  							      if (system1 != 32) {
                    	  							    	  system.append(s.charAt(22));
                    	  							      }
                    	  							      int system2 = Integer.valueOf(s.charAt(21));
                    	  							      if (system2 != 32) {
                    	  							    	  system.append(s.charAt(21));
                    	  							      }
                    	  							      int system3 = Integer.valueOf(s.charAt(20));
                    	  							      if (system3 != 32) {
                    	  							    	  system.append(s.charAt(20));
                    	  							      }
                    	  							      int system4 = Integer.valueOf(s.charAt(19));
                    	  							      if (system4 != 32) {
                    	  							    	  system.append(s.charAt(19));
                    	  							      }
                    	  							      int system5 = Integer.valueOf(s.charAt(18));
                    	  							      if (system5 != 32) {
                    	  							    	  system.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; system.length() - counter > -1; counter++) {
                    	  							    	  size.append(system.charAt(system.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("system", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("system_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("6")) {
                    	  						    	  buffer.append("\nUserdata: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      userdata.append(s.charAt(23));
                    	  							      int userdata1 = Integer.valueOf(s.charAt(22));
                    	  							      if (userdata1 != 32) {
                    	  							    	  userdata.append(s.charAt(22));
                    	  							      }
                    	  							      int userdata2 = Integer.valueOf(s.charAt(21));
                    	  							      if (userdata2 != 32) {
                    	  							    	  userdata.append(s.charAt(21));
                    	  							      }
                    	  							      int userdata3 = Integer.valueOf(s.charAt(20));
                    	  							      if (userdata3 != 32) {
                    	  							    	  userdata.append(s.charAt(20));
                    	  							      }
                    	  							      int userdata4 = Integer.valueOf(s.charAt(19));
                    	  							      if (userdata4 != 32) {
                    	  							    	  userdata.append(s.charAt(19));
                    	  							      }
                    	  							      int userdata5 = Integer.valueOf(s.charAt(18));
                    	  							      if (userdata5 != 32) {
                    	  							    	  userdata.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; userdata.length() - counter > -1; counter++) {
                    	  							    	  size.append(userdata.charAt(userdata.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("userdata", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("userdata_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("7")) {
                    	  						    	  buffer.append("\nOEM: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      oem.append(s.charAt(23));
                    	  							      int oem1 = Integer.valueOf(s.charAt(22));
                    	  							      if (oem1 != 32) {
                    	  							    	  oem.append(s.charAt(22));
                    	  							      }
                    	  							      int oem2 = Integer.valueOf(s.charAt(21));
                    	  							      if (oem2 != 32) {
                    	  							    	  oem.append(s.charAt(21));
                    	  							      }
                    	  							      int oem3 = Integer.valueOf(s.charAt(20));
                    	  							      if (oem3 != 32) {
                    	  							    	  oem.append(s.charAt(20));
                    	  							      }
                    	  							      int oem4 = Integer.valueOf(s.charAt(19));
                    	  							      if (oem4 != 32) {
                    	  							    	  oem.append(s.charAt(19));
                    	  							      }
                    	  							      int oem5 = Integer.valueOf(s.charAt(18));
                    	  							      if (oem5 != 32) {
                    	  							    	  oem.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; oem.length() - counter > -1; counter++) {
                    	  							    	  size.append(oem.charAt(oem.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("oem", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("oem_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					    	  if (c.equals("8")) {
                    	  						    	  buffer.append("\nPersist: ");
                    	  						          buffer.append(s.charAt(18));
                    	  							      buffer.append(s.charAt(19));
                    	  							      buffer.append(s.charAt(20));
                    	  							      buffer.append(s.charAt(21));
                    	  							      buffer.append(s.charAt(22));
                    	  							      buffer.append(s.charAt(23));
                    	  							      persist.append(s.charAt(23));
                    	  							      int persist1 = Integer.valueOf(s.charAt(22));
                    	  							      if (persist1 != 32) {
                    	  							    	  persist.append(s.charAt(22));
                    	  							      }
                    	  							      int persist2 = Integer.valueOf(s.charAt(21));
                    	  							      if (persist2 != 32) {
                    	  							    	  persist.append(s.charAt(21));
                    	  							      }
                    	  							      int persist3 = Integer.valueOf(s.charAt(20));
                    	  							      if (persist3 != 32) {
                    	  							    	  persist.append(s.charAt(20));
                    	  							      }
                    	  							      int persist4 = Integer.valueOf(s.charAt(19));
                    	  							      if (persist4 != 32) {
                    	  							    	  persist.append(s.charAt(19));
                    	  							      }
                    	  							      int persist5 = Integer.valueOf(s.charAt(18));
                    	  							      if (persist5 != 32) {
                    	  							    	  persist.append(s.charAt(18));
                    	  							      }
                    	  							      StringBuffer size = new StringBuffer();
                    	  							      for (int counter = 1; persist.length() - counter > -1; counter++) {
                    	  							    	  size.append(persist.charAt(persist.length() - counter));
                    	  							    	  Editor edit2 = preferences.edit();
                    	  							    	  edit2.putInt("persist", Integer.valueOf(size.toString()));
                    	  							    	  edit2.commit();
                    	  							      }
                    	  							      Editor edit3 = preferences.edit();
                    	  							      edit3.putBoolean("persist_done", true);
                    	  							      edit3.commit();
                    	  						      }
                    	  					      }
                    	  			    	  }
                    	  			      }
                    	  			    }
                    	  		    }
                    	  		    int recoveryint = preferences.getInt("recovery", 0);
                    	  		    int bootint = preferences.getInt("boot", 0);
                    	  		    int splashint = preferences.getInt("splash", 0);
                    	  		    int miscint = preferences.getInt("misc", 0);
                    	  		    int cacheint = preferences.getInt("cache", 0);
                    	  		    int systemint = preferences.getInt("system", 0);
                    	  		    int userdataint = preferences.getInt("userdata", 0);
                    	  		    int oemint = preferences.getInt("oem", 0);
                    	  		    int persistint = preferences.getInt("persist", 0);
                    	      	    int total = ((recoveryint + bootint + splashint + miscint + cacheint + systemint + userdataint + oemint + persistint)/1024);
                    	      		if (total > 468) {
                    	      			if (lib.canRead()) {
                    	      				showDialog(GEN_2_TPT);
                    	        		    } else {
                    	        		    	showDialog(CHECK_GEN);
                    	        		    }
                    	      		} else {
                    	      			if (lib.canRead()) {
                    	      				showDialog(GEN_2_STOCK);
                    	        		    } else {
                    	        		    	showDialog(GEN_3);
                    	        		    }
                    	      		}
                    	  	    } catch (IOException e) {
                    	  	    	Log.i(TAG, "" + e);
                    	  	    } catch (Exception e) {
                    	  	    	Log.i(TAG, "" + e);
                    	  	    }
                		      } else {
                			      if (s1.charAt(2) == '9') {
                			    	  showDialog(GEN_1);
                		          }
                		      }
              	      }   
                      } catch (FileNotFoundException e) {
                	    	
                      } catch (IOException e) {

                      }
            	  } else {
            		  showDialog(UNSUPPORTED);
            	  }
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
          Builder gen1builder = new AlertDialog.Builder(HomeActivity.this);
          gen1builder.setTitle(R.string.check_gen_heading);
          gen1builder.setMessage(R.string.confirm_gen1);
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
        case GEN_2_TPT:
            Builder gen2tptbuilder = new AlertDialog.Builder(HomeActivity.this);
            gen2tptbuilder.setTitle(R.string.check_gen_heading);
            gen2tptbuilder.setMessage(R.string.confirm_gen2_tpt);
            gen2tptbuilder.setCancelable(false);
            gen2tptbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	  Editor editgen = preferences.edit();
          	      Editor edit2 = preferences.edit();
          	      editgen.putInt("gen", 1);
          	      editgen.commit();
          	      edit2.putBoolean("allchecked", true);
          	      edit2.commit();
                }
            });
            gen2tptbuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
      	          showDialog(CHECK_GEN);
                }
            });
            return gen2tptbuilder.create();
        case GEN_2_STOCK:
            Builder gen2stockbuilder = new AlertDialog.Builder(HomeActivity.this);
            gen2stockbuilder.setTitle(R.string.check_gen_heading);
            gen2stockbuilder.setMessage(R.string.confirm_gen2_stock);
            gen2stockbuilder.setCancelable(false);
            gen2stockbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	  Editor editgen = preferences.edit();
          	      Editor edit2 = preferences.edit();
          	      editgen.putInt("gen", 2);
          	      editgen.commit();
          	      edit2.putBoolean("allchecked", true);
          	      edit2.commit();
                }
            });
            gen2stockbuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
      	          showDialog(CHECK_GEN);
                }
            });
            return gen2stockbuilder.create();
        case GEN_3:
            Builder gen3builder = new AlertDialog.Builder(HomeActivity.this);
            gen3builder.setTitle(R.string.check_gen_heading);
            gen3builder.setMessage(R.string.confirm_gen3);
            gen3builder.setCancelable(false);
            gen3builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	  Editor editgen = preferences.edit();
          	      Editor edit2 = preferences.edit();
          	      editgen.putInt("gen", 2);
          	      editgen.commit();
          	      edit2.putBoolean("allchecked", true);
          	      edit2.commit();
                }
            });
            gen3builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
      	          showDialog(CHECK_GEN);
                }
            });
            return gen3builder.create();
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
    		try {
    			Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tpthelper@amphoras.co.uk"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
                startActivity(emailIntent);
        		break;
    		} catch (ActivityNotFoundException e) {
    			Toast.makeText(HomeActivity.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
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
		case R.id.rate:
			Intent rate = new Intent(Intent.ACTION_VIEW);
    		rate.setData(Uri.parse("market://details?id=com.amphoras.tpthelper"));
    		startActivity(rate);
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