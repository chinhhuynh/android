package com.chinhhuynh.android.apps.wordgame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * App Activity.
 */
public class GameActivity extends ActionBarActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.app_activity);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new GameFragment())
          .commit();
    }
  }
}
