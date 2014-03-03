package com.myitprovider.crownm.services;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.myitprovider.crownm.util.Pref;

@SuppressLint("HandlerLeak")
public class CrownGPS  extends Service implements LocationListener {
	protected static final int ONE_MINUTE = 60 * 1000;

    protected static final int FIVE_MINUTES = 5 * ONE_MINUTE;

    protected static double sLongitude = 0.0;

    protected static double sLatitude = 0.0;

    protected LocationManager mLocationMgr;

    protected Location curLocation;
    
    private TimerTask mDoTask;
    
	private Timer mT = new Timer();
    
	private Handler xHandler;
	
	private static final int SOME_OPERATION=1;
    @Override
    public void onCreate() {
		   
    	long delay  = 30000;
		long period = 20000; 
		mDoTask = new TimerTask() {
			@Override
			public void run() {
				xHandler.sendMessage(Message.obtain(xHandler, SOME_OPERATION));
			}
		};
		mT.scheduleAtFixedRate(mDoTask, delay, period);
		
		xHandler=new Handler(){
         	@Override
         	public void handleMessage(Message message){
         		switch (message.what){
         		case SOME_OPERATION:
         			getLocation();
         			break;
         		}
         	}
         }; 

		 
    }
    private void getLocation(){
    	    mLocationMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        Location nloc = null;
	        Location gloc = null;
	        boolean netAvail = mLocationMgr.getProvider(LocationManager.NETWORK_PROVIDER) != null;
	        boolean gpsAvail = mLocationMgr.getProvider(LocationManager.GPS_PROVIDER) != null;
	        if (netAvail)
	            nloc = mLocationMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	        if (gpsAvail)
	            gloc = mLocationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        
	        setBestLocation(nloc, gloc);
	        
	        if (curLocation == null || (new Date()).getTime() - curLocation.getTime() > ONE_MINUTE) {
	            if (netAvail)
	                mLocationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	            if (gpsAvail)
	                mLocationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	        }
    }
    protected void setBestLocation(Location f1, Location f2) {
        if (f1 == null && f2 == null)
            return;
        if (f1 == null) {
            setLocation(f2);
            return;
        }
        if (f2 == null) {
            setLocation(f1);
            return;
        }
        boolean f1SigNewer = f1.getTime() - f2.getTime() > FIVE_MINUTES;
        boolean f2SigNewer = f2.getTime() - f1.getTime() > FIVE_MINUTES;
        if (f1SigNewer)
            setLocation(f1);
        if (f2SigNewer)
            setLocation(f1);
        boolean f1MoreAccurate = f1.getAccuracy() < f2.getAccuracy();
        if (f1.hasAccuracy() && f2.hasAccuracy() && f1MoreAccurate) {
            setLocation(f1);
        } else {
            setLocation(f2);
        }
    }
    protected void setLocation(Location loc) {
        if (loc != null) {
            curLocation = loc;
            
            sLatitude = loc.getLatitude();
            String lat=sLatitude+"";
            Pref.loadSettings(CrownGPS.this);
    		Pref.sLatitude=lat;
    		Pref.saveSettings(CrownGPS.this);
            Log.i("CROWN SERVICE TAG Latitude", lat);
            
            sLongitude = loc.getLongitude();
            String lon=sLongitude+"";
            Pref.loadSettings(CrownGPS.this);
    		Pref.sLongitude=lon;
    		Pref.saveSettings(CrownGPS.this);
            Log.i("CROWN SERVICE TAG Longitude", lon);
        }
    
    }
    public void stopLocating() {
        if (mLocationMgr != null) {
            try {
                mLocationMgr.removeUpdates(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mLocationMgr = null;
        }
    }
    @Override
    public void onLocationChanged(Location loc) {
        if (loc != null) {
            setLocation(loc);
            stopLocating();
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
    	 stopLocating();

    }

    @Override
    public void onProviderEnabled(String provider) {
    
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      

    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}