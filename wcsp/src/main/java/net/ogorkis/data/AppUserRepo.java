package net.ogorkis.data;

import javax.ejb.Stateless;

import net.ogorkis.model.AppUser;

@Stateless
public class AppUserRepo {

	public AppUser findAppUser(String login) {
		AppUser user = new AppUser();
		user.setName(login);
		return user;
	}

}
