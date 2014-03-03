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
import com.myitprovider.crownm.util.Pref;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class CrownVisitProject extends Activity implements OnRefreshListener {

	public Spinner spinner, spinner1, spinner2,spinner3,spinner4;
	private ProgressDialog pDialog;
	private PullToRefreshLayout mPullToRefreshLayout;
	 
	InputStream inputstream;
	JSONObject json;
	String mErrorMessage,res = "",pid,fid,cid;
	
	
	
	String result=null;
	String line=null;
	String[] project, firmname, person, projectid, firmid, personid;
	String firms;
	final List<String> list1 = new ArrayList<String>();
	final List<String> list11 = new ArrayList<String>();
    final List<String> list2 = new ArrayList<String>();
    final List<String> list22 = new ArrayList<String>();
    final List<String> list3 = new ArrayList<String>();
    final List<String> list33 = new ArrayList<String>();
    
   
   
	
	Button visit;
	ToggleButton onoff;
	EditText feed;
	TextView test;
	
    @SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crown_visit_project);
        
        Fragment fr;
    	
    		FragmentManager fm = getFragmentManager();
    		FragmentTransaction ft = fm.beginTransaction();
    		 fr = fm.findFragmentById(R.id.fragment1);
    		ft.show(fr);
    		 //ft.replace(R.id.fragment1, fr);
    		//ft.add(R.id.fragment1, fr);
    		ft.commit();
    		
    	
        addItemsOnSpinner1();
	       onRefresh();
	       
        spinner = (Spinner)findViewById(R.id.spinner2);//projectname
        spinner1 = (Spinner) findViewById(R.id.spinner1);//purpose
        spinner2 = (Spinner) findViewById(R.id.spinner3);//firm
        spinner3 = (Spinner) findViewById(R.id.spinner4);//person
        
        onoff = (ToggleButton)findViewById(R.id.toggleButton1);
        
        feed = (EditText)findViewById(R.id.editText1);
        
        test = (TextView) findViewById(R.id.textView5);
        
        visit = (Button) findViewById(R.id.button1);
        visit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean mError= false;
				 if (TextUtils.isEmpty(feed.getText())) {
                     mErrorMessage = ("Please provide some feedback")+"\n";
                     feed.setError(mErrorMessage);
                     mError = true;
                 }


				 if (!mError) {

					 AlertDialog.Builder dialog = new AlertDialog.Builder(CrownVisitProject.this);	    

					 dialog.setMessage("Visit Project?");

					 dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
						 public void onClick(DialogInterface arg0, int arg1) {
							String Name = spinner.getSelectedItem().toString();
							String Purpose = spinner1.getSelectedItem().toString();
							String Person = spinner3.getSelectedItem().toString();
							String Location = onoff.getText().toString();
							
							 //String Feedback = feed.getText().toString();
							 String Uid = Pref.user;
							 String lat = Pref.sLatitude;
							 String Long = Pref.sLongitude;
	
	
							 new Visit().execute(Uid, Name, lat, Long,Purpose,Location,Person);
						 }

					 });

					 dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		
						 public void onClick(DialogInterface arg0, int arg1) {}
	
					 });

					 dialog.show();

				 }
				 else{
					 Toast.makeText(CrownVisitProject.this, "Visit Project Error!\n" + mErrorMessage, Toast.LENGTH_LONG).show();
					 mErrorMessage = "";
				 }
			}
        });
        	mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
        	ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);  
        	mPullToRefreshLayout.setRefreshing(true);
    
      
    }
   
    public void onRefresh(){
    	
    	 new GetFirm().execute();
    }

        
       public void addItemsOnSpinner1() {
        	
            spinner1 = (Spinner) findViewById(R.id.spinner1);
            final List<String> list = new ArrayList<String>();
            list.add("Sales");
            list.add("Technical");
            list.add("Follow Up");
            list.add("Collection");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_text, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(dataAdapter);
            
            spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
             

           }
        
 

    	@Override
    	public void onBackPressed() {
    		AlertDialog.Builder dialog = new AlertDialog.Builder(this);	    

    	    dialog.setMessage("Exit Crown MSFA Application?");

    	    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface arg0, int arg1) {
    	        	finish();
    	        }
    	    });

    	    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface arg0, int arg1) {}
    	        
    	    });

    	    dialog.show();
        
    		
    	}
    	
    	
    	private class GetFirm extends AsyncTask<Void, Void, Void> {
    		
    		String URL = "http://197.254.42.106:85/crown/msfa_ws/bootstrap.php?t=projects&uid="+Pref.user;
    		
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
     			
    			
    			project = new String[JA.length()];     
    			projectid = new String[JA.length()];      
    			firmname = new String[JA.length()];
    			firmid = new String[JA.length()];
    			person = new String[JA.length()];
    			personid = new String[JA.length()];
    			
    			
    			for(int i=0;i<JA.length();i++)
    			{
    			
    			JSONObject e =  JA.getJSONObject(i);
    			
    			
    			project[i] = e.getString("name");
    			projectid[i] = e.getString("pid");
    			firmname[i]=e.getString("contractor");
    			firmid[i]=e.getString("contractorid");
    			person[i]=e.getString("contact");
    			personid[i]=e.getString("contactid");
    			
    			list1.add(project[i]);
    			list11.add(projectid[i]);
    			list2.add(firmname[i]);
    			list22.add(firmid[i]);
    			list3.add(person[i]);
    			list33.add(personid[i]);
    			
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
    			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text, list1);
    			dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    			spinner.setAdapter(dataAdapter1);
    			               



    			spinner.setOnItemSelectedListener(new OnItemSelectedListener()
    			{
    			@Override
    			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
    			{
    			// TODO Auto-generated method stub

   				 pid = list11.get(position);   
   				       Log.i("pid", pid); 

    			spinner_fn2();
	               
    			                                                                               
    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0)
    			{
    			list1.add("Select Project ");
    			}
    			                               
    			});            
    			}
    			private void spinner_fn2() {
        			// TODO Auto-generated method stub
        			               
    
        			ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text,list2);
        			dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        			spinner2.setAdapter(dataAdapter2);
        		
        			spinner2.setOnItemSelectedListener(new OnItemSelectedListener()
        			{
        			@Override
        			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
        			{

       				 fid = list22.get(position);   
       				       Log.i("fid", fid); 

        			spinner_fn3();
    	               
        			                                                                               
        			}

        			@Override
        			public void onNothingSelected(AdapterView<?> arg0)
        			{
        			// TODO Auto-generated method stub
        			}
        			                               
        			});            
        			}
    			private void spinner_fn3() {
        			// TODO Auto-generated method stub
        			
        			ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text, list3);
        			dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        			spinner3.setAdapter(dataAdapter3);
    


        			spinner3.setOnItemSelectedListener(new OnItemSelectedListener()
        			{
        			@Override
        			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
        			{

       				 cid = list33.get(position);   
       				       Log.i("cid", cid);                                                                              
        			}

        			@Override
        			public void onNothingSelected(AdapterView<?> arg0)
        			{
        			// TODO Auto-generated method stub
        			}
        			                               
        			});            
        			}

           

    	
    	
    	
    	public class Visit extends AsyncTask<String, Integer, Void> {
    		String URL = getResources().getString(R.string.common_url);
    		
        	protected void onPreExecute() {
        		pDialog = new ProgressDialog(CrownVisitProject.this);
    			pDialog.setMessage("Visiting Firm..");
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
    				ArrayList<NameValuePair> namevaluepairs;
    				namevaluepairs = new ArrayList<NameValuePair>();
    				namevaluepairs.add (new BasicNameValuePair("t","logVisit"));
    				namevaluepairs.add (new BasicNameValuePair("uid",params[0]));
    				namevaluepairs.add (new BasicNameValuePair("name",params[1]));
    				namevaluepairs.add (new BasicNameValuePair("pid",pid));
    				namevaluepairs.add (new BasicNameValuePair("lat",params[2]));
    				namevaluepairs.add (new BasicNameValuePair("long",params[3]));
    				namevaluepairs.add (new BasicNameValuePair("contact",params[4]));
    				namevaluepairs.add (new BasicNameValuePair("reason",params[5]));
           			namevaluepairs.add (new BasicNameValuePair("location",params[6]));
           			
           			
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
    	             Log.i("server response", res);
    	             
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
    					 Log.i("server response", " "+res);
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
    			if(Visit.this.isCancelled())
    			{
    				Toast.makeText(CrownVisitProject.this, "Internet Connection Lost!", Toast.LENGTH_LONG).show();
    				
    			}
    		}
    		@Override
    		protected void onPostExecute(Void result) {
    			pDialog.dismiss();
    			Toast.makeText(CrownVisitProject.this, "Project Visit Successful.", Toast.LENGTH_LONG).show();
    			Intent i = new Intent(CrownVisitProject.this, CrownVisitProject.class);
    			startActivity(i);
    			
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
		    inflater.inflate(R.menu.crown_visitproject_menus, menu);
		    return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (item.getItemId()==R.id.action_settings) {
			 
			 Intent i = new Intent(CrownVisitProject.this, CrownLoginClass.class);
			 startActivity(i);
			 finish();
			
			}
			  
			if (item.getItemId()==R.id.item1){
				 Intent b = new Intent(CrownVisitProject.this, CrownNewProject.class);
				 startActivity(b);
			}
				  
			if (item.getItemId()==R.id.item3){
				 Intent c = new Intent(CrownVisitProject.this, CrownCreateContact.class);
				 startActivity(c);
			}
				  
			if (item.getItemId()==R.id.item2){
				 Intent d = new Intent(CrownVisitProject.this, CrownCreateFirm.class);
				 startActivity(d);
			}
				  
			if (item.getItemId()==R.id.change_password){
				 Intent a = new Intent(CrownVisitProject.this, CrownChangePassword.class);
				 startActivity(a);
			
			
			
		}
			return true;
		}

}
