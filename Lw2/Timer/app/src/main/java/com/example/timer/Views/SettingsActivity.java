package com.example.timer.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.timer.App;
import com.example.timer.Helpers.SettingsHelper;
import com.example.timer.R;
import com.example.timer.Views.DetailActivity;
import com.example.timer.Views.MainActivity;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SettingsHelper.SetSettings(PreferenceManager.getDefaultSharedPreferences(this));
        setTheme(SettingsHelper.Theme);
        setLocale(SettingsHelper.locale);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            SwitchPreferenceCompat listPreferenceMode = findPreference("nightMode");
            ListPreference listPreferenceFont = findPreference("fontSize");
            ListPreference listPreferenceLang = findPreference("language");
            Preference button = findPreference("resetDb");

            button.setOnPreferenceClickListener(preference -> {
                App.getInstance().getTimerDao().DeleteAll();
                getActivity().recreate();
                return true;
            });

            listPreferenceMode.setOnPreferenceChangeListener((preference, newValue) -> {
                getActivity().recreate();
                return true;
            });

            listPreferenceFont.setOnPreferenceChangeListener((preference, newValue) -> {
                getActivity().recreate();
                return true;
            });

            listPreferenceLang.setOnPreferenceChangeListener((preference, newValue) -> {
                preference.setDefaultValue(newValue);
                Locale locale = new Locale(newValue.toString());
                Locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getActivity().getResources().updateConfiguration(configuration, null);
                getActivity().recreate();
                return true;
            });
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public void setLocale(Locale locale)
    {
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, null);
    }
}