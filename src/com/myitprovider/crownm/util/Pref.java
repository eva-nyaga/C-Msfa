package com.myitprovider.crownm.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
	public static final int NOTIFICATION_ID = 1;
	
	private static SharedPreferences settings;

    private static SharedPreferences.Editor editor;
    
    public static int autologin = 0;
    
    public static int DatabaseActivated = 0;
    
    public static String user="";
    
    public static String firmId="";
    
    public static String mode="";
    
    public static String amount="";
    
    public static final String PREFS_NAME = "crownMSFA";
    
    public static String contacts = "";
    
    public static int firstlogin = 0;
    
    public static String sLongitude = "";

    public static String sLatitude = "";
    
    
    
    public static void loadSettings(Context context) {
        final SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        autologin =settings.getInt("AutoLogin", autologin);
        DatabaseActivated =settings.getInt("DB", DatabaseActivated);
        firstlogin =settings.getInt("FirstLogin", firstlogin);
        user=settings.getString("User", user);
        firmId=settings.getString("FirmId", firmId);
        mode=settings.getString("Mode", mode);
        amount=settings.getString("Amount", amount);
        sLongitude=settings.getString("Longitude", sLongitude);
        sLatitude=settings.getString("Latitude", sLatitude);
        
        
    }
    public static void saveSettings(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.putInt("AutoLogin", autologin);
        editor.putInt("DB", DatabaseActivated);
        editor.putInt("FirstLogin", firstlogin);
        editor.putString("User", user);
        editor.putString("FirmId", firmId);
        editor.putString("Amount", amount);
        editor.putString("Mode", mode);
        editor.putString("Longitude", sLongitude);
        editor.putString("Latitude", sLatitude);
        
        editor.commit();
    }

}
