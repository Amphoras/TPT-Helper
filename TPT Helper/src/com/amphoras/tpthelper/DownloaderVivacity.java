package com.amphoras.tpthelper;

/*  
TPT Helper  Copyright (C) 2011-2012  David Phillips

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class DownloaderVivacity extends ListActivity {
	SharedPreferences preferences;
	private ProgressDialog dialog;
	private ArrayList <HashMap<String, Object>> tpts;
	private static final String tptname = "tptname";
	private static final String tptlayout = "tptlayout";
	private final int DOWNLOADING = 11;
	private final int DOWNLOAD_COMPLETE = 12;
	private final int DOWNLOAD_FAILED = 13;
	private final int FILE_FOUND = 14;
	private final int CHANGE_LOCALE = 15;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main);
		dialog = new ProgressDialog(DownloaderVivacity.this);
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
              
              try {
    	          URL url = new URL("http://amphoras.co.uk/Vivacity-TPTs.txt");
    	          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	          connection.connect();
    	          File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	          FileOutputStream fos = new FileOutputStream(file);
    	          InputStream is = connection.getInputStream();
    	          byte[] buffer = new byte[1024];
    	          int length = 0;
    	          while ((length = is.read(buffer)) > 0 ) {
    	              fos.write(buffer, 0, length);
    	          }
    	          fos.close();
        	      FileInputStream fis = new FileInputStream(file);
        	      InputStreamReader isr = new InputStreamReader(fis);
        	      BufferedReader br = new BufferedReader(isr);
        	      String s = "";
        	      while((s = br.readLine()) != null) {
        	          String[] mounts = s.split("\"");
        	    	  if (mounts[0].equals("AllInOne")) {
        	    		  listitem = new HashMap<String, Object>();
        		          CharSequence allinone_tpt_heading = getText(R.string.allinone_tpt_heading);
        		          CharSequence allinone_tpt = getText(R.string.allinone_tpt);
        		          listitem.put(tptname, allinone_tpt_heading);
        		          listitem.put(tptlayout, allinone_tpt);
        		          tpts.add(listitem);
        	    	  } else {
        	    		  listitem = new HashMap<String, Object>();
          	              listitem.put(tptname, mounts[0]);
          	              listitem.put(tptlayout, mounts[1]);
          	              tpts.add(listitem);
        	    	  }
        	      }
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (position > 0) {
				try {
		    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
			    	FileInputStream fis = new FileInputStream(file);
		  	        InputStreamReader isr = new InputStreamReader(fis);
		  	        BufferedReader br = new BufferedReader(isr);
		  	        String s = "";
		  	        while((s = br.readLine()) != null) {
		  	            String[] mounts = s.split("\"");
		  	            if (mounts[0].equals("AllInOne")) {
		  	            	// Do nothing
		  	            } else {
		  	            	showDialog(position);
		  	            }
		  	        }
		    	} catch (IOException e) {
	  	            e.printStackTrace();
	  	        }
			}
			return false;
		  }
	    });
	  
	    listview.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	Editor editdownload = preferences.edit();
	    	Editor editdownloadint = preferences.edit();
	    	if (position > 0) {
	    		try {
	    			int number = 0;
		    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
			    	FileInputStream fis = new FileInputStream(file);
		  	        InputStreamReader isr = new InputStreamReader(fis);
		  	        BufferedReader br = new BufferedReader(isr);
		  	        String s = "";
		  	        while((s = br.readLine()) != null) {
		  	        	number = number + 1;
		  	            String[] mounts = s.split("\"");
		  	            if (mounts[0].equals("AllInOne")) {
		  	            	// Do nothing
		  	            } else {
		  	            	if (position == number) {
			  	            	editdownload.putString("downloadpicked", mounts[2]);
			  		    		editdownload.commit();
			  					editdownloadint.putInt("downloadint", position);
			  		    		editdownloadint.commit();
			  		    		File tpt = new File(Environment.getExternalStorageDirectory(), "/" + mounts[2]);
			  		    		File downloadtpt = new File(Environment.getExternalStorageDirectory(), "/download/" + mounts[2]);
			  		    		if (tpt.canRead() == true){
			  		    		    showDialog(FILE_FOUND);
			  		    		} else {
			  		    			if (downloadtpt.canRead() == true){
			  	    	    	        showDialog(FILE_FOUND);
			  		    			} else {
			  		    				DownloadFile();
			  		    			}
			  		    		}
			  	            }
		  	            }
		  	        }
		    	} catch (IOException e) {
	  	            e.printStackTrace();
	  	        }
            }
	      }
	    });
              } catch (MalformedURLException e) {
            	  listitem = new HashMap<String, Object>();
  	              listitem.put(tptname, "Unable to access TPT list. Please check your data connection and try again.");
  	              listitem.put(tptlayout, "");
  	              tpts.add(listitem);
  	              SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
  	      	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
  	      	  
  	      	      listview.setAdapter(adapter);
    	          e.printStackTrace();
    	      } catch (IOException e) {
    	    	  listitem = new HashMap<String, Object>();
  	              listitem.put(tptname, "Unable to access TPT list. Please check your data connection and try again.");
  	              listitem.put(tptlayout, "");
  	              tpts.add(listitem);
  	              SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
  	      	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
  	      	  
  	      	      listview.setAdapter(adapter);
    	          e.printStackTrace();
    	      }
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
              
              try {
    	          URL url = new URL("http://amphoras.co.uk/tpts.txt");
    	          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	          connection.connect();
    	          File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	          FileOutputStream fos = new FileOutputStream(file);
    	          InputStream is = connection.getInputStream();
    	          byte[] buffer = new byte[1024];
    	          int length = 0;
    	          while ((length = is.read(buffer)) > 0 ) {
    	              fos.write(buffer, 0, length);
    	          }
    	          fos.close();
        	      FileInputStream fis = new FileInputStream(file);
        	      InputStreamReader isr = new InputStreamReader(fis);
        	      BufferedReader br = new BufferedReader(isr);
        	      String s = "";
        	      while((s = br.readLine()) != null) {
        	          String[] mounts = s.split("\"");
        	    	  if (mounts[0].equals("AllInOne")) {
        	    		  listitem = new HashMap<String, Object>();
        		          CharSequence allinone_tpt_heading = getText(R.string.allinone_tpt_heading);
        		          CharSequence allinone_tpt = getText(R.string.allinone_tpt);
        		          listitem.put(tptname, allinone_tpt_heading);
        		          listitem.put(tptlayout, allinone_tpt);
        		          tpts.add(listitem);
        	    	  } else {
        	    		  listitem = new HashMap<String, Object>();
          	              listitem.put(tptname, mounts[0]);
          	              listitem.put(tptlayout, mounts[1]);
          	              tpts.add(listitem);
        	    	  }
        	      }
	    SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
	  
	    listview.setAdapter(adapter);
	  
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (position > 0) {
				try {
		    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
			    	FileInputStream fis = new FileInputStream(file);
		  	        InputStreamReader isr = new InputStreamReader(fis);
		  	        BufferedReader br = new BufferedReader(isr);
		  	        String s = "";
		  	        while((s = br.readLine()) != null) {
		  	            String[] mounts = s.split("\"");
		  	            if (mounts[0].equals("AllInOne")) {
		  	            	// Do nothing
		  	            } else {
		  	            	showDialog(position);
		  	            }
		  	        }
		    	} catch (IOException e) {
	  	            e.printStackTrace();
	  	        }
			}
			return false;
		  }
	    });
	  
	    listview.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	Editor editdownload = preferences.edit();
	    	Editor editdownloadint = preferences.edit();
	    	if (position > 0) {
	    		try {
	    			int number = 0;
		    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
			    	FileInputStream fis = new FileInputStream(file);
		  	        InputStreamReader isr = new InputStreamReader(fis);
		  	        BufferedReader br = new BufferedReader(isr);
		  	        String s = "";
		  	        while((s = br.readLine()) != null) {
		  	        	number = number + 1;
		  	            String[] mounts = s.split("\"");
		  	            if (mounts[0].equals("AllInOne")) {
		  	            	// Do nothing
		  	            } else {
		  	            	if (position == number) {
			  	            	editdownload.putString("downloadpicked", mounts[2]);
			  		    		editdownload.commit();
			  					editdownloadint.putInt("downloadint", position);
			  		    		editdownloadint.commit();
			  		    		File tpt = new File(Environment.getExternalStorageDirectory(), "/" + mounts[2]);
			  		    		File downloadtpt = new File(Environment.getExternalStorageDirectory(), "/download/" + mounts[2]);
			  		    		if (tpt.canRead() == true){
			  		    		    showDialog(FILE_FOUND);
			  		    		} else {
			  		    			if (downloadtpt.canRead() == true){
			  	    	    	        showDialog(FILE_FOUND);
			  		    			} else {
			  		    				DownloadFile();
			  		    			}
			  		    		}
			  	            }
		  	            }
		  	        }
		    	} catch (IOException e) {
	  	            e.printStackTrace();
	  	        }
            }
	      }
	    });
              } catch (MalformedURLException e) {
            	  listitem = new HashMap<String, Object>();
  	              listitem.put(tptname, "Unable to access TPT list. Please check your data connection.");
  	              listitem.put(tptlayout, "");
  	              tpts.add(listitem);
  	              SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
  	      	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
  	      	  
  	      	      listview.setAdapter(adapter);
    	          e.printStackTrace();
    	      } catch (IOException e) {
    	    	  listitem = new HashMap<String, Object>();
  	              listitem.put(tptname, "Unable to access TPT list. Please check your data connection.");
  	              listitem.put(tptlayout, "");
  	              tpts.add(listitem);
  	              SimpleAdapter adapter = new SimpleAdapter(this, tpts, R.layout.list_item,
  	      	        new String[]{tptname, tptlayout}, new int[]{R.id.tptname, R.id.tptlayout});
  	      	  
  	      	      listview.setAdapter(adapter);
    	          e.printStackTrace();
    	      }
	}
	
	public void DownloadFile() {
		DownloadFileTask task = new DownloadFileTask();
		int id = preferences.getInt("downloadint", 1);
		try {
			int number = 0;
    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
	    	FileInputStream fis = new FileInputStream(file);
  	        InputStreamReader isr = new InputStreamReader(fis);
  	        BufferedReader br = new BufferedReader(isr);
  	        String s = "";
  	        while((s = br.readLine()) != null) {
  	        	number = number + 1;
  	            String[] mounts = s.split("\"");
  	            if (mounts[0].equals("AllInOne")) {
  	            	// Do nothing
  	            } else {
  	            	if (id == number) {
  	  	            	task.execute(new String[] { mounts[3] });
  	  	            }
  	            }
  	        }
    	} catch (IOException e) {
	        e.printStackTrace();
	    }
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
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 1:
        	Builder builder1 = new AlertDialog.Builder(DownloaderVivacity.this);
        	try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder1.setTitle(mounts[0]);
      	                builder1.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder1.setCancelable(false);
            builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder1.create();
        case 2:
            Builder builder2 = new AlertDialog.Builder(DownloaderVivacity.this);
            try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder2.setTitle(mounts[0]);
      	                builder2.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder2.setCancelable(false);
            builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder2.create();
        case 3:
            Builder builder3 = new AlertDialog.Builder(DownloaderVivacity.this);
            try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder3.setTitle(mounts[0]);
      	                builder3.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder3.setCancelable(false);
            builder3.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    // Do nothing
                }
            });
            return builder3.create();
        case 4:
        	Builder builder4 = new AlertDialog.Builder(DownloaderVivacity.this);
        	try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder4.setTitle(mounts[0]);
      	                builder4.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder4.setCancelable(false);
            builder4.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder4.create();
        case 5:
            Builder builder5 = new AlertDialog.Builder(DownloaderVivacity.this);
            try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder5.setTitle(mounts[0]);
      	                builder5.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder5.setCancelable(false);
            builder5.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder5.create();
        case 6:
            Builder builder6 = new AlertDialog.Builder(DownloaderVivacity.this);
            try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder6.setTitle(mounts[0]);
      	                builder6.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder6.setCancelable(false);
            builder6.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    // Do nothing
                }
            });
            return builder6.create();
        case 7:
        	Builder builder7 = new AlertDialog.Builder(DownloaderVivacity.this);
        	try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder7.setTitle(mounts[0]);
      	                builder7.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder7.setCancelable(false);
            builder7.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder7.create();
        case 8:
            Builder builder8 = new AlertDialog.Builder(DownloaderVivacity.this);
            try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder8.setTitle(mounts[0]);
      	                builder8.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder8.setCancelable(false);
            builder8.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder8.create();
        case 9:
            Builder builder9 = new AlertDialog.Builder(DownloaderVivacity.this);
            try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder9.setTitle(mounts[0]);
      	                builder9.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder9.setCancelable(false);
            builder9.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    // Do nothing
                }
            });
            return builder9.create();
        case 10:
        	Builder builder10 = new AlertDialog.Builder(DownloaderVivacity.this);
        	try {
    			int number = 0;
        		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/Vivacity-TPTs.txt");
    	    	FileInputStream fis = new FileInputStream(file);
      	        InputStreamReader isr = new InputStreamReader(fis);
      	        BufferedReader br = new BufferedReader(isr);
      	        String s = "";
      	        while((s = br.readLine()) != null) {
      	        	number = number + 1;
      	            String[] mounts = s.split("\"");
      	            if (id == number) {
      	            	builder10.setTitle(mounts[0]);
      	                builder10.setMessage(Html.fromHtml(mounts[4]));
      	            }
      	        }
        	} catch (IOException e) {
    	        e.printStackTrace();
    	    }
            builder10.setCancelable(false);
            builder10.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return builder10.create();
        case DOWNLOADING:
            dialog = new ProgressDialog(DownloaderVivacity.this);
            CharSequence downloadmessage = getText(R.string.downloading);
            dialog.setMessage(downloadmessage);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            return dialog;
        default:
            return null;
        case DOWNLOAD_COMPLETE:
            String downloadpicked = preferences.getString("downloadpicked", "");
            Builder downloadbuilder = new AlertDialog.Builder(DownloaderVivacity.this);
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
            Builder downloadfailedbuilder = new AlertDialog.Builder(DownloaderVivacity.this);
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
            Builder filetherebuilder = new AlertDialog.Builder(DownloaderVivacity.this);
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
                    DownloaderVivacity.this.finish();
                }
            });
            return filetherebuilder.create();
        case CHANGE_LOCALE:
          	    // change the locale used in the app
              Builder localebuilder = new AlertDialog.Builder(DownloaderVivacity.this);
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
          	    		Intent i = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(i);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 1:
          	    		editlocale.putString("locale", "fr");
          	    		editlocale.commit();
          	    		Intent j = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(j);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 2:
          	    		editlocale.putString("locale", "de");
          	    		editlocale.commit();
          	    		Intent k = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(k);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 3:
          	    		editlocale.putString("locale", "ru");
          	    		editlocale.commit();
          	    		Intent l = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(l);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 4:
          	    		editlocale.putString("locale", "zh");
          	    		editlocale.commit();
          	    		Intent m = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(m);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 5:
          	    		editlocale.putString("locale", "pt");
          	    		editlocale.commit();
          	    		Intent n = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(n);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 6:
          	    		editlocale.putString("locale", "es");
          	    		editlocale.commit();
          	    		Intent o = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(o);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 7:
          	    		editlocale.putString("locale", "sr");
          	    		editlocale.commit();
          	    		Intent p = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(p);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 8:
          	    		editlocale.putString("locale", "cs");
          	    		editlocale.commit();
          	    		Intent q = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(q);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 9:
          	    		editlocale.putString("locale", "pl");
          	    		editlocale.commit();
          	    		Intent r = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(r);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 10:
          	    		editlocale.putString("locale", "hu");
          	    		editlocale.commit();
          	    		Intent s = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(s);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 11:
          	    		editlocale.putString("locale", "sv");
          	    		editlocale.commit();
          	    		Intent t = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(t);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 12:
          	    		editlocale.putString("locale", "it");
          	    		editlocale.commit();
          	    		Intent u = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(u);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 13:
          	    		editlocale.putString("locale", "nl");
          	    		editlocale.commit();
          	    		Intent v = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(v);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 14:
          	    		editlocale.putString("locale", "pt_BR");
          	    		editlocale.commit();
          	    		Intent w = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(w);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 15:
          	    		editlocale.putString("locale", "el");
          	    		editlocale.commit();
          	    		Intent x = new Intent(DownloaderVivacity.this, HomeActivity.class);
          	    	    startActivity(x);
          	    	    DownloaderVivacity.this.finish();
          	    		break;
          	    	case 16:
          	    		// Do nothing
          	    		break;
          	    	}
          	      }
          	  });
          	  return localebuilder.create();
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
    			Toast.makeText(DownloaderVivacity.this, "Unable to send feedback. Make sure you have an email app setup.", Toast.LENGTH_LONG).show();
    		}
		/* case R.id.troubleshooting:
			Intent j = new Intent(HomeActivity.this, Troubleshooting.class);
			startActivity(j);
			break; */
		case R.id.locale:
			showDialog(CHANGE_LOCALE);
			break;
		case R.id.about:
			Intent j = new Intent(DownloaderVivacity.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(DownloaderVivacity.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(DownloaderVivacity.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(DownloaderVivacity.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(DownloaderVivacity.this, License.class);
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