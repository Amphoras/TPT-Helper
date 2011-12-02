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
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class VerifyImage extends Activity {
	SharedPreferences preferences;
	private static ProgressDialog dialog;
	  // open the file for reading
	private static File nandroid = new File(Environment.getExternalStorageDirectory(), "image/nandroid.md5");
	//private static File image = new File(Environment.getExternalStorageDirectory(), "image/image.md5");
	private final int IMAGE_CHECKED = 1;
	private final int NO_NANDROID = 2;
	private final int CHANGE_LOCALE = 3;
	private final int NO_IMAGE = 4;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifyimage);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String blade = preferences.getString("blade", "European Gen1");
		//if (blade.equals("European Gen1")) {
			  // if the nandroid.md5 file exists
			if (nandroid.canRead() == true){
				verifyimage();
		    } else {
				showDialog(NO_NANDROID);
			}
		//} else {
			//if (blade.equals("European Gen2")) {
				  // if the image.md5 file exists
				//if (image.canRead() == true){
				//	verifyimage();
			    //} else {
				//	showDialog(NO_IMAGE);
				//}
			//}
		//}
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.verifyimage);
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
                showDialog(IMAGE_CHECKED);
            }
		} catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
		}
    }
	
	@Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
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
            Builder builder = new AlertDialog.Builder(VerifyImage.this);
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
        case NO_NANDROID:
      	      // show error dialog after failing to find nandroid.md5 file
            Builder nofilebuilder = new AlertDialog.Builder(VerifyImage.this);
            nofilebuilder.setTitle(R.string.error);
            nofilebuilder.setMessage(R.string.no_nandroid);
            nofilebuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	// Do nothing
                }
            });
            return nofilebuilder.create();
        case CHANGE_LOCALE:
      	    // change the locale used in the app
          Builder localebuilder = new AlertDialog.Builder(VerifyImage.this);
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
      	    		Intent i = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(i);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 1:
      	    		editlocale.putString("locale", "fr");
      	    		editlocale.commit();
      	    		Intent j = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(j);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 2:
      	    		editlocale.putString("locale", "de");
      	    		editlocale.commit();
      	    		Intent k = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(k);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 3:
      	    		editlocale.putString("locale", "ru");
      	    		editlocale.commit();
      	    		Intent l = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(l);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 4:
      	    		editlocale.putString("locale", "zh");
      	    		editlocale.commit();
      	    		Intent m = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(m);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 5:
      	    		editlocale.putString("locale", "pt");
      	    		editlocale.commit();
      	    		Intent n = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(n);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 6:
      	    		editlocale.putString("locale", "es");
      	    		editlocale.commit();
      	    		Intent o = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(o);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 7:
      	    		editlocale.putString("locale", "sr");
      	    		editlocale.commit();
      	    		Intent p = new Intent(VerifyImage.this, HomeActivity.class);
      	    	    startActivity(p);
      	    	    VerifyImage.this.finish();
      	    		break;
      	    	case 8:
      	    		// Do nothing
      	    		break;
      	    	}
      	      }
      	  });
      	  return localebuilder.create();
        case NO_IMAGE:
    	      // show error dialog after failing to find nandroid.md5 file
          Builder noimagefilebuilder = new AlertDialog.Builder(VerifyImage.this);
          noimagefilebuilder.setTitle(R.string.error);
          noimagefilebuilder.setMessage(R.string.no_nandroid);
          noimagefilebuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              	// Do nothing
              }
          });
          return noimagefilebuilder.create();
        }
        return super.onCreateDialog(id);
    }
	
	private class VerifyImageTask extends AsyncTask<FileReader, Void, String> {
		@Override
		protected void onPreExecute() {
			CharSequence verifying = getText(R.string.verifying);
			dialog = ProgressDialog.show(VerifyImage.this, "", verifying, true);
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
				            md5sum(fin, i);
					    } catch(Exception e) { 
					          Log.e("VerifyImage", "md5sum", e); 
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

	public void verifyimage() {
		VerifyImageTask task = new VerifyImageTask();
		//String blade = preferences.getString("blade", "European Gen1");
		try {
			//if (blade.equals("European Gen1")) {
				FileReader in = new FileReader(nandroid);
				task.execute(new FileReader[] {in});
			//} else {
			//	if (blade.equals("European Gen1")) {
				//	FileReader in = new FileReader(image);
				//	task.execute(new FileReader[] {in});
			//	}
			//}
		} catch (FileNotFoundException e) {
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
			Intent j = new Intent(VerifyImage.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(VerifyImage.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(VerifyImage.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(VerifyImage.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(VerifyImage.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
}