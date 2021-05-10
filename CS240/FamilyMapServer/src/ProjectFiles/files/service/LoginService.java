package files.service;

import files.dao.AuthTokenDAO;
import files.dao.DataAccessException;
import files.dao.Database;
import files.dao.UserDAO;
import files.model.AuthToken;
import files.model.User;
import files.request.LoginRequest;
import files.result.LoginResult;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The login service class performs the necessary operations of the received information from my
 * request class and clear all data in my database
 */
public class LoginService {

    Database db;


    public LoginService(){
        db = new Database();
    }

    /**
     * The performedLoginServices check to see if the given user information from the request body corresponds
     * to a user in the user table, if its does it returns the given user auth tokens, name and password
     * @param loginRequest objects of login request class that contains info of the current user
     * @return
     * @throws DataAccessException
     */
    public LoginResult performLoginServices(LoginRequest loginRequest) throws DataAccessException, SQLException {
        LoginResult loginResult = null;

        try {
            db.startTransaction();
            UserDAO userDAO = new UserDAO(db);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);
            User user = userDAO.find(loginRequest.getUserName());
            if (user != null) {
                if(user.getPassword().equals(loginRequest.getPassword())) {
                    AuthToken authToken = authTokenDAO.find(loginRequest.getUserName());
                    loginResult = new LoginResult(true, authToken.getUniqueKeys(), loginRequest.getUserName(),
                            user.getPersonID());
                }
            }
            db.closeConnection(true);

        } catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }
        return loginResult;
     }
    }
