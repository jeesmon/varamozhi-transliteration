package com.jeesmon.apps.varamozhi;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextEx extends EditText {

	public EditTextEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onCheckIsTextEditor() {
		return false;
	}
}
