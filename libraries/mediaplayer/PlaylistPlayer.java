package com.chinhhuynh.android.libraries.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Pair;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chinhhuynh on 8/3/14.
 */
public class PlaylistPlayer implements MediaPlayer.OnCompletionListener {
  private static final String TAG = "PlaylistPlayer";

  private final Context context;
  private final MediaPlayer mediaPlayer;

  private int bufferPercentage;

  /**
   * Play list history.
   */
  private ArrayList<Integer> playHistory;

  /**
   * Playlist in format <Song name, file path>.
   */
  private List<Pair<String, String>> playlist;


  public PlaylistPlayer(Context context) {
    this.context = context;
    this.playHistory = new ArrayList<Integer>();
    this.mediaPlayer = new MediaPlayer();
    this.mediaPlayer.setOnCompletionListener(this);
  }

  /**
   * Set a new play list. Calling this method will stop currently playing item and clear existing
   * play history.
   */
  public void setPlaylist(List<Pair<String, String>> playlist) {
    mediaPlayer.stop();
    playHistory.clear();
    this.playlist = playlist;
  }

  /**
   * Play a song selected by the user. Previous play history will be cleared.
   * @param position Song position in playlist.
   */
  public void playByUserAction(int position) {
    playHistory.clear();
    play(position);
  }

  /**
   * Play a play list randomly. Previous play history will be cleared.
   */
  public void play() {
    playHistory.clear();
    int position = getNextItem();
    play(position);
  }

  /**
   * Play item at specified position in the play list.
   */
  private void play(int position) {
    playHistory.add(new Integer(position));
    Pair<String, String> item = playlist.get(position);
    String name = item.first;
    String filePath = item.second;
    try {
      mediaPlayer.reset();
      mediaPlayer.setDataSource(filePath);
      mediaPlayer.prepareAsync();
      mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
          mediaPlayer.start();
        }
      });
    } catch (IOException e) {
      String error = context.getString(R.string.error_cannot_play_back);
      Toast.makeText(context, String.format(error, name), Toast.LENGTH_SHORT);
    }
  }

  /**
   * Get next item. Currently next item is selected randomly.
   * @return Position of the next item.
   */
  private int getNextItem() {
    return new Random().nextInt(playlist.size());
  }

  @Override
  public void onCompletion(MediaPlayer mediaPlayer) {
    int next = getNextItem();
    play(next);
  }
}
