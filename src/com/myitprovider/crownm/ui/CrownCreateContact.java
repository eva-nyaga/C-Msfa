package com.myitprovider.crownm.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.example.crownm.R;
import com.myitprovider.crownm.database.JSONFunctions;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class CrownCreateContact extends Activity implements OnRefreshListener{
	public ProgressDialog pDialog;
	private PullToRefreshLayout mPullToRefreshLayout;
	JSONParser jsonParser = new JSONParser();
	
	ArrayAdapter<String> dataAdapter1;
	
	// JSON Node names
	InputStream inputStream;
	String mErrorMessage, res = null,Id;
	

	Spinner spinner;
	EditText name, desig, phone, mail;
	Button create;
	int success;
	
	String[] firmname, contactname, id;
	final List<String> list1 = new ArrayList<String>();
	final List<String> list2 = new ArrayList<String>();
	final List<String> emptylist = new ArrayList<String>();
	
	String[] empty = {" "};
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crown_create_contact);
		
		onRefresh();
		
		name = (EditText)findViewById(R.id.editText1);
		desig= (EditText)findViewById(R.id.editText4);
		phone = (EditText)findViewById(R.id.editText2);
		mail = (EditText)findViewById(R.id.editText3);
		create = (Button) findViewById(R.id.button1);
		
		

		spinner = (Spinner)findViewById(R.id.spinner1);
		
		create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean mError = false;
				if (TextUtils.isEmpty(name.getText())) {
                    mErrorMessage = ("Please Provide the contact Name")+"\n";
                    name.setError(mErrorMessage);
                    mError = true;
                }
                	if (TextUtils.isEmpty(desig.getText())) {
                        mErrorMessage = ("Please provide the contact designation")+"\n";
                        desig.setError(mErrorMessage);
                        mError = true;
                    }
				if (TextUtils.isEmpty(phone.getText())) {
                    mErrorMessage = ("Please Provide the contact Phone Number")+"\n";
                    phone.setError(mErrorMessage);
                    mError = true;
                    
                }
                	if (!Util.validatePhone(phone.getText().toString())) {
                        mErrorMessage = ("Please provide a valid phone number")+"\n";
                        phone.setError(mErrorMessage);
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
                    	
                    	
                   
                if (!mError) {
                	
                	AlertDialog.Builder dialog = new AlertDialog.Builder(CrownCreateContact.this);	    

				    dialog.setMessage("ADD CONTACT PERSON?");

				    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {
				        	String Name = name.getText().toString();
		                	String Desig = desig.getText().toString();
		                	String Phone = phone.getText().toString();
		                	String Email = mail.getText().toString();
		                	
		                	String Uid = Pref.user;
		                	
		                	
		                	new CreateNewContact().execute(Name,Desig,Phone, Email, Uid);
				        }
				        
				    });

				    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {}
				        
				    });

				    dialog.show();
                	
                }
                else{
            		Toast.makeText(CrownCreateContact.this, "Add Contact Person Error!\n" + mErrorMessage,Toast.LENGTH_LONG).show();
                     mErrorMessage = "";
             	}
			}
		}); 
		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this)
        .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);  
        mPullToRefreshLayout.setRefreshing(true);
    
      
}
   
    public void onRefresh(){
    	
    	 new GetFirm().execute();
    
	
	}
	private class GetFirm extends AsyncTask<Void, Void, Void> {
		String URL = getResources().getString(R.string.get_firms_url);
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}

		@Override
		
		
		protected Void doInBackground(Void... arg0) {
			
			 
			
			try
			{
			JSONObject json= JSONFunctions.getJSONfromURL(URL);
			Log.i("JSON OBJECT", json.toString());
 			
			JSONArray JA=new JSONArray();
 			JA = json.getJSONArray("PAYLOAD");
 			
			
			firmname = new String[JA.length()];   
			id = new String[JA.length()];      
			
			
			for(int i=0;i<JA.length();i++)
			{
		    JSONObject e =  JA.getJSONObject(i);
			firmname[i] = e.getString("firm_name");
			id[i] = e.getString("firm_id");  
			
			}
			

			for(int i=0;i<firmname.length;i++){
			{
			list1.add(firmname[i]);
			list2.add(id[i]);
			//emptylist.add(empty[0]);
			//id[i] = Pref.firmId;
			}
			}
			}
			catch(Exception e)
			{
				Log.e("Fail", e.toString());
			}
			
			
		        
		
			return null;
			}
		@Override
		 protected void onPostExecute(Void result) {
         	
			
         	spinner_fn();
         	mPullToRefreshLayout.setRefreshComplete();
         }
	}

			private void spinner_fn() {
			// TODO Auto-generated method stub
		 dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text, list1);
			dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter1);
			               



			spinner.setOnItemSelectedListener(new OnItemSelectedListener()
			{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
			{
				 Id = list2.get(position);   
				       Log.i("id", Id); 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			list1.add("Select Firm ");
			}
			                               
			});            
			}
			
			
	
class CreateNewContact extends AsyncTask<String, Integer, Void> {
	
	 String url_create_contact = getResources().getString(R.string.common_url);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CrownCreateContact.this);
			pDialog.setMessage("Creating Contact..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		
		// Creating user
		 
		protected Void doInBackground(String... args) {
		
			try
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url_create_contact);
				
				//adding our data
				ArrayList<NameValuePair>namevaluepairs = new ArrayList<NameValuePair>();
				namevaluepairs.add (new BasicNameValuePair("t","newContact"));
				namevaluepairs.add (new BasicNameValuePair("name",args[0]));
				namevaluepairs.add (new BasicNameValuePair("designation",args[1]));
				namevaluepairs.add (new BasicNameValuePair("firmid",Id));
				namevaluepairs.add (new BasicNameValuePair("phoneNo",args[2]));
				namevaluepairs.add (new BasicNameValuePair("email",args[3]));
				namevaluepairs.add (new BasicNameValuePair("uid",args[4]));
				
				

				httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
				
				//Execute post request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				inputStream=entity.getContent();
				
				Log.e("Debug", "Sent"+inputStream);

				
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
					
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
	          
	            inputStream.close();
	             res = sb.toString().trim();
	             
	             Log.i("Log Here", "server response" +res);
	           
	             
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
			if(CreateNewContact.this.isCancelled())
			{
				Toast.makeText(CrownCreateContact.this, "Internet Connection Lost!", Toast.LENGTH_LONG).show();
				
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			Toast.makeText(CrownCreateContact.this, "Contact Person Successfully added!", Toast.LENGTH_LONG).show();
			finish();
		}
		}

@Override
public void onRefreshStarted(View view) {
	// TODO Auto-generated method stub
	new GetFirm().execute();
}
	
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.crown_createcontact_menus, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	  
	if (item.getItemId()==R.id.item2){
		 Intent d = new Intent(CrownCreateContact.this, CrownCreateFirm.class);
		 startActivity(d);
	}
		  
	if (item.getItemId()==R.id.item1){
		 Intent a = new Intent(CrownCreateContact.this, CrownNewProject.class);
		 startActivity(a);
	
	
	
}
	return true;
}
}
