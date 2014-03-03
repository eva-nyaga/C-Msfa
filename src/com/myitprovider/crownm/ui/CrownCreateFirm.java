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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrownCreateFirm extends Activity{
public ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();

	
	// JSON Node names
	InputStream inputstream;
	int success;
	String mErrorMessage,res=null;
	
	EditText fname, loc, phone, site, mail, special, Ceo, area;
	Button create;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crown_create_firm);
		
		fname = (EditText)findViewById(R.id.editText7);
		loc = (EditText)findViewById(R.id.editText1);
		area = (EditText)findViewById(R.id.editText8);
		phone = (EditText)findViewById(R.id.editText2);
		site = (EditText)findViewById(R.id.editText3);
		mail = (EditText)findViewById(R.id.editText4);
		special = (EditText)findViewById(R.id.editText6);
		Ceo = (EditText)findViewById(R.id.editText5);
		
		
		create=(Button)findViewById(R.id.button2);
		create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				boolean mError = false;
				if (TextUtils.isEmpty(fname.getText())) {
					mErrorMessage = ("Please provide the firm name")+"\n";
					fname.setError(mErrorMessage);
                    mError = true;
                }
                if (TextUtils.isEmpty(loc.getText())) {
                    mErrorMessage = ("Please provide the firm location")+"\n";
                    loc.setError(mErrorMessage);
                    mError = true;
                }
                if (TextUtils.isEmpty(area.getText())) {
                    mErrorMessage = ("Please provide the firm's bussiness area")+"\n";
                    area.setError(mErrorMessage);
                    mError = true;
                }
               if (TextUtils.isEmpty(phone.getText())) {
                    mErrorMessage = ("Please provide firm's contacts")+"\n";
                    phone.setError(mErrorMessage);
                    mError = true;
               }
                    if (!Util.validatePhone(phone.getText().toString())) {
                        mErrorMessage = ("Please provide a valid phone Number")+"\n";
                        phone.setError(mErrorMessage);
                        mError = true;
                   
                }
               if (TextUtils.isEmpty(site.getText())) {
                    mErrorMessage = ("Please provide the firm's website")+"\n";
                    site.setError(mErrorMessage);
                    mError = true;
                }

                	if (TextUtils.isEmpty(mail.getText())) {
                        mErrorMessage = ("Please provide the Email Address")+"\n";
                        mail.setError(mErrorMessage);
                        mError = true;
                	}
                	if (!Util.validateEmail(mail.getText().toString())) {
                        mErrorMessage = ("Please provide a valid email address")+"\n";
                        mail.setError(mErrorMessage);
                        mError = true;
                   
                }
                	
               if (TextUtils.isEmpty(special.getText())) {
                    mErrorMessage = ("Please provide the firms' specialty")+"\n";
                    special.setError(mErrorMessage);
                    mError = true;
                }
               if (TextUtils.isEmpty(Ceo.getText())) {
                    mErrorMessage = ("Please provide the firms' CEO's name")+"\n";
                    Ceo.setError(mErrorMessage);
                    mError = true;
                }else
               if (!mError) {
                	
                	AlertDialog.Builder dialog = new AlertDialog.Builder(CrownCreateFirm.this);	    

				    dialog.setMessage("Create Firm?");

				    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {
				        	String Name = fname.getText().toString();
		                	String location = loc.getText().toString();
		                	String Area = area.getText().toString();
		                	String website = site.getText().toString();
		                	String email = mail.getText().toString();
		                	String specialty = special.getText().toString();
		                	String ceo = Ceo.getText().toString();
		                	String phoneno = phone.getText().toString();
		                	String uid = Pref.user;
		                	
		                	new CreateNewFirm().execute(Name,Area,location, specialty,ceo, website, email, phoneno,uid);
				        }
				        
				    });

				    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {}
				        
				    });

				    dialog.show();
                	
                }
                else{
            	Toast.makeText(CrownCreateFirm.this, "Add Firm Error!\n" + mErrorMessage, Toast.LENGTH_LONG).show();
                 mErrorMessage = "";
            	}
			}
		});
	}
	  
	
 private class CreateNewFirm extends AsyncTask<String, Integer, Void> {

	 String url_create_firm= getResources().getString(R.string.common_url);;
		
		 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CrownCreateFirm.this);
			pDialog.setMessage("Creating Firm..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		
		
		 
		protected Void doInBackground(String... args) {
			
			try
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url_create_firm);
				
				//adding our data
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add (new BasicNameValuePair("t","newFirm"));
				params.add (new BasicNameValuePair("firmName",args[0]));
				params.add (new BasicNameValuePair("area",args[1]));
				params.add (new BasicNameValuePair("location",args[2]));
				params.add (new BasicNameValuePair("specialty",args[3]));
				params.add (new BasicNameValuePair("ceo",args[4]));
				params.add (new BasicNameValuePair("website",args[5]));
				params.add (new BasicNameValuePair("email",args[6]));
				params.add (new BasicNameValuePair("phoneNo",args[7]));
				params.add (new BasicNameValuePair("uid",args[8]));
				httpPost.setEntity (new UrlEncodedFormEntity(params));
				
				//Execute post request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				inputstream=entity.getContent();
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
				 Log.e("log_tag", "Error in http connection "+e.toString());
				return null;
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
	             
	             
			}
			
			catch(Exception e){
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }
			if (isCancelled())
			{
				return null;
			}
			
			return null;
		}
		@Override
		protected void onProgressUpdate(final Integer... progress) {
			if(CreateNewFirm.this.isCancelled())
			{
				Toast.makeText(CrownCreateFirm.this, "Internet Connection Lost!", Toast.LENGTH_LONG).show();
				
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			Toast.makeText(CrownCreateFirm.this, "Firm Successfully added!", Toast.LENGTH_LONG).show();
			finish();
		}
		}
	
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
 	MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.crown_createfirm_menus, menu);
     return true;
 }

 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
 	
 		  
 	if (item.getItemId()==R.id.item2){
 		 Intent d = new Intent(CrownCreateFirm.this, CrownCreateContact.class);
 		 startActivity(d);
 	}
 		  
 	if (item.getItemId()==R.id.item1){
 		 Intent a = new Intent(CrownCreateFirm.this, CrownNewProject.class);
 		 startActivity(a);
 	
 	
 	
 }
 	return true;
 }


}
