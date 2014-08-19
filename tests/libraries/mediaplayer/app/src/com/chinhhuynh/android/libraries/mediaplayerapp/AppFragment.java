package com.chinhhuynh.android.libraries.mediaplayerapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chinhhuynh.android.libraries.mediaplayer.MediaItem;
import com.chinhhuynh.android.libraries.mediaplayer.Playlist;
import com.chinhhuynh.android.libraries.mediaplayer.impl.PlaylistImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * App Fragment.
 */
public class AppFragment extends Fragment {
  private Context context;
  private Playlist playlist;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    List<MediaItem> mediaItems = getMediaItems();
    context = getActivity();
    playlist = new PlaylistImpl(getActivity(), mediaItems);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.app_fragment, container, false);
    layout.addView(playlist.getView(context));
    return layout;
  }

  private static List<MediaItem> getMediaItems() {
    ArrayList<MediaItem> items = new ArrayList<MediaItem>();
    items.add(new MediaItem("Monster", "monster.mp3", "Rihanna", "R.E.M"));
    return items;
  }
}
