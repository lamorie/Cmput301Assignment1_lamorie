package ca.ualberta.cs.todolistblamorie;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import ca.ualberta.cs.todolistblamorie.R;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {
	/*The MainActivity Class organises the ListView of the unArchived items, saving them in the proper
	 * SharedPreferences file.
	 * */
	
	private CheckListAdapter CLA = null;
	/*This class was heavily influenced by the code found here
	http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html*/
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadList();
	}
	
	protected void onResume(){
		super.onResume();
		setContentView(R.layout.activity_main);
		loadList();
	}
	
	public void save(){
		/*This method was heavily influenced by the code found here
		 * http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
		 * written by users Setu and MuhammadAamirALi
		 * */
		ArrayList<ListItem> dataList = CLA.getDataList();
		SharedPreferences preferences = getSharedPreferences("ListItems", 0);
		Editor editor = preferences.edit();
		Gson gson = new Gson();
		for(ListItem item : dataList ){
			String json = gson.toJson(item);
			editor.putString(item.getTitle(), json);
		}
		editor.commit();
	}
	
	private void loadList(){
		/*This method was heavily influenced by the code found here
		 * http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
		 * written by users Setu and MuhammadAamirALi
		 * */
		final ArrayList<ListItem> loadedList = new ArrayList<ListItem>();
		SharedPreferences saved = getSharedPreferences("ListItems", Context.MODE_PRIVATE);
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
		CLA = new CheckListAdapter(this,R.layout.listitem, loadedList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(CLA);
		/*This onItemLongClickListener was heavily influenced by the code found here
		 * http://stackoverflow.com/questions/12244297/how-to-add-multiple-buttons-on-a-single-alertdialog
		 * which was submitted by user Carnal
		 * */
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    builder.setTitle("Choose an Action");
			    builder.setItems(new CharSequence[]
			            {"Delete Selected", "Archive Selected", "Archive All", "Email Selected", "Email All"},
			            new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int which) {
			                	ArrayList<ListItem> list = CLA.getDataList();
			                	final int size = list.size();
			                    switch (which) {
			                        case 0: 
			                		    for(int i=0;i < size; i++){
			                		    	ListItem item = list.get(i);
			                		    	if(item.isSelected()){
			                		     		CLA.delItem(item);	
			                		    	}
			                		    }
			                		    loadList();
			                            Toast.makeText(MainActivity.this, "Selected items Deleted", 0).show();
			                            break;
			                        case 1:
			                        	for(int i=0;i < size; i++){
			                		    	ListItem item = list.get(i);
			                		    	if(item.isSelected()){
			                		     		CLA.archive(item);
			                		    	}
			                        	}
			                        	loadList();
			                            Toast.makeText(MainActivity.this, "Selected items Archived", 0).show();
			                            break;
			                        case 2:
			                        	for(int i=0;i < size; i++){
			                		    	ListItem item = list.get(i);
			                		     	CLA.archive(item);
			                        	}
			                        	loadList();
			                        	Toast.makeText(MainActivity.this, "All items Archived", 0).show();
			                            break;
			                        case 3:
			                        	emailSelected(size, list);
			                        	break;
			                        case 4:
			                        	emailAll(size, list);
			                        	break;
			                    }
			                }
			            });
			    builder.create().show();
				return true;
			}
			
		});
	}
	
	
	public void loadSummary(View v){		
		Summary summ = new Summary(getApplicationContext());
		
		int check_toDos = summ.getCheck_toDos();
		int uncheck_toDos = summ.getArch_unchecked();
		int arch_toDos = summ.getArch_toDos();
		int arch_checked = summ.getArch_checked();
		int arch_unchecked = summ.getArch_unchecked();
		
		new AlertDialog.Builder(this)
		.setTitle("Summary")
		.setMessage("Checked ToDo Items: " + String.valueOf(check_toDos) + "\n"
				+ "Unchecked ToDo Items: " + String.valueOf(uncheck_toDos) + "\n"
				+ "Archived ToDo Items: " + String.valueOf(arch_toDos) + "\n"
				+ "Checked Archived ToDo Items: " + String.valueOf(arch_checked) + "\n"
				+ "Unchecked Archived ToDo Items: " + String.valueOf(arch_unchecked))
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		}})
		.show();
	}
	public void addItem(View v){
		final EditText newItem = new EditText(this);

		new AlertDialog.Builder(this)
		.setTitle("New ToDo Item")
		.setView(newItem)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			String title = newItem.getText().toString();
			ListItem newItem = new ListItem(title, false);
			CLA.addNewItem(newItem);
			save();
			loadList();
		}})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int whichButton) {
		}
		}
		).show();
	}
	
	private void emailSelected(int size, ArrayList<ListItem> list ){
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
    		Toast.makeText(MainActivity.this, "No email clinets on device.", Toast.LENGTH_SHORT).show();
    	}
	}
	
	private void emailAll(int size, ArrayList<ListItem> list ){
		Intent i = new Intent(Intent.ACTION_SEND);
    	i.setType("message/rfc822");
    	i.putExtra(Intent.EXTRA_SUBJECT, "Items From ToDo List");
    	String body = "";
    	for(int j=0;j < size; j++){
	    	ListItem item = list.get(j);
	    	body = body + "\n"+
	    			item.getTitle();
	    }
    	i.putExtra(Intent.EXTRA_TEXT, body);
    	try{
    		startActivity(Intent.createChooser(i,  "Send mail..."));
    	} catch(android.content.ActivityNotFoundException ex){
    		Toast.makeText(MainActivity.this, "No email clinets on device.", Toast.LENGTH_SHORT).show();
    	}
	}
	
	public void archView(View v){
		Intent intent = new Intent(this, ArchiveActivity.class);
		startActivity(intent);
	}
}
	
	
