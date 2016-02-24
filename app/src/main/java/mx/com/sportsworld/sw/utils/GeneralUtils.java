package mx.com.sportsworld.sw.utils;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The Class GeneralUtils.
 */
@SuppressLint("SimpleDateFormat")
public class GeneralUtils {

	/** The Constant DATE_FORMAT. */
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	// get date in this format dddd-mm-yy
	/**
	 * Gets the date.
	 * 
	 * @param dateString
	 *            the date string
	 * @return the date
	 */
	public static int[] getDate(String dateString) {
		String[] temp;
		temp = dateString.split("-");
		int[] dateInt = new int[temp.length];
		dateInt[0] = Integer.parseInt(temp[0]);
		dateInt[1] = Integer.parseInt(temp[1]);
		dateInt[2] = Integer.parseInt(temp[2]);
		return dateInt;
	}

	/**
	 * Gets the face date.
	 * 
	 * @param dateString
	 *            the date string
	 * @return the face date
	 */
	public static int[] getFaceDate(String dateString) {
		String[] temp;
		temp = dateString.split("/");
		int[] dateInt = new int[temp.length];
		dateInt[0] = Integer.parseInt(temp[0]);
		dateInt[1] = Integer.parseInt(temp[1]);
		dateInt[2] = Integer.parseInt(temp[2]);
		return dateInt;
	}

	/**
	 * Generate date.
	 * 
	 * @param params
	 *            the params
	 * @return the date
	 */
	@SuppressWarnings("deprecation")
	public static Date generateDate(int[] params) {
		return new Date(params[0] + 18, params[1], params[2]);
	}

	/**
	 * Generate face date.
	 * 
	 * @param params
	 *            the params
	 * @return the date
	 */
	@SuppressWarnings("deprecation")
	public static Date generateFaceDate(int[] params) {
		return new Date(params[2], params[0], params[1]);
	}

	/**
	 * Today day.
	 * 
	 * @return the date
	 */
	@SuppressWarnings("deprecation")
	public static Date todayDay() {
		Calendar todayCalendar = Calendar.getInstance();
		return new Date(todayCalendar.get(Calendar.YEAR),
				todayCalendar.get(Calendar.MONTH) + 1,
				todayCalendar.get(Calendar.DAY_OF_MONTH));
	}

	// Split a string
	/**
	 * Split string.
	 * 
	 * @param textToSplit
	 *            the text to split
	 * @param delimiter
	 *            the delimiter
	 * @return the string[]
	 */
	public static String[] splitString(String textToSplit, String delimiter) {
		String[] temp;
		temp = textToSplit.split(delimiter);
		return temp;
	}

	/**
	 * Check height.
	 * 
	 * @param height
	 *            the height
	 * @return true, if successful
	 */
	public static boolean checkHeight(double height) {
		boolean result = false;
		if (height >= 1.4 && height <= 2.5)
			result = true;
		return result;
	}

	/**
	 * Check weight.
	 * 
	 * @param wieght
	 *            the wieght
	 * @return true, if successful
	 */
	public static boolean checkWeight(double wieght) {
		boolean result = false;
		if (wieght >= 35 && wieght <= 200)
			result = true;
		return result;
	}

	/**
	 * Check age.
	 * 
	 * @param age
	 *            the age
	 * @return true, if successful
	 */
	public static boolean checkAge(int age) {
		boolean result = false;
		if (age >= 18 && age <= 120)
			result = true;
		return result;
	}

	/**
	 * Generate age.
	 * 
	 * @param age
	 *            the age
	 * @param isMember
	 *            the is member
	 * @return the int
	 */
	public static int generateAge(String age, boolean isMember) {
		int result = 0;
		int[] parseYear = null;

		if (age != null) {
			if (isMember)
				parseYear = getDate(age);
			else
				parseYear = getFaceDate(age);

			Calendar dob = Calendar.getInstance();
			dob.set(parseYear[2], parseYear[0], parseYear[1]);
			Calendar today = Calendar.getInstance();
			result = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			if (today.get(Calendar.DAY_OF_YEAR) <= dob
					.get(Calendar.DAY_OF_YEAR))
				result--;
		}
		return result;
	}

	/**
	 * Generate current date.
	 * 
	 * @return the calendar
	 */
	public static Calendar generateCurrentDate() {
		Calendar c = Calendar.getInstance();
		return c;
	}

	/**
	 * String to calendar.
	 * 
	 * @param strDate
	 *            the str date
	 * @return the calendar
	 */
	public static Calendar stringToCalendar(String strDate) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			c.setTime(sdf.parse(strDate));
		} catch (Exception ex) {

		}
		return c;
	}

	/**
	 * Thousand format.
	 * 
	 * @param number
	 *            the number
	 * @return the string
	 */
	public static String thousandFormat(int number) {
		String formatNumber = "";
		try {
			formatNumber = String.format(Locale.US, "%,8d%n", number);
		} catch (Exception ex) {

		}

		return formatNumber;

	}

	/**
	 * Change char to char.
	 * 
	 * @param tmpString
	 *            the tmp string
	 * @param charOne
	 *            the char one
	 * @param charTwo
	 *            the char two
	 * @return the string
	 */
	public static String changeCharToChar(String tmpString,
			CharSequence charOne, CharSequence charTwo) {
		String replaceStr = "";
		replaceStr = tmpString.replace(charOne, charTwo);
		return replaceStr;
	}

}
