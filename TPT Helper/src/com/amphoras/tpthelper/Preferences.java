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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity {
	SharedPreferences preferences;
	private final int CHANGE_LOCALE = 1;
	
	/** Called when the activity is first created */
	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    final ListPreference models = (ListPreference) getPreferenceManager().findPreference("model");
	    
	    int board = preferences.getInt("board", 1);
	    String model = "blade";
	    switch (board) {
	    case 1:
	    	model = "blade";
	    	break;
	    case 2:
	    	model = "skate";
	    	break;
	    case 3:
	    	model = "blade2";
	    	break;
	    }
	    models.setValue(model);

	    models.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {            
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	            if (newValue.toString().equals("blade")) {
	        	    Editor edit = preferences.edit();
    		        edit.putInt("board", 1);
    	            edit.commit();
	            } else {  
	            	if (newValue.toString().equals("skate")) {
		        	    Editor edit = preferences.edit();
	    		        edit.putInt("board", 2);
	    	            edit.commit();
	            	} else {
	            		if (newValue.toString().equals("blade2")) {
	    	        	    Editor edit = preferences.edit();
	        		        edit.putInt("board", 3);
	        	            edit.commit();
	            		}
	            	}
	            }
	            return true;
	        }
	    });
	    
        final ListPreference gens = (ListPreference) getPreferenceManager().findPreference("gens");
	    
	    int gen = preferences.getInt("gen", 1);
	    String gen_value = "1";
	    switch (gen) {
	    case 1:
	    	gen_value = "1";
	    	break;
	    case 2:
	    	gen_value = "2";
	    	break;
	    }
	    gens.setValue(gen_value);
	    
	    gens.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {            
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	            if (newValue.toString().equals("1")) {
	        	    Editor edit = preferences.edit();
    		        edit.putInt("gen", 1);
    	            edit.commit();
	            } else {  
	            	if (newValue.toString().equals("2")) {
		        	    Editor edit = preferences.edit();
	    		        edit.putInt("gen", 2);
	    	            edit.commit();
	            	}
	            }
	            return true;
	        }
	    });
	    
        final ListPreference types = (ListPreference) getPreferenceManager().findPreference("types");
	    
	    String type = preferences.getString("blade", "European Blade");
	    types.setValue(type);
	    
	    types.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {            
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	            if (newValue.toString().equals("European Blade")) {
	        	    Editor edit = preferences.edit();
    		        edit.putString("blade", "European Blade");
    	            edit.commit();
	            } else {  
	            	if (newValue.toString().equals("Chinese Blade")) {
		        	    Editor edit = preferences.edit();
	    		        edit.putString("blade", "Chinese Blade");
	    	            edit.commit();
	            	}
	            }
	            return true;
	        }
	    });
	    
        final ListPreference blade2s = (ListPreference) getPreferenceManager().findPreference("blade2");
	    
	    String blade2 = preferences.getString("blade2", "SF2");
	    blade2s.setValue(blade2);

	    blade2s.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {            
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	            if (newValue.toString().equals("SF2")) {
	        	    Editor edit = preferences.edit();
    		        edit.putString("blade2", "SF2");
    	            edit.commit();
	            } else {  
	            	if (newValue.toString().equals("TMV")) {
		        	    Editor edit = preferences.edit();
	    		        edit.putString("blade2", "TMV");
	    	            edit.commit();
	            	}
	            }
	            return true;
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
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
	        emailIntent.setType("plain/text");
	        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tpthelper@amphoras.co.uk"});
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
			Intent j = new Intent(Preferences.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(Preferences.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(Preferences.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(Preferences.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(Preferences.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(Preferences.this);
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
      	    		Intent i = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    Preferences.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    Preferences.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    Preferences.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    Preferences.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    Preferences.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    Preferences.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    Preferences.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(Preferences.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    Preferences.this.finish();
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
}
