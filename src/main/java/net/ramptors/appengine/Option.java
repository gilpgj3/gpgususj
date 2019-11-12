package net.ramptors.appengine;

public class Option {
	private final String value;
	private final boolean selected;
	private final String text;
	public Option(String value, boolean selected, String text) {
		this.value = value;
		this.selected = selected;
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public boolean isSelected() {
		return selected;
	}
	public String getText() {
		return text;
	}
}