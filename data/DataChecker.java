// 
// Decompiled by Procyon v0.5.36
// 

package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataChecker
{
    public static boolean isValidDate(final String inDate) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        }
        catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
