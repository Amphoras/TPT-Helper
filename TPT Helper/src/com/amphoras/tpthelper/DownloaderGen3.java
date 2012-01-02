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

public class DownloaderGen3 extends ListActivity {
	SharedPreferences preferences;
	private ProgressDialog dialog;
	final File dir = Environment.getExternalStorageDirectory();
	final File gen3v1a = new File(dir, "Gen3-v1a.zip");
	final File downloadgen3v1a = new File(dir, "download/Gen3-v1a.zip");
	final File gen3v2a = new File(dir, "Gen3-v2a.zip");
	final File downloadgen3v2a = new File(dir, "download/Gen3-v2a.zip");
	final File gen3stock = new File(dir, "Gen3-stock.zip");
	final File downloadgen3stock = new File(dir, "download/Gen3-stock.zip");
	private ArrayList <HashMap<String, Object>> tpts;
	private static final String tptname = "tptname";
	private static final String tptlayout = "tptlayout";
	private final int DOWNLOADING = 1;
	private final int DOWNLOAD_COMPLETE = 2;
	private final int DOWNLOAD_FAILED = 3;
	private final int FILE_FOUND = 4;
	private final int CHANGE_LOCALE = 5;
	private final int GEN3V1A = 101;
	private final int GEN3V2A = 102;
	private final int GEN3STOCK = 103;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main);
		dialog = new ProgressDialog(DownloaderGen3.this);
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
	          listitem.put(tptname, "Gen 3 v1a");
	          listitem.put(tptlayout, "2mb cache, 128mb system, 311mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 3 v2a");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 279mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 3 stock");
	          listitem.put(tptlayout, "37mb cache, 210mb system, 204mb data, 4mb oem");
	          tpts.add(listitem);
	       
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(GEN3V1A);
			      break;
		      case 2:
		    	  showDialog(GEN3V2A);
				  break;
		      case 3:
		    	  showDialog(GEN3STOCK);
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
	    		editdownload.putString("downloadpicked", "Gen3-v1a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (gen3v1a.canRead() == true){
	    		    showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen3v1a.canRead() == true){
    	    	        showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Gen3-v2a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (gen3v2a.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen3v2a.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Gen3-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (gen3stock.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen3stock.canRead() == true){
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
	          listitem.put(tptname, "Gen 2 v1a");
	          listitem.put(tptlayout, "2mb cache, 128mb system, 311mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 2 v2a");
	          listitem.put(tptlayout, "2mb cache, 160mb system, 279mb data, 0.1mb oem");
	          tpts.add(listitem);
	  
	          listitem = new HashMap<String, Object>();
	          listitem.put(tptname, "Gen 2 stock");
	          listitem.put(tptlayout, "37mb cache, 210mb system, 204mb data, 4mb oem");
	          tpts.add(listitem);
	       
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
		      case 1:
		    	  showDialog(GEN3V1A);
			      break;
		      case 2:
		    	  showDialog(GEN3V2A);
				  break;
		      case 3:
		    	  showDialog(GEN3STOCK);
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
	    		editdownload.putString("downloadpicked", "Gen3-v1a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 0);
	    		editdownloadint.commit();
	    		if (gen3v1a.canRead() == true){
	    		    showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen3v1a.canRead() == true){
    	    	        showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 2:
	    		editdownload.putString("downloadpicked", "Gen3-v2a.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 1);
	    		editdownloadint.commit();
	    		if (gen3v2a.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen3v2a.canRead() == true){
	    				showDialog(FILE_FOUND);
	    			} else {
	    				DownloadFile();
	    			}
	    		}
	    		break;
	    	case 3:
	    		editdownload.putString("downloadpicked", "Gen3-stock.zip");
	    		editdownload.commit();
				editdownloadint.putInt("downloadint", 2);
	    		editdownloadint.commit();
	    		if (gen3stock.canRead() == true){
	    			showDialog(FILE_FOUND);
	    		} else {
	    			if (downloadgen3stock.canRead() == true){
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
			task.execute(new String[] { "https://www.sugarsync.com/pf/D6476836_1861667_723585" });
			break;
		case 1:
			task.execute(new String[] { "https://www.sugarsync.com/pf/D6476836_1861667_723573" });
			break;
		case 2:
			task.execute(new String[] { "https://www.sugarsync.com/pf/D6476836_1861667_779071" });
			break;
		}
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DOWNLOADING:
                dialog = new ProgressDialog(DownloaderGen3.this);
                CharSequence downloadmessage = getText(R.string.downloading);
                dialog.setMessage(downloadmessage);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setCancelable(false);
                return dialog;
            default:
                return null;
            case DOWNLOAD_COMPLETE:
            	String downloadpicked = preferences.getString("downloadpicked", "");
            	Builder downloadbuilder = new AlertDialog.Builder(DownloaderGen3.this);
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
            	Builder downloadfailedbuilder = new AlertDialog.Builder(DownloaderGen3.this);
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
            	Builder filetherebuilder = new AlertDialog.Builder(DownloaderGen3.this);
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
                    	DownloaderGen3.this.finish();
                    }
                });
            	return filetherebuilder.create();
            case CHANGE_LOCALE:
          	    // change the locale used in the app
              Builder localebuilder = new AlertDialog.Builder(DownloaderGen3.this);
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
          	    		Intent i = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(i);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 1:
          	    		editlocale.putString("locale", "fr");
          	    		editlocale.commit();
          	    		Intent j = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(j);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 2:
          	    		editlocale.putString("locale", "de");
          	    		editlocale.commit();
          	    		Intent k = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(k);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 3:
          	    		editlocale.putString("locale", "ru");
          	    		editlocale.commit();
          	    		Intent l = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(l);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 4:
          	    		editlocale.putString("locale", "zh");
          	    		editlocale.commit();
          	    		Intent m = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(m);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 5:
          	    		editlocale.putString("locale", "pt");
          	    		editlocale.commit();
          	    		Intent n = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(n);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 6:
          	    		editlocale.putString("locale", "es");
          	    		editlocale.commit();
          	    		Intent o = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(o);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 7:
          	    		editlocale.putString("locale", "sr");
          	    		editlocale.commit();
          	    		Intent p = new Intent(DownloaderGen3.this, HomeActivity.class);
          	    	    startActivity(p);
          	    	    DownloaderGen3.this.finish();
          	    		break;
          	    	case 8:
          	    		// Do nothing
          	    		break;
          	    	}
          	      }
          	  });
          	  return localebuilder.create();
            case GEN3V1A:
                Builder builder1 = new AlertDialog.Builder(DownloaderGen3.this);
                builder1.setTitle("Gen 2 custom");
                builder1.setMessage(Html.fromHtml("<b>Size:</b> 16.14MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Normal Android<br /><b>Fastboot:</b> Enabled on vol+"));
                builder1.setCancelable(false);
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder1.create();
            case GEN3V2A:
                Builder builder2 = new AlertDialog.Builder(DownloaderGen3.this);
                builder2.setTitle("Gen 2 custom b");
                builder2.setMessage(Html.fromHtml("<b>Size:</b> 16.13MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Normal Android<br /><b>Fastboot:</b> Enabled on vol+"));
                builder2.setCancelable(false);
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	// Do nothing
                    }
                });
                return builder2.create();
            case GEN3STOCK:
                Builder builder3 = new AlertDialog.Builder(DownloaderGen3.this);
                builder3.setTitle("Gen 2 stock");
                builder3.setMessage(Html.fromHtml("<b>Size:</b> 16.14MB<br /><b>Recovery:</b> ClockworkMod v5.0.2.0<br /><b>Splash:</b> Normal Android<br /><b>Fastboot:</b> Enabled on vol+"));
                builder3.setCancelable(false);
                builder3.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  	    // Do nothing
                    }
                });
                return builder3.create();
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
			Intent j = new Intent(DownloaderGen3.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(DownloaderGen3.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(DownloaderGen3.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(DownloaderGen3.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(DownloaderGen3.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
}