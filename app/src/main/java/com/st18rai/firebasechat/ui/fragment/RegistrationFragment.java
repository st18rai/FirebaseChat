package com.st18rai.firebasechat.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.st18rai.firebasechat.R;
import com.st18rai.firebasechat.interfaces.Constants;
import com.st18rai.firebasechat.ui.BaseFragment;
import com.st18rai.firebasechat.util.FragmentUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment extends BaseFragment {
    @BindView(R.id.name)
    TextInputLayout name;

    @BindView(R.id.email)
    TextInputLayout email;

    @BindView(R.id.password)
    TextInputLayout password;

    @BindView(R.id.button_register)
    Button buttonRegister;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set title and toolbar state
        showBackButton();
        setTitle(getString(R.string.register));
    }

    @Override
    public void onStop() {
        super.onStop();

        hideBackButton();
    }

    @OnClick(R.id.button_register)
    void onRegisterClick() {

        String userName = name.getEditText().getText().toString();
        String userEmail = email.getEditText().getText().toString();
        String userPassword = password.getEditText().getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(getContext(), getResources().getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
        } else if (userPassword.length() < 6) {
            Toast.makeText(getContext(), getResources().getString(R.string.password_warning), Toast.LENGTH_SHORT).show();
        } else {
            registerUser(userName, userEmail, userPassword);
        }

    }

    private void registerUser(String name, String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String userID = firebaseUser.getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USERS).child(userID);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Constants.ID, userID);
                hashMap.put(Constants.USERNAME, name);
                hashMap.put(Constants.IMAGE_URL, Constants.DEFAULT);

                databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        FragmentUtil.replaceFragment(getFragmentManager(), new UsersListFragment(), false);
                    }
                });
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.register_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
