package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	
	// Get folder name for current date
    public static String getDateFolder() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    // Get timestamped name for files/reports/screenshots
    public static String getTimestampedName(String baseName) {
        return baseName + "__" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

}
