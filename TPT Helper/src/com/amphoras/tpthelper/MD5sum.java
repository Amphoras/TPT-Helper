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
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Environment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MD5sum extends Activity {
	SharedPreferences preferences;
	private TextView textview;
	private static ProgressDialog dialog;
	private final int CHECK_FINISHED = 1;
	private final int CHANGE_LOCALE = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.md5sum);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		textview = (TextView) findViewById(R.id.md5result);
		md5sum();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.md5sum);
	}

	private class CheckMD5Task extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence calculating = getText(R.string.calculating);
			dialog = ProgressDialog.show(MD5sum.this, "", calculating, true);
		}
		
		@Override
		protected String doInBackground(FileInputStream... fins) {
			String response = "";
			for (FileInputStream fin : fins) {
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
		              // saves the md5sum to a preference
		            Editor edit = preferences.edit();
				    edit.putString("checksum", md5sum);
			        edit.commit();
			        response = md5sum;
				} catch (NoSuchAlgorithmException e) {
		            
				}
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			  // shows dialog and updates the text views 
			showDialog(CHECK_FINISHED);
			String filepath = preferences.getString("filepath", "");
			StringBuffer text = new StringBuffer();
			CharSequence md5of = getText(R.string.md5of);
			text.append(md5of + " ");
			text.append(filepath);
			text.append("\n\n");
			CharSequence md5is = getText(R.string.md5is);
			text.append(md5is + " ");
			text.append(result);
			String checksum = preferences.getString("checksum", "");
            String expectedmd5 = preferences.getString("expectedmd5", "");
			if (expectedmd5.equals(checksum)) {
				text.append("\n\n");
				CharSequence md5matches = getText(R.string.md5matches);
				text.append(md5matches);
			}
			textview.setText(text);
		}
	}

	public void md5sum() {
		CheckMD5Task task = new CheckMD5Task();
		try {
			String filepath = preferences.getString("filepath", "");
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + filepath);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case CHECK_FINISHED:
              // get md5sum of file
            String checksum = preferences.getString("checksum", "");
            String expectedmd5 = preferences.getString("expectedmd5", "");
            Builder md5builder = new AlertDialog.Builder(MD5sum.this);
            md5builder.setTitle(R.string.md5);
              // show dialog with md5sum and whether it matches or not
            if (expectedmd5.equals(checksum)) {
            	CharSequence md5is = getText(R.string.md5is);
            	CharSequence md5matches = getText(R.string.md5matches);
            	md5builder.setMessage(md5is + " " + checksum + ". " + md5matches);
            } else {
            	CharSequence md5is = getText(R.string.md5is);
            	md5builder.setMessage(md5is + " " + checksum);
            }
            md5builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
            	    // Do nothing
                }
            });
            return md5builder.create();
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(MD5sum.this);
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
          CharSequence cancel = getText(R.string.cancel);
          final CharSequence[] locales = {english, french, german, russian, chinese, portuguese, spanish, serbian, czech, cancel};
      	  localebuilder.setItems(locales, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor editlocale = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		editlocale.putString("locale", "en");
      	    		editlocale.commit();
      	    		Intent i = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 8:
      	    		editlocale.putString("locale", "cs");
      	    		editlocale.commit();
      	    		Intent q = new Intent(MD5sum.this, HomeActivity.class);
      	    	    startActivity(q);
      	    	    MD5sum.this.finish();
      	    		break;
      	    	case 9:
      	    		// Do nothing
      	    		break;
      	    	}
      	      }
      	  });
      	  return localebuilder.create();
        }
        return super.onCreateDialog(id);
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
    			Toast.makeText(MD5sum.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
		/* case R.id.troubleshooting:
			Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
			startActivity(j);
			break; */
		case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
		case R.id.about:
			Intent j = new Intent(MD5sum.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(MD5sum.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(MD5sum.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(MD5sum.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(MD5sum.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
}
