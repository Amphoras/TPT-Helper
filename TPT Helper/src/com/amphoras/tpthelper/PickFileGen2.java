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

public class PickFileGen2 extends Activity {
	SharedPreferences preferences;
	final File dir = Environment.getExternalStorageDirectory();
	final File gen2v1a = new File(dir, "Gen2-v1a.zip");
	final File downloadgen2v1a = new File(dir, "download/Gen2-v1a.zip");
	final File gen2v2a = new File(dir, "Gen2-v2a.zip");
	final File downloadgen2v2a = new File(dir, "download/Gen2-v2a.zip");
	final File gen2stock = new File(dir, "Gen2-stock.zip");
	final File downloadgen2stock = new File(dir, "download/Gen2-stock.zip.zip");
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
        	Builder builder1 = new AlertDialog.Builder(PickFileGen2.this);
            builder1.setTitle(R.string.pickmd5);
            builder1.setCancelable(false);
            CharSequence cancel = getText(R.string.cancel);
            CharSequence other = getText(R.string.other);
            final CharSequence[] zips1 = {"Gen2-v1a.zip", "Gen2-v2a.zip", "Gen2-stock.zip", other, cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editmd5 = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editmd5.putString("expectedmd5", "7d73894ae1013d1bc65ff6dfbc1cc9d4");
        	    		editmd5.commit();
        	    		if (gen2v1a.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("filepath", "/Gen2-v1a.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileGen2.this, MD5sum.class);
        	    	        startActivity(i);
        	    	        PickFileGen2.this.finish();
        	    		} else {
        	    			if (downloadgen2v1a.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("filepath", "/download/Gen2-v1a.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileGen2.this, MD5sum.class);
            	    	        startActivity(i);
            	    	        PickFileGen2.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen2-v1a.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 1:
        	    		editmd5.putString("expectedmd5", "83624b0365216b8e97c13f55e7fdebab");
        	    		editmd5.commit();
        	    		if (gen2v2a.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("filepath", "/Gen2-v2a.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileGen2.this, MD5sum.class);
        	    	        startActivity(i);
        	    	        PickFileGen2.this.finish();
        	    		} else {
        	    			if (downloadgen2v2a.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("filepath", "/download/Gen2-v2a.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileGen2.this, MD5sum.class);
            	    	        startActivity(i);
            	    	        PickFileGen2.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen2-v2a.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 2:
        	    		editmd5.putString("expectedmd5", "4e8d9c15ac2e640e805fcfdaa44b0579");
        	    		editmd5.commit();
        	    		if (gen2stock.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("filepath", "/Gen2-stock.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileGen2.this, MD5sum.class);
        	    	        startActivity(i);
        	    	        PickFileGen2.this.finish();
        	    		} else {
        	    			if (downloadgen2stock.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("filepath", "/download/Gen2-stock.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileGen2.this, MD5sum.class);
            	    	        startActivity(i);
            	    	        PickFileGen2.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen2-stock.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 3:
        	    		Intent i = new Intent(PickFileGen2.this, EnterFile.class);
                		startActivityForResult(i, 1);
                		break;
        	    	case 4:
                		PickFileGen2.this.finish();
                		break;
        	    	}
        	    }
        	});
        	return builder1.create();
        case FILE_UNFOUND:
        	String filepicked = preferences.getString("filepicked", "");
        	Builder builder2 = new AlertDialog.Builder(PickFileGen2.this);
            builder2.setTitle(R.string.file_not_found_heading);
            builder2.setCancelable(false);
            CharSequence file_not_found1 = getText(R.string.file_not_found1);
            CharSequence file_not_found2 = getText(R.string.file_not_found2);
            builder2.setMessage(file_not_found1 + " " + filepicked + "" + file_not_found2);
            builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    Intent i = new Intent(PickFileGen2.this, Downloader.class);
              	    startActivity(i);
              	    PickFileGen2.this.finish();
                }
            });
            builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    PickFileGen2.this.finish();
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
    	      Intent i = new Intent(PickFileGen2.this, MD5sum.class);
    	      startActivity(i);
    	      PickFileGen2.this.finish();
          }
          break;
      }
    }
}