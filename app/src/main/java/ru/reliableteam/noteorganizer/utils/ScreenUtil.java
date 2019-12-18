package ru.reliableteam.noteorganizer.utils;

import android.app.Activity;
import android.content.res.Configuration;

public class ScreenUtil {
    public static boolean isLandscapeOrientation(Activity activity) {
        Configuration configuration = activity.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isTablet(Activity activity) {
        Configuration configuration = activity.getResources().getConfiguration();
        boolean xlarge = ((configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static int getDisplayColumns(Activity activity) {
        int columnCountBase = 2;

        if (isTablet(activity))
            columnCountBase += 2;

        if (isLandscapeOrientation(activity))
            columnCountBase += 1;

        return columnCountBase;
    }
}
