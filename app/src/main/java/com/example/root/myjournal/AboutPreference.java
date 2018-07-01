package com.example.root.myjournal;

import android.content.Context;
import android.preference.DialogPreference;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;

// AboutPreference class
public class AboutPreference extends DialogPreference {
    // Constructor
    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // On bind dialog view
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // Get version text view
        TextView version = (TextView) view.findViewById(R.id.about);

        // Set version in text view
        if (version != null) {
            String v = (String) version.getText();
            String s = String.format(v, BuildConfig.VERSION_NAME);
            version.setText(s);
        }

        // Get built text view
        TextView built = (TextView) view.findViewById(R.id.built);

        // Set built date in text view
        if (built != null) {
            String d = (String) built.getText();
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            //ToDo:Research on variable BUILT
//            String s = String.format(d, dateFormat.format(BuildConfig.BUILT));
//            built.setText(s);
        }

        // Get copyright text view
        TextView copyright = (TextView) view.findViewById(R.id.copyright);

        // Set movement method
        copyright.setMovementMethod(LinkMovementMethod.getInstance());

        // Get licence text view
        TextView licence = (TextView) view.findViewById(R.id.licence);

        // Set movement method
        licence.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
