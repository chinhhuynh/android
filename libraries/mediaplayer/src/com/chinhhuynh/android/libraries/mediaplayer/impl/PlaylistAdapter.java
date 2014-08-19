package com.chinhhuynh.android.libraries.mediaplayer.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinhhuynh.android.libraries.mediaplayer.MediaItem;
import com.chinhhuynh.android.libraries.mediaplayer.R;

import java.util.List;

/**
 * Playlist adapter.
 */
public class PlaylistAdapter extends BaseAdapter {
  private final Context context;
  private final List<MediaItem> playlist;

  public PlaylistAdapter(Context context, List<MediaItem> playlist) {
    this.context = context;
    this.playlist = playlist;
  }

  @Override
  public int getCount() {
    return playlist.size();
  }

  @Override
  public MediaItem getItem(int position) {
    return playlist.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView displayName;
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.media_item, parent, false);
      displayName = (TextView) convertView.findViewById(R.id.display_name);
      convertView.setTag(R.id.display_name, displayName);
    } else {
      displayName = (TextView) convertView.getTag(R.id.display_name);
    }
    MediaItem item = playlist.get(position);
    displayName.setText(item.getDisplayName());
    return convertView;
  }
}
