package model;

public class Login {
	private String user;
	private String pass;
	private boolean acceder;
	
	public Login() {
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
    public boolean acceder() {
        return "admin".equals(user) && "123".equals(pass);
    }

}
