package com.myitprovider.crownm.fragments;import java.io.ObjectOutputStream.PutField;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import org.json.JSONArray;import org.json.JSONObject;import com.example.crownm.R;import com.myitprovider.crownm.database.JSONFunctions;import com.myitprovider.crownm.ui.CrownVisitProject;import com.myitprovider.crownm.util.Pref;import com.myitprovider.crownm.fragments.ListViewAdapter;import android.app.Fragment;import android.app.ListFragment;import android.app.ProgressDialog;import android.content.Context;import android.os.AsyncTask;import android.os.Bundle;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.ListAdapter;import android.widget.ListView;import android.widget.SimpleAdapter;import android.widget.TextView;public class ListFragmentClass extends Fragment {		public ProgressDialog pDialog;		ArrayList<HashMap<String, String>> productsList;	ArrayList<HashMap<String,String>> alist=new ArrayList<HashMap<String,String>>();  	public SimpleAdapter simpleAdapter;	public ArrayAdapter<String> dataAdapter1;		ListViewAdapter lv;	 	ListView projects;	String[] project, firmname, person;	String name;		final List<String> list1 = new ArrayList<String>();    View view;    	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		// TODO Auto-generated method stub		  view = inflater.inflate(R.layout.list_fragment_layout, container, false);		 		  		        projects = (ListView) view.findViewById(R.id.list1);    	    	      	        	        new GetProjects().execute();	        		return view;					}			private class GetProjects extends AsyncTask<Void, Void, Void> {				String URL = "http://197.254.42.106:85/crown/msfa_ws/bootstrap.php?t=projects&uid="+Pref.user;				@Override		protected void onPreExecute() {			 			super.onPreExecute();		}				@Override						protected Void doInBackground(Void... arg0) {									try			{			JSONObject json= JSONFunctions.getJSONfromURL(URL);			Log.i("JSON OBJECT", json.toString()); 			JSONArray JA=new JSONArray(); 			JA = json.getJSONArray("PAYLOAD"); 									project = new String[JA.length()];       			firmname = new String[JA.length()];			person = new String[JA.length()];									for(int i=0;i<JA.length();i++)			{						JSONObject e =  JA.getJSONObject(i);									project[i] = e.getString("name");			firmname[i] =e.getString("contractor");			person[i]=e.getString("contact");						String output = "Projects:" + project[i] + "\nContractor:" + firmname[i] + "\nContact Person:" + person[i];			                          list1.add(output);             Log.i("alist", output.toString());                        dataAdapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.list_layout, R.id.textView1, list1);       	int[] flags = {R.drawable.ic_launcher_1};                        // Each row in the list stores country name, currency and flag        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();  {                      for(int a=0;a<10;a++){        	HashMap<String, String> hm = new HashMap<String,String>();            hm.put("txt", "Project : " + project[i]);            hm.put("cur","Contractor : " + firmname[i]);            hm.put("con","Contact person : " + person[i]);            hm.put("flag", Integer.toString(flags[i]) );                        aList.add(hm);               }                // Keys used in Hashmap        String[] from = { "flag","txt","cur","con" };                // Ids of views in listview_layout        int[] to = { R.id.flag,R.id.txt,R.id.cur,R.id.con};                        // Instantiating an adapter to store each items        // R.layout.listview_layout defines the layout of each item     simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(), aList, R.layout.listview_layout, from, to);                // Getting a reference to listview of main.xml layout file                                          }			}					}			catch(Exception e)			{				Log.e("Listview", e.toString());			}					        					return null;			}				@Override		 protected void onPostExecute(Void result) {			//projects.setAdapter(new ListViewAdapter(getActivity().getApplicationContext()));				// Setting the adapter to the listView			//projects.clearChoices();	        projects.setAdapter(simpleAdapter);             	         }					}    }		