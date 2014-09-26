package ca.ualberta.cs.todolistblamorie;

public class ListItem {
	/*The ListItem class creates an object with both a title and a boolean
	 * indicating the state fo it's checkbox
	 * */
	private String title;
	private boolean selected;
	
	public ListItem(String title, boolean selected){
		super();
		this.title = title;
		this.selected = selected;
	}
	
	public String getTitle(){
		return title;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}
