package com.myitprovider.crownm.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
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


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CrownNewProject extends Activity  implements OnRefreshListener{
public ProgressDialog pDialog;
private PullToRefreshLayout mPullToRefreshLayout;

	JSONParser jsonParser = new JSONParser();


	public Spinner spinner, spinner1, spinner2,spinner3;
	Button set1, set2, create;
	EditText projectname, arch, dev, pmanager, QS, sub, sales, paintvalue, paintvol, start, end,loc; 
	int success;
	String mErrorMessage, res = "";
	
	InputStream inputstream;
	
	List<String> firms = new ArrayList<String>();
	String[] firmname, contactname, salespersonname, firmid, contactid, salespersonid;
	String fid,cid,sid;
	
	
	final List<String> list1 = new ArrayList<String>();
	final List<String> list11 = new ArrayList<String>();
	final List<String> list2 = new ArrayList<String>();
	final List<String> list22 = new ArrayList<String>();
	final List<String> list3 = new ArrayList<String>();
	final List<String> list33 = new ArrayList<String>();
	

	int mYear=2010;
	int mMonth;
	int mDay;
	int sYear;
	int sMonth;
	int sDay;
	    
 
	static final int FROM_START_DATE = 1;
	static final int TO_END_DATE = 0;
	
	String result=null;
	String line=null;
	String[] contacts;
	final List<String> contactlist = new ArrayList<String>();
	
	public CrownNewProject() {
		 Calendar c = Calendar.getInstance();
		    mYear = c.get(Calendar.YEAR);
		    mMonth = c.get(Calendar.MONTH);
		    mDay = c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crown_new_project);
		
		 onRefresh();
		
		
		final HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
		hsv.post(new Runnable() {

	        @SuppressLint("InlinedApi")
			@Override
	        public void run() {
	            
	            
		hsv.fullScroll(HorizontalScrollView.LAYOUT_DIRECTION_LTR);
	        }
		});
		
		spinner = (Spinner) findViewById(R.id.spinner3);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner4);
		
		projectname = (EditText)findViewById(R.id.editText1);
		arch = (EditText)findViewById(R.id.editText2);
		dev = (EditText)findViewById(R.id.editText3);
		pmanager = (EditText)findViewById(R.id.editText4);
		QS = (EditText)findViewById(R.id.editText5);
		sub = (EditText)findViewById(R.id.editText6);
		paintvalue =(EditText) findViewById(R.id.editText8);
		paintvol = (EditText)findViewById(R.id.editText7);
		loc = (EditText) findViewById(R.id.editText10);
		

		
		set2 = (Button) findViewById(R.id.button3);
		set2.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {

				showDialog(FROM_START_DATE);
				
			}
		});
		
		set1 = (Button) findViewById(R.id.button4);
		set1.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {

				showDialog(TO_END_DATE);
				
			}
		});
		
		create = (Button)findViewById(R.id.button5);
		create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				boolean mError=false;
				
				if (TextUtils.isEmpty(projectname.getText())) {
					mErrorMessage = ("Please fill in the Project Name")+"\n";
					projectname.setError(mErrorMessage);
                    mError = true;
                }
               if (TextUtils.isEmpty(loc.getText())) {
            	   mErrorMessage = ("Please provide the Location")+"\n";
            	   loc.setError(mErrorMessage);
                   mError = true;
                }
              
                	
                if (TextUtils.isEmpty(set2.getText())) {
                    mErrorMessage = ("Please select the Starting Date")+"\n";
                    set2.setError(mErrorMessage);
                    mError = true;
                }
                if (TextUtils.isEmpty(set1.getText())) {
                    mErrorMessage = ("Please select the Expected Date of completion")+"\n";
                    set1.setError(mErrorMessage);
                    mError = true;
                }
               if (TextUtils.isEmpty(arch.getText())) {
                     mErrorMessage = ("Please provide the architect's Name")+"\n";
                     arch.setError(mErrorMessage);
                     mError = true;
                }
               if (TextUtils.isEmpty(dev.getText())) {
                     mErrorMessage = ("Please the developer's Name")+"\n";
                     dev.setError(mErrorMessage);
                     mError = true;
                }
	           if (TextUtils.isEmpty(pmanager.getText())) {
	                 mErrorMessage = ("Please provide the project manager's Name")+"\n";
	                 pmanager.setError(mErrorMessage);
	                 mError = true;
	            }
	           if (TextUtils.isEmpty(QS.getText())) {
	                 mErrorMessage = ("Please provide the quantity survayor's Name")+"\n";
	                 QS.setError(mErrorMessage);
	                 mError = true;
	           }
	          if (TextUtils.isEmpty(sub.getText())) {
	                 mErrorMessage = ("Please provider the painting sub-conttactor's Name")+"\n";
	                 sub.setError(mErrorMessage);
	                 mError = true;
	           }
	         
	          if (TextUtils.isEmpty(paintvalue.getText())) {
	                 mErrorMessage = ("Please provide the Paint Value")+"\n";
	                 paintvalue.setError(mErrorMessage);
	                 mError = true;
	           }
	          if (TextUtils.isEmpty(paintvol.getText())) {
	                 mErrorMessage = ("Please provide the Paint Volume")+"\n";
	                 paintvol.setError(mErrorMessage);
	                 mError = true;
	           }
	          if (!mError) {
                	
                	AlertDialog.Builder dialog = new AlertDialog.Builder(CrownNewProject.this);	    

				    dialog.setMessage("Create Project?");

				    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {
				        	
				        	String Name = projectname.getText().toString();
		                	String location = loc.getText().toString();
		                	
		                	String stage = spinner.getSelectedItem().toString();
		                	
		                	String startdate = set2.getText().toString();
		                	String enddate = set1.getText().toString();
		                	String architect = arch.getText().toString();
		                	String developer = dev.getText().toString();
		                	String pm = pmanager.getText().toString();
		                	String qs = QS.getText().toString();
		                	String painter = sub.getText().toString();
		                	
		                	String Uid = Pref.user;
		                	String paintvolume = paintvol.getText().toString();
		                	String paintValue = paintvalue.getText().toString();
		                	
		                	new CreateNewProject().execute(Name,location, stage,startdate,enddate,architect,qs, pm, painter, developer, Uid, paintvolume, paintValue );
				        }
				        
				    });

				    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {}
				        
				    });

				    dialog.show();
                	
                }
                else{
            		 Toast.makeText(CrownNewProject.this, "Create Project Error!\n" + "Some fields are missing" ,Toast.LENGTH_LONG).show();
                  mErrorMessage = "";
            	}
              }
		});
	
		addItemsOnSpinner();
		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this)
        .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);  
        mPullToRefreshLayout.setRefreshing(true);
    
      
}
   
    public void onRefresh(){
    	
    	 new GetFirm().execute();
    	 new GetContact().execute();
    	 new GetSalesreps().execute();
    
	}
	
	 public void addItemsOnSpinner() {
	     	
	       
	       List<String> list = new ArrayList<String>();
	       list.add("Stage");
	       list.add("Starting");
	       list.add("OnGoing");
	       list.add("Completed");
	       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(dataAdapter);
	        }

	
		
		    
		   private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

			   @Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
					mYear = year;
			        mMonth = monthOfYear;
			        mDay = dayOfMonth;
					set1.setText(+mYear+"-"+mMonth+"-"+mDay);
				
		   };
		   };
		   
		   private DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {

			   @Override
				public void onDateSet(DatePicker date, int mwaka, int mwezi,int siku) {
					sYear = mwaka;
			        sMonth = mwezi;
			        sDay = siku;
					set2.setText(+sYear+"-"+sMonth+"-"+sDay);
					
			   
		   };
		   };
		   @Override
           protected Dialog onCreateDialog(int id) {
               switch (id) {
               case TO_END_DATE:
        // create a new DatePickerDialog with values you want to show 
                   return new DatePickerDialog(this,mDateSetListener, mYear, mMonth, mDay);
               case FROM_START_DATE:
               	return new DatePickerDialog(this,nDateSetListener, sYear, sMonth, sDay);
	
               }
			return null;	
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
	    			firmid = new String[JA.length()];       
	    			
	    			for(int i=0;i<JA.length();i++)
	    			{
	    		    JSONObject e =  JA.getJSONObject(i);
	    			firmname[i] = e.getString("firm_name");
	    			firmid[i] = e.getString("firm_id");  
	    			
	    			}
	    			

	    			for(int i=0;i<firmname.length;i++){
	    			{
	    			list1.add(firmname[i]);
	    			list11.add(firmid[i]);
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
	    			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text, list1);
	    			dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    			spinner1.setAdapter(dataAdapter1);
	    			               



	    			spinner1.setOnItemSelectedListener(new OnItemSelectedListener()
	    			{
	    			@Override
	    			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
	    			{
	    				
	    		
	    				 fid = list11.get(position);   
					       Log.i("id", fid); 
	    			}

	    			@Override
	    			public void onNothingSelected(AdapterView<?> arg0)
	    			{
	    			list1.add("Select Firm ");
	    			}
	    			                               
	    			});            
	    			}
	    			
	    			 private class GetContact extends AsyncTask<Void, Void, Void> {
	    		    		String URL = getResources().getString(R.string.get_contacts_url);
	    		    		
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
	    		     			
	    		    			
	    		    			contactname = new String[JA.length()];       
	    		    			contactid = new String[JA.length()];   
	    		    			
	    		    			for(int i=0;i<JA.length();i++)
	    		    			{
	    		    		    JSONObject e =  JA.getJSONObject(i);
	    		    			contactname[i] = e.getString("contact_name");
	    		    			contactid[i] = e.getString("contact_id");
	    		    				  
	    		    			
	    		    			}
	    		    			

	    		    			for(int i=0;i<contactname.length;i++){
	    		    			{
	    		    			list2.add(contactname[i]);
	    		    			list22.add(contactid[i]);
	    		    			
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
	    		             	
	    		             	spinner_fn1();
	    		             	mPullToRefreshLayout.setRefreshComplete();
	    		             }
	    		    	}

	    		    			private void spinner_fn1() {
	    		    			// TODO Auto-generated method stub
	    		    			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text, list2);
	    		    			dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    		    			spinner2.setAdapter(dataAdapter1);
	    		    			               



	    		    			spinner2.setOnItemSelectedListener(new OnItemSelectedListener()
	    		    			{
	    		    			@Override
	    		    			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
	    		    			{
	    		    				
	    		    		
	    		    				 cid = list22.get(position);   
	    						       Log.i("id", cid); 
	    		    			                                                                               
	    		    			}

	    		    			@Override
	    		    			public void onNothingSelected(AdapterView<?> arg0)
	    		    			{
	    		    			list1.add("Select Contact Person ");
	    		    			}
	    		    			                               
	    		    			});            
	    		    			}	
	    		    			
	    		    			private class GetSalesreps extends AsyncTask<Void, Void, Void> {
	    	    		    		String URL = getResources().getString(R.string.get_salesreps_url);
	    	    		    		
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
	    	    		     			
	    	    		    			
	    	    		    			salespersonname = new String[JA.length()];  
	    	    		    			salespersonid = new String[JA.length()];  
	    	    		    			
	    	    		    			
	    	    		    			for(int i=0;i<JA.length();i++)
	    	    		    			{
	    	    		    		    JSONObject e =  JA.getJSONObject(i);
	    	    		    		    salespersonname[i] = e.getString("name");
	    	    		    		    salespersonid[i] = e.getString("id");
	    	    		    				  
	    	    		    			
	    	    		    			}
	    	    		    			

	    	    		    			for(int i=0;i<salespersonname.length;i++){
	    	    		    			{
	    	    		    			list3.add(salespersonname[i]);
	    	    		    			list33.add(salespersonid[i]);
	    	    		    			
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
	    	    		             	
	    	    		             	spinner_fn2();
	    	    		             	mPullToRefreshLayout.setRefreshComplete();
	    	    		             }
	    	    		    	}

	    	    		    			private void spinner_fn2() {
	    	    		    			// TODO Auto-generated method stub
	    	    		    			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text, list3);
	    	    		    			dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	    		    			spinner3.setAdapter(dataAdapter1);
	    	    		    			               



	    	    		    			spinner3.setOnItemSelectedListener(new OnItemSelectedListener()
	    	    		    			{
	    	    		    			@Override
	    	    		    			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
	    	    		    			{
	    	    		    				
	    	    		    				sid = list33.get(position);   
	 	    						       Log.i("id", sid);                                                                              
	    	    		    			}

	    	    		    			@Override
	    	    		    			public void onNothingSelected(AdapterView<?> arg0)
	    	    		    			{
	    	    		    			list3.add("Sales person ");
	    	    		    			}
	    	    		    			                               
	    	    		    			});            
	    	    		    			}	
	    	    		    			



		  
		   public class CreateNewProject extends AsyncTask<String, Integer, Void> {

			   String url_create_project =getResources().getString(R.string.common_url);
				// Before starting background thread Show Progress Dialog
				 
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					pDialog = new ProgressDialog(CrownNewProject.this);
					pDialog.setMessage("Creating Project....");
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(false);
					pDialog.show();
				}

				
				// Creating user
				 
				protected Void doInBackground(String... args) {
					try
					{
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost(url_create_project);
						
						//adding our data
						ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
						namevaluepairs.add (new BasicNameValuePair("t","newProject"));
						namevaluepairs.add (new BasicNameValuePair("name",args[0]));
						namevaluepairs.add (new BasicNameValuePair("location",args[1]));
						namevaluepairs.add (new BasicNameValuePair("stage",args[2]));
		       			namevaluepairs.add (new BasicNameValuePair("startDate",args[3]));
		       			namevaluepairs.add (new BasicNameValuePair("endDate",args[4]));
		       			namevaluepairs.add (new BasicNameValuePair("firm_id",fid));
						namevaluepairs.add (new BasicNameValuePair("architect",args[5]));
						namevaluepairs.add (new BasicNameValuePair("contact",cid));
		       			namevaluepairs.add (new BasicNameValuePair("qs",args[6]));
		       			namevaluepairs.add (new BasicNameValuePair("pm",args[7]));
						namevaluepairs.add (new BasicNameValuePair("painter",args[8]));
						namevaluepairs.add (new BasicNameValuePair("developer",args[9]));
		       			namevaluepairs.add (new BasicNameValuePair("salesman",sid));
		       			namevaluepairs.add (new BasicNameValuePair("uid",args[10]));
		       			namevaluepairs.add (new BasicNameValuePair("volume",args[11]));
		       			namevaluepairs.add (new BasicNameValuePair("value",args[12]));
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
					if(CreateNewProject.this.isCancelled())
					{
						Toast.makeText(CrownNewProject.this, "Internet Connection Lost!", Toast.LENGTH_LONG).show();
						
					}
				}
				
				@Override
				protected void onPostExecute(Void result) {
					pDialog.dismiss();
					Toast.makeText(CrownNewProject.this, "Project Successfully added!", Toast.LENGTH_LONG).show();
					finish();
				}
		}

		@Override
		public void onRefreshStarted(View view) {
			// TODO Auto-generated method stub
			new GetFirm().execute();
			new GetContact().execute();
			
		}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.crown_newproject_menus, menu);
		    return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			if (item.getItemId()==R.id.refresh){
				
				new GetContact().execute();
				new GetFirm().execute();
			}
				  
			if (item.getItemId()==R.id.item3){
				 Intent c = new Intent(CrownNewProject.this, CrownCreateContact.class);
				 startActivity(c);
			}
				  
			if (item.getItemId()==R.id.item2){
				 Intent d = new Intent(CrownNewProject.this, CrownCreateFirm.class);
				 startActivity(d);
			}
				  
			
			return true;
		}


}

	 
	
	   

	


