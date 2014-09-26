package ca.ualberta.cs.todolistblamorie;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

public class Summary {
	/*The Summary class contains the counts for the Summary popup that is 
	 * created when the summary button is pushed
	 * */
	private int check_toDos;
	private int uncheck_toDos;
	private int arch_toDos;
	private int arch_checked;
	private int arch_unchecked;
	private Context context;
	
	public Summary(Context context){
		this.context = context;
	}

	public int getCheck_toDos() {
		final ArrayList<ListItem> loadedList = new ArrayList<ListItem>();
		SharedPreferences saved = context.getSharedPreferences("ListItems", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		Map<String, ?> values = saved.getAll();
		Set<String> keys = values.keySet();
		check_toDos = 0;
		for (String key : keys){
			String json = saved.getString(key, "");
			ListItem item = gson.fromJson(json, ListItem.class);
			if (item != null){
				loadedList.add(item);
				if(item.isSelected() == true){
					check_toDos = check_toDos + 1;
				}
			}
		}
		
		return check_toDos;
	}


	public int getUncheck_toDos() {
		final ArrayList<ListItem> loadedList = new ArrayList<ListItem>();
		SharedPreferences saved = context.getSharedPreferences("ListItems", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		Map<String, ?> values = saved.getAll();
		Set<String> keys = values.keySet();
		uncheck_toDos = 0;
		for (String key : keys){
			String json = saved.getString(key, "");
			ListItem item = gson.fromJson(json, ListItem.class);
			if (item != null){
				loadedList.add(item);
				if(item.isSelected() != true){
					uncheck_toDos = uncheck_toDos + 1;
				}
			}
		}
		
		return uncheck_toDos;
	}


	public int getArch_toDos() {
		final ArrayList<ListItem> loadedList = new ArrayList<ListItem>();
		SharedPreferences saved = context.getSharedPreferences("ArchItems", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		Map<String, ?> values = saved.getAll();
		Set<String> keys = values.keySet();
		arch_toDos = 0;
		for (String key : keys){
			String json = saved.getString(key, "");
			ListItem item = gson.fromJson(json, ListItem.class);
			if (item != null){
				loadedList.add(item);
				arch_toDos = arch_toDos + 1;
			}
		}
		
		return arch_toDos;
	}


	public int getArch_checked() {
		final ArrayList<ListItem> loadedList = new ArrayList<ListItem>();
		SharedPreferences saved = context.getSharedPreferences("ArchItems", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		Map<String, ?> values = saved.getAll();
		Set<String> keys = values.keySet();
		arch_checked = 0;
		for (String key : keys){
			String json = saved.getString(key, "");
			ListItem item = gson.fromJson(json, ListItem.class);
			if (item != null){
				loadedList.add(item);
				if(item.isSelected() == true){
					arch_checked = check_toDos + 1;
				}
			}
		}
		
		return arch_checked;
	}


	public int getArch_unchecked() {
		final ArrayList<ListItem> loadedList = new ArrayList<ListItem>();
		SharedPreferences saved = context.getSharedPreferences("ArchItems", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		Map<String, ?> values = saved.getAll();
		Set<String> keys = values.keySet();
		arch_unchecked = 0;
		for (String key : keys){
			String json = saved.getString(key, "");
			ListItem item = gson.fromJson(json, ListItem.class);
			if (item != null){
				loadedList.add(item);
				if(item.isSelected() != true){
					arch_unchecked = arch_unchecked + 1;
				}
			}
		}
		
		return arch_unchecked;
	}
}
