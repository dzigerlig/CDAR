package cdar.bll.model.wiki;


import java.io.IOException;
import java.io.Serializable;

import org.wikipedia.Wiki;

public class WikiEntryOld implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String text;
	private String textHtml;
	
	public WikiEntryOld(String name) {
		setName(name);
		initializeMembers();
	}

	private void initializeMembers() {
		Wiki c = new Wiki();
		
		try {
			setText(c.getPageText(getName()));
			//setTextHtml(WikiModel.toHtml(getText()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextHtml() {
		return textHtml;
	}

	public void setTextHtml(String textHtml) {
		this.textHtml = textHtml;
	}

}
