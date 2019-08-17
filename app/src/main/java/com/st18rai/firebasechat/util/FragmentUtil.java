package com.st18rai.firebasechat.util;


import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.st18rai.firebasechat.R;


public class FragmentUtil {
    private static void replaceFragment(final FragmentManager manager, Fragment fragment,
                                        final boolean addToBackStack, Bundle bundle, int containerId) {
        final FragmentTransaction fTrans;
        fTrans = manager.beginTransaction();

        if (containerId != 0) {
            fTrans.replace(containerId, fragment, fragment.getClass().getSimpleName());
        } else {
            fTrans.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        }

        new Handler().post(() -> {

            fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            if (addToBackStack) {
                fTrans.addToBackStack(null);
            } else {
                try {
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            if (bundle != null) {
                fragment.setArguments(bundle);
            }

            fTrans.commitAllowingStateLoss();
        });
    }

    private static void addFragment(FragmentManager manager, Fragment fragment, boolean addToBackStack,
                                    Bundle bundle, int containerId) {
        FragmentTransaction fTrans;
        fTrans = manager.beginTransaction();

        if (containerId != 0) {
            fTrans.add(containerId, fragment, fragment.getClass().getSimpleName());
        } else {
            fTrans.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }

        if (addToBackStack) {
            fTrans.addToBackStack(null);
        } else {
            manager.popBackStackImmediate();
        }

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        fTrans.commit();
    }

    public static void replaceFragment(FragmentManager manager, Fragment fragment, boolean addToBackStack) {
        replaceFragment(manager, fragment, addToBackStack, null, 0);
    }

    public static void replaceFragment(FragmentManager manager, Fragment fragment, boolean addToBackStack,
                                       Bundle bundle) {
        replaceFragment(manager, fragment, addToBackStack, bundle, 0);
    }

    public static void addFragment(FragmentManager manager, Fragment fragment, boolean addToBackStack,
                                   Bundle bundle) {
        addFragment(manager, fragment, addToBackStack, bundle, 0);
    }

    public static void addFragment(FragmentManager manager, Fragment fragment, boolean addToBackStack) {
        addFragment(manager, fragment, addToBackStack, null, 0);
    }
}
