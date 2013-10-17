package net.ogorkis.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AppUser {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
