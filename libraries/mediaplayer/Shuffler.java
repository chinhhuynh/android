package com.chinhhuynh.android.libraries.mediaplayer;

import java.util.List;
import java.util.Random;

/**
 * Contains logic to select the next song in the playlist.
 */
public class Shuffler {
  private List<MediaItem> playlist;
  private MediaItem currentItem;
  private boolean isShuffleEnabled;

  public Shuffler(List<MediaItem> playlist) {
    this.playlist = playlist;
    this.currentItem = null;
    this.isShuffleEnabled = true;
  }

  /**
   * Get current item;
   */
  public MediaItem getCurrentItem() {
    return currentItem;
  }

  /**
   * Move to the next item.
   */
  public void moveNext() {
    if (currentItem == null || playlist.size() == 0) {
      return;
    }
    int currentIndex = playlist.indexOf(currentItem);
    int nextIndex = getNextIndex(currentIndex);
    MediaItem nextItem = playlist.get(nextIndex);
    currentItem = nextItem;
  }

  /**
   * Get index of the next item given current item index.
   */
  private int getNextIndex(int currentIndex) {
    if (isShuffleEnabled) {
      return new Random().nextInt(playlist.size());
    } else {
      return currentIndex == -1 ? 0 : (currentIndex + 1) % playlist.size();
    }
  }

  /**
   * Enable/disable shuffling.
   * @param isEnabled Enable shuffle if set to true. Disable shuffle if set to false.
   */
  public void setShuffle(boolean isEnabled) {
    this.isShuffleEnabled = isEnabled;
  }

  public interface RepeatMode {
    // don't repeat.
    public final int OFF = 0;

    // repeat playlist.
    public final int ON = 1;

    // repeat current media item.
    public final int SINGLE = 2;
  }
}
