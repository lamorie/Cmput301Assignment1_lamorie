package ca.ualberta.cs.todolistblamorie;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class CheckListAdapter extends ArrayAdapter<ListItem> {
		/*This is the custom ArrayAdapter used to perform specific operations 
		 * on the ArrayList as well as load the view of each item for the ListView
		 * */
		private ArrayList<ListItem> dataList;
		private Context context;
		/*This class was heavily influenced by the code found here
		http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html*/
		public CheckListAdapter(Context context, int ViewId, ArrayList<ListItem> list){
			super(context, ViewId, list);
			this.context = context;
			this.dataList = new ArrayList<ListItem>();
			this.dataList.addAll(list);
		}
		
		private class ViewHolder {
			TextView title;
			CheckBox selected;
		}
		public void addNewItem(ListItem newItem){
			SharedPreferences preferences = context.getSharedPreferences("ListItems", 0);
			Editor editor = preferences.edit();
			Gson gson = new Gson();
				String json = gson.toJson(newItem);
				editor.putString(newItem.getTitle(), json);
			editor.commit();
		}
		
		public void addArchItems(ListItem archItem){
			SharedPreferences preferences = context.getSharedPreferences("ArchItems", 0);
			Editor editor = preferences.edit();
			Gson gson = new Gson();
			String json = gson.toJson(archItem);
			editor.putString(archItem.getTitle(), json);
			editor.commit();
		}
		
		public void archive(ListItem item){
			addArchItems(item);
			delItem(item);
		}
		
		public void unarchive(ListItem item){
			addNewItem(item);
			delArch(item);
		}
		
		public void delItem(ListItem item){
			dataList.remove(item);
			SharedPreferences saved = context.getSharedPreferences("ListItems", Context.MODE_PRIVATE);
			saved.edit().remove(item.getTitle()).commit();
			
		}
		
		public void delArch(ListItem item){
			dataList.remove(item);
			SharedPreferences saved = context.getSharedPreferences("ArchItems", Context.MODE_PRIVATE);
			saved.edit().remove(item.getTitle()).commit();
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));;
			
			if (convertView == null){
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.listitem, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.textView1);
				holder.selected = (CheckBox) convertView.findViewById(R.id.checkBox1);
				convertView.setTag(holder);
				
				holder.selected.setOnClickListener( new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CheckBox cb = (CheckBox) v;
						ListItem item = (ListItem) cb.getTag();
						item.setSelected(cb.isChecked());
						SharedPreferences savedLI = context.getSharedPreferences("ListItems", Context.MODE_PRIVATE);
						if(savedLI.contains(item.getTitle())){
							addNewItem(item);
						}
						else{
							addArchItems(item);
						}
					}
				});
			}
			
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			ListItem item = dataList.get(position);
			holder.title.setText(item.getTitle());
			holder.selected.setText(item.getTitle());
			holder.selected.setChecked(item.isSelected());
			holder.selected.setTag(item);
			
			return convertView;
		}

		public ArrayList<ListItem> getDataList() {
			return dataList;
		}
	}

