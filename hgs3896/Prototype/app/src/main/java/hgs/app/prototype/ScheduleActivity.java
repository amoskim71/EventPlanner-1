package hgs.app.prototype;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

/*
 * References 1 : https://developer.android.com/guide/topics/providers/content-provider-basics.html
 * References 2 : https://developer.android.com/guide/topics/providers/calendar-provider
 * References 3 : https://developer.android.com/reference/android/os/AsyncTask
 * References 4 : https://developer.android.com/training/permissions/requesting
 */
public class ScheduleActivity extends AppCompatActivity{

    private class LoadCalendarTask extends AsyncTask<Void, Integer, Boolean> {

        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        public final String[] CALENDAR_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };

        // The indices for the projection array above.
        private final int PROJECTION_ID_INDEX = 0;
        private final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        private final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        private final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        public final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Events.CALENDAR_ID,                      // 0
                CalendarContract.Events.ORGANIZER,                        // 1
                CalendarContract.Events.TITLE,                            // 2
                CalendarContract.Events.DTSTART,                          // 3
                CalendarContract.Events.DTEND                             // 4
        };

        @Override
        protected Boolean doInBackground(Void... voids) {
            // 쿼리 처리

            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();

            // Submit the query and get a Cursor object back.
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return false;
            }

            // Query calendars
            cur = cr.query(CalendarContract.Calendars.CONTENT_URI, CALENDAR_PROJECTION, null, null, null);

            if( cur == null )
                return false;

            // Use the cursor to step through the returned records
            while (cur.moveToNext()) {
                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                // Get the field values
                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                // Do something with the values...
                Log.d("Hams", String.format("%d %s %s %s", calID, displayName, accountName, ownerName));
            }

            // Query events in each calendar
            cur = cr.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, null, null, CalendarContract.Events.CALENDAR_ID);

            if( cur == null )
                return false;

            // Use the cursor to step through the returned records
            while (cur.moveToNext()) {
                long eventID = 0;
                String organizer = null;
                String title = null;
                long dtstart = 0;
                long dtend = 0;

                // Get the field values
                eventID = cur.getLong(0);
                organizer = cur.getString(1);
                title = cur.getString(2);
                dtstart = cur.getLong(3);
                dtend = cur.getLong(4);

                Calendar s = Calendar.getInstance();
                s.setTimeInMillis(dtstart);
                Calendar e = Calendar.getInstance();
                e.setTimeInMillis(dtend);

                // Make logs with the values
                Log.d("Hams", String.format("%d %s %s %s %s", eventID, organizer, title, s.getTime(), e.getTime()));
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // GUI 업데이트
            // setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // 처리 완료 후 GUI 업데이트
            // showDialog(":Loaded " + result);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.READ_CALENDAR)) {
                // Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response!
                // After the user sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 100);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 100);
            }
        }else{
            new LoadCalendarTask().execute();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length >= 1){
            new LoadCalendarTask().execute();
        }
    }
}
