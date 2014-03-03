package com.myitprovider.crownm.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.example.crownm.R;
import com.myitprovider.crownm.database.JSONParser;
import com.myitprovider.crownm.util.Pref;
import com.myitprovider.crownm.util.Util;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrownLoginClass extends Activity{
	
	
	ProgressDialog pDialog;
	EditText us, ps;
	Button login, forgot;

	LocationManager location;
	boolean gpsAvail;
	ArrayList<NameValuePair> namevaluepairs;
	JSONParser jsonParser = new JSONParser();
	InputStream inputstream;
	String res="";
	
	String mErrorMessage="";
	String username, password,version;
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crown_login);
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		this.setTitle(getResources().getString(R.string.app_name)+" " + "Version:"+version);
		
		login = (Button) findViewById(R.id.button1);
		forgot =(Button) findViewById(R.id.button2);
		
	
		us=(EditText) findViewById(R.id.editText1);
		ps=(EditText) findViewById(R.id.editText2);
		
		 Pref.loadSettings(CrownLoginClass.this);
		location = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 
	    gpsAvail = location.isProviderEnabled(LocationManager.GPS_PROVIDER) ;
	     
	     
	    login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {


				boolean mError = false;
		    	if (TextUtils.isEmpty(us.getText())) {
		    		mErrorMessage = ("Username field is empty")+"\n";
		        	us.setError(mErrorMessage);
		            mError = true;
		        }
		        if (TextUtils.isEmpty(ps.getText())) {
		            mErrorMessage = ("Password field is empty")+"\n";
		        	ps.setError(mErrorMessage);
		            mError = true;
		        }else
		       
		        if(!mError && Util.isConnected(CrownLoginClass.this)){
		        	
					if (location.isProviderEnabled(LocationManager.GPS_PROVIDER)){
						 username = us.getText().toString();
					     password = ps.getText().toString();   
					     try {
							version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					         
		        	new Login().execute();
		        	
		        	}
		            else{
		            	new AlertDialog.Builder(CrownLoginClass.this)
		    	        .setTitle("Enable Gps Connection")
		    	        .setMessage("Enable Gps Connection")
		    	        .setPositiveButton("Settings",
		    	                new DialogInterface.OnClickListener() {
		    	                    @Override
		    	                    public void onClick(DialogInterface dialog, int which) {
		    	                    	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		    	                    }
		    	                })
		    	        .setNegativeButton("Cancel",
		    	                new DialogInterface.OnClickListener() {
		    	                    @Override
		    	                    public void onClick(DialogInterface dialog, int which) {
		    	                    	CrownLoginClass.this.finish();
		    	                    }
		    	                }).show();
		            	
		            }
		        	 }
		        	 else{
		        		 Toast.makeText(CrownLoginClass.this, " No Internet Connection Detected", Toast.LENGTH_LONG).show();
		        	 }
				
			}
		});
	}
	

	
	
		
      
        
        public class Login extends AsyncTask<Void, Integer, Void> {
        	
        	
        	
        	protected void onPreExecute() {
    			
        		pDialog = new ProgressDialog(CrownLoginClass.this);
    			pDialog.setMessage("Authenticating User..");
    			pDialog.setIndeterminate(false);
    			pDialog.setCancelable(true);
    			pDialog.show();
        		         }
    		@Override
    		protected Void doInBackground(Void... arg0) {
    			
    			String URL =getResources().getString(R.string.common_url);
    			try
    			{
    				HttpClient httpclient = new DefaultHttpClient();
    				HttpPost httpPost = new HttpPost(URL);
    				 
    				//adding our data
    				namevaluepairs = new ArrayList<NameValuePair>(3);
    				namevaluepairs.add (new BasicNameValuePair("t","login"));
    				namevaluepairs.add (new BasicNameValuePair("u",username));
    				namevaluepairs.add (new BasicNameValuePair("p",password));
    				namevaluepairs.add (new BasicNameValuePair("v",version));
    				httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
    				
    				//Execute post request
    				HttpResponse response = httpclient.execute(httpPost);
    				HttpEntity entity = response.getEntity();
    				inputstream=entity.getContent();
    			}
    		    catch(Exception e){
    		            Log.e("Crown Login ", "Error in http connection "+e.toString());
    		    }
    				try
    				{
    					
    				BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream,"iso-8859-1"),8);
    	            StringBuilder sb = new StringBuilder();
    	            String line = null;
    	            while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                }
    	            inputstream.close();
    	             res = sb.toString().trim();
    	             Log.i("Server response", res);
    	             
    	             
    			}
    				catch (ConnectTimeoutException c) {
    					Log.e("Crown MSFA", "Error in ConnectTimeoutException "+c.toString());
    					
    					return null;
    				} catch (SocketTimeoutException s) {
    					Log.e("Crown MSFA", "Error in SocketTimeoutException "+s.toString());
    					
    					return null;
    				} catch (NullPointerException n) {
    					Log.e("Crown MSFA", "Error in NullPointerException "+n.toString());
    					
    					return null;
    				} catch (MalformedURLException m) {
    					Log.e("Crown MSFA", "Error in MalformedURLException "+m.toString());
    					
    					return null;
    				} catch (IOException io) {
    					Log.e("Crown MSFA", "Error in IOException "+io.toString());
    					
    					return null;
    				} catch (Exception e) {
    					Log.e("Crown MSFA", "Error in Internet connection Exception "+e.toString());
    					
    					return null;
    				}
    			
    			
    			if (isCancelled())
    			{
    				return null;
    			}
    			
    			return null;
    		}
    		@Override
    		protected void onProgressUpdate(final Integer... progress) {
    			if(Login.this.isCancelled())
    			{
    				Toast.makeText(CrownLoginClass.this, "Internet Connection Lost!", Toast.LENGTH_LONG).show();	
    			}
    		}
    		
    		@Override
    		protected void onPostExecute(Void result) {
    			
    	        pDialog.dismiss();
    	        if(res.equals("-1")){
    	        	Toast.makeText(CrownLoginClass.this, "Old Version", Toast.LENGTH_LONG).show();
    	        	forgot.setText("Download new version \n Click Here!!");
    	        	forgot.setBackgroundColor(Color.parseColor("#FF0000"));
    	        	forgot.setVisibility(View.VISIBLE);
    	        	forgot.setOnClickListener(new View.OnClickListener() {
    	                public void onClick(View v) {
    	                	String URL ="http://my-it-provider.net/crown/app";
    						String url =URL.toString().trim();
    						Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    						startActivity(browse);
    	                }
    	        	
    	        	});
    	        	
    	        return;
    	        }else if (res.equals("0")){
    	        	Toast.makeText(CrownLoginClass.this, "UserName And Password Don't Match", Toast.LENGTH_LONG).show();
    	        	forgot.setText("Forgot password? \n Click Here!!");
    	        	forgot.setBackgroundColor(Color.parseColor("#FF0000"));
    	        	forgot.setVisibility(View.VISIBLE);
    	        	forgot.setOnClickListener(new View.OnClickListener() {
    	                public void onClick(View v) {
    	                	String PHONENUMBER ="0206533603";
    						String number ="tel:"+PHONENUMBER.toString().trim();
    						Intent call = new Intent (Intent.ACTION_CALL,Uri.parse(number));
    		        		startActivity(call);
    	                }
    	        	
    	        	});
    	        	
    	        return;
    	        } else 
    	       
    	       {
    	        	String uid=res;
    	        	Log.v("uid", res);
    	        	Intent intent = new Intent(CrownLoginClass.this, CrownVisitProject.class);
    	        	Pref.loadSettings(CrownLoginClass.this);
    	        	Pref.user =uid;
    	        	Pref.saveSettings(CrownLoginClass.this);
                    startActivity(intent);
                    CrownLoginClass.this.finish();
    	        	
		}
    }
   }
}
