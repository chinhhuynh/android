package com.chinhhuynh.gymtracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.chinhhuynh.gymtracker.R;

public final class NumberPickerDialog {

    public interface EventsListener {
        void onNumberSelect(int value);
        void onCancel();
    }

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResId;

    private View mLayoutView;
    private EventsListener mListener;

    private int mSelectedValue;
    private int mMinValue;
    private int mMaxValue;
    private int mInterval;

    public NumberPickerDialog(Context context, int layoutResId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mLayoutResId = layoutResId;
    }

    public NumberPickerDialog selectedValue(int selectedValue) {
        mSelectedValue = selectedValue;
        return this;
    }

    public NumberPickerDialog minValue(int minValue) {
        mMinValue = minValue;
        return this;
    }

    public NumberPickerDialog maxValue(int maxValue) {
        mMaxValue = maxValue;
        return this;
    }

    public NumberPickerDialog interval(int interval) {
        mInterval = interval;
        return this;
    }

    public NumberPickerDialog listener(EventsListener listener) {
        mListener = listener;
        return this;
    }

    public void show() {
        mLayoutView = mLayoutInflater.inflate(mLayoutResId, null);
        final NumberPicker numberPicker = (NumberPicker) mLayoutView.findViewById(R.id.number_picker);
        int selectedDisplayValue = mSelectedValue;
        int count = (mMaxValue - mMinValue) / mInterval + 1;
        final int startIndex = mMinValue / mInterval;
        String[] displayValues = new String[count + 1 - startIndex];
        for (int i = startIndex; i <= count; i++) {
            int value = mInterval * i;
            displayValues[i - startIndex] = Integer.toString(value);
        }

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(count - 1);
        numberPicker.setDisplayedValues(displayValues);
        numberPicker.setValue(selectedDisplayValue / mInterval - startIndex);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.select_weight_title);
        alertDialogBuilder.setView(mLayoutView);
        alertDialogBuilder
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int selected = (numberPicker.getValue() + startIndex) * mInterval;
                                notifyNumberSelect(selected);
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                notifyCancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void notifyNumberSelect(int value) {
        if (mListener != null) {
            mListener.onNumberSelect(value);
        }
    }

    private void notifyCancel() {
        if (mListener != null) {
            mListener.onCancel();
        }
    }
}
