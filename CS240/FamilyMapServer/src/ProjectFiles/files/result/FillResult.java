package files.result;



import files.success.Success;
/**
 * The fill result class sets the given fill information from the
 * fillservices and set them to the required fill results variables
 * It also extends the success super class to include the success message
 */
public class FillResult extends Success {

    /**
     * message of num of users, persons and events added to DAO
     */

    /**
     * The fill result cinstructor sets the given variables to the
     * message and success variables
     * @param message message if users info are filled
     * @param success success message if filled was successful
     */
    public FillResult(String message, boolean success){
        super.setMessage(message);
        super.setSuccess(success);
    }


}
