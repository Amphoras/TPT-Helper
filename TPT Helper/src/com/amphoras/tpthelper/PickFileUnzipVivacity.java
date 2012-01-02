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

public class PickFileUnzipVivacity extends Activity {
	SharedPreferences preferences;
	final File dir = Environment.getExternalStorageDirectory();
	final File vivacityv1a = new File(dir, "Vivacity-v1a.zip");
	final File downloadvivacityv1a = new File(dir, "download/Vivacity-v1a.zip");
	final File vivacityv1b = new File(dir, "Vivacity-v1b.zip");
	final File downloadvivacityv1b = new File(dir, "download/Vivacity-v1b.zip");
	final File vivacityv1c = new File(dir, "Vivacity-v1c.zip");
	final File downloadvivacityv1c = new File(dir, "download/Vivacity-v1c.zip");
	private final int PICK_FILE = 1;
	private final int FILE_UNFOUND = 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unzip);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        showDialog(PICK_FILE);
    }

    @Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case PICK_FILE:
            Builder builder1 = new AlertDialog.Builder(PickFileUnzipVivacity.this);
            builder1.setTitle(R.string.pickunzip);
            builder1.setCancelable(false);
            CharSequence cancel = getText(R.string.cancel);
            CharSequence other = getText(R.string.other);
            final CharSequence[] zips1 = {"Vivacity-v1a.zip", "Vivacity-v1b.zip", "Vivacity-v1c.zip", other, cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int item) {
        	    	switch (item) {
        	    	case 0:
        	    		if (vivacityv1a.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Vivacity-v1a.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzipVivacity.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzipVivacity.this.finish();
        	    		} else {
        	    			if (downloadvivacityv1a.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Vivacity-v1a.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzipVivacity.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzipVivacity.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Vivacity-v1a.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 1:
        	    		if (vivacityv1b.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Vivacity-v1b.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzipVivacity.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzipVivacity.this.finish();
        	    		} else {
        	    			if (downloadvivacityv1b.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Vivacity-v1b.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzipVivacity.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzipVivacity.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Vivacity-v1b.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 2:
        	    		if (vivacityv1c.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Vivacity-v1c.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzipVivacity.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzipVivacity.this.finish();
        	    		} else {
        	    			if (downloadvivacityv1c.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Vivacity-v1c.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzipVivacity.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzipVivacity.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Vivacity-v1c.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 3:
        	    		Intent i = new Intent(PickFileUnzipVivacity.this, EnterFileUnzip.class);
                		startActivityForResult(i, 1);
                		break;
        	    	case 4:
                		PickFileUnzipVivacity.this.finish();
                		break;
        	    	}
        	    }
        	});
        	return builder1.create();
        case FILE_UNFOUND:
        	String filepicked = preferences.getString("filepicked", "");
        	Builder builder2 = new AlertDialog.Builder(PickFileUnzipVivacity.this);
            builder2.setTitle(R.string.file_not_found_heading);
            builder2.setCancelable(false);
            CharSequence file_not_found1 = getText(R.string.file_not_found1);
            CharSequence file_not_found2 = getText(R.string.file_not_found2);
            builder2.setMessage(file_not_found1 + " " + filepicked + "" + file_not_found2);
            builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    Intent i = new Intent(PickFileUnzipVivacity.this, DownloaderVivacity.class);
              	    startActivity(i);
              	    PickFileUnzipVivacity.this.finish();
                }
            });
            builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    PickFileUnzipVivacity.this.finish();
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
    	      edit.putString("zipname", "/" + filename);
    		  edit.commit();
    	      Intent i = new Intent(PickFileUnzipVivacity.this, Unzipper.class);
    	      startActivity(i);
    	      PickFileUnzipVivacity.this.finish();
          }
          break;
      }
    }
}