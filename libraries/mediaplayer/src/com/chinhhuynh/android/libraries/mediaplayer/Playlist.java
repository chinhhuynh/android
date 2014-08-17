package com.chinhhuynh.android.libraries.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Represent a playlist.
 */
public class Playlist implements MediaPlayer.OnCompletionListener {
  private static final String TAG = "Playlist";

  private final Context context;

  /**
   * Playlist shuffler.
   */
  private Shuffler shuffler;

  public Playlist(Context context, List<MediaItem> playlist) {
    this.context = context;
    this.shuffler = new Shuffler(playlist);
  }

  /**
   * Play the playlist.
   */
  public void playNext() {
    shuffler.moveNext();
    MediaItem item = shuffler.getCurrentItem();
    play(item);
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
}
