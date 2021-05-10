package edu.byu.cs.tweeter.client.view.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.LoginTask;

public class LoginFragment extends Fragment implements LoginPresenter.View, LoginTask.Observer, View.OnClickListener {


    private LoginPresenter presenter;
    private Toast loginInToast;
    EditText editTextuserName = null;
    EditText editTextpassword = null;
    private static View loginView = null;
    private static final String LOG_TAG = "LoginActivity";



    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        presenter = new LoginPresenter(this);


        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        loginView = view;
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    public boolean check_if_all_fields_are_clicked(String username, String password){

        if(!username.isEmpty() && !password.isEmpty()){
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        loginInToast = Toast.makeText(getContext(), "Logging In", Toast.LENGTH_LONG);

        loginInToast.show();

        StaticHelperClass.setIsRegister(false);

        StaticHelperClass.setUserClicked(0);

        // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
        editTextuserName = loginView.findViewById(R.id.editTextTextPersonName);
        editTextpassword = loginView.findViewById(R.id.editTextTextPassword);
        String username = editTextuserName.getText().toString();
        String password = editTextpassword.getText().toString();
        System.out.println(username);
        System.out.println(password);


        if(check_if_all_fields_are_clicked(username, password)) {
            StaticHelperClass.setIsUser(true);
            LoginRequest loginRequest = new LoginRequest(username, password);
            LoginTask loginTask = new LoginTask(presenter, this);
            loginTask.execute(loginRequest);
        }else {
            Toast.makeText(getActivity(), "Failed to include all fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void loginSuccessful(LoginResponse loginResponse) {
        int click = StaticHelperClass.getUserClicked();
        if(click == 0){
            StaticHelperClass.setMainUser(loginResponse.getUser());
        }
        click ++;
        StaticHelperClass.setUserClicked(click);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, loginResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, loginResponse.getAuthToken());
        intent.putExtra(MainActivity.IS_FOLLOW, false);
        loginInToast.cancel();
        StaticHelperClass.setIsUser(true);
        startActivity(intent);
    }

    @Override
    public void loginUnsuccessful(LoginResponse loginResponse) {
        Toast.makeText(getActivity(), "Failed to login. " + loginResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getActivity(), "Failed to login because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }

}