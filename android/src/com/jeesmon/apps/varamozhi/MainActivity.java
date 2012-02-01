package com.jeesmon.apps.varamozhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	public static boolean preferenceChanged = false;
	
	private static int unicodeFix = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContent();
    }
    
    private void getContent() {
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    	try {
    		unicodeFix = Integer.parseInt(pref.getString("com.jeesmon.apps.varamozhi.rendering.option", "0"));
    	}
    	catch(Exception e) {
    		unicodeFix = 0;
    	}
    	
        setContentView(R.layout.main);
        
        Resources res = getResources();
		Typeface tf = Typefaces.get(this,
				res.getString(R.string.font_name));
		
		final EditText manglish = (EditText) findViewById(R.id.manglish);
		final TextView malayalam = (TextView) findViewById(R.id.malayalam);
		final Button share = (Button) findViewById(R.id.share);
		final Button copy = (Button) findViewById(R.id.copy);
		final Button help = (Button) findViewById(R.id.help);
		final Button clear = (Button) findViewById(R.id.clear);
		
		malayalam.setTypeface(tf);
		manglish.setTypeface(tf);
		
		manglish.addTextChangedListener(new TextWatcher() {
			@Override
		    public void afterTextChanged(Editable s) {
		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    }
		    
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
				String text = ComplexCharacterMapper.fix(Varamozhi.stringFromJNI(manglish.getText().toString()), unicodeFix);
				malayalam.setText(text);
				if(text.length() > 0) {
					share.setEnabled(true);
					copy.setEnabled(true);
					clear.setEnabled(true);
				}
				else {
					share.setEnabled(false);
					copy.setEnabled(false);
					clear.setEnabled(false);
				}
			}
		});
		
		share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Varamozhi.stringFromJNI(manglish.getText().toString()));
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shared from Varamozhi Transliteration");
				startActivity(Intent.createChooser(sharingIntent, "Share using"));
			}
		});
		
		copy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				copyText(manglish);
			    Toast.makeText(MainActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
			}
		});
		
		help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				copyText(manglish);
				Toast.makeText(MainActivity.this, "Text also copied to clipboard. Paste it to the app if not transferred.", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("https://sites.google.com/site/cibu/mozhi"));
		        startActivity(intent);
			}
		});
		
		clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				manglish.setText("");
			}
		});
    }
    
	@Override
	protected void onStart() {
		super.onStart();
		
		if(preferenceChanged) {
			preferenceChanged = false;
			getContent();
		}
	}

	private void copyText(final EditText manglish) {
		ClipboardManager ClipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipMan.setText(Varamozhi.stringFromJNI(manglish.getText().toString()));
	}
}