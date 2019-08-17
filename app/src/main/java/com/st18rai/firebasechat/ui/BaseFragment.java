package com.st18rai.firebasechat.ui;

import androidx.fragment.app.Fragment;

import com.st18rai.firebasechat.R;

import java.util.Objects;

public class BaseFragment extends Fragment {

    protected void setTitle(String title) {
        ((MainActivity) Objects.requireNonNull(getActivity())).setTitle(title);
    }

    protected void showBackButton() {
        ((MainActivity) Objects.requireNonNull(getActivity())).setBackButtonEnabled(true);
    }

    protected void hideBackButton() {
        ((MainActivity) Objects.requireNonNull(getActivity())).setBackButtonEnabled(false);
        ((MainActivity) getActivity()).setTitle(getString(R.string.app_name));
    }

}
