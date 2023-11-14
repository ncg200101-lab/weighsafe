package com.weigh.safemobile.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferenceManager {
    private static SharedPreferences mSharedPreferences;
    private static final String KEY_USER_PREF = "KEY_USER_PREFERENCES";
    private static final String SELECTED_POSITION = "SELECTED_POSITION";
    private static final String INSTALLATION_GUIDE = "INSTALLATION_GUIDE";

    private static final String CURRENT_VEHICLE = "CURRENT_VEHICLE";
    private static final String CURRENT_HITCH = "CURRENT_HITCH";
    private static final String CURRENT_TRAILER = "CURRENT_TRAILER";
    private static final String CURRENT_LIST_CARGO = "CURRENT_LIST_CARGO";

    private static final String WIZARD_ON = "WIZARD_ON";

    private static final String TONGUE_WEIGHT = "TONGUE_WEIGHT";
    private static final String TOWBALL_TO_ENDOF_SPRINGBARS = "TOWBALL_TO_ENDOF_SPRINGBARS";
    private static final String TOWBALL_TO_TRAILERAXLE = "TOWBALLTO_TRAILERAXLE";
    private static final String REARAXLE_TO_ENDOF_RECEIVER = "REARAXLE_TO_ENDOF_RECEIVER";
    private static final String HOLES_VISIBLE = "HOLES_VISIBLE";
    private static final String DRAWBAR_POSITION = "DRAWBAR_POSITION";

    private static final String SHOW_DISCLAIMER = "SHOW_DISCLAIMER";



    public static boolean setCurrentVehicle(Context mContext, int ID) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURRENT_VEHICLE, ID);
        return editor.commit();
    }

    public static int getCurrentVehicle(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(CURRENT_VEHICLE, 0);
    }

    public static boolean setCurrentHitch(Context mContext, int ID) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURRENT_HITCH, ID);
        return editor.commit();
    }

    public static int getCurrentHitch(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(CURRENT_HITCH, 0);
    }

    public static boolean setCurrentTrailer(Context mContext, int ID) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURRENT_TRAILER, ID);
        return editor.commit();
    }

    public static int getCurrentTrailer(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(CURRENT_TRAILER, 0);
    }

    public static boolean setCurrentCargoList(Context mContext, List<String> idList) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Set<String> setData = new HashSet<String>();
        setData.addAll(idList);
        editor.putStringSet(CURRENT_LIST_CARGO, setData);
        return editor.commit();
    }

    public static List<String> getCurrentCargoList(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        Set<String> setData = mSharedPreferences.getStringSet(CURRENT_LIST_CARGO, null);
        List<String> cargoList = new ArrayList<>();
        cargoList.addAll(setData);
        return cargoList;
    }

    /**
     * method to setPositionID
     *
     * @param ID this is the first parameter for setID  method
     * @return return value
     */
    public static boolean setPositionID(Context mContext, int ID) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(SELECTED_POSITION, ID);
        return editor.commit();
    }

    /**
     * method to getTongueWeight
     *
     * @return return value
     */
    public static float getTongueWeight(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(TONGUE_WEIGHT, 0);
    }

    /**
     * method to setTongueWeight
     *
     * @param value this is the first parameter for setTongueWeight  method
     * @return return value
     */
    public static boolean setTongueWeight(Context mContext, float value) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(TONGUE_WEIGHT, value);
        return editor.commit();
    }

    /**
     * method to getTowBallToEndOfSpringBars
     *
     * @return return value
     */
    public static float getTowBallToEndOfSpringBars(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(TOWBALL_TO_ENDOF_SPRINGBARS, 0);
    }

    /**
     * method to setTowBallToEndOfSpringBars
     *
     * @param value this is the first parameter for setTongueWeight  method
     * @return return value
     */
    public static boolean setTowBallToEndOfSpringBars(Context mContext, float value) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(TOWBALL_TO_ENDOF_SPRINGBARS, value);
        return editor.commit();
    }

    /**
     * method to getTowBallToTrailerAxle
     *
     * @return return value
     */
    public static float getTowBallToTrailerAxle(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(TOWBALL_TO_TRAILERAXLE, 0);
    }

    /**
     * method to setTowBallToTrailerAxle
     *
     * @param value this is the first parameter for setTongueWeight  method
     * @return return value
     */
    public static boolean setTowBallToTrailerAxle(Context mContext, float value) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(TOWBALL_TO_TRAILERAXLE, value);
        return editor.commit();
    }

    /**
     * method to getRearAxleToEndOFReceiver
     *
     * @return return value
     */
    public static float getRearAxleToEndOFReceiver(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(REARAXLE_TO_ENDOF_RECEIVER, 0);
    }

    /**
     * method to setRearAxleToEndOfReceiver
     *
     * @param value this is the first parameter for setTongueWeight  method
     * @return return value
     */
    public static boolean setRearAxleToEndOfReceiver(Context mContext, float value) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(REARAXLE_TO_ENDOF_RECEIVER, value);
        return editor.commit();
    }

    /**
     * method to getHolesVisible
     *
     * @return return value
     */
    public static float getHolesVisible(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(HOLES_VISIBLE, 0);
    }

    /**
     * method to setHolesVisible
     *
     * @param value this is the first parameter for setTongueWeight  method
     * @return return value
     */
    public static boolean setHolesVisible(Context mContext, float value) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(HOLES_VISIBLE, value);
        return editor.commit();
    }

    /**
     * method to getDrawBarPosition
     *
     * @return return value
     */
    public static float getDrawBarPosition(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(DRAWBAR_POSITION, 0);
    }

    /**
     * method to setDrawBarPosition
     *
     * @param value this is the first parameter for setTongueWeight  method
     * @return return value
     */
    public static boolean setDrawBarPosition(Context mContext, float value) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(DRAWBAR_POSITION, value);
        return editor.commit();
    }

    /**
     * method to getPositionID
     *
     * @return return value
     */
    public static int getPositionID(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(SELECTED_POSITION, 0);
    }



    /**
     * method to set Installation Guide First loading
     *
     * @return return value
     */

    public static boolean setInstallationGuide(Context mContext, boolean isOn) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(INSTALLATION_GUIDE, isOn);
        return editor.commit();
    }

    public static boolean getInstallationGuide(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(INSTALLATION_GUIDE, false);
    }

    /**
     * method to set the Wizard On
     *
     * @return return value
     */
    public static boolean setWizardOn(Context mContext, boolean isOn) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(WIZARD_ON, isOn);
        return editor.commit();
    }

    public static boolean getWizardOn(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(WIZARD_ON, false);
    }

    /**
     * method to set the ShowDisclaimer On
     *
     * @return return value
     */
    public static boolean setShownDisclaimer(Context mContext, boolean isOn) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(SHOW_DISCLAIMER, isOn);
        return editor.commit();
    }

    public static boolean getShownDisclaimer(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(KEY_USER_PREF, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(SHOW_DISCLAIMER, false);
    }

}
