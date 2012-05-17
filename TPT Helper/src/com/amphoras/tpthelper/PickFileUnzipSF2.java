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

public class PickFileUnzipSF2 extends Activity {
	SharedPreferences preferences;
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
        	Builder builder1 = new AlertDialog.Builder(PickFileUnzipSF2.this);
            builder1.setTitle(R.string.pickmd5);
            builder1.setCancelable(false);
            CharSequence cancel = getText(R.string.cancel);
            CharSequence other = getText(R.string.other);
            CharSequence[] zips1 = new CharSequence[10];
            int number = 0;
            try {
  	            URL url = new URL("http://amphoras.co.uk/SF2-TPTs.txt");
  	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
  	            connection.connect();
  	            File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/SF2-TPTs.txt");
  	            FileOutputStream fos = new FileOutputStream(file);
  	            InputStream is = connection.getInputStream();
  	            byte[] buffer = new byte[1024];
  	            int length = 0;
  	            while ((length = is.read(buffer)) > 0 ) {
  	                fos.write(buffer, 0, length);
  	            }
  	            fos.close();
  	          try {
  		    	FileInputStream fis = new FileInputStream(file);
  	  	        InputStreamReader isr = new InputStreamReader(fis);
  	  	        BufferedReader br = new BufferedReader(isr);
  	  	        String s = "";
  	  	        while((s = br.readLine()) != null) {
  	  	            String[] mounts = s.split("\"");
  	  	            if (mounts[0].equals("AllInOne")) {
  	  	            	// Do nothing
  	  	            } else {
  	  	            	number = number + 1;
  	  	            	zips1[number - 1] = mounts[2];
  	  	            }
  	  	        }
              CharSequence[] zips2 = new CharSequence[number + 2];
              for (int i = 0; i < number; i++) {
              	zips2[i] = zips1[i];
              }
              zips2[number] = other;
              zips2[number + 1] = cancel;
              final int position = number;
          	builder1.setItems(zips2, new DialogInterface.OnClickListener() {
          	    public void onClick(DialogInterface dialog, int item) {
          	    	if (item < position) {
        	    		try {
        	    			int number = 0;
        		    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/SF2-TPTs.txt");
        			    	FileInputStream fis = new FileInputStream(file);
        		  	        InputStreamReader isr = new InputStreamReader(fis);
        		  	        BufferedReader br = new BufferedReader(isr);
        		  	        String s = "";
        		  	        while((s = br.readLine()) != null) {
        		  	            String[] mounts = s.split("\"");
        		  	            if (mounts[0].equals("AllInOne")) {
        		  	            	// Do nothing
        		  	            } else {
        		  	            	if (item == number) {
        		        	    		File tpt = new File(Environment.getExternalStorageDirectory(), "/" + mounts[2]);
        		        	    		File downloadtpt = new File(Environment.getExternalStorageDirectory(), "/download/" + mounts[2]);
        		        	    		if (tpt.canRead() == true){
        		        	    		    Editor edit = preferences.edit();
        		        	    			edit.putString("zipname", "/" + mounts[2]);
        		        	    			edit.commit();
        		        	    			Intent i = new Intent(PickFileUnzipSF2.this, Unzipper.class);
        		        	    	        startActivity(i);
        		        	    	        PickFileUnzipSF2.this.finish();
        		        	    		} else {
        		        	    			if (downloadtpt.canRead() == true){
        		            	    	        Editor edit = preferences.edit();
        		            	    		    edit.putString("zipname", "/download/" + mounts[2]);
        		            	    			edit.commit();
        		            	    			Intent i = new Intent(PickFileUnzipSF2.this, Unzipper.class);
        		            	    	        startActivity(i);
        		            	    	        PickFileUnzipSF2.this.finish();
        		        	    			} else {
        		        	    				Editor edit = preferences.edit();
        		            	    			edit.putString("filepicked", mounts[2]);
        		            	    			edit.commit();
        		        	    				showDialog(FILE_UNFOUND);
        		        	    			}
        		        	    		}
        			  	            }
        		  	            	number = number + 1;
        		  	            }
        		  	        }
        		    	} catch (IOException e) {
        	  	            e.printStackTrace();
        	  	        }
        	    	} else {
        	    		if (item == position) {
        	    			Intent i = new Intent(PickFileUnzipSF2.this, EnterFileUnzip.class);
                    		startActivityForResult(i, 1);
        	    		} else {
        	    			if (item == position + 1) {
        	    				PickFileUnzipSF2.this.finish();
        	    			}
        	    		}
        	    	}
          	    }
          	});
              } catch (IOException e) {
    	        	CharSequence[] zips = {"Unable to access TPT list.", "Please check your data connection.", other, cancel};
              	builder1.setItems(zips, new DialogInterface.OnClickListener() {
              	    public void onClick(DialogInterface dialog, int item) {
              	    	switch (item) {
              	    	case 0:
              	    		PickFileUnzipSF2.this.finish();
              	    		break;
              	    	case 1:
              	    		PickFileUnzipSF2.this.finish();
              	    		break;
              	    	case 2:
              	    		Intent i = new Intent(PickFileUnzipSF2.this, EnterFile.class);
                      		startActivityForResult(i, 1);
                      		break;
              	    	case 3:
              	    		PickFileUnzipSF2.this.finish();
              	    		break;
              	    	}
              	    }
              	});
    	            e.printStackTrace();
    	        }
            } catch (MalformedURLException e) {
            	try {
                	File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/SF2-TPTs.txt");
    		    	FileInputStream fis = new FileInputStream(file);
    	  	        InputStreamReader isr = new InputStreamReader(fis);
    	  	        BufferedReader br = new BufferedReader(isr);
    	  	        String s = "";
    	  	        while((s = br.readLine()) != null) {
    	  	            String[] mounts = s.split("\"");
    	  	            if (mounts[0].equals("AllInOne")) {
    	  	            	// Do nothing
    	  	            } else {
    	  	            	number = number + 1;
    	  	            	zips1[number - 1] = mounts[2];
    	  	            }
    	  	        }
                CharSequence[] zips2 = new CharSequence[number + 2];
                for (int i = 0; i < number; i++) {
                	zips2[i] = zips1[i];
                }
                zips2[number] = other;
                zips2[number + 1] = cancel;
                final int position = number;
            	builder1.setItems(zips2, new DialogInterface.OnClickListener() {
            	    public void onClick(DialogInterface dialog, int item) {
            	    	if (item < position) {
            	    		try {
            	    			int number = 0;
            		    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/SF2-TPTs.txt");
            			    	FileInputStream fis = new FileInputStream(file);
            		  	        InputStreamReader isr = new InputStreamReader(fis);
            		  	        BufferedReader br = new BufferedReader(isr);
            		  	        String s = "";
            		  	        while((s = br.readLine()) != null) {
            		  	            String[] mounts = s.split("\"");
            		  	            if (mounts[0].equals("AllInOne")) {
            		  	            	// Do nothing
            		  	            } else {
            		  	            	if (item == number) {
            		        	    		File tpt = new File(Environment.getExternalStorageDirectory(), "/" + mounts[2]);
            		        	    		File downloadtpt = new File(Environment.getExternalStorageDirectory(), "/download/" + mounts[2]);
            		        	    		if (tpt.canRead() == true){
            		        	    		    Editor edit = preferences.edit();
            		        	    			edit.putString("zipname", "/" + mounts[2]);
            		        	    			edit.commit();
            		        	    			Intent i = new Intent(PickFileUnzipSF2.this, Unzipper.class);
            		        	    	        startActivity(i);
            		        	    	        PickFileUnzipSF2.this.finish();
            		        	    		} else {
            		        	    			if (downloadtpt.canRead() == true){
            		            	    	        Editor edit = preferences.edit();
            		            	    		    edit.putString("zipname", "/download/" + mounts[2]);
            		            	    			edit.commit();
            		            	    			Intent i = new Intent(PickFileUnzipSF2.this, Unzipper.class);
            		            	    	        startActivity(i);
            		            	    	        PickFileUnzipSF2.this.finish();
            		        	    			} else {
            		        	    				Editor edit = preferences.edit();
            		            	    			edit.putString("filepicked", mounts[2]);
            		            	    			edit.commit();
            		        	    				showDialog(FILE_UNFOUND);
            		        	    			}
            		        	    		}
            			  	            }
            		  	            	number = number + 1;
            		  	            }
            		  	        }
            		    	} catch (IOException e) {
            	  	            e.printStackTrace();
            	  	        }
            	    	} else {
            	    		if (item == position) {
            	    			Intent i = new Intent(PickFileUnzipSF2.this, EnterFileUnzip.class);
                        		startActivityForResult(i, 1);
            	    		} else {
            	    			if (item == position + 1) {
            	    				PickFileUnzipSF2.this.finish();
            	    			}
            	    		}
            	    	}
            	    }
            	});
                } catch (IOException e2) {
      	        	CharSequence[] zips = {"Unable to access TPT list.", "Please check your data connection.", other, cancel};
                	builder1.setItems(zips, new DialogInterface.OnClickListener() {
                	    public void onClick(DialogInterface dialog, int item) {
                	    	switch (item) {
                	    	case 0:
                	    		PickFileUnzipSF2.this.finish();
                	    		break;
                	    	case 1:
                	    		PickFileUnzipSF2.this.finish();
                	    		break;
                	    	case 2:
                	    		Intent i = new Intent(PickFileUnzipSF2.this, EnterFile.class);
                        		startActivityForResult(i, 1);
                        		break;
                	    	case 3:
                	    		PickFileUnzipSF2.this.finish();
                	    		break;
                	    	}
                	    }
                	});
      	            e2.printStackTrace();
      	        }
  	            e.printStackTrace();
  	        } catch (IOException e) {
  	        	try {
  	            	File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/SF2-TPTs.txt");
  			    	FileInputStream fis = new FileInputStream(file);
  		  	        InputStreamReader isr = new InputStreamReader(fis);
  		  	        BufferedReader br = new BufferedReader(isr);
  		  	        String s = "";
  		  	        while((s = br.readLine()) != null) {
  		  	            String[] mounts = s.split("\"");
  		  	            if (mounts[0].equals("AllInOne")) {
  		  	            	// Do nothing
  		  	            } else {
  		  	            	number = number + 1;
  		  	            	zips1[number - 1] = mounts[2];
  		  	            }
  		  	        }
  	            CharSequence[] zips2 = new CharSequence[number + 2];
  	            for (int i = 0; i < number; i++) {
  	            	zips2[i] = zips1[i];
  	            }
  	            zips2[number] = other;
  	            zips2[number + 1] = cancel;
  	            final int position = number;
  	        	builder1.setItems(zips2, new DialogInterface.OnClickListener() {
  	        	    public void onClick(DialogInterface dialog, int item) {
  	        	    	if (item < position) {
  	        	    		try {
  	        	    			int number = 0;
  	        		    		File file = new File(Environment.getExternalStorageDirectory(), "/TPT Helper/SF2-TPTs.txt");
  	        			    	FileInputStream fis = new FileInputStream(file);
  	        		  	        InputStreamReader isr = new InputStreamReader(fis);
  	        		  	        BufferedReader br = new BufferedReader(isr);
  	        		  	        String s = "";
  	        		  	        while((s = br.readLine()) != null) {
  	        		  	            String[] mounts = s.split("\"");
  	        		  	            if (mounts[0].equals("AllInOne")) {
  	        		  	            	// Do nothing
  	        		  	            } else {
  	        		  	            	if (item == number) {
  	        		        	    		File tpt = new File(Environment.getExternalStorageDirectory(), "/" + mounts[2]);
  	        		        	    		File downloadtpt = new File(Environment.getExternalStorageDirectory(), "/download/" + mounts[2]);
  	        		        	    		if (tpt.canRead() == true){
  	        		        	    		    Editor edit = preferences.edit();
  	        		        	    			edit.putString("zipname", "/" + mounts[2]);
  	        		        	    			edit.commit();
  	        		        	    			Intent i = new Intent(PickFileUnzipSF2.this, Unzipper.class);
  	        		        	    	        startActivity(i);
  	        		        	    	        PickFileUnzipSF2.this.finish();
  	        		        	    		} else {
  	        		        	    			if (downloadtpt.canRead() == true){
  	        		            	    	        Editor edit = preferences.edit();
  	        		            	    		    edit.putString("zipname", "/download/" + mounts[2]);
  	        		            	    			edit.commit();
  	        		            	    			Intent i = new Intent(PickFileUnzipSF2.this, Unzipper.class);
  	        		            	    	        startActivity(i);
  	        		            	    	        PickFileUnzipSF2.this.finish();
  	        		        	    			} else {
  	        		        	    				Editor edit = preferences.edit();
  	        		            	    			edit.putString("filepicked", mounts[2]);
  	        		            	    			edit.commit();
  	        		        	    				showDialog(FILE_UNFOUND);
  	        		        	    			}
  	        		        	    		}
  	        			  	            }
  	        		  	            	number = number + 1;
  	        		  	            }
  	        		  	        }
  	        		    	} catch (IOException e) {
  	        	  	            e.printStackTrace();
  	        	  	        }
  	        	    	} else {
  	        	    		if (item == position) {
  	        	    			Intent i = new Intent(PickFileUnzipSF2.this, EnterFileUnzip.class);
  	                    		startActivityForResult(i, 1);
  	        	    		} else {
  	        	    			if (item == position + 1) {
  	        	    				PickFileUnzipSF2.this.finish();
  	        	    			}
  	        	    		}
  	        	    	}
  	        	    }
  	        	});
  	            } catch (IOException e2) {
  	  	        	CharSequence[] zips = {"Unable to access TPT list.", "Please check your data connection.", other, cancel};
  	            	builder1.setItems(zips, new DialogInterface.OnClickListener() {
  	            	    public void onClick(DialogInterface dialog, int item) {
  	            	    	switch (item) {
  	            	    	case 0:
  	            	    		PickFileUnzipSF2.this.finish();
  	            	    		break;
  	            	    	case 1:
  	            	    		PickFileUnzipSF2.this.finish();
  	            	    		break;
  	            	    	case 2:
  	            	    		Intent i = new Intent(PickFileUnzipSF2.this, EnterFile.class);
  	                    		startActivityForResult(i, 1);
  	                    		break;
  	            	    	case 3:
  	            	    		PickFileUnzipSF2.this.finish();
  	            	    		break;
  	            	    	}
  	            	    }
  	            	});
  	  	            e2.printStackTrace();
  	  	        }
  	            e.printStackTrace();
  	        }
        	return builder1.create();
        case FILE_UNFOUND:
        	String filepicked = preferences.getString("filepicked", "");
        	Builder builder2 = new AlertDialog.Builder(PickFileUnzipSF2.this);
            builder2.setTitle(R.string.file_not_found_heading);
            builder2.setCancelable(false);
            CharSequence file_not_found1 = getText(R.string.file_not_found1);
            CharSequence file_not_found2 = getText(R.string.file_not_found2);
            builder2.setMessage(file_not_found1 + " " + filepicked + "" + file_not_found2);
            builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    Intent i = new Intent(PickFileUnzipSF2.this, DownloaderSF2.class);
              	    startActivity(i);
              	    PickFileUnzipSF2.this.finish();
                }
            });
            builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
              	    PickFileUnzipSF2.this.finish();
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
    	      Intent i = new Intent(PickFileUnzipSF2.this, MD5sum.class);
    	      startActivity(i);
    	      PickFileUnzipSF2.this.finish();
          }
          break;
      }
    }
}