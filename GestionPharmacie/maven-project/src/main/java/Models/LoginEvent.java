package Models;

public class LoginEvent {
    private int id;
    private int userId;
    private String dateLogin;
    private String dateLogout;

    public LoginEvent(int id, int userId, String dateLogin, String dateLogout) {
        this.id = id;
        this.userId = userId;
        this.dateLogin = dateLogin;
        this.dateLogout = dateLogout;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDateLogin() {
        return dateLogin;
    }

    public String getDateLogout() {
        return dateLogout;
    }
}
