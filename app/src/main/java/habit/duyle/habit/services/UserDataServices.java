package habit.duyle.habit.services;

/**
 * Created by leanh on 4/3/2017.
 */

public class UserDataServices {
    private static UserDataServices userDataServices = new UserDataServices();
    private boolean userNotInDatabase=true;
    private String email;
    private String rememberMe;
    private String automateLogin="false";

    public boolean isUserNotInDatabase() {
        return userNotInDatabase;
    }

    public static UserDataServices getInstance(){
        return userDataServices;
    }

    public void setUserNotInDatabase(boolean userNotInDatabase) {
        this.userNotInDatabase = userNotInDatabase;
    }
    public String getEmail() {
        return email;
    }

    public String getRememberMe() {
        return rememberMe;
    }

    public String getAutomateLogin() {
        return automateLogin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setAutomateLogin(String automateLogin) {
        this.automateLogin = automateLogin;
    }
}
