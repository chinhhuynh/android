package com.chinhhuynh.android.apps.wordgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.chinhhuynh.android.libraries.wordgames.views.TextTile;

import java.util.Random;

/**
 * Adapter for game board.
 */
public class GameBoardAdapter extends BaseAdapter {
  private static final char[] ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

  private final Context context;
  private final int numColumns;
  private final int numTiles;
  private final TextTile[][] tiles;
  private final int tileSize;
  private final int tilePadding;

  /**
   * Create a game board with specified rows and columns. Tile's size will be calculated to fit in
   * specified board width and spacing. Board height should be set to wrap_content and will be
   * determined at runtime.
   * @param numRows Number of rows in the board.
   * @param numColumns Number of columns in the board.
   * @param boardWidth Board width.
   * @param spacing Spacing between each tile.
   */
  public GameBoardAdapter(Context context, int numRows, int numColumns, int boardWidth,
      int spacing) {
    this.context = context;
    this.numColumns = numColumns;
    this.numTiles = numRows * numColumns;
    this.tiles = new TextTile[numRows][numColumns];
    this.tileSize = (boardWidth - numColumns * spacing) / numColumns;
    this.tilePadding = tileSize / 4;
  }

  @Override
  public int getCount() {
    return numTiles;
  }

  @Override
  public TextTile getItem(int index) {
    return getTile(index);
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(int index, View view, ViewGroup viewGroup) {
    TextTile tile = getTile(index);
    if (tile == null) {
      tile = (TextTile) LayoutInflater.from(context).inflate(R.layout.text_tile, viewGroup, false);
      initTile(tile);
      setTile(index, tile);
    }
    return tile;
  }

  private void initTile(TextTile tile) {
    Random random = new Random();
    AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(tileSize, tileSize);
    tile.setLayoutParams(layoutParams);
    tile.setText(Character.toString(ALPHABETS[random.nextInt(ALPHABETS.length)]));
    tile.setPadding(tilePadding);
  }

  private void setTile(int index, TextTile tile) {
    int row = getRow(index);
    int column = getColumn(index);
    tiles[row][column] = tile;
  }

  private TextTile getTile(int index) {
    int row = getRow(index);
    int column = getColumn(index);
    return tiles[row][column];
  }

  private int getRow(int index) {
    return index / numColumns;
  }

  private int getColumn(int index) {
    return index % numColumns;
  }
}
