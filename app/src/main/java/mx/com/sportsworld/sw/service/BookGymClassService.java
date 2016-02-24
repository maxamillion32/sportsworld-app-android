package mx.com.sportsworld.sw.service;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import java.io.IOException;
import java.util.Locale;

import org.json.JSONException;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.GymClassResource;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class BookGymClassService.
 */
public class BookGymClassService extends SportsWorldWebApiService<Void> {

    /** The Constant EXTRA_GYM_CLASS_ID. */
    public static final String EXTRA_GYM_CLASS_ID = "com.upster.extra.GYM_CLASS_ID";
    
    /** The Constant EXTRA_FACILITY_PROGRAMED_ACTIVITY_ID. */
    public static final String EXTRA_FACILITY_PROGRAMED_ACTIVITY_ID = "com.upster.extra.FACILITY_PROGRAMED_ACTIVITY_ID";
    
    /** The Constant EXTRA_CLASS_DATE. */
    public static final String EXTRA_CLASS_DATE = "com.upster.extra.CLASS_DATE";
    
    /** The Constant EXTRA_NOTIFY_BRANCH. */
    public static final String EXTRA_NOTIFY_BRANCH = "com.upster.extra.NOTIFY_BRANCH";

    /**
	 * Instantiates a new book gym class service.
	 */
    public BookGymClassService() {
        super(BookGymClassService.class.getName());
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#makeApiCall(android.content.Intent)
     */
    @Override
    public RequestResult<Void> makeApiCall(Intent intent) throws IOException, JSONException {

        final SportsWorldAccountManager accountMngr = getAccountManager();
        if (!accountMngr.isLoggedInAsMember()) {
            throw new UnsupportedOperationException("This user is not a member");
        }

        final boolean notifyBranch = intent.getBooleanExtra(EXTRA_NOTIFY_BRANCH, false);

        if (!notifyBranch) {
            return null;
        }

        final long facilityProgramedActivityId = intent.getLongExtra(
                EXTRA_FACILITY_PROGRAMED_ACTIVITY_ID, -1L);
        final long classDateTime = intent.getLongExtra(EXTRA_CLASS_DATE, -1L);

        if ((facilityProgramedActivityId == -1d) || (classDateTime == -1)) {
            throw new IllegalArgumentException("You forgot to pass one or more of these: "
                    + "EXTRA_FACILITY_PROGRAMED_ACTIVITY_ID, EXTRA_CLASS_DATE_TIME");
        }

        final long userId = Long.parseLong(accountMngr.getCurrentUserId());

        return GymClassResource.bookGymClass(getApplicationContext(), facilityProgramedActivityId,
                userId, classDateTime);
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#proccessRequestResult(android.content.Intent, com.sportsworld.android.net.RequestResultParser.RequestResult)
     */
    @Override
    public boolean proccessRequestResult(Intent intent, RequestResult<Void> result) {
        final boolean saveOnLocalCalendar = intent.getBooleanExtra(EXTRA_NOTIFY_BRANCH, false);
        if (saveOnLocalCalendar) {
            saveOnLocalCalendar(0);
        }
        return true;
    }

    /**
	 * Save on local calendar.
	 * 
	 * @param gymClassId
	 *            the gym class id
	 */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void saveOnLocalCalendar(long gymClassId) {

        Cursor calendarCursor = null;
        Cursor gymClassCursor = null;
        try {

            String[] projection = new String[] { "_id", "name" };

            Uri calendarsUri = (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) ? CalendarContract.Calendars.CONTENT_URI
                    : Uri.parse("content://com.android.calendar/calendars");

            final ContentResolver cr = getContentResolver();

            calendarCursor = cr.query(calendarsUri, projection, null, null, null);
            gymClassCursor = cr.query(
                    SportsWorldContract.GymClass.buildGymClassUri(String.valueOf(gymClassId)),
                    new String[] { SportsWorldContract.GymClass.STARTS_AT,
                            SportsWorldContract.GymClass.COACH_NAME,
                            SportsWorldContract.GymClass.NAME}, null /* selection */,
                    null /* selectionArgs */, null /* sortOrder */);

            if (calendarCursor.moveToFirst() && gymClassCursor.moveToFirst()) {
                String calId = calendarCursor.getString(calendarCursor.getColumnIndex("_id"));

               
                ContentValues calendarEvent = new ContentValues();
                calendarEvent.put("calendar_id", calId);
                calendarEvent.put("title", gymClassCursor.getString(gymClassCursor
                        .getColumnIndex(SportsWorldContract.GymClass.NAME)));
                calendarEvent.put("description", String.format(Locale.US, getApplicationContext()
                        .getString(R.string.coach_name), gymClassCursor.getString(gymClassCursor
                        .getColumnIndex(SportsWorldContract.GymClass.COACH_NAME))));
                calendarEvent.put("eventLocation", "Upster " /* + guess it */);
                calendarEvent.put("dtstart", System.currentTimeMillis() /*
                                                                         * lastSelectedDate.
                                                                         * getTimeInMillis()
                                                                         */);

              
                calendarEvent.put("allDay", 0);
                calendarEvent.put("hasAlarm", 1);
                calendarEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Mexico_City");

                Uri eventsUri = Uri.parse("content://com.android.calendar/events");
                cr.insert(eventsUri, calendarEvent);

            }

        } finally {
            if (calendarCursor != null) {
                calendarCursor.close();
            }
            if (gymClassCursor != null) {
                gymClassCursor.close();
            }
        }

    }

}
