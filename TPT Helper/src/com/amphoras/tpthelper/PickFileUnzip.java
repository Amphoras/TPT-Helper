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

public class PickFileUnzip extends Activity {
	SharedPreferences preferences;
	final File dir = Environment.getExternalStorageDirectory();
	final File gen1v10a = new File(dir, "Gen1-to-Gen2-TPT-v10a.zip");
	final File downloadgen1v10a = new File(dir, "download/Gen1-to-Gen2-TPT-v10a.zip");
	final File gen1v10b = new File(dir, "Gen1-to-Gen2-TPT-v10b.zip");
	final File downloadgen1v10b = new File(dir, "download/Gen1-to-Gen2-TPT-v10b.zip");
	final File gen1v10c = new File(dir, "Gen1-to-Gen2-TPT-v10c.zip");
	final File downloadgen1v10c = new File(dir, "download/Gen1-to-Gen2-TPT-v10c.zip");
	final File gen1v10stock = new File(dir, "Gen1-to-Gen2-TPT-v10-stock.zip");
	final File downloadgen1v10stock = new File(dir, "download/Gen1-to-Gen2-TPT-v10-stock.zip");
	final File revertv2 = new File(dir, "Gen2-to-Gen1-TPT-v2-stock.zip");
	final File downloadrevertv2 = new File(dir, "download/Gen2-to-Gen1-TPT-v2-stock.zip");
	final File cm7n257 = new File(dir, "cm7-n257-blade-gen1-to-gen2-tpt.zip");
	final File downloadcm7n257 = new File(dir, "download/cm7-n257-blade-gen1-to-gen2-tpt.zip");
	final File mmhmp8 = new File(dir, "Gen1-to-Gen2-TPT-MMHMP-RLS8.zip");
	final File downloadmmhmp8 = new File(dir, "download/Gen1-to-Gen2-TPT-MMHMP-RLS8.zip");
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
            Builder builder1 = new AlertDialog.Builder(PickFileUnzip.this);
            builder1.setTitle(R.string.pickunzip);
            builder1.setCancelable(false);
            CharSequence cancel = getText(R.string.cancel);
            CharSequence other = getText(R.string.other);
            final CharSequence[] zips1 = {"Gen 1 to Gen 2 v10a", "Gen1 to Gen2 v10b", "Gen1 to Gen2 v10c", "Gen 1 to Gen 2 v10 stock", "Gen2 to Gen1 v2 stock", "CM7.1 N257 Gen 1 to Gen 2", "MMHMP RLS8 Gen 1 to Gen 2", other, cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int item) {
        	    	switch (item) {
        	    	case 0:
        	    		if (gen1v10a.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v10a.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadgen1v10a.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v10a.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v10a.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 1:
        	    		if (gen1v10b.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v10b.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadgen1v10b.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v10b.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v10b.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 2:
        	    		if (gen1v10c.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v10c.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadgen1v10c.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v10c.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v10c.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 3:
        	    		if (gen1v10stock.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v10-stock.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadgen1v10stock.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v10-stock.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v10-stock.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 4:
        	    		if (revertv2.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen2-to-Gen1-TPT-v2-stock.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadrevertv2.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen2-to-Gen1-TPT-v2-stock.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen2-to-Gen1-TPT-v2-stock.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 5:
        	    		if (cm7n257.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/cm7-n257-blade-gen1-to-gen2-tpt.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadcm7n257.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/cm7-n257-blade-gen1-to-gen2-tpt.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "cm7-n257-blade-gen1-to-gen2-tpt.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 6:
        	    		if (mmhmp8.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-MMHMP-RLS8.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadmmhmp8.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-MMHMP-RLS8.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-MMHMP-RLS8.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 7:
        	    		Intent i = new Intent(PickFileUnzip.this, EnterFileUnzip.class);
                		startActivityForResult(i, 1);
                		break;
        	    	case 8:
                		PickFileUnzip.this.finish();
                		break;
        	    	}
        	    }
        	});
        	return builder1.create();
        case FILE_UNFOUND:
        	String filepicked = preferences.getString("filepicked", "");
        	Builder builder2 = new AlertDialog.Builder(PickFileUnzip.this);
            builder2.setTitle(R.string.file_not_found_heading);
            builder2.setCancelable(false);
            CharSequence file_not_found1 = getText(R.string.file_not_found1);
            CharSequence file_not_found2 = getText(R.string.file_not_found2);
            builder2.setMessage(file_not_found1 + " " + filepicked + "" + file_not_found2);
            builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    Intent i = new Intent(PickFileUnzip.this, DirectDownloader.class);
              	    startActivity(i);
              	    PickFileUnzip.this.finish();
                }
            });
            builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    PickFileUnzip.this.finish();
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
    	      Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
    	      startActivity(i);
    	      PickFileUnzip.this.finish();
          }
          break;
      }
    }
}