package com.amphoras.tpthelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class CustomTPTGen2Gen3 extends Activity {
	private final int PICK_GEN = 1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.md5sum);
        showDialog(PICK_GEN);
    }

    @Override
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case PICK_GEN:
        	Builder builder1 = new AlertDialog.Builder(CustomTPTGen2Gen3.this);
            builder1.setTitle(R.string.pick_gen);
            builder1.setCancelable(false);
            CharSequence cancel = getText(R.string.cancel);
            final CharSequence[] tpts = {"Gen 2 TPT", /*"Gen 3 TPT",*/ cancel};
        	builder1.setItems(tpts, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	switch (item) {
        	    	case 0:
        	    		Intent gen_2 = new Intent(CustomTPTGen2Gen3.this, CustomTPTGen2.class);
        	    		startActivity(gen_2);
        	    		CustomTPTGen2Gen3.this.finish();
        	    		break;
        	    	/*case 1:
        	    		Intent gen_3 = new Intent(CustomTPTGen2Gen3.this, CustomTPTGen2.class);
        	    		startActivity(gen_3);
        	    		CustomTPTGen2Gen3.this.finish();
        	    		break;*/
        	    	case 1:
                		CustomTPTGen2Gen3.this.finish();
                		break;
        	    	}
        	    }
        	});
        	return builder1.create();
        }
        return super.onCreateDialog(id);
    }
}
