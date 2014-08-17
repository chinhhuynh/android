package com.chinhhuynh.android.libraries.mediaplayerapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * App Activity.
 */
public class AppActivity extends ActionBarActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.app_activity);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new AppFragment())
          .commit();
    }
  }
}
