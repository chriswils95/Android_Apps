package edu.byu.cs.tweeter.client.view.Register;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Base64;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.client.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.RegisterTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;

import static android.app.Activity.RESULT_OK;
import static edu.byu.cs.tweeter.client.util.ByteArrayUtils.bytesFromInputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements RegisterTask.Observer, Serializable, RegisterPresenter.View, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RegisterPresenter presenter;
    private Toast registerInToast;
    private static final String LOG_TAG = "RegisterActivity";
    private static View registerView = null;
    static final int CAMERA_PIC_REQUEST = 1337;
    EditText editTextfirstName = null;
    EditText editTextlastName = null;
    EditText editTextuserName = null;
    EditText editTextpassword = null;



    private byte [] imageBytes = null;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        presenter = new RegisterPresenter(this);

        Button registerButton = view.findViewById(R.id.registerButton);
        Button photoButton = view.findViewById(R.id.take_photo);
        registerButton.setOnClickListener(this);
        photoButton.setOnClickListener(this);

        registerView = view;
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                registerInToast = Toast.makeText(getContext(), "Registering", Toast.LENGTH_LONG);
                registerInToast.show();
                editTextfirstName = registerView.findViewById(R.id.register_firstname);
                editTextlastName = registerView.findViewById(R.id.register_lastname);
                editTextuserName = registerView.findViewById(R.id.register_username);
                editTextpassword = registerView.findViewById(R.id.register_password);
                StaticHelperClass.setIsRegister(true);


                if(check_if_all_fields_are_clicked()) {

                    String imageString = Base64.getEncoder().encodeToString(imageBytes);

                    // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                    RegisterRequest registerRequest = new RegisterRequest(editTextfirstName.getText().toString(), editTextlastName.getText().toString(),
                            "@" + editTextuserName.getText().toString(), editTextpassword.getText().toString(),
                            imageString);
                    RegisterTask registerTask = new RegisterTask(presenter, this);
                    registerTask.execute(registerRequest);
                }else {
                    Toast.makeText(getActivity(), "Failed to include all fields", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.take_photo:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                break;
        }
    }

    public static InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }


    public boolean check_if_all_fields_are_clicked(){
        String first_name = editTextfirstName.getText().toString();
        String last_name = editTextlastName.getText().toString();
        String user_name = editTextuserName.getText().toString();
        String password = editTextpassword.getText().toString();

        if(!first_name.isEmpty() && !last_name.isEmpty() && !user_name.isEmpty() && !password.isEmpty() && imageBytes != null){
            return true;
        }
        return false;
    }


    public static byte [] bytesFromBitmap(Bitmap imageBitmap) throws IOException {
        InputStream imagecontent = null;

        try{
            imagecontent = bitmap2InputStream(imageBitmap);
            return bytesFromInputStream(imagecontent);

        }catch (Exception e){
            throw new IOException("Error");
        }
    }

    @Override
    public void registerSuccessful(RegisterResponse registerResponse) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, registerResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, registerResponse.getAuthToken());
        intent.putExtra(MainActivity.IS_FOLLOW, false);
        registerInToast.cancel();
        StaticHelperClass.setIsUser(true);
        startActivity(intent);
    }

    @Override
    public void registerUnsuccessful(RegisterResponse registerResponse) {
        Toast.makeText(getActivity(), "Failed to login. " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getActivity(), "Failed to login because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            try {
                imageBytes = bytesFromBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}