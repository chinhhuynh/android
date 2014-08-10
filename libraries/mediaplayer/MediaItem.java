package com.chinhhuynh.android.libraries.mediaplayer;

/**
 * Represent a media item.
 */
public class MediaItem {
  private final String displayName;
  private final String dataSource;
  private final String artist;
  private final String album;

  public MediaItem(String displayName, String dataSource, String artist, String album) {
    this.displayName = displayName;
    this.dataSource = dataSource;
    this.artist = artist;
    this.album = album;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getDataSource() {
    return dataSource;
  }

  public String getArtist() {
    return artist;
  }

  public String getAlbum() {
    return album;
  }
}
