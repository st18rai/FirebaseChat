package com.st18rai.firebasechat.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.st18rai.firebasechat.R;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.button_register)
    public void onRegisterClick() {

        String userName = name.getEditText().getText().toString();
        String userEmail = email.getEditText().getText().toString();
        String userPassword = password.getEditText().getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)){
            Toast.makeText(getContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
        } else if (userPassword.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(userName, userEmail, userPassword);
        }

    }

    private void registerUser(String name, String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String userID = firebaseUser.getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", userID);
                hashMap.put("username", name);
                hashMap.put("imageURL", "default");

                databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        FragmentUtil.replaceFragment(getFragmentManager(), new UsersListFragment(), false);
                    }
                });
            } else {
                Toast.makeText(getContext(), "You can't register with this email or password!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
