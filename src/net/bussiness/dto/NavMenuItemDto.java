package net.bussiness.dto;

public class NavMenuItemDto {

	private String title;
	private int icon;
	private int count = 0;
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;

	public NavMenuItemDto() {
	}

	public NavMenuItemDto(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavMenuItemDto(String title, int icon, boolean isCounterVisible,
			int count) {
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIcon() {
		return this.icon;
	}

	public int getCount() {
		return this.count;
	}

	public boolean getCounterVisibility() {
		return this.isCounterVisible;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setCounterVisibility(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}

	@Override
	public String toString() {
		return "NavMenuItemDto [title=" + title + ", icon=" + icon + ", count="
				+ count + ", isCounterVisible=" + isCounterVisible + "]";
	}
}
