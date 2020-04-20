package tech.nilu.wallet.db.converter;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

/**
 * Created by root on 11/28/17.
 */

public class BigDecimalConverter {
    @TypeConverter
    public static BigDecimal fromBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public static String amountToBigDecimal(BigDecimal amount) {
        return amount == null ? null : amount.toString();
    }
}
