package com.chinhhuynh.gymtracker.tasks;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.chinhhuynh.gymtracker.log.Timber;

public final class ExtractAssetsTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = ExtractAssetsTask.class.getSimpleName();

    public static final String EXERCISE_FOLDER = "exercises";
    public static final String SOUND_FOLDER = "sounds";

    private final Context mContext;
    private final AssetManager mAssetManager;
    private final byte[] mCopyBuffer;

    public ExtractAssetsTask(Context context) {
        mContext = context;
        mAssetManager = context.getAssets();
        mCopyBuffer = new byte[1024];
    }

    @Override
    protected Void doInBackground(Void... params) {
        extractFolder(EXERCISE_FOLDER);
        extractFolder(SOUND_FOLDER);
        return null;
    }

    private void extractFolder(String folderName) {
        String[] files = null;
        try {
            files = mAssetManager.list(folderName);
        } catch (IOException e) {
            Timber.e(TAG, "Failed to get asset files list. Error: %s", e.toString());
        }
        for (String filename : files) {
            extractFile(folderName, filename);
        }
    }

    private void extractFile(String folderName, String filename) {
        File outDir = mContext.getDir(folderName, Context.MODE_PRIVATE);
        File outFile = new File(outDir, filename);
        InputStream in = null;
        OutputStream out = null;
        try {
            in = mAssetManager.open(folderName + File.separatorChar + filename);
            out = new FileOutputStream(outFile);
            int read;

            while ((read = in.read(mCopyBuffer)) != -1) {
                out.write(mCopyBuffer, 0, read);
            }

            out.flush();
        } catch (IOException e) {
            Timber.e(TAG, "Failed to copy asset file %s. Error: %s.", filename, e.toString());
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
