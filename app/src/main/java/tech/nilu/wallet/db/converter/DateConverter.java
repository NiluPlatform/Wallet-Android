package tech.nilu.wallet.db.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by root on 11/28/17.
 */

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
