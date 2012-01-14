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
import java.io.IOException;
import java.io.InputStreamReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class About extends Activity {
	private final int CHANGE_LOCALE = 1;
	SharedPreferences preferences;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelog);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			InputStreamReader in = new InputStreamReader((getAssets().open("About.txt")));
			BufferedReader br = new BufferedReader(in); 
	        String s;
	        StringBuffer buffer = new StringBuffer();
	        while((s = br.readLine()) != null) {
	            buffer.append(s);
	            buffer.append("\n");
	        }
	        TextView changelog = (TextView) findViewById(R.id.changelog);
	        changelog.setText(buffer);
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		Button ok = (Button) findViewById(R.id.changelog_button);
        ok.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		About.this.finish();
        	}
        });
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.changelog);
		try {
			InputStreamReader in = new InputStreamReader((getAssets().open("About.txt")));
			BufferedReader br = new BufferedReader(in); 
	        String s;
	        StringBuffer buffer = new StringBuffer();
	        while((s = br.readLine()) != null) {
	            buffer.append(s);
	            buffer.append("\n");
	        }
	        TextView changelog = (TextView) findViewById(R.id.changelog);
	        changelog.setText(buffer);
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		Button ok = (Button) findViewById(R.id.changelog_button);
        ok.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		About.this.finish();
        	}
        });
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
    			Toast.makeText(About.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
		/* case R.id.troubleshooting:
			Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
			startActivity(j);
			break; */
		case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
		case R.id.about:
			Intent j = new Intent(About.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(About.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(About.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(About.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(About.this, License.class);
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
        switch (id) {
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(About.this);
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
      	    		Intent i = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    About.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    About.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    About.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    About.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    About.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    About.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    About.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    About.this.finish();
      	    		break;
      	    	case 8:
      	    		editlocale.putString("locale", "cs");
      	    		editlocale.commit();
      	    		Intent q = new Intent(About.this, HomeActivity.class);
      	    	    startActivity(q);
      	    	    About.this.finish();
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
}