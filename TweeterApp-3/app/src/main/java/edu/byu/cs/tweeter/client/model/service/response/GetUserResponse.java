package edu.byu.cs.tweeter.client.model.service.response;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;


    public class GetUserResponse extends Response {
        private User user;
        private AuthToken authToken;
        private boolean following;


        /**
         * Creates a response indicating that the corresponding request was unsuccessful.
         *
         * @param message a message describing why the request was unsuccessful.
         */
        public GetUserResponse(String message) {
            super(false, message);
        }

        /**
         * Creates a response indicating that the corresponding request was successful.
         *
         * @param user the now logged in user.
         * @param authToken the auth token representing this user's session with the server.
         */
        public GetUserResponse(User user, AuthToken authToken, boolean isFollow) {
            super(true, null);
            this.user = user;
            this.authToken = authToken;
            this.following = isFollow;
        }


        public void setUser(User user) {
            this.user = user;
        }



        public void setAuthToken(AuthToken authToken) {
            this.authToken = authToken;
        }

        public  GetUserResponse(){
            super(true, null);

        }

        public boolean isFollowing() {
            return following;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }

        /**
         * Returns the logged in user.
         *
         * @return the user.
         */
        public User getUser() {
            return user;
        }

        /**
         * Returns the auth token.
         *
         * @return the auth token.
         */
        public AuthToken getAuthToken() {
            return authToken;
        }
}
