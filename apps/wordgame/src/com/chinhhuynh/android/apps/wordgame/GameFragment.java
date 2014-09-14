package com.chinhhuynh.android.apps.wordgame;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

/**
 * App Fragment.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameFragment extends Fragment {
  private Context context;
  GameBoardAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.app_fragment, container, false);
    final GridView gameBoard = (GridView) view.findViewById(R.id.grid_game_board);
    gameBoard.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        if (adapter == null) {
          int numColumns = gameBoard.getNumColumns();
          int numRows = numColumns + 1;
          adapter = new GameBoardAdapter(context, numRows, numColumns, gameBoard.getWidth(),
              gameBoard.getHorizontalSpacing());
          gameBoard.setAdapter(adapter);
        }
      }
    });
    return view;
  }
}
