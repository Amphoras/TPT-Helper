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
	final File customTPT = new File(dir, "Gen1-to-Gen2-TPT-v8-custom.zip");
	final File downloadcustomTPT = new File(dir, "download/Gen1-to-Gen2-TPT-v8-custom.zip");
	final File stockTPT = new File(dir, "Gen1-to-Gen2-TPT-v8-stock.zip");
	final File downloadstockTPT = new File(dir, "download/Gen1-to-Gen2-TPT-v8-stock.zip");
	final File revertTPT = new File(dir, "Gen2-to-Gen1-TPT-v2-stock.zip");
	final File downloadrevertTPT = new File(dir, "download/Gen2-to-Gen1-TPT-v2-stock.zip");
	final File cm71rc1TPT = new File(dir, "cm-7.1.0-RC1-Blade-TPT.zip");
	final File downloadcm71rc1TPT = new File(dir, "download/cm-7.1.0-RC1-Blade-TPT.zip");
	final File gsfb19TPT = new File(dir, "gsf-blade-b19-tpt.zip");
	final File downloadgsfb19TPT = new File(dir, "download/gsf-blade-b19-tpt.zip");
	final File customv7bTPT = new File(dir, "Gen1-to-Gen2-TPT-v7b.zip");
	final File downloadcustomv7bTPT = new File(dir, "download/Gen1-to-Gen2-TPT-v7b.zip");
	final File customv4TPT = new File(dir, "Gen1-to-Gen2-TPT-v4.zip");
	final File downloadcustomv4TPT = new File(dir, "download/Gen1-to-Gen2-TPT-v4.zip");
	final File gsfb23TPT = new File(dir, "gsf-blade-b23-tpt.zip");
	final File downloadgsfb23TPT = new File(dir, "download/gsf-blade-b23-tpt.zip");
	final File gsfb24TPT = new File(dir, "gsf-blade-b24-tpt.zip");
	final File downloadgsfb24TPT = new File(dir, "download/gsf-blade-b24-tpt.zip");
	final File custom9TPT = new File(dir, "Gen1-to-Gen2-TPT-v9-custom.zip");
	final File downloadcustom9TPT = new File(dir, "download/Gen1-to-Gen2-TPT-v9-custom.zip");
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
            final CharSequence[] zips1 = {"Gen 1 to Gen 2 v9 custom", "Gen1 to Gen2 v8 custom", "Gen1 to Gen2 v8 stock", "Gen 1 to Gen 2 v7b", "Gen 1 to Gen 2 v4", "Gen2 to Gen1 v2 stock", "CM7.1 RC1 Gen 1 to Gen 2", "GSF B24 Gen 1 to Gen 2", "GSF B23 Gen 1 to Gen 2", "GSF B19 Gen 1 to Gen 2", other, cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int item) {
        	    	switch (item) {
        	    	case 0:
        	    		if (custom9TPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v9-custom.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadcustom9TPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v9-custom.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v9-custom.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 1:
        	    		if (customTPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v8-custom.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadcustomTPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v8-custom.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v8-custom.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 2:
        	    		if (stockTPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v8-stock.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadstockTPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v8-stock.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v8-stock.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 3:
        	    		if (customv7bTPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v7b.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadcustomv7bTPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v7b.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v7b.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 4:
        	    		if (customv4TPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen1-to-Gen2-TPT-v4.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadcustomv4TPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/Gen1-to-Gen2-TPT-v4.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "Gen1-to-Gen2-TPT-v4.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 5:
        	    		if (revertTPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/Gen2-to-Gen1-TPT-v2-stock.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadrevertTPT.canRead() == true){
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
        	    	case 6:
        	    		if (cm71rc1TPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/cm-7.1.0-RC1-Blade-TPT.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadcm71rc1TPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/cm-7.1.0-RC1-Blade-TPT.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "cm-7.1.0-RC1-Blade-TPT.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 7:
        	    		if (gsfb24TPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/gsf-blade-b24-tpt.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadgsfb24TPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/gsf-blade-b24-tpt.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "gsf-blade-b24-tpt.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 8:
        	    		if (gsfb23TPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/gsf-blade-b23-tpt.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadgsfb23TPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/gsf-blade-b23-tpt.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "gsf-blade-b23-tpt.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 9:
        	    		if (gsfb19TPT.canRead() == true){
        	    		    Editor edit = preferences.edit();
        	    			edit.putString("zipname", "/gsf-blade-b19-tpt.zip");
        	    			edit.commit();
        	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
        	    	        startActivity(i);
        	    	        PickFileUnzip.this.finish();
        	    		} else {
        	    			if (downloadgsfb19TPT.canRead() == true){
            	    	        Editor edit = preferences.edit();
            	    		    edit.putString("zipname", "/download/gsf-blade-b19-tpt.zip");
            	    			edit.commit();
            	    	        Intent i = new Intent(PickFileUnzip.this, Unzipper.class);
            	    	        startActivity(i);
            	    	        PickFileUnzip.this.finish();
        	    			} else {
        	    				Editor edit = preferences.edit();
            	    			edit.putString("filepicked", "gsf-blade-b19-tpt.zip");
            	    			edit.commit();
        	    				showDialog(FILE_UNFOUND);
        	    			}
        	    		}
        	    		break;
        	    	case 10:
        	    		Intent i = new Intent(PickFileUnzip.this, EnterFileUnzip.class);
                		startActivityForResult(i, 1);
                		PickFileUnzip.this.finish();
                		break;
        	    	case 11:
                		PickFileUnzip.this.finish();
                		break;
        	    	}
        	    }
        	});
            AlertDialog alert1 = builder1.create();
            alert1.show();
            break;
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
              	    Intent i = new Intent(PickFileUnzip.this, Downloader.class);
              	    startActivity(i);
              	    PickFileUnzip.this.finish();
                }
            });
            builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    PickFileUnzip.this.finish();
                }
            });
            AlertDialog alert2 = builder2.create();
            alert2.show();
            break;
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