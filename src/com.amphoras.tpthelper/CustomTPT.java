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
import java.io.OutputStream;
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

public class CustomTPT extends Activity {
	SharedPreferences preferences;
    private final int PICK_SPLASH = 1;
    private final int PICK_PARTITION = 2;
    private final int PICK_RECOVERY = 3;
    private final int PICK_SYSTEM = 4;
    private final int UNZIP_FAILED = 5;
    private final int DOWNLOADING = 6;
    private final int DOWNLOAD_FAILED = 7;
    private final int FINISHED = 8;
    private final int MD5_MISMATCH = 9;
    private final int CHANGE_LOCALE = 10;
    private String unziplocation = Environment.getExternalStorageDirectory() + "/";
    private String unziplocationfiles = Environment.getExternalStorageDirectory() + "/TPT Helper/";
    private static ProgressDialog dialog;
    private static File dir = Environment.getExternalStorageDirectory();
    private static File basezip = new File(dir, "TPT Helper/custom-TPT-base.zip");
    private static File extrafiles = new File(dir, "TPT Helper/custom-tpt-files.zip");
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.md5sum);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
  	    showDialog(PICK_SPLASH);
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.md5sum);
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
			Intent j = new Intent(CustomTPT.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(CustomTPT.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(CustomTPT.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(CustomTPT.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(CustomTPT.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		CharSequence cancel = getText(R.string.cancel);
        switch (id) {
        case PICK_SPLASH:
        	Builder builder1 = new AlertDialog.Builder(CustomTPT.this);
            builder1.setTitle(R.string.pickmd5);
            builder1.setCancelable(false);
            final CharSequence[] zips1 = {"Normal Android", "Big Android", "CyanogenMod", cancel};
        	builder1.setItems(zips1, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editsplash = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editsplash.putInt("splashpicked", 1);
        	    		editsplash.commit();
        	    		showDialog(PICK_PARTITION);
        	    		break;
        	    	case 1:
        	    		editsplash.putInt("splashpicked", 2);
        	    		editsplash.commit();
        	    		showDialog(PICK_PARTITION);
        	    		break;
        	    	case 2:
        	    		editsplash.putInt("splashpicked", 3);
        	    		editsplash.commit();
        	    		showDialog(PICK_PARTITION);
        	    		break;
        	    	case 3:
        	    		CustomTPT.this.finish();
        	    		break;
        	    	}
        	    }
        	});
        	AlertDialog alert1 = builder1.create();
            alert1.show();
            break;
        case PICK_PARTITION:
        	Builder builder2 = new AlertDialog.Builder(CustomTPT.this);
            builder2.setTitle(R.string.pickmd5);
            builder2.setCancelable(false);
            final CharSequence[] zips2 = {"2mb cache, 160mb system, 294mb data, 0.1mb oem", "2mb cache, 140mb system, 314mb data, 0.1mb oem", "2mb cache, 138mb system, 315.6mb data, 0.1mb oem", "37mb cache, 210mb system, 204mb data 4mb oem", cancel};
        	builder2.setItems(zips2, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editpartitions = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editpartitions.putInt("partitionspicked", 160);
        	    		editpartitions.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 1:
        	    		editpartitions.putInt("partitionspicked", 140);
        	    		editpartitions.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 2:
        	    		editpartitions.putInt("partitionspicked", 138);
        	    		editpartitions.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 3:
        	    		editpartitions.putInt("partitionspicked", 210);
        	    		editpartitions.commit();
        	    		showDialog(PICK_RECOVERY);
        	    		break;
        	    	case 4:
        	    		CustomTPT.this.finish();
        	    		break;
        	    	}
        	    }
        	});
        	AlertDialog alert2 = builder2.create();
            alert2.show();
            break;
        case PICK_RECOVERY:
        	Builder builder3 = new AlertDialog.Builder(CustomTPT.this);
            builder3.setTitle(R.string.pickmd5);
            builder3.setCancelable(false);
            final CharSequence[] zips3 = {"ClockworkMod v5.0.2.0", "ClockWorkMod v3.0.2.4", "PhoTom v4.0.1.5", cancel};
        	builder3.setItems(zips3, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editrecovery = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editrecovery.putInt("recoverypicked", 1);
        	    		editrecovery.commit();
        	    		//showDialog(PICK_SYSTEM);
        	    		buildTPT();
        	    		break;
        	    	case 1:
        	    		editrecovery.putInt("recoverypicked", 2);
        	    		editrecovery.commit();
        	    		//showDialog(PICK_SYSTEM);
        	    		buildTPT();
        	    		break;
        	    	case 2:
        	    		editrecovery.putInt("recoverypicked", 3);
        	    		editrecovery.commit();
        	    		//showDialog(PICK_SYSTEM);
        	    		buildTPT();
        	    		break;
        	    	case 3:
        	    		CustomTPT.this.finish();
        	    		break;
        	    	}
        	    }
        	});
        	AlertDialog alert3 = builder3.create();
            alert3.show();
            break;
        case PICK_SYSTEM:
        	Builder builder4 = new AlertDialog.Builder(CustomTPT.this);
            builder4.setTitle(R.string.pickmd5);
            builder4.setCancelable(false);
            final CharSequence[] zips4 = {"No rom", "CyanogenMod 7.1 RC1", "Ginger Stir Fry B24", "Ginger Stir Fry B23", "Ginger Stir Fry B19", cancel};
        	builder4.setItems(zips4, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	Editor editsystem = preferences.edit();
        	    	switch (item) {
        	    	case 0:
        	    		editsystem.putInt("systempicked", 1);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 1:
        	    		editsystem.putInt("systempicked", 2);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 2:
        	    		editsystem.putInt("systempicked", 3);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 3:
        	    		editsystem.putInt("systempicked", 4);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 4:
        	    		editsystem.putInt("systempicked", 5);
        	    		editsystem.commit();
        	    		buildTPT();
        	    		break;
        	    	case 5:
        	    		CustomTPT.this.finish();
        	    		break;
        	    	}
        	    }
        	});
        	AlertDialog alert4 = builder4.create();
            alert4.show();
            break;
        case UNZIP_FAILED:
            Builder failedbuilder = new AlertDialog.Builder(CustomTPT.this);
            failedbuilder.setTitle(R.string.unzip);
              // show dialog about failure
            failedbuilder.setMessage(R.string.unzipbad);
            failedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
          	        CustomTPT.this.finish();
                }
            });
            AlertDialog failedalert = failedbuilder.create();
            failedalert.show();
            break;
        case DOWNLOADING:
            dialog = new ProgressDialog(CustomTPT.this);
            CharSequence downloadmessage = getText(R.string.downloading);
            dialog.setMessage(downloadmessage);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.show();
            break;
        case DOWNLOAD_FAILED:
        	Builder downloadfailedbuilder = new AlertDialog.Builder(CustomTPT.this);
        	downloadfailedbuilder.setTitle(R.string.download_failed_heading);
            CharSequence download_failed = getText(R.string.download_failed);
            downloadfailedbuilder.setMessage(download_failed + " custom-TPT-base.zip");
            downloadfailedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            AlertDialog downloadfailedalert = downloadfailedbuilder.create();
            downloadfailedalert.show();
            break;
        case FINISHED:
        	Builder finishedbuilder = new AlertDialog.Builder(CustomTPT.this);
        	finishedbuilder.setTitle(R.string.finished_heading);
            CharSequence finished = getText(R.string.finished);
            finishedbuilder.setMessage(finished);
            finishedbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	CustomTPT.this.finish();
                }
            });
            AlertDialog finishedalert = finishedbuilder.create();
            finishedalert.show();
            break;
        case MD5_MISMATCH:
        	Builder md5builder = new AlertDialog.Builder(CustomTPT.this);
        	md5builder.setTitle(R.string.md5_mismatch_heading);
            CharSequence md5 = getText(R.string.md5_mismatch);
            md5builder.setMessage(md5);
            md5builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            AlertDialog md5alert = md5builder.create();
            md5alert.show();
            break;
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(CustomTPT.this);
          localebuilder.setTitle(R.string.change_locale_heading);
          localebuilder.setCancelable(false);
          CharSequence english = getText(R.string.english);
          CharSequence french = getText(R.string.french);
          CharSequence german = getText(R.string.german);
          CharSequence russian = getText(R.string.russian);
          CharSequence chinese = getText(R.string.chinese);
          CharSequence portuguese = getText(R.string.portuguese);
          CharSequence spanish = getText(R.string.spanish);
          final CharSequence[] locales = {english, french, german, russian, chinese, portuguese, spanish, cancel};
      	  localebuilder.setItems(locales, new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int item) {
      	    	Editor editlocale = preferences.edit();
      	    	switch (item) {
      	    	case 0:
      	    		editlocale.putString("locale", "en");
      	    		editlocale.commit();
      	    		Intent i = new Intent(CustomTPT.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    CustomTPT.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(CustomTPT.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    CustomTPT.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(CustomTPT.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    CustomTPT.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(CustomTPT.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    CustomTPT.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(CustomTPT.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    CustomTPT.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(CustomTPT.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    CustomTPT.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(CustomTPT.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    CustomTPT.this.finish();
      	    		break;
      	    	case 7:
      	    		// Do nothing
      	    		break;
      	    	}
      	      }
      	  });
          AlertDialog localealert = localebuilder.create();
          localealert.show();
          break;
        }
        return super.onCreateDialog(id);
    }
	
	private void buildTPT() {
		if (basezip.canRead() == true) {
			if (extrafiles.canRead() == true) {
				unzip();
			} else {
				DownloadFile2();
			}
		} else {
			DownloadFile();
		}
	}
	
	private void addFiles() {
		CopyTask task = new CopyTask();
		String sfin = new String("");
		task.execute(new String[] {sfin});
	}
	
	private void CopyAssets(String filename, String newfilename) {
	    try {
	    	  // open the file from the assets folder
	    	InputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/TPT Helper/custom-tpt-files/" + filename);
	          // set where to save it
		    OutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/image/" + newfilename);
	        byte[] buffer = new byte[1024];
		    int i;
		      // while you can still read to the buffer, write to the output
		    while((i = fis.read(buffer)) != -1){
		        fos.write(buffer, 0, i);
		    }
		      // closes the input/output
	        fis.close();
	        fos.close();
	    } catch(Exception e) {

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
	
	private void MakeDirectory2(String path) {
		String image = Environment.getExternalStorageDirectory() + "/TPT Helper/";
	    File file = new File(image + path);
	      // if the location doesn't exist, create it
	    if(!file.isDirectory()) {
	        file.mkdirs();
	    }
	}
	
	private class UnzipTask extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence unzipping = getText(R.string.unzipping);
			dialog = ProgressDialog.show(CustomTPT.this, "", unzipping, true);
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
				unzip2();
			} else {
				if (result.equals("Unzip failed")) {
					showDialog(UNZIP_FAILED);
				}
			}
		}
	}

	public void unzip() {
		UnzipTask task = new UnzipTask();
		try {
			String zipname = "/TPT Helper/custom-TPT-base.zip";
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + zipname);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class UnzipTask2 extends AsyncTask<FileInputStream, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence unzipping = getText(R.string.unzipping);
			dialog = ProgressDialog.show(CustomTPT.this, "", unzipping, true);
		}
		
		@Override
		protected String doInBackground(FileInputStream... fins) {
			String response = "";
			for (FileInputStream fin : fins) {
				  // delete the files directory if it already exists
				File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/custom-tpt-files");
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
			            MakeDirectory2(ze.getName());
			        } else {
			        	// open output to location
			          FileOutputStream fos = new FileOutputStream(unziplocationfiles + ze.getName());
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
				verifyimage();
			} else {
				if (result.equals("Unzip failed")) {
					showDialog(UNZIP_FAILED);
				}
			}
		}
	}

	public void unzip2() {
		UnzipTask2 task = new UnzipTask2();
		try {
			String zipname = "/TPT Helper/custom-tpt-files.zip";
			FileInputStream sfin = new FileInputStream(Environment.getExternalStorageDirectory() + zipname);
			task.execute(new FileInputStream[] {sfin});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
			        File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/custom-TPT-base.zip");
			          // check the directory exists and create it if it doen't
			        MakeDirectory("TPT Helper");
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
				if (extrafiles.canRead() == true) {
					unzip();
				} else {
					DownloadFile2();
				}
			} else {
				showDialog(DOWNLOAD_FAILED);
			}
		}
	}

	public void DownloadFile() {
		DownloadFileTask task = new DownloadFileTask();
		task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/custom-TPT-base.zip" });
	}
	
	private class DownloadFileTask2 extends AsyncTask<String, String, String> {
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
			        File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/custom-tpt-files.zip");
			          // check the directory exists and create it if it doen't
			        MakeDirectory("TPT Helper");
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
				unzip();
			} else {
				showDialog(DOWNLOAD_FAILED);
			}
		}
	}

	public void DownloadFile2() {
		DownloadFileTask2 task = new DownloadFileTask2();
		task.execute(new String[] { "http://dl.dropbox.com/u/41652192/TPT%20Helper/custom-tpt-files.zip" });
	}
	
	private class CopyTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence copying = getText(R.string.copying);
			dialog = ProgressDialog.show(CustomTPT.this, "", copying, true);
		}
		
		@Override
		protected String doInBackground(String... fins) {
			String response = "";
			int splash = preferences.getInt("splashpicked", 1);
			int partition = preferences.getInt("partitionspicked", 160);
			int recovery = preferences.getInt("recoverypicked", 1);
			//int system = preferences.getInt("systempicked", 1);
			switch (splash) {
			case 1:
				CopyAssets("NormalAndroid.img", "splash.img");
				break;
			case 2:
				CopyAssets("BigAndroid.img", "splash.img");
				break;
			case 3:
				CopyAssets("CyanogenMod.img", "splash.img");
				break;
			}
			switch (partition) {
			case 160:
				CopyAssets("160system.mbn", "partition.mbn");
				CopyAssets("160system2.mbn", "partition_zte.mbn");
				CopyAssets("160system3.mbn", "appsboot.mbn");
				CopyAssets("160system4.mbn", "appsboothd.mbn");
				break;
			case 140:
				CopyAssets("140system.mbn", "partition.mbn");
				CopyAssets("140system2.mbn", "partition_zte.mbn");
				CopyAssets("140system3.mbn", "appsboot.mbn");
				CopyAssets("140system4.mbn", "appsboothd.mbn");
				break;
			case 138:
				CopyAssets("138system.mbn", "partition.mbn");
				CopyAssets("138system2.mbn", "partition_zte.mbn");
				CopyAssets("138system3.mbn", "appsboot.mbn");
				CopyAssets("138system4.mbn", "appsboothd.mbn");
				break;
			case 210:
				CopyAssets("210system.mbn", "partition.mbn");
				CopyAssets("210system2.mbn", "partition_zte.mbn");
				CopyAssets("210system3.mbn", "appsboot.mbn");
				CopyAssets("210system4.mbn", "appsboothd.mbn");
				break;
			}
			switch (recovery) {
			case 1:
				CopyAssets("CWMv5.img", "recovery.img");
				break;
			case 2:
				CopyAssets("CWMv3.img", "recovery.img");
				break;
			case 3:
				CopyAssets("PhoTom.img", "recovery.img");
				break;
			}
			//switch (system) {
			//case 1:
				CopyAssets("standard.img", "system.img");
				switch (recovery) {
				case 1:
					CopyAssets("CWMv5.img", "boot.img");
					break;
				case 2:
					CopyAssets("CWMv3.img", "boot.img");
					break;
				case 3:
					CopyAssets("PhoTom.img", "boot.img");
					break;
				}
				//break;
			/*case 2:
				CopyAssets("CM7.img", "system.img");
				CopyAssets("CM7boot.img", "boot.img");
				break;
			case 3:
				CopyAssets("GSFB24.img", "system.img");
				CopyAssets("GSFB24boot.img", "boot.img");
				break;
			case 4:
				CopyAssets("GSFB23.img", "system.img");
				CopyAssets("GSFB23boot.img", "boot.img");
				break;
			case 5:
				CopyAssets("GSFB19.img", "system.img");
				CopyAssets("GSFB19boot.img", "boot.img");
				break;
			}*/
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			checkOthers();
		}
	}
	
	private class VerifyImageTask extends AsyncTask<FileReader, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence verifying = getText(R.string.verifying);
			dialog = ProgressDialog.show(CustomTPT.this, "", verifying, true);
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
					File checkFile = new File(dir, "image/" + location);
					if (checkFile.canRead() == true){
						try  {
							  // add one to number of files checked
							b = b + 1;
							Editor edit4 = preferences.edit();
						    edit4.putLong("no of files checked", b);
					        edit4.commit();
				        	  // calculate the md5sum and see if it matches for each file
				        	FileInputStream fin = new FileInputStream(loc);
				            md5sum(fin, i);
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
			Long a = preferences.getLong("no of matches", 0);
			Long b = preferences.getLong("no of files checked", 0);
			if (a == b) {
				addFiles();
			} else {
				showDialog(MD5_MISMATCH);
			}
		}
	}

	public void verifyimage() {
		VerifyImageTask task = new VerifyImageTask();
		try {
			FileReader in = new FileReader(new File(dir, "image/nandroid.md5"));
			task.execute(new FileReader[] {in});
		} catch (FileNotFoundException e) {

		}
	}
	
	public void md5sum(FileInputStream fin, int a) {
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
        	
            }
		  } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
		  }
    }
	
	public boolean md5sum2(FileInputStream fin, String checksum) {
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
            if (checksum.equals(md5sum)) {
    	          // if the calculated md5sum matches the expected one
    	        return true;
            }
		} catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
		}
		return false;
    }
	
	public void checkOthers() {
		String splashmd5 = "";
		String partitionmd5 = "";
		String partition2md5 = "";
		String partition3md5 = "";
		String partition4md5 = "";
		String recoverymd5 = "";
		String systemmd5 = "";
		String bootmd5 = "";
		int splash = preferences.getInt("splashpicked", 1);
		int partition = preferences.getInt("partitionspicked", 160);
		int recovery = preferences.getInt("recoverypicked", 1);
		//int system = preferences.getInt("systempicked", 1);
		switch (splash) {
		case 1:
			splashmd5 = "6517e43357b2ccb1e3c23ce1af67f1e5";
			break;
		case 2:
			splashmd5 = "0cfe844f4a4b84eef82c1767902b6618";
			break;
		case 3:
			splashmd5 = "83d8df20241cec8bbc7b4a6bef329284";
			break;
		}
		switch (partition) {
		case 160:
			partitionmd5 = "a377a29c5bdd338b6f36eefa0dfd07ba";
			partition2md5 = "0ecaa42f488f465ac9480853137f3388";
			partition3md5 = "643ef920c47c89eb7923d6382924fcf3";
			partition4md5 = "9c379b44e171472a2bec54272e2e3346";
			break;
		case 140:
			partitionmd5 = "a377a29c5bdd338b6f36eefa0dfd07ba";
			partition2md5 = "13bca625ec4c3124bd29cc9ea6c5450d";
			partition3md5 = "a3a3d73c8d8c8b9766cbbb07edd1ed4d";
			partition4md5 = "9c379b44e171472a2bec54272e2e3346";
			break;
		case 138:
			partitionmd5 = "a377a29c5bdd338b6f36eefa0dfd07ba";
			partition2md5 = "80b6efb768bd628cebfe0abc96c47725";
			partition3md5 = "f8c8265ab32515e83f2848ef63c90323";
			partition4md5 = "9c379b44e171472a2bec54272e2e3346";
			break;
		case 210:
			partitionmd5 = "a377a29c5bdd338b6f36eefa0dfd07ba";
			partition2md5 = "2fef076b697904315f5dc7666a2373c1";
			partition3md5 = "b2573915d1af913716543417c79d9498";
			partition4md5 = "9c379b44e171472a2bec54272e2e3346";
			break;
		}
		switch (recovery) {
		case 1:
			recoverymd5 = "777287e4a51c90c30f8e952785ebd062";
			break;
		case 2:
			recoverymd5 = "320a0182b428dd879b9cdf23c1f214a8";
			break;
		case 3:
			recoverymd5 = "64b68f806e57771f478eed3cfe03b1af";
			break;
		}
		//switch (system) {
		//case 1:
		    systemmd5 = "3d0bfef87dae68e4888802e30926b318";
		    switch (recovery) {
			case 1:
				bootmd5 = "777287e4a51c90c30f8e952785ebd062";
				break;
			case 2:
				bootmd5 = "320a0182b428dd879b9cdf23c1f214a8";
				break;
			case 3:
				bootmd5 = "64b68f806e57771f478eed3cfe03b1af";
				break;
			}
			//break;
		/*case 2:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		case 3:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		case 4:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		case 5:
			systemmd5 = "";
		    bootmd5 = "";
			break;
		}*/
		
		String splashfile = Environment.getExternalStorageDirectory() + "/image/splash.img";
		String partitionfile = Environment.getExternalStorageDirectory() + "/image/partition.mbn";
		String partition2file = Environment.getExternalStorageDirectory() + "/image/partition_zte.mbn";
		String partition3file = Environment.getExternalStorageDirectory() + "/image/appsboot.mbn";
		String partition4file = Environment.getExternalStorageDirectory() + "/image/appsboothd.mbn";
		String recoveryfile = Environment.getExternalStorageDirectory() + "/image/recovery.img";
		String systemfile = Environment.getExternalStorageDirectory() + "/image/system.img";
		String bootfile = Environment.getExternalStorageDirectory() + "/image/boot.img";
		try {
		FileInputStream splashfin = new FileInputStream(splashfile);
		FileInputStream partitionfin = new FileInputStream(partitionfile);
		FileInputStream partition2fin = new FileInputStream(partition2file);
		FileInputStream partition3fin = new FileInputStream(partition3file);
		FileInputStream partition4fin = new FileInputStream(partition4file);
		FileInputStream recoveryfin = new FileInputStream(recoveryfile);
		FileInputStream systemfin = new FileInputStream(systemfile);
		FileInputStream bootfin = new FileInputStream(bootfile);
		if (md5sum2(splashfin, splashmd5) == true) {
			if (md5sum2(partitionfin, partitionmd5) == true) {
				if (md5sum2(partition2fin, partition2md5) == true) {
					if (md5sum2(partition3fin, partition3md5) == true) {
						if (md5sum2(partition4fin, partition4md5) == true) {
							if (md5sum2(recoveryfin, recoverymd5) == true) {
								if (md5sum2(systemfin, systemmd5) == true) {
									if (md5sum2(bootfin, bootmd5) == true) {
										showDialog(FINISHED);
									} else {
										showDialog(MD5_MISMATCH);
									}
								} else {
									showDialog(MD5_MISMATCH);
								}
							} else {
								showDialog(MD5_MISMATCH);
							}
						} else {
							showDialog(MD5_MISMATCH);
						}
					} else {
						showDialog(MD5_MISMATCH);
					}
				} else {
					showDialog(MD5_MISMATCH);
				}
			} else {
				showDialog(MD5_MISMATCH);
			}
		} else {
			showDialog(MD5_MISMATCH);
		}
		} catch (IOException e) {
			
		}
	}
}
