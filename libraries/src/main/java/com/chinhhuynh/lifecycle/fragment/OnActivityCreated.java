package com.chinhhuynh.lifecycle.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Implements this interface to listen to {@link Fragment#onActivityCreated(Bundle)} callback.
 */
public interface OnActivityCreated {

    void onActivityCreated(Bundle savedInstanceState);
}
