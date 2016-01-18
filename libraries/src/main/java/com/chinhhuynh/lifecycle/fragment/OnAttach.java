package com.chinhhuynh.lifecycle.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Implements this interface to listen to {@link Fragment#onAttach(Activity)} callback.
 */
public interface OnAttach {

    void onAttach(Activity activity);
}
