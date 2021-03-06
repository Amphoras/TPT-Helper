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

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterFile extends Activity {
	SharedPreferences preferences;
	private final int FILE_UNFOUND = 1;
	private final int CHANGE_LOCALE = 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterfile);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button enterfile = (Button) findViewById(R.id.enterfile);
        enterfile.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		  // gets the name of the file from the input box
        		EditText fileentry = (EditText) findViewById(R.id.fileentry);
        		String filename = fileentry.getText().toString();
        		File zipfile = new File(Environment.getExternalStorageDirectory(), filename);
        		if (zipfile.canRead()) {
        			  // if the file exists, return that its ok
        			Intent resultIntent = new Intent();
                    resultIntent.putExtra("filename", filename);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
        		} else {
        			  // if it doesn't, show an error
        			Editor edit = preferences.edit();
  			        edit.putString("chosenfile", filename);
  		            edit.commit();
        			showDialog(FILE_UNFOUND);
        		}
        	}
        });
    }
    
    @Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case FILE_UNFOUND:
        	String filename = preferences.getString("chosenfile", "");
            Builder builder = new AlertDialog.Builder(EnterFile.this);
            builder.setTitle(R.string.error);
            CharSequence unfound1 = getText(R.string.unfound1);
            CharSequence unfound2 = getText(R.string.unfound2);
            builder.setMessage(unfound1 + "" + filename + "" + unfound2);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder.create();
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(EnterFile.this);
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
      	    		Intent i = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 8:
      	    		editlocale.putString("locale", "cs");
      	    		editlocale.commit();
      	    		Intent q = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(q);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 9:
      	    		editlocale.putString("locale", "pl");
      	    		editlocale.commit();
      	    		Intent r = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(r);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 10:
      	    		editlocale.putString("locale", "hu");
      	    		editlocale.commit();
      	    		Intent s = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(s);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 11:
      	    		editlocale.putString("locale", "sv");
      	    		editlocale.commit();
      	    		Intent t = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(t);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 12:
      	    		editlocale.putString("locale", "it");
      	    		editlocale.commit();
      	    		Intent u = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(u);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 13:
      	    		editlocale.putString("locale", "nl");
      	    		editlocale.commit();
      	    		Intent v = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(v);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 14:
      	    		editlocale.putString("locale", "pt_BR");
      	    		editlocale.commit();
      	    		Intent w = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(w);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 15:
      	    		editlocale.putString("locale", "el");
      	    		editlocale.commit();
      	    		Intent x = new Intent(EnterFile.this, HomeActivity.class);
      	    	    startActivity(x);
      	    	    EnterFile.this.finish();
      	    		break;
      	    	case 16:
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
    			Toast.makeText(EnterFile.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
    	/* case R.id.troubleshooting:
    		Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
    		startActivity(j);
    		break; */
    	case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
    	case R.id.about:
			Intent j = new Intent(EnterFile.this, About.class);
			startActivity(j);
			break;
    	case R.id.instructions:
    		Intent k = new Intent(EnterFile.this, Instructions.class);
    		startActivity(k);
    		break;
    	case R.id.show_changelog:
    		Intent l = new Intent(EnterFile.this, Changelog.class);
    		startActivity(l);
    		break;
    	case R.id.preferences:
    		Intent m = new Intent(EnterFile.this, Preferences.class);
    		startActivity(m);
    		break;
    	case R.id.license:
			Intent n = new Intent(EnterFile.this, License.class);
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
}
