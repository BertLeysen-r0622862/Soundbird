package be.wienert.soundbird.data.local;

import android.arch.persistence.room.TypeConverter;

import java.util.UUID;

public class UUIDConverter {
    @TypeConverter
    public static UUID toUUID(String uuid) {
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public static String toString(UUID uuid) {
        return uuid.toString();
    }
}
