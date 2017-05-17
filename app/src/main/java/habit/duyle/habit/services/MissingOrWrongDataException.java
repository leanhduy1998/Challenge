package habit.duyle.habit.services;

/**
 * Created by Duy Le on 1/21/2017.
 */

public class MissingOrWrongDataException extends Exception {
    public MissingOrWrongDataException(String msg){
        super(msg);
    }
}
