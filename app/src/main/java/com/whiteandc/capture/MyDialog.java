package com.whiteandc.capture;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whiteandc.capture.R;

public class MyDialog extends DialogFragment {

    private String text="";
    private String title="";

    String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MyDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container);
        TextView textView = (TextView) view.findViewById(R.id.dialog_text);
        textView.setText(text);
        getDialog().setTitle(title);

        return view;
    }
}