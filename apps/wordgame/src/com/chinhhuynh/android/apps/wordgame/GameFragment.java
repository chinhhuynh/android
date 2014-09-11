package com.chinhhuynh.android.apps.wordgame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * App Fragment.
 */
public class GameFragment extends Fragment {
  private Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getActivity();
  }
}
