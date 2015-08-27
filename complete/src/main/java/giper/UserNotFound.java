package giper;

/**
 * Created by prochiy on 8/27/15.
 */
public class UserNotFound extends Exception {
    public UserNotFound(){
        super("user not found in database");
    }
}
