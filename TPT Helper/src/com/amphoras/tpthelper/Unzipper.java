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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzipper extends Activity {
	SharedPreferences preferences;
	private TextView textview;
	private static ProgressDialog dialog;
	private String unziplocation = Environment.getExternalStorageDirectory() + "/";
	private final int UNZIP_COMPLETED = 1;
	private final int UNZIP_FAILED = 2;
	private final int CHANGE_LOCALE = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unzip);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		textview = (TextView) findViewById(R.id.unzipdone);
		unzip();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.unzip);
	}

	private class UnzipTask extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence unzipping = getText(R.string.unzipping);
			dialog = ProgressDialog.show(Unzipper.this, "", unzipping, true);
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
			      CharSequence unzipgood = getText(R.string.unzipgood);
			      response = (String) unzipgood;
			    } catch(Exception e) {
			        // if there was an error, return failure
			      CharSequence unzipbad = getText(R.string.unzipbad);
			      response = (String) unzipbad;
			    }
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			  // show correct dialog for result
			if (result.equals("Unzip completed")) {
				showDialog(UNZIP_COMPLETED);
			} else {
				if (result.equals("Unzip failed")) {
					showDialog(UNZIP_FAILED);
				}
			}
			  // update text views with result etc.
			String zipname = preferences.getString("zipname", "");
			StringBuffer text = new StringBuffer();
			CharSequence unzip_file = getText(R.string.unzip_file);
			text.append(unzip_file + " ");
			text.append(zipname);
			text.append("\n\n");
			text.append(result);
			textview.setText(text);
		}
	}

	public void unzip() {
		UnzipTask task = new UnzipTask();
		try {
			String zipname = preferences.getString("zipname", "");
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + zipname);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case UNZIP_COMPLETED:
            Builder unzipbuilder = new AlertDialog.Builder(Unzipper.this);
            unzipbuilder.setTitle(R.string.unzip);
              // show dialog confirming unzip
            unzipbuilder.setMessage(R.string.unzipgood);
            unzipbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
            	    // Do nothing
                }
            });
            return unzipbuilder.create();
        case UNZIP_FAILED:
            Builder failedbuilder = new AlertDialog.Builder(Unzipper.this);
            failedbuilder.setTitle(R.string.unzip);
              // show dialog about failure
            failedbuilder.setMessage(R.string.unzipbad);
            failedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
          	        // Do nothing
                }
            });
            return failedbuilder.create();
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(Unzipper.this);
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
      	    		Intent i = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(Unzipper.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    Unzipper.this.finish();
      	    		break;
      	    	case 8:
      	    		// Do nothing
      	    		break;
      	    	}
      	      }
      	  });
      	  return localebuilder.create();
        }
        return super.onCreateDialog(id);
	}
	
	private void MakeDirectory(String path) {
		String image = Environment.getExternalStorageDirectory() + "/";
	    File file = new File(image + path);
	      // if the location doesn't exist, create it
	    if(!file.isDirectory()) {
	      file.mkdirs();
	    }
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
			Intent j = new Intent(Unzipper.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(Unzipper.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(Unzipper.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(Unzipper.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(Unzipper.this, License.class);
			startActivity(n);
			break;
		}
		return true;
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
}