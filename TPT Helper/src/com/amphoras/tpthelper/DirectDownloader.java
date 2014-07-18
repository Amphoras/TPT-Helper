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
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DirectDownloader extends ListActivity {
	SharedPreferences preferences;
	private ProgressDialog dialog;
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
	final File mmhmp9 = new File(dir, "Gen1-to-Gen2-TPT-MMHMP-RLS9.zip");
	final File downloadmmhmp9 = new File(dir, "download/Gen1-to-Gen2-TPT-MMHMP-RLS9.zip");
	private ArrayList <HashMap<String, Object>> tpts;
	private static final String tptname = "tptname";
	private static final String tptlayout = "tptlayout";
	private final int DOWNLOADING = 1;
	private final int DOWNLOAD_COMPLETE = 2;
	private final int DOWNLOAD_FAILED = 3;
	private final int FILE_FOUND = 4;
	private final int CHANGE_LOCALE = 5;
	private final int V10A = 101;
	private final int V10B = 102;
	private final int V10C = 103;
	private final int V10STOCK = 104;
	private final int V2REVERT = 105;
	private final int CM7N257 = 106;
	private final int MMHMP9 = 107;

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
	          listitem.put(tptname, "Gen 1 to Gen 2 v10a");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 294mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v10b");
	          listitem.put(tptlayout, "2mb cache, 140mb system, 314mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v10c");
	          listitem.put(tptlayout, "2mb cache, 138mb system, 316mb data, 0.1mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v10 stock");
	          listitem.put(tptlayout, "37mb cache, 210mb system, 204mb data, 4mb oem");
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
	          listitem.put(tptname, "CM 7.1 N257 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: CyanogenMod 7.1 Nightly 257");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "MMHMP RLS9 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Moldovan Mile High Mountain Pie RLS9");
	          tpts.add(listitem);
	       
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(V10A);
			      break;
		      case 2:
		    	  showDialog(V10B);
				  break;
		      case 3:
		    	  showDialog(V10C);
				  break;
		      case 4:
		    	  showDialog(V10STOCK);
				  break;
		      case 5:
		    	  showDialog(V2REVERT);
				  break;
		      case 7:
		    	  showDialog(CM7N257);
				  break;
		      case 8:
		    	  showDialog(MMHMP9);
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
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (gen1v10a.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10a.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (gen1v10b.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10b.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10c.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (gen1v10c.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10c.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 4:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 3);
	    		editdownloadint.commit();
	    		if (gen1v10stock.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10stock.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 5:
	    		editdownload.putString("downloadpicked", "Gen2-to-Gen1-TPT-v2-stock.zip");
	    		editdownload.commit();
	    		editdownloadint.putInt("downloadint", 4);
	    		editdownloadint.commit();
	    		if (revertv2.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadrevertv2.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 7:
	    		editdownload.putString("downloadpicked", "cm7-n257-blade-gen1-to-gen2-tpt.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 5);
	    		editdownloadint.commit();
	    		if (cm7n257.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcm7n257.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 8:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-MMHMP-RLS9.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 6);
	    		editdownloadint.commit();
	    		if (mmhmp9.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadmmhmp9.canRead() == true){
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
	  
	    tpts = new ArrayList<HashMap<String, Object>>();
	  
	    HashMap<String, Object> listitem;
	          listitem = new HashMap<String, Object>();
	          CharSequence standard_tpt_heading = getText(R.string.standard_tpt_heading);
	          CharSequence standard_tpt = getText(R.string.standard_tpt);
              listitem.put(tptname, standard_tpt_heading);
              listitem.put(tptlayout, standard_tpt);
              tpts.add(listitem);
              
              listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v10a");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 294mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v10b");
	          listitem.put(tptlayout, "2mb cache, 140mb system, 314mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v10c");
	          listitem.put(tptlayout, "2mb cache, 138mb system, 316mb data, 0.1mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 1 to Gen 2 v10 stock");
	          listitem.put(tptlayout, "37mb cache, 210mb system, 204mb data, 4mb oem");
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
	          listitem.put(tptname, "CM 7.1 N257 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: CyanogenMod 7.1 Nightly 257");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "MMHMP RLS9 Gen 1 to Gen 2");
	          listitem.put(tptlayout, "ROM: Moldovan Mile High Mountain Pie RLS9");
	          tpts.add(listitem);
	       
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(V10A);
			      break;
		      case 2:
		    	  showDialog(V10B);
				  break;
		      case 3:
		    	  showDialog(V10C);
				  break;
		      case 4:
		    	  showDialog(V10STOCK);
				  break;
		      case 5:
		    	  showDialog(V2REVERT);
				  break;
		      case 7:
		    	  showDialog(CM7N257);
				  break;
		      case 8:
		    	  showDialog(MMHMP9);
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
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (gen1v10a.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10a.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (gen1v10b.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10b.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10c.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (gen1v10c.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10c.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 4:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-v10-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 3);
	    		editdownloadint.commit();
	    		if (gen1v10stock.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen1v10stock.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 5:
	    		editdownload.putString("downloadpicked", "Gen2-to-Gen1-TPT-v2-stock.zip");
	    		editdownload.commit();
	    		editdownloadint.putInt("downloadint", 4);
	    		editdownloadint.commit();
	    		if (revertv2.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadrevertv2.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 7:
	    		editdownload.putString("downloadpicked", "cm7-n257-blade-gen1-to-gen2-tpt.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 5);
	    		editdownloadint.commit();
	    		if (cm7n257.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadcm7n257.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 8:
	    		editdownload.putString("downloadpicked", "Gen1-to-Gen2-TPT-MMHMP-RLS9.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 6);
	    		editdownloadint.commit();
	    		if (mmhmp9.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadmmhmp9.canRead() == true){
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
			task.execute(new String[] { "http://copy.com/gegkbgFULjia/Gen1-to-Gen2-TPT-v10a.zip" });
			break;
		case 1:
			task.execute(new String[] { "http://copy.com/ZltEecIByzYr/Gen1-to-Gen2-TPT-v10b.zip" });
			break;
		case 2:
			task.execute(new String[] { "http://copy.com/cd6gvrPOjRbZ/Gen1-to-Gen2-TPT-v10c.zip" });
			break;
		case 3:
			task.execute(new String[] { "http://copy.com/eVYgC3RtHK2Q/Gen1-to-Gen2-TPT-v10-stock.zip" });
			break;
		case 4:
			task.execute(new String[] { "http://copy.com/Ma1xkBkFYpVB/Gen2-to-Gen1-TPT-v2-stock.zip" });
			break;
		case 5:
			task.execute(new String[] { "http://copy.com/9dJiu3fvhz9w/cm7-n257-blade-gen1-to-gen2-tpt.zip" });
			break;
		case 6:
			task.execute(new String[] { "http://copy.com/VIUBxyNKewTc/Gen1-to-Gen2-TPT-MMHMP-RLS9.zip" });
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
                return dialog;
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
                return downloadbuilder.create();
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
                return downloadfailedbuilder.create();
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
                return filetherebuilder.create();
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
          	    		editlocale.putString("locale", "pt");
          	    		editlocale.commit();
          	    		Intent n = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(n);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 6:
          	    		editlocale.putString("locale", "es");
          	    		editlocale.commit();
          	    		Intent o = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(o);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 7:
          	    		editlocale.putString("locale", "sr");
          	    		editlocale.commit();
          	    		Intent p = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(p);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 8:
          	    		editlocale.putString("locale", "cs");
          	    		editlocale.commit();
          	    		Intent q = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(q);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 9:
          	    		editlocale.putString("locale", "pl");
          	    		editlocale.commit();
          	    		Intent r = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(r);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 10:
          	    		editlocale.putString("locale", "hu");
          	    		editlocale.commit();
          	    		Intent s = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(s);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 11:
          	    		editlocale.putString("locale", "sv");
          	    		editlocale.commit();
          	    		Intent t = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(t);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 12:
          	    		editlocale.putString("locale", "it");
          	    		editlocale.commit();
          	    		Intent u = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(u);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 13:
          	    		editlocale.putString("locale", "nl");
          	    		editlocale.commit();
          	    		Intent v = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(v);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 14:
          	    		editlocale.putString("locale", "pt_BR");
          	    		editlocale.commit();
          	    		Intent w = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(w);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 15:
          	    		editlocale.putString("locale", "el");
          	    		editlocale.commit();
          	    		Intent x = new Intent(DirectDownloader.this, HomeActivity.class);
          	    	    startActivity(x);
          	    	    DirectDownloader.this.finish();
          	    		break;
          	    	case 16:
          	    		// Do nothing
          	    		break;
          	    	}
          	      }
          	  });
          	  return localebuilder.create();
            case V10A:
                Builder builder1 = new AlertDialog.Builder(DirectDownloader.this);
                builder1.setTitle("Gen 1 to Gen 2 v10a");
                builder1.setMessage(Html.fromHtml("<b>Size:</b> 16.15MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Normal Android"));
                builder1.setCancelable(false);
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder1.create();
            case V10B:
                Builder builder2 = new AlertDialog.Builder(DirectDownloader.this);
                builder2.setTitle("Gen 1 to Gen 2 v10b");
                builder2.setMessage(Html.fromHtml("<b>Size:</b> 16.15MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Normal Android"));
                builder2.setCancelable(false);
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder2.create();
            case V10C:
                Builder builder3 = new AlertDialog.Builder(DirectDownloader.this);
                builder3.setTitle("Gen 1 to Gen 2 v10c");
                builder3.setMessage(Html.fromHtml("<b>Size:</b> 16.15MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Normal Android"));
                builder3.setCancelable(false);
                builder3.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                return builder3.create();
            case V10STOCK:
                Builder builder4 = new AlertDialog.Builder(DirectDownloader.this);
                builder4.setTitle("Gen 1 to Gen 2 v10 stock");
                builder4.setMessage(Html.fromHtml("<b>Size:</b> 16.16MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Normal Android"));
                builder4.setCancelable(false);
                builder4.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                return builder4.create();
            case V2REVERT:
                Builder builder5 = new AlertDialog.Builder(DirectDownloader.this);
                builder5.setTitle("Gen 2 to Gen 1 v2 stock");
                builder5.setMessage(Html.fromHtml("<b>Size:</b> 16.39MB<br /><b>Recovery:</b> ClockworkMod <br /><b>Splash:</b> Normal Android"));
                builder5.setCancelable(false);
                builder5.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                return builder5.create();
            case CM7N257:
                Builder builder6 = new AlertDialog.Builder(DirectDownloader.this);
                builder6.setTitle("CM 7.1 N257 Gen 1 to Gen 2");
                builder6.setMessage(Html.fromHtml("<b>Size:</b> 106.57MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> CyanogenMod<br /><b>Partitions:</b> 2mb cache, 160mb system, 294mb data, 0.1mb oem"));
                builder6.setCancelable(false);
                builder6.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder6.create();
            case MMHMP9:
                Builder builder8 = new AlertDialog.Builder(DirectDownloader.this);
                builder8.setTitle("MMHMP RLS9 Gen 1 to Gen 2");
                builder8.setMessage(Html.fromHtml("<b>Size:</b> 96.06MB<br /><b>Recovery:</b> ClockworkMod v4.0.1.5<br /><b>Splash:</b> Normal Android<br /><b>Partitions:</b> 2mb cache, 138mb system, 316mb data, 0.1mb oem"));
                builder8.setCancelable(false);
                builder8.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder8.create();
        }
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
    			Toast.makeText(DirectDownloader.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
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
		case R.id.rate:
			Intent rate = new Intent(Intent.ACTION_VIEW);
    		rate.setData(Uri.parse("market://details?id=com.amphoras.tpthelper"));
    		startActivity(rate);
    		break;
		}
		return true;
	}
}