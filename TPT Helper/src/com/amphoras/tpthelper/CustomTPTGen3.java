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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class CustomTPTGen3 extends Activity {
	SharedPreferences preferences;
    private final int PICK_SPLASH = 1;
    private final int PICK_RECOVERY = 2;
    private final int PICK_CACHE = 3;
    private final int PICK_SYSTEM = 4;
    private final int SET_USERDATA = 5;
    private final int PICK_ROM = 6;
    private final int UNZIP_FAILED = 7;
    private final int DOWNLOADING = 8;
    private final int DOWNLOAD_FAILED = 9;
    private final int FINISHED = 10;
    private final int MD5_MISMATCH = 11;
    private final int CHANGE_LOCALE = 12;
    private final int PICK_CACHE2 = 13;
    private final int PICK_SYSTEM2 = 14;
    private final int ZTEPACK_FAILED = 15;
    private final int IMAGEBIN_FAILED = 16;
    private String unziplocation = Environment.getExternalStorageDirectory() + "/";
    private String unziplocationfiles = Environment.getExternalStorageDirectory() + "/TPT Helper/Blade/Gen3/";
    private static ProgressDialog dialog;
    private static File dir = Environment.getExternalStorageDirectory();
    private static File basezip = new File(dir, "/TPT Helper/Blade/Gen3/Gen3-TPT-base.zip");
    private static File extrafiles = new File(dir, "/TPT Helper/Blade/Gen3/Gen3-tpt-files.zip");
    private final String TAG = "CustomTPTGen3";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.md5sum);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
  	    showDialog(PICK_SPLASH);
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.md5sum);
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
    			Toast.makeText(CustomTPTGen3.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
		/* case R.id.troubleshooting:
			Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
			startActivity(j);
			break; */
		case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
		case R.id.about:
			Intent j = new Intent(CustomTPTGen3.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(CustomTPTGen3.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(CustomTPTGen3.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(CustomTPTGen3.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(CustomTPTGen3.this, License.class);
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
		CharSequence cancel = getText(R.string.cancel);
        switch (id) {
        case PICK_SPLASH:
        	Builder builder1 = new AlertDialog.Builder(CustomTPTGen3.this);
            builder1.setTitle(R.string.pick_splash);
            builder1.setCancelable(false);
            final CharSequence[] zips1 = {"Normal Android", "Big Android", "CyanogenMod", "Unchanged", cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editsplash = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editsplash.putInt("splashpicked", 1);
        	    		editsplash.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 1:
        	    		editsplash.putInt("splashpicked", 2);
        	    		editsplash.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 2:
        	    		editsplash.putInt("splashpicked", 3);
        	    		editsplash.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 3:
        	    		editsplash.putInt("splashpicked", 4);
        	    		editsplash.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 4:
        	    		CustomTPTGen3.this.finish();
        	    		break;
        	    	}
        	    }
        	});
        	return builder1.create();
        case PICK_RECOVERY:
        	Builder builder3 = new AlertDialog.Builder(CustomTPTGen3.this);
            builder3.setTitle(R.string.pick_recovery);
            builder3.setCancelable(false);
            final CharSequence[] zips3 = {"ClockworkMod v5.0.2.0", "ClockWorkMod v3.0.2.4", "PhoTom v4.0.1.5", cancel};
        	builder3.setItems(zips3, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editrecovery = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editrecovery.putInt("recoverypicked", 1);
        	    		editrecovery.commit();
        	    		showDialog(PICK_CACHE);
        	    		break;
        	    	case 1:
        	    		editrecovery.putInt("recoverypicked", 2);
        	    		editrecovery.commit();
        	    		showDialog(PICK_CACHE);
        	    		break;
        	    	case 2:
        	    		editrecovery.putInt("recoverypicked", 3);
        	    		editrecovery.commit();
        	    		showDialog(PICK_CACHE);
        	    		break;
        	    	case 3:
        	    		CustomTPTGen3.this.finish();
        	    		break;
        	    	}
        	    }
        	});
        	return builder3.create();
        case PICK_CACHE:
        	Builder enter_cache = new AlertDialog.Builder(CustomTPTGen3.this);
            enter_cache.setTitle(R.string.pick_cache_heading);
            enter_cache.setMessage(R.string.pick_cache);
            final EditText edit_cache = new EditText(this);
            enter_cache.setView(edit_cache);
            enter_cache.setCancelable(false);
            enter_cache.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
            	    try {
            	        int cache = Integer.valueOf(edit_cache.getText().toString());
            	        if (cache < 2) {
            	        	showDialog(PICK_CACHE2);
            	        } else {
            		        if (cache > 60) {
            		        	showDialog(PICK_CACHE2);
            		        } else {
            			        Editor set_cache = preferences.edit();
            			        set_cache.putInt("cache", cache);
            			        set_cache.commit();
            			        showDialog(PICK_SYSTEM);
            		        }
            	        }
            	    } catch (Exception e) {
            	    	showDialog(PICK_CACHE2);
            	    }
                }
            });
            enter_cache.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	CustomTPTGen3.this.finish();
                }
            });
            return enter_cache.create();
        case PICK_SYSTEM:
            Builder enter_system = new AlertDialog.Builder(CustomTPTGen3.this);
            enter_system.setTitle(R.string.pick_system_heading);
            enter_system.setMessage(R.string.pick_system);
            final EditText edit_system = new EditText(this);
            enter_system.setView(edit_system);
            enter_system.setCancelable(false);
            enter_system.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	  try {
              	      int system = Integer.valueOf(edit_system.getText().toString());
              	      if (system < 90) {
              	    	showDialog(PICK_SYSTEM2);
              	      } else {
              		      if (system > 260) {
              		    	showDialog(PICK_SYSTEM2);
              		      } else {
              			      Editor set_system = preferences.edit();
              			      set_system.putInt("system", system);
              			      set_system.commit();
              			      showDialog(SET_USERDATA);
              		      }
              	      }
              	  } catch (Exception e) {
              		  showDialog(PICK_SYSTEM2);
              	  }
                }
            });
            enter_system.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	CustomTPTGen3.this.finish();
                }
            });
            return enter_system.create();
        case SET_USERDATA:
            Builder enter_userdata = new AlertDialog.Builder(CustomTPTGen3.this);
            enter_userdata.setTitle(R.string.set_userdata_heading);
            CharSequence set_userdata1 = getText(R.string.set_userdata1);
            CharSequence set_userdata2 = getText(R.string.set_userdata2);
            CharSequence set_userdata3 = getText(R.string.set_userdata3);
            CharSequence set_userdata4 = getText(R.string.set_userdata4);
            int cache = preferences.getInt("cache", 2);
            int system = preferences.getInt("system", 160);
            int userdata = (441 - system - cache);
            enter_userdata.setMessage(set_userdata1 + " " + cache + set_userdata2 + " " + system + set_userdata3 + " " + userdata + set_userdata4);
            enter_userdata.setCancelable(false);
            enter_userdata.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	int cache = preferences.getInt("cache", 2);
                	int system = preferences.getInt("system", 160);
                	int userdata = (441 - system - cache);
                	Editor edit = preferences.edit();
                	edit.putInt("userdata", userdata);
                	edit.commit();
                	buildTPT();
                }
            });
            enter_userdata.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	removeDialog(SET_USERDATA);
              	    showDialog(PICK_CACHE);
                }
            });
            return enter_userdata.create();
        case PICK_ROM:
        	Builder builder4 = new AlertDialog.Builder(CustomTPTGen3.this);
            builder4.setTitle(R.string.pickmd5);
            builder4.setCancelable(false);
            final CharSequence[] zips4 = {"No rom", "CyanogenMod 7.1 RC1", "GSF", "MMHMP", cancel};
        	builder4.setItems(zips4, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editsystem = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editsystem.putInt("rompicked", 1);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 1:
        	    		editsystem.putInt("rompicked", 2);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 2:
        	    		editsystem.putInt("rompicked", 3);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 3:
        	    		editsystem.putInt("rompicked", 4);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 4:
        	    		CustomTPTGen3.this.finish();
        	    		break;
        	    	}
        	    }
        	});
        	return builder4.create();
        case UNZIP_FAILED:
            Builder failedbuilder = new AlertDialog.Builder(CustomTPTGen3.this);
            failedbuilder.setTitle(R.string.unzip);
              // show dialog about failure
            failedbuilder.setMessage(R.string.unzipbad);
            failedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
          	        CustomTPTGen3.this.finish();
                }
            });
            return failedbuilder.create();
        case DOWNLOADING:
            dialog = new ProgressDialog(CustomTPTGen3.this);
            CharSequence downloadmessage = getText(R.string.downloading);
            dialog.setMessage(downloadmessage);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            return dialog;
        case DOWNLOAD_FAILED:
        	Builder downloadfailedbuilder = new AlertDialog.Builder(CustomTPTGen3.this);
        	downloadfailedbuilder.setTitle(R.string.download_failed_heading);
            CharSequence download_failed = getText(R.string.download_failed);
            String download_file = preferences.getString("download_failed", "Gen3-TPT-base.zip");
            downloadfailedbuilder.setMessage(download_failed + " " + download_file);
            downloadfailedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	CustomTPTGen3.this.finish();
                }
            });
            return downloadfailedbuilder.create();
        case FINISHED:
        	Builder finishedbuilder = new AlertDialog.Builder(CustomTPTGen3.this);
        	finishedbuilder.setTitle(R.string.finished_heading);
            CharSequence finished = getText(R.string.finished);
            finishedbuilder.setMessage(finished);
            finishedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	CustomTPTGen3.this.finish();
                }
            });
            return finishedbuilder.create();
        case MD5_MISMATCH:
        	Builder md5builder = new AlertDialog.Builder(CustomTPTGen3.this);
        	md5builder.setTitle(R.string.md5_mismatch_heading);
            CharSequence md5 = getText(R.string.md5_mismatch);
            md5builder.setMessage(md5);
            md5builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return md5builder.create();
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(CustomTPTGen3.this);
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
          final CharSequence[] locales = {english, french, german, russian, chinese, portuguese, spanish, serbian, czech, polish, cancel};
      	  localebuilder.setItems(locales, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor editlocale = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		editlocale.putString("locale", "en");
      	    		editlocale.commit();
      	    		Intent i = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 8:
      	    		editlocale.putString("locale", "cs");
      	    		editlocale.commit();
      	    		Intent q = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(q);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 9:
      	    		editlocale.putString("locale", "pl");
      	    		editlocale.commit();
      	    		Intent r = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(r);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 10:
      	    		editlocale.putString("locale", "hu");
      	    		editlocale.commit();
      	    		Intent s = new Intent(CustomTPTGen3.this, HomeActivity.class);
      	    	    startActivity(s);
      	    	    CustomTPTGen3.this.finish();
      	    		break;
      	    	case 11:
      	    		// Do nothing
      	    		break;
      	    	}
      	      }
      	  });
      	  return localebuilder.create();
        case PICK_CACHE2:
        	Builder enter_cache2 = new AlertDialog.Builder(CustomTPTGen3.this);
            enter_cache2.setTitle(R.string.pick_cache_heading);
            enter_cache2.setMessage(R.string.pick_cache);
            final EditText edit_cache2 = new EditText(this);
            enter_cache2.setView(edit_cache2);
            enter_cache2.setCancelable(false);
            enter_cache2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
            	    try {
            	        int cache = Integer.valueOf(edit_cache2.getText().toString());
            	        if (cache < 2) {
            	        	showDialog(PICK_CACHE);
            	        } else {
            		        if (cache > 60) {
            		        	showDialog(PICK_CACHE);
            		        } else {
            			        Editor set_cache = preferences.edit();
            			        set_cache.putInt("cache", cache);
            			        set_cache.commit();
            			        showDialog(PICK_SYSTEM);
            		        }
            	        }
            	    } catch (Exception e) {
            	    	showDialog(PICK_CACHE);
            	    }
                }
            });
            enter_cache2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	CustomTPTGen3.this.finish();
                }
            });
            return enter_cache2.create();
        case PICK_SYSTEM2:
            Builder enter_system2 = new AlertDialog.Builder(CustomTPTGen3.this);
            enter_system2.setTitle(R.string.pick_system_heading);
            enter_system2.setMessage(R.string.pick_system);
            final EditText edit_system2 = new EditText(this);
            enter_system2.setView(edit_system2);
            enter_system2.setCancelable(false);
            enter_system2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	  try {
              	      int system = Integer.valueOf(edit_system2.getText().toString());
              	      if (system < 90) {
              	    	showDialog(PICK_SYSTEM);
              	      } else {
              		      if (system > 260) {
              		    	showDialog(PICK_SYSTEM);
              		      } else {
              			      Editor set_system = preferences.edit();
              			      set_system.putInt("system", system);
              			      set_system.commit();
              			      showDialog(SET_USERDATA);
              		      }
              	      }
              	  } catch (Exception e) {
              		  showDialog(PICK_SYSTEM);
              	  }
                }
            });
            enter_system2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	CustomTPTGen3.this.finish();
                }
            });
            return enter_system2.create();
        case ZTEPACK_FAILED:
        	Builder ztepackbuilder = new AlertDialog.Builder(CustomTPTGen3.this);
        	ztepackbuilder.setTitle(R.string.error);
            ztepackbuilder.setMessage(R.string.ztepack_failed);
            ztepackbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return ztepackbuilder.create();
        case IMAGEBIN_FAILED:
        	Builder imagebinbuilder = new AlertDialog.Builder(CustomTPTGen3.this);
        	imagebinbuilder.setTitle(R.string.error);
            imagebinbuilder.setMessage(R.string.image_bin_failed);
            imagebinbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return imagebinbuilder.create();
        }
        return super.onCreateDialog(id);
    }
	
	private void buildTPT() {
		if (basezip.canRead() == true) {
			if (extrafiles.canRead() == true) {
				unzip();
			} else {
				DownloadFile2();
			}
		} else {
			DownloadFile();
		}
	}
	
	public void DownloadFile() {
		DownloadFileTask task = new DownloadFileTask();
		task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Blade/Gen%203/Gen3-TPT-base.zip" });
	}
	
	private class DownloadFileTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
            showDialog(DOWNLOADING);
		}
		
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String urlstring : urls) {
				try {
					  // set the download URL
			        URL url = new URL(urlstring);
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        connection.connect();
			          // set where to save file
			        File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Blade/Gen3/Gen3-TPT-base.zip");
			          // check the directory exists and create it if it doen't
			        MakeDirectory("TPT Helper/Blade/Gen3");
			        FileOutputStream fos = new FileOutputStream(file);
			        InputStream is = connection.getInputStream();
			          // get total size of file
			        int total = connection.getContentLength();
			        int downloaded = 0;
			        byte[] buffer = new byte[1024];
			        int length = 0;
			          // read buffer and write to file
			        while ((length = is.read(buffer)) > 0 ) {
			            fos.write(buffer, 0, length);
			              // update amount downloaded then show progress
			            downloaded += length;
			            publishProgress(""+(int)((downloaded*100)/total));
			        }
			        fos.close();
			        response = "Download Completed";
			    } catch (MalformedURLException e) {
			        e.printStackTrace();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			return response;
		}

		protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
       }
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (result.equals("Download Completed")){
				if (extrafiles.canRead() == true) {
					unzip();
				} else {
					DownloadFile2();
				}
			} else {
				Editor edit = preferences.edit();
				edit.putString("download_failed", "Gen3-TPT-base.zip");
				edit.commit();
				File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Blade/Gen3/Gen3-TPT-base.zip");
				deleteDirectory(file);
				showDialog(DOWNLOAD_FAILED);
			}
		}
	}
	
	public void DownloadFile2() {
		DownloadFileTask2 task = new DownloadFileTask2();
		task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Blade/Gen%203/Gen3-tpt-files.zip" });
	}
	
	private class DownloadFileTask2 extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
            showDialog(DOWNLOADING);
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String urlstring : urls) {
				try {
					  // set the download URL
			        URL url = new URL(urlstring);
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        connection.connect();
			          // set where to save file
			        File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Blade/Gen3/Gen3-tpt-files.zip");
			          // check the directory exists and create it if it doen't
			        MakeDirectory("TPT Helper/Blade/Gen3");
			        FileOutputStream fos = new FileOutputStream(file);
			        InputStream is = connection.getInputStream();
			          // get total size of file
			        int total = connection.getContentLength();
			        int downloaded = 0;
			        byte[] buffer = new byte[1024];
			        int length = 0;
			          // read buffer and write to file
			        while ((length = is.read(buffer)) > 0 ) {
			            fos.write(buffer, 0, length);
			              // update amount downloaded then show progress
			            downloaded += length;
			            publishProgress(""+(int)((downloaded*100)/total));
			        }
			        fos.close();
			        response = "Download Completed";
			    } catch (MalformedURLException e) {
			        e.printStackTrace();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			return response;
		}

		protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
       }

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (result.equals("Download Completed")){
				unzip();
			} else {
				Editor edit = preferences.edit();
				edit.putString("download_failed", "Gen3-tpt-files.zip");
				edit.commit();
				File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Blade/Gen3/Gen3-tpt-files.zip");
				deleteDirectory(file);
				showDialog(DOWNLOAD_FAILED);
			}
		}
	}

	public void unzip() {
		UnzipTask task = new UnzipTask();
		try {
			String zipname = "/TPT Helper/Blade/Gen3/Gen3-TPT-base.zip";
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + zipname);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class UnzipTask extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence unzipping = getText(R.string.unzipping);
			dialog = ProgressDialog.show(CustomTPTGen3.this, "", unzipping, true);
		}
		
		@Override
		protected String doInBackground(FileInputStream... fins) {
			String response = "";
			for (FileInputStream fin : fins) {
				  // delete the image directory if it already exists
				File file = new File(Environment.getExternalStorageDirectory(), "/image");
				deleteDirectory(file);
				try  {
			    	// sets chosen zip as input
			      ZipInputStream zin = new ZipInputStream(fin);
			      ZipEntry ze = null;
			        // as long as there is another entry
			      while ((ze = zin.getNextEntry()) != null) {
			    	  // check if the next entry is a directory
			        if(ze.isDirectory()) {
			        	  // if so, create it if it doesn't exist
			            MakeDirectory(ze.getName());
			        } else {
			        	// open output to location
			          FileOutputStream fos = new FileOutputStream(unziplocation + ze.getName());
			          byte[] buffer = new byte[1024];
			          int length;
			            // read zip input to buffer
			          while ((length = zin.read(buffer))>0) {
			        	  // while buffer has data in it, write the data to the location
			        	fos.write(buffer, 0, length);
			          }
			            // close input/ouput streams when finished
			          zin.closeEntry();
			          fos.close();
			        }
			      }
			      zin.close();
			        // if end reached without errors, return success
			      response = "Unzip completed";
			    } catch(Exception e) {
			        // if there was an error, return failure
			      response = "Unzip failed";
			    }
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (result.equals("Unzip completed")) {
				unzip2();
			} else {
				if (result.equals("Unzip failed")) {
					showDialog(UNZIP_FAILED);
				}
			}
		}
	}
	
	public void unzip2() {
		UnzipTask2 task = new UnzipTask2();
		try {
			String zipname = "/TPT Helper/Blade/Gen3/Gen3-tpt-files.zip";
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + zipname);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class UnzipTask2 extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence unzipping = getText(R.string.unzipping);
			dialog = ProgressDialog.show(CustomTPTGen3.this, "", unzipping, true);
		}

		@Override
		protected String doInBackground(FileInputStream... fins) {
			String response = "";
			for (FileInputStream fin : fins) {
				  // delete the files directory if it already exists
				File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Blade/Gen3/Gen3-tpt-files");
				deleteDirectory(file);
				try  {
			    	// sets chosen zip as input
			      ZipInputStream zin = new ZipInputStream(fin);
			      ZipEntry ze = null;
			        // as long as there is another entry
			      while ((ze = zin.getNextEntry()) != null) {
			    	  // check if the next entry is a directory
			        if(ze.isDirectory()) {
			        	  // if so, create it if it doesn't exist
			            MakeDirectory2(ze.getName());
			        } else {
			        	// open output to location
			          FileOutputStream fos = new FileOutputStream(unziplocationfiles + ze.getName());
			          byte[] buffer = new byte[1024];
			          int length;
			            // read zip input to buffer
			          while ((length = zin.read(buffer))>0) {
			        	  // while buffer has data in it, write the data to the location
			        	fos.write(buffer, 0, length);
			          }
			            // close input/ouput streams when finished
			          zin.closeEntry();
			          fos.close();
			        }
			      }
			      zin.close();
			        // if end reached without errors, return success
			      response = "Unzip completed";
			    } catch(Exception e) {
			        // if there was an error, return failure
			      response = "Unzip failed";
			    }
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (result.equals("Unzip completed")) {
				verifyimage();
			} else {
				if (result.equals("Unzip failed")) {
					showDialog(UNZIP_FAILED);
				}
			}
		}
	}
	
	public void verifyimage() {
		VerifyImageTask task = new VerifyImageTask();
		try {
			FileReader in = new FileReader(new File(dir, "image/nandroid.md5"));
			task.execute(new FileReader[] {in});
		} catch (FileNotFoundException e) {

		}
	}
	
	private class VerifyImageTask extends AsyncTask<FileReader, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence verifying = getText(R.string.verifying);
			dialog = ProgressDialog.show(CustomTPTGen3.this, "", verifying, true);
		}
		
		@Override
		protected String doInBackground(FileReader... frs) {
			String response = "";
			for (FileReader in : frs) {
				  // reset all file names and match checks
				Editor editfile1 = preferences.edit();
			    editfile1.putString("file1", "");
		        editfile1.commit();
		        Editor editfile2 = preferences.edit();
			    editfile2.putString("file2", "");
		        editfile2.commit();
		        Editor editfile3 = preferences.edit();
			    editfile3.putString("file3", "");
		        editfile3.commit();
		        Editor editfile4 = preferences.edit();
			    editfile4.putString("file4", "");
		        editfile4.commit();
		        Editor editfile5 = preferences.edit();
			    editfile5.putString("file5", "");
		        editfile5.commit();
		        Editor editfile6 = preferences.edit();
			    editfile6.putString("file6", "");
		        editfile6.commit();
		        Editor editfile7 = preferences.edit();
			    editfile7.putString("file7", "");
		        editfile7.commit();
		        Editor editfile8 = preferences.edit();
			    editfile8.putString("file8", "");
		        editfile8.commit();
		        Editor editfile9 = preferences.edit();
			    editfile9.putString("file9", "");
		        editfile9.commit();
		        Editor editfile10 = preferences.edit();
			    editfile10.putString("file10", "");
		        editfile10.commit();
		        Editor editfile11 = preferences.edit();
			    editfile11.putString("file11", "");
		        editfile11.commit();
		        Editor editfile12 = preferences.edit();
			    editfile12.putString("file12", "");
		        editfile12.commit();
		        Editor editfile13 = preferences.edit();
			    editfile13.putString("file13", "");
		        editfile13.commit();
		        Editor editfile14 = preferences.edit();
			    editfile14.putString("file14", "");
		        editfile14.commit();
		        Editor editfile15 = preferences.edit();
			    editfile15.putString("file15", "");
		        editfile15.commit();
		        Editor editfile16 = preferences.edit();
			    editfile16.putString("file16", "");
		        editfile16.commit();
		        Editor editfile17 = preferences.edit();
			    editfile17.putString("file17", "");
		        editfile17.commit();
		        Editor editfile18 = preferences.edit();
			    editfile18.putString("file18", "");
		        editfile18.commit();
		        Editor editmatch1 = preferences.edit();
			    editmatch1.putString("match1", "");
		        editmatch1.commit();
		        Editor editmatch2 = preferences.edit();
			    editmatch2.putString("match2", "");
		        editmatch2.commit();
		        Editor editmatch3 = preferences.edit();
			    editmatch3.putString("match3", "");
		        editmatch3.commit();
		        Editor editmatch4 = preferences.edit();
			    editmatch4.putString("match4", "");
		        editmatch4.commit();
		        Editor editmatch5 = preferences.edit();
			    editmatch5.putString("match5", "");
		        editmatch5.commit();
		        Editor editmatch6 = preferences.edit();
			    editmatch6.putString("match6", "");
		        editmatch6.commit();
		        Editor editmatch7 = preferences.edit();
			    editmatch7.putString("match7", "");
		        editmatch7.commit();
		        Editor editmatch8 = preferences.edit();
			    editmatch8.putString("match8", "");
		        editmatch8.commit();
		        Editor editmatch9 = preferences.edit();
			    editmatch9.putString("match9", "");
		        editmatch9.commit();
		        Editor editmatch10 = preferences.edit();
			    editmatch10.putString("match10", "");
		        editmatch10.commit();
		        Editor editmatch11 = preferences.edit();
			    editmatch11.putString("match11", "");
		        editmatch11.commit();
		        Editor editmatch12 = preferences.edit();
			    editmatch12.putString("match12", "");
		        editmatch12.commit();
		        Editor editmatch13 = preferences.edit();
			    editmatch13.putString("match13", "");
		        editmatch13.commit();
		        Editor editmatch14 = preferences.edit();
			    editmatch14.putString("match14", "");
		        editmatch14.commit();
		        Editor editmatch15 = preferences.edit();
			    editmatch15.putString("match15", "");
		        editmatch15.commit();
		        Editor editmatch16 = preferences.edit();
			    editmatch16.putString("match16", "");
		        editmatch16.commit();
		        Editor editmatch17 = preferences.edit();
			    editmatch17.putString("match17", "");
		        editmatch17.commit();
		        Editor editmatch18 = preferences.edit();
			    editmatch18.putString("match18", "");
		        editmatch18.commit();
				try {
					  // read from the file line by line
				    BufferedReader br = new BufferedReader(in); 
				    String s;
				    int a = 0;
				    while((s = br.readLine()) != null) { 
				      // for each line add 1 to a to get the total number of lines
				    a = a + 1;
				    int length = s.length();
				    StringBuffer buffer = new StringBuffer();
				      // take the first 32 characters of each line as the md5 sum
				    for (int i = 0; i <= 31; i++) {
						buffer.append(s.charAt(i));
					}
				    String md5 = buffer.toString();
				    StringBuffer buffer2 = new StringBuffer();
				      // Ignore the two spaces, then take the rest of the line as the file
				    for (int i = 34; i <= length -1 ; i++) {
						buffer2.append(s.charAt(i));
					}
				    String filename = buffer2.toString();
				    Editor edit = preferences.edit();
				      // store each file name as file1, file2, etc.
				    edit.putString("file" + a, filename);
			        edit.commit();
			        Editor edit2 = preferences.edit();
			          // store each md5 sum as md51, md52, etc.
				    edit2.putString("md5" + a, md5);
			        edit2.commit();
				    }
				    in.close();
				    Editor edit = preferences.edit();
				      // store the number of lines as the total number of files
				    edit.putLong("no of files", a);
			        edit.commit();
			        Editor edit2 = preferences.edit();
			          // set number of matches to zero
				    edit2.putLong("no of matches", 0);
			        edit2.commit();
			        Editor edit3 = preferences.edit();
			          // set number of files checked to zero
				    edit3.putLong("no of files checked", 0);
			        edit3.commit();
			        int b = 0;
				    for (int i = 1; i <= a; i++) {
				      // set location of file in image folder
				    String location = preferences.getString("file" + i, "");
				    String loc = Environment.getExternalStorageDirectory() + "/image/" + location;
					File checkFile = new File(dir, "image/" + location);
					if (checkFile.canRead() == true){
						try  {
							  // add one to number of files checked
							b = b + 1;
							Editor edit4 = preferences.edit();
						    edit4.putLong("no of files checked", b);
					        edit4.commit();
				        	  // calculate the md5sum and see if it matches for each file
				        	FileInputStream fin = new FileInputStream(loc);
				            md5sum(fin, i);
					    } catch(Exception e) { 

					    }
					} else {
						Editor edit5 = preferences.edit();
					    edit5.putString("match" + i, "File not found");
				        edit5.commit();
					}
					}
				}
				catch (IOException e) {

				}
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			Long a = preferences.getLong("no of matches", 0);
			Long b = preferences.getLong("no of files checked", 0);
			if (a == b) {
				addFiles();
			} else {
				showDialog(MD5_MISMATCH);
			}
		}
	}
	
	public void md5sum(FileInputStream fin, int a) {
		  // call in the total number of files
		Long b = preferences.getLong("no of files", 0);
		  // calculate the md5sum of the file
		try {
			  // creates digester that will be used to calculate the md5sum
			MessageDigest digester = MessageDigest.getInstance("MD5");
			  // creates buffer to read from file
		    byte[] buffer = new byte[8192];
		    int i = 0;
		    try {
			    while ((i = fin.read(buffer)) > 0) {
			    	  // while the buffer is not empty, add the data to the digester
			        digester.update(buffer, 0, i);
			    }
		    } catch (IOException e) {
			    
		    }
		      // creates buffer holding md5sum output
		    byte[] output = digester.digest();
		    String md5sum = "";
            for (int j = 0; j < output.length; j++) {
          	      // adds each character to the string that will store the md5sum
                md5sum += Integer.toString((output[j] & 0xff) + 0x100, 16).substring(1);
            }
              // call in the expected md5sum
            String checksum = preferences.getString("md5" + a, "");
              // call in the number of matches so far
            Long c = preferences.getLong("no of matches", 0);
            if (checksum.equals(md5sum)) {
      	          // if the calculated md5sum matches the expected one, add 1 to the no. of matches
      	        c = c + 1;
      	        Editor edit = preferences.edit();
		        edit.putLong("no of matches", c);
	            edit.commit();
	            Editor edit2 = preferences.edit();
		        edit2.putString("match" + a, "Match");
	            edit2.commit();
            } else {
      	        Editor edit = preferences.edit();
		        edit.putString("match" + a, "No Match");
	            edit.commit();
            }
            if (a == b) {
      	          // if on the final file
        	
            }
		  } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
		  }
    }

	private void addFiles() {
		CopyTask task = new CopyTask();
		String sfin = new String("");
		task.execute(new String[] {sfin});
	}
	
	private class CopyTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence copying = getText(R.string.copying);
			dialog = ProgressDialog.show(CustomTPTGen3.this, "", copying, true);
		}
		
		@Override
		protected String doInBackground(String... fins) {
			String response = "";
			int splash = preferences.getInt("splashpicked", 1);
			int recovery = preferences.getInt("recoverypicked", 1);
			//int system = preferences.getInt("rompicked", 1);
			switch (splash) {
			case 1:
				CopyAssets("NormalAndroid.img", "splash.img");
				break;
			case 2:
				CopyAssets("BigAndroid.img", "splash.img");
				break;
			case 3:
				CopyAssets("CyanogenMod.img", "splash.img");
				break;
			case 4:
				  // Copy nothing
				break;
			}
			switch (recovery) {
			case 1:
				CopyAssets("CWMv5.img", "recovery.img");
				break;
			case 2:
				CopyAssets("CWMv3.img", "recovery.img");
				break;
			case 3:
				CopyAssets("PhoTom.img", "recovery.img");
				break;
			}
			//switch (system) {
			//case 1:
				CopyAssets("standard.img", "system.img");
				switch (recovery) {
				case 1:
					CopyAssets("CWMv5.img", "boot.img");
					break;
				case 2:
					CopyAssets("CWMv3.img", "boot.img");
					break;
				case 3:
					CopyAssets("PhoTom.img", "boot.img");
					break;
				}
				//break;
			/*case 2:
				CopyAssets("CM7.img", "system.img");
				CopyAssets("CM7boot.img", "boot.img");
				break;
			case 3:
				CopyAssets("GSFB24.img", "system.img");
				CopyAssets("GSFB24boot.img", "boot.img");
				break;
			case 4:
				CopyAssets("GSFB23.img", "system.img");
				CopyAssets("GSFB23boot.img", "boot.img");
				break;
			case 5:
				CopyAssets("GSFB19.img", "system.img");
				CopyAssets("GSFB19boot.img", "boot.img");
				break;
			}*/
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			checkOthers();
		}
	}
	
	private void CopyAssets(String filename, String newfilename) {
	    try {
	    	  // open the file from the assets folder
	    	InputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/TPT Helper/Blade/Gen3/Gen3-tpt-files/" + filename);
	          // set where to save it
		    OutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/image/" + newfilename);
	        byte[] buffer = new byte[1024];
		    int i;
		      // while you can still read to the buffer, write to the output
		    while((i = fis.read(buffer)) != -1){
		        fos.write(buffer, 0, i);
		    }
		      // closes the input/output
	        fis.close();
	        fos.close();
	    } catch(Exception e) {
	    	Log.i(TAG, "Error copying files: " + e);
	    }
	}
	
	private void MakeDirectory(String path) {
		String image = Environment.getExternalStorageDirectory() + "/";
	    File file = new File(image + path);
	      // if the location doesn't exist, create it
	    if(!file.isDirectory()) {
	        file.mkdirs();
	    }
	}
	
	private void MakeDirectory2(String path) {
		String image = Environment.getExternalStorageDirectory() + "/TPT Helper/Blade/Gen3/";
	    File file = new File(image + path);
	      // if the location doesn't exist, create it
	    if(!file.isDirectory()) {
	        file.mkdirs();
	    }
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
	
	public void checkOthers() {
		String splashmd5 = "";
		String recoverymd5 = "";
		String systemmd5 = "";
		String bootmd5 = "";
		boolean nosplash = false;
		int splash = preferences.getInt("splashpicked", 1);
		int recovery = preferences.getInt("recoverypicked", 1);
		//int system = preferences.getInt("rompicked", 1);
		switch (splash) {
		case 1:
			splashmd5 = "6517e43357b2ccb1e3c23ce1af67f1e5";
			break;
		case 2:
			splashmd5 = "0cfe844f4a4b84eef82c1767902b6618";
			break;
		case 3:
			splashmd5 = "83d8df20241cec8bbc7b4a6bef329284";
			break;
		case 4:
			nosplash = true;
			break;
		}
		switch (recovery) {
		case 1:
			recoverymd5 = "777287e4a51c90c30f8e952785ebd062";
			break;
		case 2:
			recoverymd5 = "320a0182b428dd879b9cdf23c1f214a8";
			break;
		case 3:
			recoverymd5 = "64b68f806e57771f478eed3cfe03b1af";
			break;
		}
		//switch (system) {
		//case 1:
		    systemmd5 = "3d0bfef87dae68e4888802e30926b318";
		    switch (recovery) {
			case 1:
				bootmd5 = "777287e4a51c90c30f8e952785ebd062";
				break;
			case 2:
				bootmd5 = "320a0182b428dd879b9cdf23c1f214a8";
				break;
			case 3:
				bootmd5 = "64b68f806e57771f478eed3cfe03b1af";
				break;
			}
			//break;
		/*case 2:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		case 3:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		case 4:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		case 5:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		}*/
		
		String recoveryfile = Environment.getExternalStorageDirectory() + "/image/recovery.img";
		String systemfile = Environment.getExternalStorageDirectory() + "/image/system.img";
		String bootfile = Environment.getExternalStorageDirectory() + "/image/boot.img";
		try {
		FileInputStream recoveryfin = new FileInputStream(recoveryfile);
		FileInputStream systemfin = new FileInputStream(systemfile);
		FileInputStream bootfin = new FileInputStream(bootfile);
		if (md5sum2(recoveryfin, recoverymd5) == true) {
			if (md5sum2(systemfin, systemmd5) == true) {
				if (md5sum2(bootfin, bootmd5) == true) {
					if (nosplash == false) {
						String splashfile = Environment.getExternalStorageDirectory() + "/image/splash.img";
						FileInputStream splashfin = new FileInputStream(splashfile);
						if (md5sum2(splashfin, splashmd5) == true) {
							WritePartitions();
						} else {
							showDialog(MD5_MISMATCH);
						}
					} else {
						WritePartitions();
					}
				} else {
					showDialog(MD5_MISMATCH);
				}
			} else {
				showDialog(MD5_MISMATCH);
			}
		} else {
			showDialog(MD5_MISMATCH);
		}
		} catch (IOException e) {
			Log.i(TAG, ""+e);
		}
	}
	
	public boolean md5sum2(FileInputStream fin, String checksum) {
		  // calculate the md5sum of the file
		try {
			  // creates digester that will be used to calculate the md5sum
			MessageDigest digester = MessageDigest.getInstance("MD5");
			  // creates buffer to read from file
		    byte[] buffer = new byte[8192];
		    int i = 0;
		    try {
			    while ((i = fin.read(buffer)) > 0) {
			    	  // while the buffer is not empty, add the data to the digester
			        digester.update(buffer, 0, i);
			    }
		    } catch (IOException e) {
			    
		    }
		      // creates buffer holding md5sum output
		    byte[] output = digester.digest();
		    String md5sum = "";
          for (int j = 0; j < output.length; j++) {
        	      // adds each character to the string that will store the md5sum
              md5sum += Integer.toString((output[j] & 0xff) + 0x100, 16).substring(1);
          }
          if (checksum.equals(md5sum)) {
  	          // if the calculated md5sum matches the expected one
  	        return true;
          }
		} catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
		}
		return false;
    }
	
	private void WritePartitions() {
		WritePartitionsTask task = new WritePartitionsTask();
		String sfin = new String("");
		task.execute(new String[] {sfin});
	}
	
	private class WritePartitionsTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence writing = getText(R.string.writing);
			dialog = ProgressDialog.show(CustomTPTGen3.this, "", writing, true);
		}
		
		@Override
		protected String doInBackground(String... fins) {
			String response = "";
			int cache = preferences.getInt("cache", 2);
	    	int system = preferences.getInt("system", 160);
	    	int userdata = preferences.getInt("userdata", 279);
	    	WritePartitionZTE(cache, system, userdata);
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			checkZTEPack();
		}
	}
	
	private void WritePartitionZTE(int cache_int, int system_int, int userdata_int) {
	    try {
	    	File partition_zte = new File(Environment.getExternalStorageDirectory() + "/image/partition_zte.mbn");
	    	byte[] data = new byte[280];
	    	FileInputStream fis = new FileInputStream(partition_zte);
	    	fis.read(data);
	    	int j = 0;
    		byte b = (byte) j;
	    	  // add the offset for cache
	    	int cache_offset_int = 555;
	    	  // add the size of cache
	    	int cache_size = 8*cache_int;
	    	String cache_hex = Integer.toHexString(cache_size);
	    	while(cache_hex.length() < 4) {
	    		cache_hex = "0"+cache_hex;
	    	}
	    	String cache_str1 = cache_hex.substring(2, 4);
	    	j = Integer.parseInt(cache_str1, 16);
	    	b = (byte) j;
	    	data[132] = b;
	    	String cache_str2 = cache_hex.substring(0, 2);
	    	j = Integer.parseInt(cache_str2, 16);
	    	b = (byte) j;
	    	data[133] = b;
	    	  // add the offset for system
	    	int system_offset_int = cache_size+cache_offset_int;
	    	String system_offset_hex = Integer.toHexString(system_offset_int);
	    	while(system_offset_hex.length() < 4) {
	    		system_offset_hex = "0"+system_offset_hex;
	    	}
	    	String system_offset_str1 = system_offset_hex.substring(2, 4);
	    	j = Integer.parseInt(system_offset_str1, 16);
	    	b = (byte) j;
	    	data[156] = b;
	    	String system_offset_str2 = system_offset_hex.substring(0, 2);
	    	j = Integer.parseInt(system_offset_str2, 16);
	    	b = (byte) j;
	    	data[157] = b;
	    	  // add the size of system
	    	int system_size = 8*system_int;
	    	String system_hex = Integer.toHexString(system_size);
	    	while(system_hex.length() < 4) {
	    		system_hex = "0"+system_hex;
	    	}
	    	String system_str1 = system_hex.substring(2, 4);
	    	j = Integer.parseInt(system_str1, 16);
	    	b = (byte) j;
	    	data[160] = b;
	    	String system_str2 = system_hex.substring(0, 2);
	    	j = Integer.parseInt(system_str2, 16);
	    	b = (byte) j;
	    	data[161] = b;
	    	  // add the offset for userdata
	    	int userdata_offset_int = system_size+system_offset_int;
	    	String userdata_offset_hex = Integer.toHexString(userdata_offset_int);
	    	while(userdata_offset_hex.length() < 4) {
	    		userdata_offset_hex = "0"+userdata_offset_hex;
	    	}
	    	String userdata_offset_str1 = userdata_offset_hex.substring(2, 4);
	    	j = Integer.parseInt(userdata_offset_str1, 16);
	    	b = (byte) j;
	    	data[184] = b;
	    	String userdata_offset_str2 = userdata_offset_hex.substring(0, 2);
	    	j = Integer.parseInt(userdata_offset_str2, 16);
	    	b = (byte) j;
	    	data[185] = b;
	    	  // add the size of userdata
	    	int userdata_size = 8*userdata_int;
	    	String userdata_hex = Integer.toHexString(userdata_size);
	    	while(userdata_hex.length() < 4) {
	    		userdata_hex = "0"+userdata_hex;
	    	}
	    	String userdata_str1 = userdata_hex.substring(2, 4);
	    	j = Integer.parseInt(userdata_str1, 16);
	    	b = (byte) j;
	    	data[188] = b;
	    	String userdata_str2 = userdata_hex.substring(0, 2);
	    	j = Integer.parseInt(userdata_str2, 16);
	    	b = (byte) j;
	    	data[189] = b;
	    	  // add the offset for oem
	    	int oem_offset_int = userdata_size+userdata_offset_int;
	    	String oem_offset_hex = Integer.toHexString(oem_offset_int);
	    	while(oem_offset_hex.length() < 4) {
	    		oem_offset_hex = "0"+oem_offset_hex;
	    	}
	    	String oem_offset_str1 = oem_offset_hex.substring(2, 4);
	    	j = Integer.parseInt(oem_offset_str1, 16);
	    	b = (byte) j;
	    	data[212] = b;
	    	String oem_offset_str2 = oem_offset_hex.substring(0, 2);
	    	j = Integer.parseInt(oem_offset_str2, 16);
	    	b = (byte) j;
	    	data[213] = b;
	    	
	    	FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/image/partition_zte.mbn");
	    	fos.write(data);
	    	fos.close();
	    	WriteAppsboot(cache_int, system_int, userdata_int);
	    } catch(Exception e) {
	    	Log.e(TAG, "PZTE fail: " + e);
	    }
	}
	
	private void WriteAppsboot(int cache_int, int system_int, int userdata_int) {
	    try {
	    	File appsboot = new File(Environment.getExternalStorageDirectory() + "/image/appsboot.mbn");
	    	byte[] data = new byte[51160];
	    	FileInputStream fis = new FileInputStream(appsboot);
	    	fis.read(data);
	    	int j = 0;
    		byte b = (byte) j;
	    	  // add the offset for cache
	    	int cache_offset_int = 556;
	    	  // add the size of cache
	    	int cache_size = 8*cache_int;
	    	String cache_hex = Integer.toHexString(cache_size);
	    	while(cache_hex.length() < 4) {
	    		cache_hex = "0"+cache_hex;
	    	}
	    	String cache_str1 = cache_hex.substring(2, 4);
	    	j = Integer.parseInt(cache_str1, 16);
	    	b = (byte) j;
	    	data[50688] = b;
	    	String cache_str2 = cache_hex.substring(0, 2);
	    	j = Integer.parseInt(cache_str2, 16);
	    	b = (byte) j;
	    	data[50689] = b;
	    	  // add the offset for system
	    	int system_offset_int = cache_size+cache_offset_int;
	    	String system_offset_hex = Integer.toHexString(system_offset_int);
	    	while(system_offset_hex.length() < 4) {
	    		system_offset_hex = "0"+system_offset_hex;
	    	}
	    	String system_offset_str1 = system_offset_hex.substring(2, 4);
	    	j = Integer.parseInt(system_offset_str1, 16);
	    	b = (byte) j;
	    	data[50716] = b;
	    	String system_offset_str2 = system_offset_hex.substring(0, 2);
	    	j = Integer.parseInt(system_offset_str2, 16);
	    	b = (byte) j;
	    	data[50717] = b;
	    	  // add the size of system
	    	int system_size = 8*system_int;
	    	String system_hex = Integer.toHexString(system_size);
	    	while(system_hex.length() < 4) {
	    		system_hex = "0"+system_hex;
	    	}
	    	String system_str1 = system_hex.substring(2, 4);
	    	j = Integer.parseInt(system_str1, 16);
	    	b = (byte) j;
	    	data[50720] = b;
	    	String system_str2 = system_hex.substring(0, 2);
	    	j = Integer.parseInt(system_str2, 16);
	    	b = (byte) j;
	    	data[50721] = b;
	    	  // add the offset for userdata
	    	int userdata_offset_int = system_size+system_offset_int;
	    	String userdata_offset_hex = Integer.toHexString(userdata_offset_int);
	    	while(userdata_offset_hex.length() < 4) {
	    		userdata_offset_hex = "0"+userdata_offset_hex;
	    	}
	    	String userdata_offset_str1 = userdata_offset_hex.substring(2, 4);
	    	j = Integer.parseInt(userdata_offset_str1, 16);
	    	b = (byte) j;
	    	data[50748] = b;
	    	String userdata_offset_str2 = userdata_offset_hex.substring(0, 2);
	    	j = Integer.parseInt(userdata_offset_str2, 16);
	    	b = (byte) j;
	    	data[50749] = b;
	    	  // add the size of userdata
	    	int userdata_size = 8*userdata_int;
	    	String userdata_hex = Integer.toHexString(userdata_size);
	    	while(userdata_hex.length() < 4) {
	    		userdata_hex = "0"+userdata_hex;
	    	}
	    	String userdata_str1 = userdata_hex.substring(2, 4);
	    	j = Integer.parseInt(userdata_str1, 16);
	    	b = (byte) j;
	    	data[50752] = b;
	    	String userdata_str2 = userdata_hex.substring(0, 2);
	    	j = Integer.parseInt(userdata_str2, 16);
	    	b = (byte) j;
	    	data[50753] = b;
	    	  // add the offset for oem
	    	int oem_offset_int = userdata_size+userdata_offset_int;
	    	String oem_offset_hex = Integer.toHexString(oem_offset_int);
	    	while(oem_offset_hex.length() < 4) {
	    		oem_offset_hex = "0"+oem_offset_hex;
	    	}
	    	String oem_offset_str1 = oem_offset_hex.substring(2, 4);
	    	j = Integer.parseInt(oem_offset_str1, 16);
	    	b = (byte) j;
	    	data[50780] = b;
	    	String oem_offset_str2 = oem_offset_hex.substring(0, 2);
	    	j = Integer.parseInt(oem_offset_str2, 16);
	    	b = (byte) j;
	    	data[50781] = b;
	    	
	    	FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/image/appsboot.mbn");
	    	fos.write(data);
	    	fos.close();
	    } catch(Exception e) {
	    	Log.e(TAG, "AB fail: " + e);
	    }
	}
	
	private void checkZTEPack() {
		File ztepack = new File("/system/bin/ztepack");
		if (ztepack.canRead()) {
			buildImage();
		} else {
			copyZTEPack();
		}
	}
	
	private void copyZTEPack() {
		try {
			byte[] buffer = new byte[12071];
			InputStream is = (getAssets().open("ztepack"));
			is.read(buffer);
			FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/ztepack");
	    	fos.write(buffer);
	    	fos.close();
		    Process copy_ztepack = Runtime.getRuntime().exec("su");
		    DataOutputStream dos = new DataOutputStream(copy_ztepack.getOutputStream());
		    dos.writeBytes("mount -o remount,rw /dev/mtdblock5 /system\n");
		    dos.writeBytes("mv /sdcard/ztepack /system/bin/ztepack\n");
		    dos.writeBytes("chmod 0777 /system/bin/ztepack\n");
		    dos.writeBytes("mount -o remount,ro /dev/mtdblock5 /system\n");
		    dos.writeBytes("exit\n");
		    copy_ztepack.waitFor();
		    File ztepack = new File("/system/bin/ztepack");
			if (ztepack.canRead()) {
				buildImage();
			} else {
				Log.i(TAG, "Failed to copy ztepack: Runtime completed but file not present");
				showDialog(ZTEPACK_FAILED);
			}
		} catch (Exception e) {
			Log.i(TAG, "Failed to copy ztepack: " + e);
			showDialog(ZTEPACK_FAILED);
		}
	}
	
	private void buildImage() {
		BuildImageTask task = new BuildImageTask();
		String sfin = new String("");
		task.execute(new String[] {sfin});
	}
	
	private class BuildImageTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence building = getText(R.string.building);
			dialog = ProgressDialog.show(CustomTPTGen3.this, "", building, true);
		}
		
		@Override
		protected String doInBackground(String... fins) {
			String response = "";
			try {
				File directory = new File(Environment.getExternalStorageDirectory() + "/image");
				String[] command = new String[]{"/system/bin/ztepack", "-p"};
			    Process build_image = Runtime.getRuntime().exec(command, null, directory);
			    build_image.waitFor();
			} catch (Exception e) {
				Log.i(TAG, "Failed to build image: " + e);
				showDialog(IMAGEBIN_FAILED);
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			showDialog(FINISHED);
		}
	}
}