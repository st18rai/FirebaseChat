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
import com.st18rai.firebasechat.R;
import com.st18rai.firebasechat.ui.BaseFragment;
import com.st18rai.firebasechat.util.FragmentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {
    @BindView(R.id.email)
    TextInputLayout email;

    @BindView(R.id.password)
    TextInputLayout password;

    @BindView(R.id.button_login)
    Button buttonLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @OnClick(R.id.register)
    void onRegisterClick() {
        FragmentUtil.replaceFragment(getFragmentManager(), new RegistrationFragment(), true);
    }

    @OnClick(R.id.button_login)
    void onLoginClick() {
        String userEmail = email.getEditText().getText().toString();
        String userPassword = password.getEditText().getText().toString();

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(getContext(), getResources().getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    FragmentUtil.replaceFragment(getFragmentManager(), new UsersListFragment(), false);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}