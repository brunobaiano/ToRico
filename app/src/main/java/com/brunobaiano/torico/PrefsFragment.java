package com.brunobaiano.torico;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Bruno on 19/07/2015.
 */
public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);
    }
}