package files.success;

/**
 * The success class is a super class that will be used by my other classes
 * to set the success message of the results
 */
public class Success {
    /**
     * This is the success message that will be set in the requested classes
     */

    boolean success;

    /**
     * This just store a message
     */
    String message;

    /**
     * This function returns the message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * This function sets the message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     *This function sets the success boolean
     * @param success
     */
   public void setSuccess(boolean success){
       this.success = success;
   }

    /**
     *This returns the success boolean
     * @return
     */
    public boolean getSuccess(){
        return  success;
    }
}
