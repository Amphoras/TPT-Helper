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

// old version of downloader using mediafire links, no longer used

import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Downloader extends ListActivity {
  private ArrayList <HashMap<String, Object>> tpts;
	private static final String tptname = "tptname";
	private static final String tptlayout = "tptlayout";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.main);
	  ListView listview = getListView();
	  listview.setTextFilterEnabled(true);
	  
	  tpts = new ArrayList<HashMap<String,Object>>();
	  
	  HashMap<String, Object> listitem;
	          listitem = new HashMap<String, Object>();
              listitem.put(tptname, R.string.standard_tpt_heading);
              listitem.put(tptlayout, R.string.standard_tpt);
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
	          listitem.put(tptname, R.string.allinone_tpt_heading);
	          listitem.put(tptlayout, R.string.allinone_tpt);
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
		    	  showDialog(1);
			      break;
		      case 2:
		    	  showDialog(2);
				  break;
		      case 3:
		    	  showDialog(3);
				  break;
		      case 4:
		    	  showDialog(4);
				  break;
		      case 5:
		    	  showDialog(5);
				  break;
		      case 6:
		    	  showDialog(6);
				  break;
		      case 8:
		    	  showDialog(8);
				  break;
		      case 9:
		    	  showDialog(9);
				  break;
		      case 10:
		    	  showDialog(10);
				  break;
		      case 11:
		    	  showDialog(11);
				  break;
		      }
			return false;
		}
	  });
	  
	  listview.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	      switch (position) {
	      case 1:
	    	  Intent downloadtpt1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?luy3ddd8abxsz0o"));
		      startActivity(downloadtpt1);
		      break;
	      case 2:
	    	  Intent downloadtpt2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?3msdgd9vdci6ugm"));
		      startActivity(downloadtpt2);
		      break;
	      case 3:
		      Intent downloadtpt3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?fmsmzqqpkwo1r1a"));
			  startActivity(downloadtpt3);
			  break;
	      case 4:
			  Intent downloadtpt4 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?7z2swplnkvdjb4t"));
			  startActivity(downloadtpt4);
			  break;
	      case 5:
			  Intent downloadtpt5 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?bxvuk6ohcw8il8m"));
			  startActivity(downloadtpt5);
			  break;
	      case 6:
			  Intent downloadtpt6 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?w2t6x2y9rg34u2w"));
			  startActivity(downloadtpt6);
			  break;
	      case 8:
	    	  Intent downloadtpt7 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?3yx2og9fl3331bh"));
			  startActivity(downloadtpt7);
			  break;
	      case 9:
	    	  Intent downloadtpt8 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?s1a6kc7cea2qqx6"));
			  startActivity(downloadtpt8);
			  break;
	      case 10:
	    	  Intent downloadtpt9 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?sb9im0b0c00gd7v"));
			  startActivity(downloadtpt9);
			  break;
	      case 11:
	    	  Intent downloadtpt10 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediafire.com/?6an05dylu62plka"));
			  startActivity(downloadtpt10);
			  break;
	      }
	    }
	  });
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 1:
            Builder builder1 = new AlertDialog.Builder(Downloader.this);
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
        case 2:
            Builder builder2 = new AlertDialog.Builder(Downloader.this);
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
        case 3:
            Builder builder3 = new AlertDialog.Builder(Downloader.this);
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
        case 4:
            Builder builder4 = new AlertDialog.Builder(Downloader.this);
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
        case 5:
            Builder builder5 = new AlertDialog.Builder(Downloader.this);
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
        case 6:
            Builder builder6 = new AlertDialog.Builder(Downloader.this);
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
        case 8:
            Builder builder8 = new AlertDialog.Builder(Downloader.this);
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
        case 9:
            Builder builder9 = new AlertDialog.Builder(Downloader.this);
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
        case 10:
            Builder builder10 = new AlertDialog.Builder(Downloader.this);
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
        case 11:
            Builder builder11 = new AlertDialog.Builder(Downloader.this);
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
		case R.id.about:
			Intent j = new Intent(Downloader.this, About.class);
			startActivity(j);
			break;
		case R.id.instructions:
			Intent k = new Intent(Downloader.this, Instructions.class);
			startActivity(k);
			break;
		case R.id.show_changelog:
			Intent l = new Intent(Downloader.this, Changelog.class);
			startActivity(l);
			break;
		case R.id.preferences:
			Intent m = new Intent(Downloader.this, Preferences.class);
			startActivity(m);
			break;
		case R.id.license:
			Intent n = new Intent(Downloader.this, License.class);
			startActivity(n);
			break;
		}
		return true;
	}
}