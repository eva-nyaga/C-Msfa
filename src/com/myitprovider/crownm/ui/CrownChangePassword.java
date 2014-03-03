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
import org.json.JSONObject;

import com.example.crownm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrownChangePassword extends Activity{
	
	
	
	ProgressDialog pDialog;
	EditText usname, psword, newpass, confirm;
	Button change;
	String mErrorMessage,res=null;
	HttpPost httpPost;JSONObject json;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	ArrayList<NameValuePair> namevaluepairs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crown_change_password);
		
		usname = (EditText)findViewById(R.id.editText1);
		psword = (EditText)findViewById(R.id.editText2);
		newpass = (EditText)findViewById(R.id.editText3);
		confirm = (EditText)findViewById(R.id.editText4);
		
		change = (Button)findViewById(R.id.button1);
		change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean mError = false;
				mError = false;
            	
            	String password =newpass.getText().toString();
            	String confirmpassword =confirm.getText().toString();
            	
				if (TextUtils.isEmpty(usname.getText())) {
					mErrorMessage = ("Please enter your username")+"\n";
					usname.setError(mErrorMessage);
                    mError = true;
                }
				if (TextUtils.isEmpty(psword.getText())) {
					mErrorMessage = ("Please enter your old password")+"\n";
					psword.setError(mErrorMessage);
                    mError = true;
                }
				if (TextUtils.isEmpty(newpass.getText())) {
					mErrorMessage = ("Please enter your new password")+"\n";
					newpass.setError(mErrorMessage);
                    mError = true;
                }
				if (TextUtils.isEmpty(confirm.getText())) {
					mErrorMessage = ("Please confirm your new password")+"\n";
					confirm.setError(mErrorMessage);
                    mError = true;
                }
				if(!password.equals(confirmpassword)){
					mErrorMessage = ("Passwords Dont Match!!")+"\n";
					confirm.setError(mErrorMessage);
                    mError = true;
                }
				if (!mError) {

                	AlertDialog.Builder dialog = new AlertDialog.Builder(CrownChangePassword.this);	    

				    dialog.setMessage("Change Password?");

				    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {
				        	String username = usname.getText().toString();
		                	String oldpsword = psword.getText().toString();
		                	String newpsword = newpass.getText().toString();
		                	
		                	
		                	new NewPassword().execute(username,oldpsword,newpsword);				        }
				        
				    });

				    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {}
				        
				    });

				    dialog.show();
					
				}
				else{
            	Toast.makeText(CrownChangePassword.this, "Reset Password Error!\n" + mErrorMessage,  Toast.LENGTH_LONG).show();
                    mErrorMessage = "";
            	}
			}
		});
	}
	
	public class NewPassword extends AsyncTask<String, Integer, Void> {
    	
		String URL= getResources().getString(R.string.common_url);
		
    	protected void onPreExecute() {
			
    		pDialog = new ProgressDialog(CrownChangePassword.this);
			pDialog.setMessage("Reseting Password..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
    		         }
		@Override
		protected Void doInBackground(String... params) {
			
			try
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL);
				
				//adding our data
				namevaluepairs = new ArrayList<NameValuePair>();
				namevaluepairs.add (new BasicNameValuePair("t","changepassword"));
				namevaluepairs.add (new BasicNameValuePair("u",params[0]));
				namevaluepairs.add (new BasicNameValuePair("p",params[1]));
				namevaluepairs.add (new BasicNameValuePair("new",params[2]));
				httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
				
				//Execute post request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				inputstream=entity.getContent();
			}
		    catch(Exception e){
		            Log.e("log_tag", "Error in http connection "+e.toString());
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
					 Log.e("log_tag", "Error converting result "+e.toString());
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
			if(NewPassword.this.isCancelled())
			{
				Toast.makeText(CrownChangePassword.this, "Internet Connection Lost!", Toast.LENGTH_LONG).show();
				
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
		pDialog.dismiss();
			 
			    if(res.equals("0")){
			    	Toast.makeText(CrownChangePassword.this, "Error!! Please Verify credentials and try again", Toast.LENGTH_LONG).show();
		        }
		        if(res.equals("1")){
		        	Toast.makeText(CrownChangePassword.this, "Password Sucessfully Reset!", Toast.LENGTH_LONG).show();
					finish();
		        }
			
		}
		}


	


}
