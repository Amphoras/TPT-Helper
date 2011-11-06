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
	  showDialog(CHECK_TYPE);
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
      String firststart = preferences.getString("firststart", "");
      if (firststart == "") {
    	  showDialog(SHOW_DISCLAIMER);
      }
	  
	  String[] options = getResources().getStringArray(R.array.options);
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.home_list_item, options));

	  ListView listview = getListView();
	  listview.setTextFilterEnabled(true);

	  listview.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	switch (position) {
		      case 0:
		    	  Intent i = new Intent(HomeActivity.this, AllInOne.class);
	        	  startActivity(i);
			      break;
		      case 1:
		    	  Intent j = new Intent(HomeActivity.this, DirectDownloader.class);
		          startActivity(j);
				  break;
		      case 2:
		    	  Intent k = new Intent(HomeActivity.this, PickFile.class);
	        	  startActivity(k);
				  break;
		      case 3:
		    	  Intent l = new Intent(HomeActivity.this, PickFileUnzip.class);
	        	  startActivity(l);
				  break;
		      case 4:
		    	  Intent m = new Intent(HomeActivity.this, VerifyImage.class);
	        	  startActivity(m);
				  break;
		      case 5:
		    	  showDialog(START_TPT);
				  break;
		      case 6:
		    	  Intent n = new Intent(HomeActivity.this, CustomTPT.class);
	        	  startActivity(n);
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
	    	switch (position) {
		      case 0:
		    	  Intent i = new Intent(HomeActivity.this, DirectDownloader.class);
	        	  startActivity(i);
			      break;
		      case 1:
		    	  Intent j = new Intent(HomeActivity.this, PickFile.class);
	        	  startActivity(j);
				  break;
		      case 2:
		    	  Intent k = new Intent(HomeActivity.this, PickFileUnzip.class);
	        	  startActivity(k);
				  break;
		      case 3:
		    	  Intent l = new Intent(HomeActivity.this, VerifyImage.class);
	        	  startActivity(l);
				  break;
		      case 4:
		    	  showDialog(POWER_OFF);
				  break;
		      case 5:
		    	  showDialog(DELETE_IMAGE);
				  break;
		      case 6:
		    	  Intent m = new Intent(HomeActivity.this, AllInOne.class);
	        	  startActivity(m);
				  break;
		      case 7:
		          showDialog(START_TPT);
				  break;
		      /* case 8:
		    	  
		    	  break; */
		    }
	    }
	  });
	}
    
    @Override
	protected Dialog onCreateDialog(int id) {
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
        		    edit.putString("firststart", "no");
        	        edit.commit();
                }
            });
            AlertDialog alert = builder.create();
            try {
                alert.show();
            } catch (IllegalArgumentException e) {
            	
  	        } catch (RuntimeException e) {
  	        	
  	        }
            break;
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
            		  Boolean xbin = preferences.getBoolean("xbin", true);
                	  try {
    					if (xbin == true) {
    						Runtime.getRuntime().exec(new String[]{"/system/xbin/su","-c","reboot -p"});
    					} else {
    						Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c","reboot -p"});
    					}
    				  } catch (IOException e1) {
    				  }
            	  }
              }
          });
          poweroffbuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  // Do nothing
              }
          });
          AlertDialog poweroffalert = poweroffbuilder.create();
          poweroffalert.show();
          break;
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
            AlertDialog deleteimagealert = deleteimagebuilder.create();
            deleteimagealert.show();
            break;
        case IMAGE_DELETED:
      	    // comfirm image folder is deleted
          Builder imagedeletedbuilder = new AlertDialog.Builder(HomeActivity.this);
          imagedeletedbuilder.setTitle(R.string.delete_image_heading);
          imagedeletedbuilder.setMessage(R.string.image_deleted);
          imagedeletedbuilder.setCancelable(false);
          imagedeletedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
      	          // Do nothing
              }
          });
          AlertDialog imagedeletedalert = imagedeletedbuilder.create();
          imagedeletedalert.show();
          break;
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
  				    } catch (IOException e1) {
  				    }
                }
            });
            lowbatbuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
        	          // Do nothing
                }
            });
            AlertDialog lowbatalert = lowbatbuilder.create();
            lowbatalert.show();
            break;
        case START_TPT:
      	    // warn before starting TPT
          Builder autotptbuilder = new AlertDialog.Builder(HomeActivity.this);
          autotptbuilder.setTitle(R.string.auto_tpt_heading);
          autotptbuilder.setMessage(R.string.auto_tpt);
          autotptbuilder.setCancelable(false);
          autotptbuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  Intent localIntent1 = new Intent();
		          ComponentName localComponentName = new ComponentName("com.android.settings", "com.android.settings.deviceinfo.SDRise");
		          localIntent1.setComponent(localComponentName);
		          try {
		        	  HomeActivity.this.startActivity(localIntent1);
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
          AlertDialog autotptalert = autotptbuilder.create();
          autotptalert.show();
          break;
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
          AlertDialog autotptfailalert = autotptfailbuilder.create();
          autotptfailalert.show();
          break;
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
          AlertDialog localealert = localebuilder.create();
          localealert.show();
          break;
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
          checktypebuilder.setTitle("Blade Type");
          checktypebuilder.setMessage(type + " detected. Is this correct?");
          checktypebuilder.setCancelable(false);
          checktypebuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
      	          Editor edit = preferences.edit();
      	          edit.putString("blade", bladetype);
      	          edit.commit();
              }
          });
          AlertDialog checktypealert = checktypebuilder.create();
          checktypealert.show();
          break;
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