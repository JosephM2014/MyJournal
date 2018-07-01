package com.example.root.myjournal;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;

import java.util.TimeZone;

// QueryHandler
public class QueryHandler extends AsyncQueryHandler
{
    private static final String TAG = "QueryHandler";

    // Projection arrays
    private static final String[] CALENDAR_PROJECTION = new String[]
        {
            Calendars._ID
        };

    // The indices for the projection array above.
    private static final int CALENDAR_ID_INDEX = 0;

    private static final int CALENDAR = 0;
    private static final int EVENT    = 1;
    private static final int REMINDER = 2;

    private static QueryHandler queryHandler;

    // QueryHandler
    private QueryHandler(ContentResolver resolver)
    {
        super(resolver);
    }

    // insertEvent
    public static void insertEvent(Context context, long startTime,
                                   long endTime, String title)
    {
        ContentResolver resolver = context.getContentResolver();

        if (queryHandler == null)
            queryHandler = new QueryHandler(resolver);

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startTime);
        values.put(Events.DTEND, endTime);
        values.put(Events.TITLE, title);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Calendar query start");

        queryHandler.startQuery(CALENDAR, values, Calendars.CONTENT_URI,
                                CALENDAR_PROJECTION, null, null, null);
    }

    // onQueryComplete
    @Override
    public void onQueryComplete(int token, Object object, Cursor cursor)
    {
        // Use the cursor to move through the returned records
        cursor.moveToFirst();

        // Get the field values
        long calendarID = cursor.getLong(CALENDAR_ID_INDEX);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Calendar query complete " + calendarID);

        ContentValues values = (ContentValues) object;
        values.put(Events.CALENDAR_ID, calendarID);
        values.put(Events.EVENT_TIMEZONE,
                   TimeZone.getDefault().getDisplayName());

        startInsert(EVENT, null, Events.CONTENT_URI, values);
    }

    // onInsertComplete
    @Override
    public void onInsertComplete(int token, Object object, Uri uri)
    {
        if (uri != null)
        {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "Insert complete " + uri.getLastPathSegment());

            switch (token)
            {
            case EVENT:
                long eventID = Long.parseLong(uri.getLastPathSegment());
                ContentValues values = new ContentValues();
                values.put(Reminders.MINUTES, 10);
                values.put(Reminders.EVENT_ID, eventID);
                values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                startInsert(REMINDER, null, Reminders.CONTENT_URI, values);
                break;
            }
        }
    }
}
