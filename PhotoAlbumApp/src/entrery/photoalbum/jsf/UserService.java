package entrery.photoalbum.jsf;

import javax.faces.context.FacesContext;

public class UserService {

	public static String getLoggedInUser() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		//return "sasho";
	}
}
