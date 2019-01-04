package be.wienert.soundbird.data.local;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

public class UriConverter {
    @TypeConverter
    public static Uri toUri(String uri) {
        return Uri.parse(uri);
    }

    @TypeConverter
    public static String toString(Uri uri) {
        return uri.toString();
    }
}
