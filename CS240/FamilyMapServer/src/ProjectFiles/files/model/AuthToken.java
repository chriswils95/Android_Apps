//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


package files.model;
/**
 * The Authtoken model class specifies the functionality that makes a user
 * like all necessary information needed for a user to be qualified as a authToken
 * It uses getters and setters and even parameterized constructors to set these variable
 */
public class AuthToken {
    /**
     * The associated username is just the username of the user,
     */
    private String associatedUsername;
    /**
     * whilst the unique keys is the UUID generated auth token keys of the user
     */
    private String uniqueKeys;

    /**
     * The authToken constructors sets a user with its given information. It also sets the success message by
     * extending the success super class
     * @param associatedUsername username to be set with my private username variable
     * @param uniqueKeys unique keys to be set to my unique leys variable
     */
    public AuthToken(String associatedUsername, String uniqueKeys) {
        this.associatedUsername = associatedUsername;
        this.uniqueKeys = uniqueKeys;
    }

    /**
     * This function returns the associated username
     * @return
     */
    public String getAssociatedUsername() {
        return this.associatedUsername;
    }

    /**
     * This function sets the associated username
     * @param associatedUsername
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * This function returns the unique auth token keys
     * @return unique keys
     */
    public String getUniqueKeys() {
        return this.uniqueKeys;
    }

    /**
     * This function sets the unique auth token keys
     * @param uniqueKeys
     */
    public void setUniqueKeys(String uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }

}
