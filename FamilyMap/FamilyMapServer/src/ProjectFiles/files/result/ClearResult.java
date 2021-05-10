
package files.result;

import files.success.Success;

/**
 * The clear result class extends the sucess super class and sets the success message
 * It also sets the given message
 */

public class ClearResult extends Success {
    /**
     * message if cleared succeeds
     */

    /**
     * The clear constructor sets the necessary message and success varibales
     * @param message message if clear succeeded
     * @param success success message if clear succeeded
     */
    public ClearResult(String message, boolean success){
        super.setMessage(message);
        super.setSuccess(success);
    }
}
