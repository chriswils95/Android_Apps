package files.result;


import files.success.Success;
/**
 * The load result class sets the given load information from the
 * loadservices and set them to the required load results variables
 * It also extends the success super class to include the success message
 */
public class LoadResult extends Success {

    /**
     * message of num of users, persons and events added to DAO
     */

    /**
     * The load result constructor sets the given variables to the
     * message and success variables
     * @param message message if users info are loaded
     * @param success success message if load was successful
     */
    public LoadResult(String message,
                      boolean success){
        super.setMessage(message);
        super.setSuccess(success);
    }
}
