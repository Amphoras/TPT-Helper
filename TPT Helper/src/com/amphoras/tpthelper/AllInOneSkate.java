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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AllInOneSkate extends Activity {
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
	private static ProgressDialog dialog;
	private String unziplocation = Environment.getExternalStorageDirectory() + "/";
	private static File nandroid = new File(Environment.getExternalStorageDirectory(), "image/nandroid.md5");
	private TextView textview;
	private TextView textview2;
	private TextView textview3;
	private ProgressDialog downloaddialog;
	private final int DOWNLOADING = 0;
	private final int PICK_TPT = 1;
	private final int TPT_FOUND = 2;
	private final int MD5_MISMATCH = 3;
	private final int UNZIP_FAILED = 4;
	private final int NO_NANDROID = 5;
	private final int IMAGE_CHECKED = 6;
	private final int DOWNLOAD_FAILED = 7;
	private final int CHANGE_LOCALE = 8;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.allinone);
	  preferences = PreferenceManager.getDefaultSharedPreferences(this);
	  textview = (TextView) findViewById(R.id.downloadresultaio);
	  textview2 = (TextView) findViewById(R.id.md5resultaio);
      textview3 = (TextView) findViewById(R.id.unzipdoneaio);
	  showDialog(PICK_TPT);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.allinone);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		CharSequence cancel = getText(R.string.cancel);
        switch (id) {
        case DOWNLOADING:
            downloaddialog = new ProgressDialog(AllInOneSkate.this);
            CharSequence downloadmessage = getText(R.string.downloading);
            downloaddialog.setMessage(downloadmessage);
            downloaddialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            downloaddialog.setCancelable(false);
            return downloaddialog;
        case PICK_TPT:
        	Builder builder1 = new AlertDialog.Builder(AllInOneSkate.this);
            builder1.setTitle(R.string.pickallinone);
            builder1.setCancelable(false);
            final CharSequence[] zips1 = {"Skate-v1a.zip", "Skate-v2a.zip", "Skate-v1b.zip", "Skate-v2b.zip", cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editmd5 = preferences.edit();
        	    	Editor editdownload = preferences.edit();
        	    	Editor editdownloadint = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editdownload.putString("downloadpicked", "Skate-v1a.zip");
        	    		editdownload.commit();
        				editdownloadint.putInt("downloadint", 0);
        	    		editdownloadint.commit();
        	    		editmd5.putString("expectedmd5", "9ded013763541d3ccacea94d99d6f645");
        	    		editmd5.commit();
        	    		if (skatev1a.canRead() == true){
        	    		    showDialog(TPT_FOUND);
        	    		} else {
        	    			if (downloadskatev1a.canRead() == true){
            	    	        showDialog(TPT_FOUND);
        	    			} else {
        	    		     	DownloadFile();
        	    			}
        	    		}
        	    		break;
        	    	case 1:
        	    		editdownload.putString("downloadpicked", "Skate-v2a.zip");
        	    		editdownload.commit();
        				editdownloadint.putInt("downloadint", 1);
        	    		editdownloadint.commit();
        	    		editmd5.putString("expectedmd5", "790f6b04654a2abfde9ed2ae75b91fec");
        	    		editmd5.commit();
        	    		if (skatev2a.canRead() == true){
        	    			showDialog(TPT_FOUND);
        	    		} else {
        	    			if (downloadskatev2a.canRead() == true){
        	    				showDialog(TPT_FOUND);
        	    			} else {
        	    				DownloadFile();
        	    			}
        	    		}
        	    		break;
        	    	case 2:
        	    		editdownload.putString("downloadpicked", "Skate-v1b.zip");
        	    		editdownload.commit();
        				editdownloadint.putInt("downloadint", 2);
        	    		editdownloadint.commit();
        	    		editmd5.putString("expectedmd5", "a9a4fe7c802b035b8dd0298ea39fbd5a");
        	    		editmd5.commit();
        	    		if (skatev1b.canRead() == true){
        	    			showDialog(TPT_FOUND);
        	    		} else {
        	    			if (downloadskatev1b.canRead() == true){
        	    				showDialog(TPT_FOUND);
        	    			} else {
        	    				DownloadFile();
        	    			}
        	    		}
        	    		break;
        	    	case 3:
        	    		editdownload.putString("downloadpicked", "Skate-v2b.zip");
        	    		editdownload.commit();
        				editdownloadint.putInt("downloadint", 3);
        	    		editdownloadint.commit();
        	    		editmd5.putString("expectedmd5", "48bd788c6c3753d1f599b71f0560241e");
        	    		editmd5.commit();
        	    		if (skatev2b.canRead() == true){
        	    			showDialog(TPT_FOUND);
        	    		} else {
        	    			if (downloadskatev2b.canRead() == true){
        	    				showDialog(TPT_FOUND);
        	    			} else {
        	    				DownloadFile();
        	    			}
        	    		}
        	    		break;
        	    	case 4:
                		AllInOneSkate.this.finish();
                		break;
        	    	}
        	    }
        	});
        	return builder1.create();
        case TPT_FOUND:
        	final String filepicked = preferences.getString("downloadpicked", "");
        	Builder filetherebuilder = new AlertDialog.Builder(AllInOneSkate.this);
        	filetherebuilder.setTitle(R.string.file_there_heading);
        	CharSequence filethere1 = getText(R.string.file_there1);
        	CharSequence filethere2 = getText(R.string.file_there2);
        	filetherebuilder.setMessage(filethere1 + " " + filepicked + " " + filethere2);
        	filetherebuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	DownloadFile();
                }
            });
        	filetherebuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Editor setpath = preferences.edit();
                	setpath.putBoolean("nodownload", true);
                	setpath.commit();
                	File TPT = new File(dir, filepicked);
                	if (TPT.canRead() == true){
    	    		    Editor edit = preferences.edit();
    	    			edit.putString("filepicked", filepicked);
    	    			edit.commit();
    	    			md5sum();
    	    		} else {
        	    	    Editor edit = preferences.edit();
        	    		edit.putString("filepicked", "/download" + filepicked);
        	            edit.commit();
        	    		md5sum();
    	    		}
                }
            });
            return filetherebuilder.create();
        case MD5_MISMATCH:
        	Builder builder3 = new AlertDialog.Builder(AllInOneSkate.this);
            builder3.setTitle(R.string.md5_mismatch_heading);
            builder3.setCancelable(false);
            builder3.setMessage(R.string.md5_mismatch);
            builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Intent i = new Intent(AllInOneSkate.this, DownloaderSkate.class);
              	    startActivity(i);
              	    AllInOneSkate.this.finish();
                }
            });
            builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    AllInOneSkate.this.finish();
                }
            });
            return builder3.create();
        case UNZIP_FAILED:
          Builder failedbuilder = new AlertDialog.Builder(AllInOneSkate.this);
          failedbuilder.setTitle(R.string.unzipbad);
            // show dialog about failure
          failedbuilder.setMessage(R.string.unzip_fail);
          failedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  AllInOneSkate.this.finish();
              }
          });
          return failedbuilder.create();
        case NO_NANDROID:
    	    // show error dialog after failing to find nandroid.md5 file
          Builder nofilebuilder = new AlertDialog.Builder(AllInOneSkate.this);
          nofilebuilder.setTitle(R.string.error);
          nofilebuilder.setMessage(R.string.no_nandroid2);
          nofilebuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
            	  AllInOneSkate.this.finish();
              }
          });
          return nofilebuilder.create();
        case IMAGE_CHECKED:
      	    // get no. of files and matches
      	  Long a = preferences.getLong("no of files", 0);
      	  Long b = preferences.getLong("no of files checked", 0);
      	  Long c = preferences.getLong("no of matches", 0);
      	  String file1 = preferences.getString("file1", "");
      	  String file2 = preferences.getString("file2", "");
      	  String file3 = preferences.getString("file3", "");
      	  String file4 = preferences.getString("file4", "");
      	  String file5 = preferences.getString("file5", "");
      	  String file6 = preferences.getString("file6", "");
      	  String file7 = preferences.getString("file7", "");
      	  String file8 = preferences.getString("file8", "");
      	  String file9 = preferences.getString("file9", "");
      	  String file10 = preferences.getString("file10", "");
      	  String file11 = preferences.getString("file11", "");
      	  String file12 = preferences.getString("file12", "");
      	  String file13 = preferences.getString("file13", "");
      	  String file14 = preferences.getString("file14", "");
      	  String file15 = preferences.getString("file15", "");
      	  String file16 = preferences.getString("file16", "");
      	  String file17 = preferences.getString("file17", "");
      	  String file18 = preferences.getString("file18", "");
      	  String match1 = preferences.getString("match1", "");
      	  String match2 = preferences.getString("match2", "");
      	  String match3 = preferences.getString("match3", "");
      	  String match4 = preferences.getString("match4", "");
      	  String match5 = preferences.getString("match5", "");
      	  String match6 = preferences.getString("match6", "");
      	  String match7 = preferences.getString("match7", "");
      	  String match8 = preferences.getString("match8", "");
      	  String match9 = preferences.getString("match9", "");
      	  String match10 = preferences.getString("match10", "");
      	  String match11 = preferences.getString("match11", "");
      	  String match12 = preferences.getString("match12", "");
      	  String match13 = preferences.getString("match13", "");
      	  String match14 = preferences.getString("match14", "");
      	  String match15 = preferences.getString("match15", "");
      	  String match16 = preferences.getString("match16", "");
      	  String match17 = preferences.getString("match17", "");
      	  String match18 = preferences.getString("match18", "");
          Builder builder = new AlertDialog.Builder(AllInOneSkate.this);
          builder.setTitle(R.string.files_verified);
            // show dialog with no. of matches/no. of files
          if (a == b) {
          	CharSequence no_of_matches = getText(R.string.no_of_matches);
          	builder.setMessage(no_of_matches + " " + c + "/" + b);
          } else {
          	if ((a - b) > 1) {
          		CharSequence no_of_matches = getText(R.string.no_of_matches);
          		CharSequence files_not_found = getText(R.string.files_not_found);
          		builder.setMessage(no_of_matches + " " + c + "/" + b + ". " + (a - b) + " " + files_not_found);
          	} else {
          		CharSequence no_of_matches = getText(R.string.no_of_matches);
          		CharSequence file_not_found = getText(R.string.file_not_found);
          		builder.setMessage(no_of_matches + " " + c + "/" + b + ". " + (a - b) + " " + file_not_found);
          	}
          }
          builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              	// Do nothing
              }
          });
            // set number of files, matches and missing files
          TextView nooffiles = (TextView) findViewById(R.id.nooffiles);
          String btext = b.toString();
          nooffiles.setText(btext);
          TextView noofmatches = (TextView) findViewById(R.id.noofmatches);
          String ctext = c.toString();
          noofmatches.setText(ctext);
          TextView missingfiles = (TextView) findViewById(R.id.missingfiles);
          Long ab = (a - b);
          String abtext = ab.toString();
          missingfiles.setText(abtext);
            // set the name of each file checked
          TextView textfile1 = (TextView) findViewById(R.id.file1);
          textfile1.setText(file1);
          TextView textfile2 = (TextView) findViewById(R.id.file2);
          textfile2.setText(file2);
          TextView textfile3 = (TextView) findViewById(R.id.file3);
          textfile3.setText(file3);
          TextView textfile4 = (TextView) findViewById(R.id.file4);
          textfile4.setText(file4);
          TextView textfile5 = (TextView) findViewById(R.id.file5);
          textfile5.setText(file5);
          TextView textfile6 = (TextView) findViewById(R.id.file6);
          textfile6.setText(file6);
          TextView textfile7 = (TextView) findViewById(R.id.file7);
          textfile7.setText(file7);
          TextView textfile8 = (TextView) findViewById(R.id.file8);
          textfile8.setText(file8);
          TextView textfile9 = (TextView) findViewById(R.id.file9);
          textfile9.setText(file9);
          TextView textfile10 = (TextView) findViewById(R.id.file10);
          textfile10.setText(file10);
          TextView textfile11 = (TextView) findViewById(R.id.file11);
          textfile11.setText(file11);
          TextView textfile12 = (TextView) findViewById(R.id.file12);
          textfile12.setText(file12);
          TextView textfile13 = (TextView) findViewById(R.id.file13);
          textfile13.setText(file13);
          TextView textfile14 = (TextView) findViewById(R.id.file14);
          textfile14.setText(file14);
          TextView textfile15 = (TextView) findViewById(R.id.file15);
          textfile15.setText(file15);
          TextView textfile16 = (TextView) findViewById(R.id.file16);
          textfile16.setText(file16);
          TextView textfile17 = (TextView) findViewById(R.id.file17);
          textfile17.setText(file17);
          TextView textfile18 = (TextView) findViewById(R.id.file18);
          textfile18.setText(file18);
          TextView textmatch1 = (TextView) findViewById(R.id.match1);
          textmatch1.setText(match1);
          TextView textmatch2 = (TextView) findViewById(R.id.match2);
          textmatch2.setText(match2);
          TextView textmatch3 = (TextView) findViewById(R.id.match3);
          textmatch3.setText(match3);
          TextView textmatch4 = (TextView) findViewById(R.id.match4);
          textmatch4.setText(match4);
          TextView textmatch5 = (TextView) findViewById(R.id.match5);
          textmatch5.setText(match5);
          TextView textmatch6 = (TextView) findViewById(R.id.match6);
          textmatch6.setText(match6);
          TextView textmatch7 = (TextView) findViewById(R.id.match7);
          textmatch7.setText(match7);
          TextView textmatch8 = (TextView) findViewById(R.id.match8);
          textmatch8.setText(match8);
          TextView textmatch9 = (TextView) findViewById(R.id.match9);
          textmatch9.setText(match9);
          TextView textmatch10 = (TextView) findViewById(R.id.match10);
          textmatch10.setText(match10);
          TextView textmatch11 = (TextView) findViewById(R.id.match11);
          textmatch11.setText(match11);
          TextView textmatch12 = (TextView) findViewById(R.id.match12);
          textmatch12.setText(match12);
          TextView textmatch13 = (TextView) findViewById(R.id.match13);
          textmatch13.setText(match13);
          TextView textmatch14 = (TextView) findViewById(R.id.match14);
          textmatch14.setText(match14);
          TextView textmatch15 = (TextView) findViewById(R.id.match15);
          textmatch15.setText(match15);
          TextView textmatch16 = (TextView) findViewById(R.id.match16);
          textmatch16.setText(match16);
          TextView textmatch17 = (TextView) findViewById(R.id.match17);
          textmatch17.setText(match17);
          TextView textmatch18 = (TextView) findViewById(R.id.match18);
          textmatch18.setText(match18);
          return builder.create();
        case DOWNLOAD_FAILED:
        	String downloadfailed = preferences.getString("downloadpicked", "");
        	Builder downloadfailedbuilder = new AlertDialog.Builder(AllInOneSkate.this);
        	downloadfailedbuilder.setTitle(R.string.download_failed_heading);
            CharSequence download_failed = getText(R.string.download_failed);
            downloadfailedbuilder.setMessage(download_failed + " " + downloadfailed);
            downloadfailedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return downloadfailedbuilder.create();
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(AllInOneSkate.this);
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
          final CharSequence[] locales = {english, french, german, russian, chinese, portuguese, spanish, serbian, czech, cancel};
      	  localebuilder.setItems(locales, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor editlocale = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		editlocale.putString("locale", "en");
      	    		editlocale.commit();
      	    		Intent i = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 8:
      	    		editlocale.putString("locale", "cs");
      	    		editlocale.commit();
      	    		Intent q = new Intent(AllInOneSkate.this, HomeActivity.class);
      	    	    startActivity(q);
      	    	    AllInOneSkate.this.finish();
      	    		break;
      	    	case 9:
      	    		// Do nothing
      	    		break;
      	    	}
      	      }
      	  });
      	  return localebuilder.create();
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
    		  md5sum();
          }
          break;
      }
    }
	
	public void DownloadFile() {
		DownloadFileTask task = new DownloadFileTask();
		int id = preferences.getInt("downloadint", 0);
		switch (id) {
		case 0:
			task.execute(new String[] { "https://www.sugarsync.com/pf/D6476836_1861667_752084" });
			break;
		case 1:
			task.execute(new String[] { "https://www.sugarsync.com/pf/D6476836_1861667_752040" });
			break;
		case 2:
			task.execute(new String[] { "https://www.sugarsync.com/pf/D6476836_1861667_752088" });
			break;
		case 3:
			task.execute(new String[] { "https://www.sugarsync.com/pf/D6476836_1861667_752096" });
			break;
		}
	}
	
	private class DownloadFileTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
            downloaddialog.setProgress(Integer.parseInt(progress[0]));
       }
		
		@Override
		protected void onPostExecute(String result) {
			downloaddialog.dismiss();
			textview.setText(result);
			if (result.equals("Download Completed")){
				textview.setText("Success");
				md5sum();
			} else {
				textview.setText("Failed");
				showDialog(DOWNLOAD_FAILED);
			}
		}
	}
	
	public void md5sum() {
		CheckMD5Task task = new CheckMD5Task();
		try {
			Boolean setpath = preferences.getBoolean("setpath", false);
			String downloadpicked = "";
			if (setpath == true) {
				downloadpicked = preferences.getString("filepicked", "TPT.zip");
			} else {
				downloadpicked = preferences.getString("downloadpicked", "TPT.zip");
			}
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + downloadpicked);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class CheckMD5Task extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence calculating = getText(R.string.calculating);
			dialog = ProgressDialog.show(AllInOneSkate.this, "", calculating, true);
		}
		
		@Override
		protected String doInBackground(FileInputStream... fins) {
			String response = "";
			for (FileInputStream fin : fins) {
				try {
					  // creates digester that will be used to calculate the md5sum
					MessageDigest digester = MessageDigest.getInstance("MD5");
					  // creates buffer to read from file
				    byte[] buffer = new byte[8192];
				    int i = 0;
				    try {
					    while ((i = fin.read(buffer)) > 0) {
					    	  // while the buffer is not empty, add the data to the digester
					        digester.update(buffer, 0, i);
					    }
				    } catch (IOException e) {
					    
				    }
				      // creates buffer holding md5sum output
				    byte[] output = digester.digest();
				    String md5sum = "";
		            for (int j = 0; j < output.length; j++) {
		            	  // adds each character to the string that will store the md5sum
		                md5sum += Integer.toString((output[j] & 0xff) + 0x100, 16).substring(1);
		            }
		              // saves the md5sum to a preference
		            Editor edit = preferences.edit();
				    edit.putString("checksum", md5sum);
			        edit.commit();
			        response = md5sum;
				} catch (NoSuchAlgorithmException e) {
		            
				}
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			String checksum = preferences.getString("checksum", "");
            String expectedmd5 = preferences.getString("expectedmd5", "");
			if (expectedmd5.equals(checksum)) {
				textview2.setText("Match");
				unzip();
			} else {
				textview2.setText("No Match");
				showDialog(MD5_MISMATCH);
			}
		}
	}
	
	public void unzip() {
		UnzipTask task = new UnzipTask();
		try {
			Boolean setpath = preferences.getBoolean("setpath", false);
			String downloadpicked = "";
			if (setpath == true) {
				downloadpicked = preferences.getString("filepicked", "TPT.zip");
			} else {
				downloadpicked = preferences.getString("downloadpicked", "TPT.zip");
			}
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + downloadpicked);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class UnzipTask extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence unzipping = getText(R.string.unzipping);
			dialog = ProgressDialog.show(AllInOneSkate.this, "", unzipping, true);
		}
		
		@Override
		protected String doInBackground(FileInputStream... fins) {
			String response = "";
			for (FileInputStream fin : fins) {
				  // delete the image directory if it already exists
				File file = new File(Environment.getExternalStorageDirectory(), "/image");
				deleteDirectory(file);
				try  {
			    	// sets chosen zip as input
			      ZipInputStream zin = new ZipInputStream(fin);
			      ZipEntry ze = null;
			        // as long as there is another entry
			      while ((ze = zin.getNextEntry()) != null) {
			    	  // check if the next entry is a directory
			        if(ze.isDirectory()) {
			        	  // if so, create it if it doesn't exist
			            MakeDirectory(ze.getName());
			        } else {
			        	// open output to location
			          FileOutputStream fos = new FileOutputStream(unziplocation + ze.getName());
			          byte[] buffer = new byte[1024];
			          int length;
			            // read zip input to buffer
			          while ((length = zin.read(buffer))>0) {
			        	  // while buffer has data in it, write the data to the location
			        	fos.write(buffer, 0, length);
			          }
			            // close input/ouput streams when finished
			          zin.closeEntry();
			          fos.close();
			        }
			      }
			      zin.close();
			        // if end reached without errors, return success
			      CharSequence unzipgood = getText(R.string.unzipgood);
			      response = (String) unzipgood;
			    } catch(Exception e) {
			        // if there was an error, return failure
			      CharSequence unzipbad = getText(R.string.unzipbad);
			      response = (String) unzipbad;
			    }
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (result.equals("Unzip completed")) {
				if (nandroid.canRead() == true){
					textview3.setText("Success");
					verifyimage();
			    } else {
					showDialog(NO_NANDROID);
				}
			} else {
				if (result.equals("Unzip failed")) {
					textview3.setText("Failed");
					showDialog(UNZIP_FAILED);
				}
			}
		}
	}
	
	public void verifyimage() {
		VerifyImageTask task = new VerifyImageTask();
		try {
			FileReader in = new FileReader(new File(dir, "image/nandroid.md5"));
			task.execute(new FileReader[] {in});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class VerifyImageTask extends AsyncTask<FileReader, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence verifying = getText(R.string.verifying);
			dialog = ProgressDialog.show(AllInOneSkate.this, "", verifying, true);
		}
		
		@Override
		protected String doInBackground(FileReader... frs) {
			String response = "";
			for (FileReader in : frs) {
				  // reset all file names and match checks
				Editor editfile1 = preferences.edit();
			    editfile1.putString("file1", "");
		        editfile1.commit();
		        Editor editfile2 = preferences.edit();
			    editfile2.putString("file2", "");
		        editfile2.commit();
		        Editor editfile3 = preferences.edit();
			    editfile3.putString("file3", "");
		        editfile3.commit();
		        Editor editfile4 = preferences.edit();
			    editfile4.putString("file4", "");
		        editfile4.commit();
		        Editor editfile5 = preferences.edit();
			    editfile5.putString("file5", "");
		        editfile5.commit();
		        Editor editfile6 = preferences.edit();
			    editfile6.putString("file6", "");
		        editfile6.commit();
		        Editor editfile7 = preferences.edit();
			    editfile7.putString("file7", "");
		        editfile7.commit();
		        Editor editfile8 = preferences.edit();
			    editfile8.putString("file8", "");
		        editfile8.commit();
		        Editor editfile9 = preferences.edit();
			    editfile9.putString("file9", "");
		        editfile9.commit();
		        Editor editfile10 = preferences.edit();
			    editfile10.putString("file10", "");
		        editfile10.commit();
		        Editor editfile11 = preferences.edit();
			    editfile11.putString("file11", "");
		        editfile11.commit();
		        Editor editfile12 = preferences.edit();
			    editfile12.putString("file12", "");
		        editfile12.commit();
		        Editor editfile13 = preferences.edit();
			    editfile13.putString("file13", "");
		        editfile13.commit();
		        Editor editfile14 = preferences.edit();
			    editfile14.putString("file14", "");
		        editfile14.commit();
		        Editor editfile15 = preferences.edit();
			    editfile15.putString("file15", "");
		        editfile15.commit();
		        Editor editfile16 = preferences.edit();
			    editfile16.putString("file16", "");
		        editfile16.commit();
		        Editor editfile17 = preferences.edit();
			    editfile17.putString("file17", "");
		        editfile17.commit();
		        Editor editfile18 = preferences.edit();
			    editfile18.putString("file18", "");
		        editfile18.commit();
		        Editor editmatch1 = preferences.edit();
			    editmatch1.putString("match1", "");
		        editmatch1.commit();
		        Editor editmatch2 = preferences.edit();
			    editmatch2.putString("match2", "");
		        editmatch2.commit();
		        Editor editmatch3 = preferences.edit();
			    editmatch3.putString("match3", "");
		        editmatch3.commit();
		        Editor editmatch4 = preferences.edit();
			    editmatch4.putString("match4", "");
		        editmatch4.commit();
		        Editor editmatch5 = preferences.edit();
			    editmatch5.putString("match5", "");
		        editmatch5.commit();
		        Editor editmatch6 = preferences.edit();
			    editmatch6.putString("match6", "");
		        editmatch6.commit();
		        Editor editmatch7 = preferences.edit();
			    editmatch7.putString("match7", "");
		        editmatch7.commit();
		        Editor editmatch8 = preferences.edit();
			    editmatch8.putString("match8", "");
		        editmatch8.commit();
		        Editor editmatch9 = preferences.edit();
			    editmatch9.putString("match9", "");
		        editmatch9.commit();
		        Editor editmatch10 = preferences.edit();
			    editmatch10.putString("match10", "");
		        editmatch10.commit();
		        Editor editmatch11 = preferences.edit();
			    editmatch11.putString("match11", "");
		        editmatch11.commit();
		        Editor editmatch12 = preferences.edit();
			    editmatch12.putString("match12", "");
		        editmatch12.commit();
		        Editor editmatch13 = preferences.edit();
			    editmatch13.putString("match13", "");
		        editmatch13.commit();
		        Editor editmatch14 = preferences.edit();
			    editmatch14.putString("match14", "");
		        editmatch14.commit();
		        Editor editmatch15 = preferences.edit();
			    editmatch15.putString("match15", "");
		        editmatch15.commit();
		        Editor editmatch16 = preferences.edit();
			    editmatch16.putString("match16", "");
		        editmatch16.commit();
		        Editor editmatch17 = preferences.edit();
			    editmatch17.putString("match17", "");
		        editmatch17.commit();
		        Editor editmatch18 = preferences.edit();
			    editmatch18.putString("match18", "");
		        editmatch18.commit();
				try {
					  // read from the file line by line
				    BufferedReader br = new BufferedReader(in); 
				    String s;
				    int a = 0;
				    while((s = br.readLine()) != null) { 
				      // for each line add 1 to a to get the total number of lines
				    a = a + 1;
				    int length = s.length();
				    StringBuffer buffer = new StringBuffer();
				      // take the first 32 characters of each line as the md5 sum
				    for (int i = 0; i <= 31; i++) {
						buffer.append(s.charAt(i));
					}
				    String md5 = buffer.toString();
				    StringBuffer buffer2 = new StringBuffer();
				      // Ignore the two spaces, then take the rest of the line as the file
				    for (int i = 34; i <= length -1 ; i++) {
						buffer2.append(s.charAt(i));
					}
				    String filename = buffer2.toString();
				    Editor edit = preferences.edit();
				      // store each file name as file1, file2, etc.
				    edit.putString("file" + a, filename);
			        edit.commit();
			        Editor edit2 = preferences.edit();
			          // store each md5 sum as md51, md52, etc.
				    edit2.putString("md5" + a, md5);
			        edit2.commit();
				    }
				    in.close();
				    Editor edit = preferences.edit();
				      // store the number of lines as the total number of files
				    edit.putLong("no of files", a);
			        edit.commit();
			        Editor edit2 = preferences.edit();
			          // set number of matches to zero
				    edit2.putLong("no of matches", 0);
			        edit2.commit();
			        Editor edit3 = preferences.edit();
			          // set number of files checked to zero
				    edit3.putLong("no of files checked", 0);
			        edit3.commit();
			        int b = 0;
				    for (int i = 1; i <= a; i++) {
				      // set location of file in image folder
				    String location = preferences.getString("file" + i, "");
				    String loc = Environment.getExternalStorageDirectory() + "/image/" + location;
					File checkFile = new File(Environment.getExternalStorageDirectory(), "image/" + location);
					if (checkFile.canRead() == true){
						try  {
							  // add one to number of files checked
							b = b + 1;
							Editor edit4 = preferences.edit();
						    edit4.putLong("no of files checked", b);
					        edit4.commit();
				        	  // calculate the md5sum and see if it matches for each file
				        	FileInputStream fin = new FileInputStream(loc);
				            md5sum2(fin, i);
					    } catch(Exception e) { 
 
					    }
					} else {
						Editor edit5 = preferences.edit();
					    edit5.putString("match" + i, "File not found");
				        edit5.commit();
					}
					}
				}
				catch (IOException e) {

				}
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			showDialog(IMAGE_CHECKED);
		}
	}
	
	private void MakeDirectory(String path) {
		String image = Environment.getExternalStorageDirectory() + "/";
	    File file = new File(image + path);
	      // if the location doesn't exist, create it
	    if(!file.isDirectory()) {
	      file.mkdirs();
	    }
	}
	
	static public boolean deleteDirectory(File path) {
        if (path.exists()) {
        	  // get list of files in the directory
            File[] files = path.listFiles();
              // for each file
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                	  // if this is also a directory, run the delete method on it
                    deleteDirectory(files[i]);
                } else {
                	  // if its a file, delete it
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
	
	public void md5sum2(FileInputStream fin, int a) {
		  // call in the total number of files
		Long b = preferences.getLong("no of files", 0);
		  // calculate the md5sum of the file
		try {
			  // creates digester that will be used to calculate the md5sum
			MessageDigest digester = MessageDigest.getInstance("MD5");
			  // creates buffer to read from file
		    byte[] buffer = new byte[8192];
		    int i = 0;
		    try {
			    while ((i = fin.read(buffer)) > 0) {
			    	  // while the buffer is not empty, add the data to the digester
			        digester.update(buffer, 0, i);
			    }
		    } catch (IOException e) {
			    
		    }
		      // creates buffer holding md5sum output
		    byte[] output = digester.digest();
		    String md5sum = "";
            for (int j = 0; j < output.length; j++) {
          	      // adds each character to the string that will store the md5sum
                md5sum += Integer.toString((output[j] & 0xff) + 0x100, 16).substring(1);
            }
              // call in the expected md5sum
            String checksum = preferences.getString("md5" + a, "");
              // call in the number of matches so far
            Long c = preferences.getLong("no of matches", 0);
            if (checksum.equals(md5sum)) {
      	          // if the calculated md5sum matches the expected one, add 1 to the no. of matches
      	        c = c + 1;
      	        Editor edit = preferences.edit();
		        edit.putLong("no of matches", c);
	            edit.commit();
	            Editor edit2 = preferences.edit();
		        edit2.putString("match" + a, "Match");
	            edit2.commit();
            } else {
      	        Editor edit = preferences.edit();
		        edit.putString("match" + a, "No Match");
	            edit.commit();
            }
            if (a == b) {
      	          // if on the final file
                showDialog(IMAGE_CHECKED);
            }
		} catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
    			Toast.makeText(AllInOneSkate.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
		/* case R.id.troubleshooting:
			Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
			startActivity(j);
			break; */
		case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
		case R.id.about:
			Intent j = new Intent(AllInOneSkate.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(AllInOneSkate.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(AllInOneSkate.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(AllInOneSkate.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(AllInOneSkate.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
}