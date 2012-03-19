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

public class DownloaderSkate extends ListActivity {
	SharedPreferences preferences;
	private ProgressDialog dialog;
	final File dir = Environment.getExternalStorageDirectory();
	final File skatev1a = new File(dir, "Skate-v1a.zip");
	final File downloadskatev1a = new File(dir, "download/Skate-v1a.zip");
	final File skatev2a = new File(dir, "Skate-v2a.zip");
	final File downloadskatev2a = new File(dir, "download/Skate-v2a.zip");
	final File skatev1b = new File(dir, "Skate-v1b.zip");
	final File downloadskatev1b = new File(dir, "download/Skate-v1b.zip");
	final File skatev2b = new File(dir, "Skate-v2b.zip");
	final File downloadskatev2b = new File(dir, "download/Skate-v2b.zip");
	private ArrayList <HashMap<String, Object>> tpts;
	private static final String tptname = "tptname";
	private static final String tptlayout = "tptlayout";
	private final int DOWNLOADING = 1;
	private final int DOWNLOAD_COMPLETE = 2;
	private final int DOWNLOAD_FAILED = 3;
	private final int FILE_FOUND = 4;
	private final int CHANGE_LOCALE = 5;
	private final int V1A = 101;
	private final int V2A = 102;
	private final int V1B = 103;
	private final int V2B = 104;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main);
		dialog = new ProgressDialog(DownloaderSkate.this);
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
	          listitem.put(tptname, "Skate v1a");
	          listitem.put(tptlayout, "35mb cache, 150mb system, 255mb data, 1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Skate v2a");
	          listitem.put(tptlayout, "35mb cache, 200mb system, 205mb data, 1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Skate v1b");
	          listitem.put(tptlayout, "2mb cache, 150mb system, 288mb data, 1mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Skate v2b");
	          listitem.put(tptlayout, "2mb cache, 200mb system, 238mb data, 1mb oem");
	          tpts.add(listitem);
	 
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(V1A);
			      break;
		      case 2:
		    	  showDialog(V2A);
				  break;
		      case 3:
		    	  showDialog(V1B);
				  break;
		      case 4:
		    	  showDialog(V2B);
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
	    		editdownload.putString("downloadpicked", "Skate-v1a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (skatev1a.canRead() == true){
	    		    showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev1a.canRead() == true){
    	    	        showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Skate-v2a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (skatev2a.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev2a.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Skate-v1b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (skatev1b.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev1b.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 4:
	    		editdownload.putString("downloadpicked", "Skate-v2b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 3);
	    		editdownloadint.commit();
	    		if (skatev2b.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev2b.canRead() == true){
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
	          listitem.put(tptname, "Skate v1a");
	          listitem.put(tptlayout, "35mb cache, 150mb system, 255mb data, 1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Skate v2a");
	          listitem.put(tptlayout, "35mb cache, 200mb system, 205mb data, 1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Skate v1b");
	          listitem.put(tptlayout, "2mb cache, 150mb system, 288mb data, 1mb oem");
	          tpts.add(listitem);
	          
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Skate v2b");
	          listitem.put(tptlayout, "2mb cache, 200mb system, 238mb data, 1mb oem");
	          tpts.add(listitem);
	 
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(V1A);
			      break;
		      case 2:
		    	  showDialog(V2A);
				  break;
		      case 3:
		    	  showDialog(V1B);
				  break;
		      case 4:
		    	  showDialog(V2B);
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
	    		editdownload.putString("downloadpicked", "Skate-v1a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (skatev1a.canRead() == true){
	    		    showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev1a.canRead() == true){
    	    	        showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Skate-v2a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (skatev2a.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev2a.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Skate-v1b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (skatev1b.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev1b.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 4:
	    		editdownload.putString("downloadpicked", "Skate-v2b.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 3);
	    		editdownloadint.commit();
	    		if (skatev2b.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadskatev2b.canRead() == true){
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
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Skate/Skate-v1a.zip" });
			break;
		case 1:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Skate/Skate-v2a.zip" });
			break;
		case 2:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Skate/Skate-v1b.zip" });
			break;
		case 3:
			task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/Skate/Skate-v2b.zip" });
			break;
		}
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DOWNLOADING:
                dialog = new ProgressDialog(DownloaderSkate.this);
                CharSequence downloadmessage = getText(R.string.downloading);
                dialog.setMessage(downloadmessage);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setCancelable(false);
                return dialog;
            default:
                return null;
            case DOWNLOAD_COMPLETE:
            	String downloadpicked = preferences.getString("downloadpicked", "");
            	Builder downloadbuilder = new AlertDialog.Builder(DownloaderSkate.this);
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
            	Builder downloadfailedbuilder = new AlertDialog.Builder(DownloaderSkate.this);
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
            	Builder filetherebuilder = new AlertDialog.Builder(DownloaderSkate.this);
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
                    	DownloaderSkate.this.finish();
                    }
                });
                return filetherebuilder.create();
            case CHANGE_LOCALE:
          	    // change the locale used in the app
              Builder localebuilder = new AlertDialog.Builder(DownloaderSkate.this);
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
          	    		Intent i = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(i);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 1:
          	    		editlocale.putString("locale", "fr");
          	    		editlocale.commit();
          	    		Intent j = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(j);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 2:
          	    		editlocale.putString("locale", "de");
          	    		editlocale.commit();
          	    		Intent k = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(k);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 3:
          	    		editlocale.putString("locale", "ru");
          	    		editlocale.commit();
          	    		Intent l = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(l);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 4:
          	    		editlocale.putString("locale", "zh");
          	    		editlocale.commit();
          	    		Intent m = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(m);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 5:
          	    		editlocale.putString("locale", "pt");
          	    		editlocale.commit();
          	    		Intent n = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(n);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 6:
          	    		editlocale.putString("locale", "es");
          	    		editlocale.commit();
          	    		Intent o = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(o);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 7:
          	    		editlocale.putString("locale", "sr");
          	    		editlocale.commit();
          	    		Intent p = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(p);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 8:
          	    		editlocale.putString("locale", "cs");
          	    		editlocale.commit();
          	    		Intent q = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(q);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 9:
          	    		editlocale.putString("locale", "pl");
          	    		editlocale.commit();
          	    		Intent r = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(r);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 10:
          	    		editlocale.putString("locale", "hu");
          	    		editlocale.commit();
          	    		Intent s = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(s);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 11:
          	    		editlocale.putString("locale", "sv");
          	    		editlocale.commit();
          	    		Intent t = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(t);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 12:
          	    		editlocale.putString("locale", "it");
          	    		editlocale.commit();
          	    		Intent u = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(u);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 13:
          	    		editlocale.putString("locale", "nl");
          	    		editlocale.commit();
          	    		Intent v = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(v);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 14:
          	    		editlocale.putString("locale", "pt_BR");
          	    		editlocale.commit();
          	    		Intent w = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(w);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 15:
          	    		editlocale.putString("locale", "el");
          	    		editlocale.commit();
          	    		Intent x = new Intent(DownloaderSkate.this, HomeActivity.class);
          	    	    startActivity(x);
          	    	    DownloaderSkate.this.finish();
          	    		break;
          	    	case 16:
          	    		// Do nothing
          	    		break;
          	    	}
          	      }
          	  });
          	  return localebuilder.create();
            case V1A:
                Builder builder1 = new AlertDialog.Builder(DownloaderSkate.this);
                builder1.setTitle("Skate v1a");
                builder1.setMessage(Html.fromHtml("<b>Size:</b> 9.05MB<br /><b>Recovery:</b> ClockworkMod v4.0.1.4<br /><b>Splash:</b> Unchanged"));
                builder1.setCancelable(false);
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder1.create();
            case V2A:
                Builder builder2 = new AlertDialog.Builder(DownloaderSkate.this);
                builder2.setTitle("Skate v1b");
                builder2.setMessage(Html.fromHtml("<b>Size:</b> 9.05MB<br /><b>Recovery:</b> ClockworkMod v4.0.1.4<br /><b>Splash:</b> Unchanged"));
                builder2.setCancelable(false);
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder2.create();
            case V1B:
                Builder builder3 = new AlertDialog.Builder(DownloaderSkate.this);
                builder3.setTitle("Skate v2a");
                builder3.setMessage(Html.fromHtml("<b>Size:</b> 9.05MB<br /><b>Recovery:</b> ClockworkMod v4.0.1.4<br /><b>Splash:</b> Unchanged"));
                builder3.setCancelable(false);
                builder3.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                return builder3.create();
            case V2B:
                Builder builder4 = new AlertDialog.Builder(DownloaderSkate.this);
                builder4.setTitle("Skate v2b");
                builder4.setMessage(Html.fromHtml("<b>Size:</b> 9.05MB<br /><b>Recovery:</b> ClockworkMod v4.0.1.4<br /><b>Splash:</b> Unchanged"));
                builder4.setCancelable(false);
                builder4.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                return builder4.create();
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
    			Toast.makeText(DownloaderSkate.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
		/* case R.id.troubleshooting:
			Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
			startActivity(j);
			break; */
		case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
		case R.id.about:
			Intent j = new Intent(DownloaderSkate.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(DownloaderSkate.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(DownloaderSkate.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(DownloaderSkate.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(DownloaderSkate.this, License.class);
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