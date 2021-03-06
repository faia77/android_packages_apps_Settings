/*
 * Copyright (C) 2012 CyanogenMod
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.mahdi;

import android.content.res.Resources;
import android.os.RemoteException;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManagerGlobal;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.util.mahdi.DeviceUtils;

public class PowerMenu extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {
    private static final String TAG = "PowerMenu";

    private static final String KEY_REBOOT = "power_menu_reboot";    
    private static final String KEY_PROFILES = "power_menu_profiles";
    private static final String KEY_SCREENSHOT = "power_menu_screenshot";
    private static final String KEY_SCREENRECORD = "power_menu_screenrecord";
    private static final String KEY_ONTHEGO = "power_menu_onthego_enabled";
    private static final String KEY_AIRPLANE = "power_menu_airplane";
    private static final String KEY_SILENT = "power_menu_silent";

    private CheckBoxPreference mRebootPref;
    private ListPreference mProfilesPref;
    private CheckBoxPreference mScreenshotPref;
    private CheckBoxPreference mScreenrecordPref;
    private CheckBoxPreference mOnthegoPref;
    private CheckBoxPreference mAirplanePref;
    private CheckBoxPreference mSilentPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.power_menu_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        findPreference(Settings.System.POWER_MENU_ONTHEGO_ENABLED).setEnabled(
                DeviceUtils.hasCamera(getActivity()));

        mRebootPref = (CheckBoxPreference) findPreference(KEY_REBOOT);
        mRebootPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_REBOOT_ENABLED, 1) == 1));

	mProfilesPref = (ListPreference) findPreference(KEY_PROFILES);
        mProfilesPref.setOnPreferenceChangeListener(this);
        int mProfileShow = Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_PROFILES_ENABLED, 1);
        mProfilesPref.setValue(String.valueOf(mProfileShow));
        mProfilesPref.setSummary(mProfilesPref.getEntries()[mProfileShow]);
        // Only enable if System Profiles are also enabled
        boolean enabled = Settings.System.getInt(getContentResolver(),
                Settings.System.SYSTEM_PROFILES_ENABLED, 1) == 1;
        mProfilesPref.setEnabled(enabled);

        mScreenshotPref = (CheckBoxPreference) findPreference(KEY_SCREENSHOT);
        mScreenshotPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_SCREENSHOT_ENABLED, 0) == 1));

	mScreenrecordPref = (CheckBoxPreference) findPreference(KEY_SCREENRECORD);
        mScreenrecordPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_SCREENRECORD_ENABLED, 0) == 1));

        mOnthegoPref = (CheckBoxPreference) findPreference(KEY_ONTHEGO);
        mOnthegoPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_ONTHEGO_ENABLED, 0) == 1));

        mAirplanePref = (CheckBoxPreference) findPreference(KEY_AIRPLANE);
        mAirplanePref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_AIRPLANE_ENABLED, 1) == 1));                

        mSilentPref = (CheckBoxPreference) findPreference(KEY_SILENT);
        mSilentPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_SILENT_ENABLED, 1) == 1));
    } 

    public boolean onPreferenceChange(Preference preference, Object newValue) {
       if (preference == mProfilesPref) {
            int mProfileShow = Integer.valueOf((String) newValue);
            int index = mProfilesPref.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_PROFILES_ENABLED, mProfileShow);
            mProfilesPref.setSummary(mProfilesPref.getEntries()[index]);
            return true;
        }
        return false;
    }       

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mScreenshotPref) {
            value = mScreenshotPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_SCREENSHOT_ENABLED,
                    value ? 1 : 0);
	} else if (preference == mScreenrecordPref) {
            value = mScreenrecordPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_SCREENRECORD_ENABLED,
                    value ? 1 : 0);
        } else if (preference == mOnthegoPref) {
            value = mOnthegoPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_ONTHEGO_ENABLED,
                    value ? 1 : 0);      
        } else if (preference == mRebootPref) {
            value = mRebootPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_REBOOT_ENABLED,
                    value ? 1 : 0);        
       } else if (preference == mAirplanePref) {
            value = mAirplanePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_AIRPLANE_ENABLED,
                    value ? 1 : 0);       
       } else if (preference == mSilentPref) {
            value = mSilentPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_SILENT_ENABLED,
                    value ? 1 : 0);
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }
}
