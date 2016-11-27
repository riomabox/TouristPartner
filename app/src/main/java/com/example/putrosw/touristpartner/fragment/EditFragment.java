package com.example.putrosw.touristpartner.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.putrosw.touristpartner.R;
import com.eyro.mesosfer.ChangePasswordCallback;
import com.eyro.mesosfer.GetCallback;
import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferObject;
import com.eyro.mesosfer.MesosferUser;
import com.eyro.mesosfer.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Putro SW on 21-Nov-16.
 */
public class EditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextInputEditText textOldPassword, textNewPassword, textConfirmPassword,
            textFirstname, textLastname, textDateOfBirth, textNickname;
    private Switch switchIsMarried;

    private String oldPassword, newPassword, confirmPassword,
            firstname, lastname, dateOfBirthString, nickname, height, weight;
    private Date dateOfBirth;
    private boolean isMarried;

    private ProgressDialog loading;
    private AlertDialog dialog;
    private View myFragmentView;
    Button ClickUpdate, ClickChgPswrd;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // initialize input form view
        myFragmentView = inflater.inflate(R.layout.fragment_edit, container, false);
        textOldPassword = (TextInputEditText) myFragmentView.findViewById(R.id.text_password_old);
        textNewPassword = (TextInputEditText) myFragmentView.findViewById(R.id.text_password_new);
        textConfirmPassword = (TextInputEditText) myFragmentView.findViewById(R.id.text_password_confirm);

        textFirstname = (TextInputEditText) myFragmentView.findViewById(R.id.text_firstname);
        textLastname = (TextInputEditText) myFragmentView.findViewById(R.id.text_lastname);
        //textDateOfBirth = (TextInputEditText) myFragmentView.findViewById(R.id.text_date_of_birth);
        textNickname = (TextInputEditText) myFragmentView.findViewById(R.id.text_nickname);
        ClickUpdate = (Button)myFragmentView.findViewById(R.id.btnUpdate);
        ClickUpdate.setOnClickListener(operation);
        ClickChgPswrd = (Button)myFragmentView.findViewById(R.id.btnChgPswrd);
        ClickChgPswrd.setOnClickListener(operation);
        //textWeight = (TextInputEditText) findViewById(R.id.text_weight);
        //switchIsMarried = (Switch) findViewById(R.id.switch_is_married);

        loading = new ProgressDialog(getContext());
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // fetch user and show profile data
        this.fetchUser();
        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void fetchUser() {
        // showing a progress dialog loading
        loading.setMessage("Fetching user profile...");
        loading.show();

        final MesosferUser user = MesosferUser.getCurrentUser();
        if (user != null) {
            user.fetchAsync(new GetCallback<MesosferUser>() {
                @Override
                public void done(MesosferUser mesosferUser, MesosferException e) {
                    // hide progress dialog loading
                    loading.dismiss();

                    // check if there is an exception happen
                    if (e != null) {
                        // setup alert dialog builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setNegativeButton(android.R.string.ok, null);
                        builder.setTitle("Error Happen");
                        builder.setMessage(
                                String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                        e.getCode(), e.getMessage())
                        );
                        dialog = builder.show();
                        return;
                    }

                    Toast.makeText(getContext(), "Profile Fetched", Toast.LENGTH_SHORT).show();
                    updateView(user);
                }
            });
        }
    }

    private void updateView(MesosferUser user) {
        if (user != null) {
            textFirstname.setText(user.getFirstName());
            textLastname.setText(user.getLastName());

            MesosferObject data = user.getData();
            if (data != null) {
                //Date date = data.optDate("dateOfBirth");
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                //textDateOfBirth.setText(format.format(date));

                String nickname = data.optString("nickname");
                textNickname.setText(String.valueOf(nickname));

                //int weight = data.optInt("weight");
                //textWeight.setText(String.valueOf(weight));

                //switchIsMarried.setChecked(data.optBoolean("isMarried"));
            }
        }
    }


    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnUpdate:sembunyikanKeyBoard(v);
                    handleUpdateProfile();break;
                case R.id.btnChgPswrd:sembunyikanKeyBoard(v);
                    handleChangePassword();break;
            }
        }
    };

    private void sembunyikanKeyBoard(View v){
        InputMethodManager a = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    public void handleUpdateProfile() {
        // get all value from input
        firstname = textFirstname.getText().toString();
        lastname = textLastname.getText().toString();
        //dateOfBirthString = textDateOfBirth.getText().toString();
        nickname = textNickname.getText().toString();
        //height = textHeight.getText().toString();
        //weight = textWeight.getText().toString();
        //isMarried = switchIsMarried.isChecked();

        // validating input values
        if (!isInputProfileValid()) {
            // return if there is an invalid input
            return;
        }

        // execute update profile
        updateProfile();
    }

    private boolean isInputProfileValid() {
        // validating all input values if it is empty
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(getContext(), "First name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(getContext(), "Last name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        /*if (TextUtils.isEmpty(dateOfBirthString)) {
            Toast.makeText(getContext(), "Date of birth is empty", Toast.LENGTH_LONG).show();
            return false;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                dateOfBirth = format.parse(dateOfBirthString);
            } catch (ParseException e) {
                // show error message when user input invalid format of date
                Toast.makeText(getContext(), "Invalid format of date of birth, use `yyyy-mm-dd`", Toast.LENGTH_LONG).show();
                return false;
            }
        }*/
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(getContext(), "Nick Name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        /*if (TextUtils.isEmpty(height)) {
            Toast.makeText(this, "Height is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Weight is empty", Toast.LENGTH_LONG).show();
            return false;
        }*/
        return true;
    }

    private void updateProfile() {
        // showing a progress dialog loading
        loading.setMessage("Updating user profile...");
        loading.show();

        MesosferUser user = MesosferUser.getCurrentUser();
        if (user != null) {
            user.setFirstName(firstname);
            user.setLastName(lastname);
            // set custom field
            //user.setData("dateOfBirth", dateOfBirth);
            user.setData("nickname", nickname);
            //user.setData("height", height);
            //user.setData("weight", weight);
            //user.setData("isMarried", isMarried);
            // execute update user
            user.updateDataAsync(new SaveCallback() {
                @Override
                public void done(MesosferException e) {
                    // hide progress dialog loading
                    loading.dismiss();

                    // setup alert dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setNegativeButton(android.R.string.ok, null);

                    // check if there is an exception happen
                    if (e != null) {
                        builder.setTitle("Error Happen");
                        builder.setMessage(
                                String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                        e.getCode(), e.getMessage())
                        );
                        dialog = builder.show();
                        return;
                    }

                    builder.setMessage("Update Profile Succeeded");
                    dialog = builder.show();
                }
            });
        }
    }

    public void handleChangePassword() {
        oldPassword = textOldPassword.getText().toString();
        newPassword = textNewPassword.getText().toString();
        confirmPassword = textConfirmPassword.getText().toString();

        // validating input values
        if (!isInputPasswordValid()) {
            // return if there is an invalid input
            return;
        }

        // execute update password
        updatePassword();
    }

    private boolean isInputPasswordValid() {
        // validating all input values if it is empty
        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(getContext(), "Old password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(getContext(), "New password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(getContext(), "Old and new password are equal", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), "Confirmation password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Confirmation password is not match", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updatePassword() {
        // showing a progress dialog loading
        loading.setMessage("Updating user profile...");
        loading.show();

        MesosferUser user = MesosferUser.getCurrentUser();
        if (user != null) {
            // execute update user
            user.changePasswordAsync(oldPassword, newPassword, new ChangePasswordCallback() {
                @Override
                public void done(MesosferException e) {
                    // hide progress dialog loading
                    loading.dismiss();

                    // setup alert dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setNegativeButton(android.R.string.ok, null);

                    // check if there is an exception happen
                    if (e != null) {
                        builder.setTitle("Error Happen");
                        builder.setMessage(
                                String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                        e.getCode(), e.getMessage())
                        );
                        dialog = builder.show();
                        return;
                    }

                    builder.setTitle("Update Password Succeeded");
                    builder.setMessage("You need to re-login to use new password!");
                    dialog = builder.show();
                }
            });
        }
    }

    //@Override
    //public boolean onSupportNavigateUp() {
    //    super.onBackPressed();
    //    return true;
    //}

    @Override
    public void onDestroy() {
        // dismiss any resource showing
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onDestroy();
    }
}
