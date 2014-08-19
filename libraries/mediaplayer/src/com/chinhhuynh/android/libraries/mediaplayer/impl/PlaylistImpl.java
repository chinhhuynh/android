package com.chinhhuynh.android.libraries.mediaplayer.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chinhhuynh.android.libraries.mediaplayer.MediaItem;
import com.chinhhuynh.android.libraries.mediaplayer.Playlist;
import com.chinhhuynh.android.libraries.mediaplayer.R;

import java.io.IOException;
import java.util.List;

/**
 * Represent a playlist.
 */
public class PlaylistImpl implements Playlist, MediaPlayer.OnCompletionListener, AdapterView.OnItemClickListener {
  private static final String TAG = "PlaylistImpl";

  private final Context context;
  private final PlaylistAdapter adapter;
  private ListView listView;

  /**
   * Playlist shuffler.
   */
  private Shuffler shuffler;

  public PlaylistImpl(Context context, List<MediaItem> playlist) {
    this.context = context;
    this.adapter = new PlaylistAdapter(context, playlist);
    this.shuffler = new Shuffler(playlist);
  }

  /**
   * Play the playlist.
   */
  @Override
  public void playNext() {
    shuffler.moveNext();
    MediaItem item = shuffler.getCurrentItem();
    play(item);
  }

  @Override
  public View getView(Context context) {
    if (listView == null) {
      listView = new ListView(context);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(this);
    }
    return listView;
  }

  /**
   * Play the media item at specified position.
   */
  private void play(int position) {
    shuffler.move(position);
    MediaItem item = shuffler.getCurrentItem();
    play(item);
  }

  /**
   * Play a media item.
   * @param item
   */
  private void play(MediaItem item) {
    if (item == null) {
      return;
    }
    try {
      MediaPlayer mediaPlayer = new MediaPlayer();
      mediaPlayer.reset();
      mediaPlayer.setDataSource(item.getDataSource());
      mediaPlayer.prepareAsync();
      mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
          mediaPlayer.start();
        }
      });
    } catch (IOException e) {
      String error = context.getString(R.string.error_cannot_play_back);
      Toast.makeText(context, String.format(error, item.getDisplayName()), Toast.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  public void onCompletion(MediaPlayer mediaPlayer) {
    playNext();
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    //TODO
  }
}
