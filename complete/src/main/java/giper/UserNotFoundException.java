package giper;

/**
 * Created by prochiy on 8/27/15.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super("user not found in database");
    }
}
