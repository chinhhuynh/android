package com.chinhhuynh.gymtracker.model;

public final class Exercise {

    private final String mId;
    private final String mName;
    private final String mIconPath;

    public Exercise(String id,
                    String name,
                    String iconPath) {
        mId = id;
        mName = name;
        mIconPath = iconPath;
    }
}
