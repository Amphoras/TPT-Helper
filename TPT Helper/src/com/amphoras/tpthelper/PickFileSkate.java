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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import java.io.File;

public class PickFileSkate extends Activity {
	SharedPreferences preferences;
	final File dir = Environment.getExternalStorageDirectory();
	final File skatev1a = new File(dir, "Skate-v1a.zip");
	final File downloadskatev1a = new File(dir, "download/Skate-v1a.zip");
	final File skatev2a = new File(dir, "Skate-v2a.zip");
	final File downloadskatev2a = new File(dir, "download/Skate-v2a.zip");
	final File skatev1b = new File(dir, "Skate-v1b.zip");
	final File downloadskatev1b = new File(dir, "download/Skate-v1b.zip");
	final File skatev2b = new File(dir, "Skate-v2b.zip");
	final File downloadskatev2b = new File(dir, "download/Skate-v2b.zip");
	private final int PICK_FILE = 1;
	private final int FILE_UNFOUND = 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.md5sum);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        showDialog(PICK_FILE);
    }

    @Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case PICK_FILE:
        	Builder builder1 = new AlertDialog.Builder(PickFileSkate.this);
            builder1.setTitle(R.string.pickmd5);
            builder1.setCancelable(false);
            CharSequence cancel = getText(R.string.cancel);
            CharSequence other = getText(R.string.other);
            final CharSequence[] zips1 = {"Skate-v1a.zip", "Skate-v2a.zip", "Skate-v1b.zip", "Skate-v2b.zip", other, cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editmd5 = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editmd5.putString("expectedmd5", "9ded013763541d3ccacea94d99d6f645");
        	    		editmd5.commit();
        	    		if (skatev1a.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("filepath", "/Skate-v1a.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
        	    	        startActivity(i);
        	    	        PickFileSkate.this.finish();
        	    		} else {
        	    			if (downloadskatev1a.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("filepath", "/download/Skate-v1a.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
            	    	        startActivity(i);
            	    	        PickFileSkate.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Skate-v1a.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 1:
        	    		editmd5.putString("expectedmd5", "790f6b04654a2abfde9ed2ae75b91fec");
        	    		editmd5.commit();
        	    		if (skatev2a.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("filepath", "/Skate-v2a.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
        	    	        startActivity(i);
        	    	        PickFileSkate.this.finish();
        	    		} else {
        	    			if (downloadskatev2a.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("filepath", "/download/Skate-v2a.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
            	    	        startActivity(i);
            	    	        PickFileSkate.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Skate-v2a.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 2:
        	    		editmd5.putString("expectedmd5", "a9a4fe7c802b035b8dd0298ea39fbd5a");
        	    		editmd5.commit();
        	    		if (skatev1b.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("filepath", "/Skate-v1b.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
        	    	        startActivity(i);
        	    	        PickFileSkate.this.finish();
        	    		} else {
        	    			if (downloadskatev1b.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("filepath", "/download/Skate-v1b.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
            	    	        startActivity(i);
            	    	        PickFileSkate.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Skate-v1b.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 3:
        	    		editmd5.putString("expectedmd5", "48bd788c6c3753d1f599b71f0560241e");
        	    		editmd5.commit();
        	    		if (skatev2b.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("filepath", "/Skate-v2b.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
        	    	        startActivity(i);
        	    	        PickFileSkate.this.finish();
        	    		} else {
        	    			if (downloadskatev2b.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("filepath", "/download/Skate-v2b.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileSkate.this, MD5sum.class);
            	    	        startActivity(i);
            	    	        PickFileSkate.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Skate-v2b.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 4:
        	    		Intent i = new Intent(PickFileSkate.this, EnterFile.class);
                		startActivityForResult(i, 1);
                		break;
        	    	case 5:
                		PickFileSkate.this.finish();
                		break;
        	    	}
        	    }
        	});
        	return builder1.create();
        case FILE_UNFOUND:
        	String filepicked = preferences.getString("filepicked", "");
        	Builder builder2 = new AlertDialog.Builder(PickFileSkate.this);
            builder2.setTitle(R.string.file_not_found_heading);
            builder2.setCancelable(false);
            CharSequence file_not_found1 = getText(R.string.file_not_found1);
            CharSequence file_not_found2 = getText(R.string.file_not_found2);
            builder2.setMessage(file_not_found1 + " " + filepicked + "" + file_not_found2);
            builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    Intent i = new Intent(PickFileSkate.this, DownloaderSkate.class);
              	    startActivity(i);
              	    PickFileSkate.this.finish();
                }
            });
            builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    PickFileSkate.this.finish();
                }
            });
            return builder2.create();
        }
        return super.onCreateDialog(id);
    }
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      switch(requestCode) {
        case 1:
          if (resultCode == Activity.RESULT_OK) {
        	  String filename = data.getStringExtra("filename");
  	          Editor edit = preferences.edit();
    	      edit.putString("filepath", "/" + filename);
    		  edit.commit();
    	      Intent i = new Intent(PickFileSkate.this, MD5sum.class);
    	      startActivity(i);
    	      PickFileSkate.this.finish();
          }
          break;
      }
    }
}