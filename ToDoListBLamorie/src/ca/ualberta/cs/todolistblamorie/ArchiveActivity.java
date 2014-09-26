package ca.ualberta.cs.todolistblamorie;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ArchiveActivity extends Activity {
	/*The ArchiveActivity Class organises the ListView of the archived items, saving them in the proper
	 * SharedPreferences file.
	 * */
	private CheckListAdapter ACLA = null;
	/*This class was heavily influenced by the code found here
	http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html*/
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);

		loadArch();
	}
	
	public void save(){
		/*This method was heavily influenced by the code found here
		 * http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
		 * written by users Setu and MuhammadAamirALi
		 * */
		ArrayList<ListItem> archList = ACLA.getDataList();
		SharedPreferences preferences = getSharedPreferences("ArchItems", 0);
		Editor editor = preferences.edit();
		Gson gson = new Gson();
		for(ListItem item : archList ){
			String json = gson.toJson(item);
			editor.putString(item.getTitle(), json);
		}
		editor.commit();
	}
	
	private void loadArch(){
		/*This method was heavily influenced by the code found here
		 * http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
		 * written by users Setu and MuhammadAamirALi
		 * */
		final ArrayList<ListItem> loadedList = new ArrayList<ListItem>();
		SharedPreferences saved = getSharedPreferences("ArchItems", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		Map<String, ?> values = saved.getAll();
		Set<String> keys = values.keySet();
		
		for (String key : keys){
			String json = saved.getString(key, "");
			ListItem item = gson.fromJson(json, ListItem.class);
			if (item != null){
				loadedList.add(item);
			}
		}
		ACLA = new CheckListAdapter(this,R.layout.listitem, loadedList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(ACLA);
		/*This onItemLongClickListener was heavily influenced by the code found here
		 * http://stackoverflow.com/questions/12244297/how-to-add-multiple-buttons-on-a-single-alertdialog
		 * which was submitted by user Carnal
		 * */
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
			    builder.setTitle("Choose a Action");
			    builder.setItems(new CharSequence[]
			            {"Delete Selected", "UnArchive Selected", "UnArchive All", "Email Selected", "Email All"},
			            new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int which) {
			                	ArrayList<ListItem> list = ACLA.getDataList();
			                	int size = list.size();
			                    switch (which) {
			                        case 0:
			                		    for(int i=0;i < size; i++){
			                		    	ListItem item = list.get(i);
			                		    	if(item.isSelected()){
			                		     		ACLA.delArch(item);	
			                		    	}
			                		    }
			                		    loadArch();
			                            Toast.makeText(ArchiveActivity.this, "Selected items Deleted", 0).show();
			                            break;
			                        case 1:
			                        	for(int i=0;i < size; i++){
			                		    	ListItem item = list.get(i);
			                		    	if(item.isSelected()){
			                		    		ACLA.unarchive(item);
			                		     		
			                		    	}
			                        	}
			                        	loadArch();
			                            Toast.makeText(ArchiveActivity.this, "Selected items Unarchived", 0).show();
			                            break;
			                        case 2:
			                        	for(int i=0;i < size; i++){
			                		    	ListItem item = list.get(i);
			                		    	ACLA.unarchive(item);	
		                		     		
			                        	}
			                        	loadArch();
			                        	Toast.makeText(ArchiveActivity.this, "All items Unarchived", 0).show();
			                            break;
			                        case 3:
			                        	Intent i = new Intent(Intent.ACTION_SEND);
			                        	i.setType("message/rfc822");
			                        	i.putExtra(Intent.EXTRA_SUBJECT, "Items From ToDo List");
			                        	String body = "";
			                        	for(int j=0;j < size; j++){
			                		    	ListItem item = list.get(j);
			                		    	if(item.isSelected()){
			                		    			body = body + "\n"+
			                		    				item.getTitle();
			                		    	}
			                		    }
			                        	i.putExtra(Intent.EXTRA_TEXT, body);
			                        	try{
			                        		startActivity(Intent.createChooser(i,  "Send mail..."));
			                        	} catch(android.content.ActivityNotFoundException ex){
			                        		Toast.makeText(ArchiveActivity.this, "No email clinets on device.", Toast.LENGTH_SHORT).show();
			                        	}
			                            
			                        	break;
			                        case 4:
			                        	Intent a = new Intent(Intent.ACTION_SEND);
			                        	a.setType("message/rfc822");
			                        	a.putExtra(Intent.EXTRA_SUBJECT, "All Items From ToDo List");
			                        	String totBody = "";
			                        	for(int j=0;j < size; j++){
			                		    	ListItem item = list.get(j);
			                		    	totBody = totBody + "\n"+
		                		    		item.getTitle();
			                		    }
			                        	a.putExtra(Intent.EXTRA_TEXT, totBody);
			                        	try{
			                        		startActivity(Intent.createChooser(a,  "Send mail..."));
			                        	} catch(android.content.ActivityNotFoundException ex){
			                        		Toast.makeText(ArchiveActivity.this, "No email clinets on device.", Toast.LENGTH_SHORT).show();
			                        	}

			                        	break;
			                    }
			                }
			            });
			    builder.create().show();
				return true;
			}
			
		});
	}
	

}
