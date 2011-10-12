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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
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
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DirectDownloader extends ListActivity {
	SharedPreferences preferences;
	private ProgressDialog dialog;
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
	private ArrayList <HashMap<String, Object>> tpts;
	private static final String tptname = "tptname";
	private static final String tptlayout = "tptlayout";
	private final int DOWNLOADING = 1;
	private final int DOWNLOAD_COMPLETE = 2;
	private final int DOWNLOAD_FAILED = 3;
	private final int FILE_FOUND = 4;
	private final int CHANGE_LOCALE = 5;
	private final int V9CUSTOM = 101;
	private final int V8CUSTOM = 102;
	private final int V8STOCK = 103;
	private final int V7B = 104;
	private final int V4 = 105;
	private final int REVERT = 106;
	private final int CM71RC1 = 107;
	private final int GSFB24 = 108;
	private final int GSFB23 = 109;
	private final int GSFB19 = 110;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main);
		dialog = new ProgressDialog(DirectDownloader.this);
	    ListView listview = getListView();
	    listview.setTextFilterEnabled(true);
	  
	    tpts = new ArrayList<HashMap<String, Object>>();
	  
	    HashMap<String, Object> listitem;
	          listitem = new HashMap<String, Object>();
	          CharSequence standard_tpt_heading = getText(R.string.standard_tpt_heading);
	          CharSequence standard_tpt = getText(R.string.standard_tpt);
              listitem.put(tptname, standard_tpt_heading);
              listitem.put(tptlayout, standard_tpt);
              tpts.add(listitem);
              
              listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v9 custom");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 294mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v8 custom");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 294mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v8 stock");
	          listitem.put(tptlayout, "37mb cache, 210mb system, 204mb data 4mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v7b");
	          listitem.put(tptlayout, "2mb cache, 140mb system, 314mb data, 0.1mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v4");
	          listitem.put(tptlayout, "2mb cache, 138mb system, 315.6mb data, 0.1mb oem");
	          tpts.add(listitem);
	 
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 2 to Gen 1 v2 stock");
	          listitem.put(tptlayout, "42mb cache, 207mb system, 208mb data");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          CharSequence allinone_tpt_heading = getText(R.string.allinone_tpt_heading);
	          CharSequence allinone_tpt = getText(R.string.allinone_tpt);
	          listitem.put(tptname, allinone_tpt_heading);
	          listitem.put(tptlayout, allinone_tpt);
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "CM 7.1 RC1 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: CyanogenMod 7.1 RC1");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "GSF B24 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Ginger Stir Fry Beta 24");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "GSF B23 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Ginger Stir Fry Beta 23");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "GSF B19 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Ginger Stir Fry Beta 19");
	          tpts.add(listitem);
	       
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(V9CUSTOM);
			      break;
		      case 2:
		    	  showDialog(V8CUSTOM);
				  break;
		      case 3:
		    	  showDialog(V8STOCK);
				  break;
		      case 4:
		    	  showDialog(V7B);
				  break;
		      case 5:
		    	  showDialog(V4);
				  break;
		      case 6:
		    	  showDialog(REVERT);
				  break;
		      case 8:
		    	  showDialog(CM71RC1);
				  break;
		      case 9:
		    	  showDialog(GSFB24);
				  break;
		      case 10:
		    	  showDialog(GSFB23);
				  break;
		      case 11:
		    	  showDialog(GSFB19);
				  break;
		      }
			return false;
		  }
	    });
	  
	    listview.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	Editor editdownload = preferences.edit();
	    	Editor editdownloadint = preferences.edit();
	    	switch (position) {
	    	case 1:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v9-custom.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (custom9TPT.canRead() == true){
	    		    showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustom9TPT.canRead() == true){
    	    	        showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v8-custom.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (customTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustomTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v8-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (stockTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadstockTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 4:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v7b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 3);
	    		editdownloadint.commit();
	    		if (customv7bTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustomv7bTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 5:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v4.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 4);
	    		editdownloadint.commit();
	    		if (customv4TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustomv4TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 6:
	    		editdownload.putString("downloadpicked", "Gen2-to-Gen1-TPT-v2-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 5);
	    		editdownloadint.commit();
	    		if (revertTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadrevertTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 8:
	    		editdownload.putString("downloadpicked", "cm-7.1.0-RC1-Blade-TPT.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 6);
	    		editdownloadint.commit();
	    		if (cm71rc1TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcm71rc1TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 9:
	    		editdownload.putString("downloadpicked", "gsf-blade-b24-tpt.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 7);
	    		editdownloadint.commit();
	    		if (gsfb24TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgsfb24TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 10:
	    		editdownload.putString("downloadpicked", "gsf-blade-b23-tpt.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 8);
	    		editdownloadint.commit();
	    		if (gsfb23TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgsfb23TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 11:
	    		editdownload.putString("downloadpicked", "gsf-blade-b19-tpt.zip");
	    		editdownload.commit();
	    		editdownloadint.putInt("downloadint", 9);
	    		editdownloadint.commit();
	    		if (gsfb19TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgsfb19TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	        }
	      }
	    });
	}
	
	@Override
	public void onConfigurationChanged(Configuration config) {
	    super.onConfigurationChanged(config);
	    setContentView(R.layout.main);
	    ListView listview = getListView();
	    listview.setTextFilterEnabled(true);
	  
	    tpts = new ArrayList<HashMap<String,Object>>();
	  
	    HashMap<String, Object> listitem;
	          listitem = new HashMap<String, Object>();
	          CharSequence standard_tpt_heading = getText(R.string.standard_tpt_heading);
	          CharSequence standard_tpt = getText(R.string.standard_tpt);
              listitem.put(tptname, standard_tpt_heading);
              listitem.put(tptlayout, standard_tpt);
              tpts.add(listitem);
              
              listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v9 custom");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 294mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v8 custom");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 294mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v8 stock");
	          listitem.put(tptlayout, "37mb cache, 210mb system, 204mb data 4mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v7b");
	          listitem.put(tptlayout, "2mb cache, 140mb system, 314mb data, 0.1mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v4");
	          listitem.put(tptlayout, "2mb cache, 138mb system, 315.6mb data, 0.1mb oem");
	          tpts.add(listitem);
	 
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 2 to Gen 1 v2 stock");
	          listitem.put(tptlayout, "42mb cache, 207mb system, 208mb data");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          CharSequence allinone_tpt_heading = getText(R.string.allinone_tpt_heading);
	          CharSequence allinone_tpt = getText(R.string.allinone_tpt);
	          listitem.put(tptname, allinone_tpt_heading);
	          listitem.put(tptlayout, allinone_tpt);
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "CM 7.1 RC1 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: CyanogenMod 7.1 RC1");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "GSF B24 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Ginger Stir Fry Beta 24");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "GSF B23 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Ginger Stir Fry Beta 23");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "GSF B19 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Ginger Stir Fry Beta 19");
	          tpts.add(listitem);
	       
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item, new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(V9CUSTOM);
			      break;
		      case 2:
		    	  showDialog(V8CUSTOM);
				  break;
		      case 3:
		    	  showDialog(V8STOCK);
				  break;
		      case 4:
		    	  showDialog(V7B);
				  break;
		      case 5:
		    	  showDialog(V4);
				  break;
		      case 6:
		    	  showDialog(REVERT);
				  break;
		      case 8:
		    	  showDialog(CM71RC1);
				  break;
		      case 9:
		    	  showDialog(GSFB24);
				  break;
		      case 10:
		    	  showDialog(GSFB23);
				  break;
		      case 11:
		    	  showDialog(GSFB19);
				  break;
		      }
			return false;
		  }
	    });
	  
	    listview.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	Editor editdownload = preferences.edit();
	    	Editor editdownloadint = preferences.edit();
	    	switch (position) {
	    	case 1:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v9-custom.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (custom9TPT.canRead() == true){
	    		    showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustom9TPT.canRead() == true){
    	    	        showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v8-custom.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (customTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustomTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v8-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (stockTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadstockTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 4:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v7b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 3);
	    		editdownloadint.commit();
	    		if (customv7bTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustomv7bTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 5:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v4.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 4);
	    		editdownloadint.commit();
	    		if (customv4TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcustomv4TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 6:
	    		editdownload.putString("downloadpicked", "Gen2-to-Gen1-TPT-v2-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 5);
	    		editdownloadint.commit();
	    		if (revertTPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadrevertTPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 8:
	    		editdownload.putString("downloadpicked", "cm-7.1.0-RC1-Blade-TPT.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 6);
	    		editdownloadint.commit();
	    		if (cm71rc1TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcm71rc1TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 9:
	    		editdownload.putString("downloadpicked", "gsf-blade-b24-tpt.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 7);
	    		editdownloadint.commit();
	    		if (gsfb24TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgsfb24TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 10:
	    		editdownload.putString("downloadpicked", "gsf-blade-b23-tpt.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 8);
	    		editdownloadint.commit();
	    		if (gsfb23TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgsfb23TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 11:
	    		editdownload.putString("downloadpicked", "gsf-blade-b19-tpt.zip");
	    		editdownload.commit();
	    		editdownloadint.putInt("downloadint", 9);
	    		editdownloadint.commit();
	    		if (gsfb19TPT.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgsfb19TPT.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	        }
	      }
	    });
	}

	private class DownloadFileTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
            showDialog(DOWNLOADING);
		}
		
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String urlstring : urls) {
				try {
					  // set the download URL
			        URL url = new URL(urlstring);
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        connection.connect();
			          // set where to save file
			        String downloadpicked = preferences.getString("downloadpicked", "TPT.zip");
			        File file = new File(Environment.getExternalStorageDirectory(), downloadpicked);
			        FileOutputStream fos = new FileOutputStream(file);
			        InputStream is = connection.getInputStream();
			          // get total size of file
			        int total = connection.getContentLength();
			        int downloaded = 0;
			        byte[] buffer = new byte[1024];
			        int length = 0;
			          // read buffer and write to file
			        while ((length = is.read(buffer)) > 0 ) {
			            fos.write(buffer, 0, length);
			              // update amount downloaded then show progress
			            downloaded += length;
			            publishProgress(""+(int)((downloaded*100)/total));
			        }
			        fos.close();
			        response = "Download Completed";
			    } catch (MalformedURLException e) {
			        e.printStackTrace();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			return response;
		}

		protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
       }
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (result.equals("Download Completed")){
				showDialog(DOWNLOAD_COMPLETE);
			} else {
				showDialog(DOWNLOAD_FAILED);
			}
		}
	}

	public void DownloadFile() {
		DownloadFileTask task = new DownloadFileTask();
		int id = preferences.getInt("downloadint", 0);
		switch (id) {
		case 0:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Gen1-to-Gen2-TPT-v9-custom.zip" });
			break;
		case 1:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Gen1-to-Gen2-TPT-v8-custom.zip" });
			break;
		case 2:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Gen1-to-Gen2-TPT-v8-stock.zip" });
			break;
		case 3:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Gen1-to-Gen2-TPT-v7b.zip" });
			break;
		case 4:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Gen1-to-Gen2-TPT-v4.zip" });
			break;
		case 5:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Gen2-to-Gen1-TPT-v2-stock.zip" });
			break;
		case 6:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/cm-7.1.0-RC1-Blade-TPT.zip" });
			break;
		case 7:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/gsf-blade-b24-tpt.zip" });
			break;
		case 8:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/gsf-blade-b23-tpt.zip" });
			break;
		case 9:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/gsf-blade-b19-tpt.zip" });
			break;
		}
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DOWNLOADING:
                dialog = new ProgressDialog(DirectDownloader.this);
                CharSequence downloadmessage = getText(R.string.downloading);
                dialog.setMessage(downloadmessage);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setCancelable(false);
                dialog.show();
                break;
            default:
                return null;
            case DOWNLOAD_COMPLETE:
            	String downloadpicked = preferences.getString("downloadpicked", "");
            	Builder downloadbuilder = new AlertDialog.Builder(DirectDownloader.this);
            	downloadbuilder.setTitle(R.string.download_finished_heading);
                CharSequence download_finished = getText(R.string.download_finished);
                downloadbuilder.setMessage(download_finished + " " + downloadpicked);
                downloadbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                AlertDialog downloadalert = downloadbuilder.create();
                downloadalert.show();
                break;
            case DOWNLOAD_FAILED:
            	String downloadfailed = preferences.getString("downloadpicked", "");
            	Builder downloadfailedbuilder = new AlertDialog.Builder(DirectDownloader.this);
            	downloadfailedbuilder.setTitle(R.string.download_failed_heading);
                CharSequence download_failed = getText(R.string.download_failed);
                downloadfailedbuilder.setMessage(download_failed + " " + downloadfailed);
                downloadfailedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                AlertDialog downloadfailedalert = downloadfailedbuilder.create();
                downloadfailedalert.show();
                break;
            case FILE_FOUND:
            	String filepicked = preferences.getString("downloadpicked", "");
            	Builder filetherebuilder = new AlertDialog.Builder(DirectDownloader.this);
            	filetherebuilder.setTitle(R.string.file_there_heading);
            	CharSequence filethere1 = getText(R.string.file_there1);
            	CharSequence filethere2 = getText(R.string.file_there2);
            	filetherebuilder.setMessage(filethere1 + " " + filepicked + " " + filethere2);
            	filetherebuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	dialog.cancel();
                    	DownloadFile();
                    }
                });
            	filetherebuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	DirectDownloader.this.finish();
                    }
                });
                AlertDialog filetherealert = filetherebuilder.create();
                filetherealert.show();
                break;
            case CHANGE_LOCALE:
          	    // change the locale used in the app
              Builder localebuilder = new AlertDialog.Builder(DirectDownloader.this);
              localebuilder.setTitle(R.string.change_locale_heading);
              localebuilder.setCancelable(false);
              CharSequence english = getText(R.string.english);
              CharSequence french = getText(R.string.french);
              CharSequence german = getText(R.string.german);
              CharSequence russian = getText(R.string.russian);
              CharSequence chinese = getText(R.string.chinese);
              CharSequence cancel = getText(R.string.cancel);
              final CharSequence[] locales = {english, french, german, russian, chinese, cancel};
          	  localebuilder.setItems(locales, new DialogInterface.OnClickListener() {
          	    public void onClick(DialogInterface dialog, int item) {
          	    	Editor editlocale = preferences.edit();
          	    	switch (item) {
          	    	case 0:
          	    		editlocale.putString("locale", "en");
          	    		editlocale.commit();
          	    		Intent i = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(i);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 1:
          	    		editlocale.putString("locale", "fr");
          	    		editlocale.commit();
          	    		Intent j = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(j);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 2:
          	    		editlocale.putString("locale", "de");
          	    		editlocale.commit();
          	    		Intent k = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(k);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 3:
          	    		editlocale.putString("locale", "ru");
          	    		editlocale.commit();
          	    		Intent l = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(l);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 4:
          	    		editlocale.putString("locale", "zh");
          	    		editlocale.commit();
          	    		Intent m = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(m);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 5:
          	    		// Do nothing
          	    		break;
          	    	}
          	      }
          	  });
              AlertDialog localealert = localebuilder.create();
              localealert.show();
              break;
            case V9CUSTOM:
                Builder builder1 = new AlertDialog.Builder(DirectDownloader.this);
                builder1.setTitle("Gen 1 to Gen 2 v9 custom");
                builder1.setMessage(Html.fromHtml("<b>Size:</b> 16.1MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Big Android"));
                builder1.setCancelable(false);
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                AlertDialog alert1 = builder1.create();
                alert1.show();
                break;
            case V8CUSTOM:
                Builder builder2 = new AlertDialog.Builder(DirectDownloader.this);
                builder2.setTitle("Gen 1 to Gen 2 v8 custom");
                builder2.setMessage(Html.fromHtml("<b>Size:</b> 16.06MB<br /><b>Recovery:</b> ClockworkMod v3.2.0.7<br /><b>Splash:</b> Big Android"));
                builder2.setCancelable(false);
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
            case V8STOCK:
                Builder builder3 = new AlertDialog.Builder(DirectDownloader.this);
                builder3.setTitle("Gen 1 to Gen 2 v8 stock");
                builder3.setMessage(Html.fromHtml("<b>Size:</b> 16.06MB<br /><b>Recovery:</b> ClockworkMod v3.2.0.7<br /><b>Splash:</b> Big Android"));
                builder3.setCancelable(false);
                builder3.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                AlertDialog alert3 = builder3.create();
                alert3.show();
                break;
            case V7B:
                Builder builder4 = new AlertDialog.Builder(DirectDownloader.this);
                builder4.setTitle("Gen 1 to Gen 2 v7b");
                builder4.setMessage(Html.fromHtml("<b>Size:</b> 16.03MB<br /><b>Recovery:</b> ClockworkMod v3.2.0.7<br /><b>Splash:</b> Big Android"));
                builder4.setCancelable(false);
                builder4.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                AlertDialog alert4 = builder4.create();
                alert4.show();
                break;
            case V4:
                Builder builder5 = new AlertDialog.Builder(DirectDownloader.this);
                builder5.setTitle("Gen 1 to Gen 2 v4");
                builder5.setMessage(Html.fromHtml("<b>Size:</b> 16.12MB<br /><b>Recovery:</b> ClockworkMod v3.2.0.7<br /><b>Splash:</b> Normal Android"));
                builder5.setCancelable(false);
                builder5.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                AlertDialog alert5 = builder5.create();
                alert5.show();
                break;
            case REVERT:
                Builder builder6 = new AlertDialog.Builder(DirectDownloader.this);
                builder6.setTitle("Gen 2 to Gen 1 v2 stock");
                builder6.setMessage(Html.fromHtml("<b>Size:</b> 16.39MB<br /><b>Recovery:</b> ClockworkMod <br /><b>Splash:</b> Normal Android"));
                builder6.setCancelable(false);
                builder6.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                AlertDialog alert6 = builder6.create();
                alert6.show();
                break;
            case CM71RC1:
                Builder builder8 = new AlertDialog.Builder(DirectDownloader.this);
                builder8.setTitle("CM 7.1 RC1 Gen 1 to Gen 2");
                builder8.setMessage(Html.fromHtml("<b>Size:</b> 104.5MB<br /><b>Recovery:</b> ClockworkMod v3.2.0.7<br /><b>Splash:</b> CyanogenMod<br /><b>Partitions:</b> 2mb cache, 140mb system, 314mb data, 0.1mb oem"));
                builder8.setCancelable(false);
                builder8.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                AlertDialog alert8 = builder8.create();
                alert8.show();
                break;
            case GSFB24:
                Builder builder9 = new AlertDialog.Builder(DirectDownloader.this);
                builder9.setTitle("GSF B24 Gen 1 to Gen 2");
                builder9.setMessage(Html.fromHtml("<b>Size:</b> 96.88MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Big Android<br /><b>Partitions:</b> 2mb cache, 160mb system, 294mb data, 0.1mb oem"));
                builder9.setCancelable(false);
                builder9.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                AlertDialog alert9 = builder9.create();
                alert9.show();
                break;
            case GSFB23:
                Builder builder10 = new AlertDialog.Builder(DirectDownloader.this);
                builder10.setTitle("GSF B23 Gen 1 to Gen 2");
                builder10.setMessage(Html.fromHtml("<b>Size:</b> 98.02MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Big Android<br /><b>Partitions:</b> 2mb cache, 160mb system, 294mb data, 0.1mb oem"));
                builder10.setCancelable(false);
                builder10.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                AlertDialog alert10 = builder10.create();
                alert10.show();
                break;
            case GSFB19:
                Builder builder11 = new AlertDialog.Builder(DirectDownloader.this);
                builder11.setTitle("GSF B19 Gen 1 to Gen 2");
                builder11.setMessage(Html.fromHtml("<b>Size:</b> 103.32MB<br /><b>Recovery:</b> ClockworkMod v3.2.0.7<br /><b>Splash:</b> Big Android<br /><b>Partitions:</b> 2mb cache, 160mb system, 294mb data, 0.1mb oem"));
                builder11.setCancelable(false);
                builder11.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                AlertDialog alert11 = builder11.create();
                alert11.show();
                break;
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
			Intent j = new Intent(DirectDownloader.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(DirectDownloader.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(DirectDownloader.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(DirectDownloader.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(DirectDownloader.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
}