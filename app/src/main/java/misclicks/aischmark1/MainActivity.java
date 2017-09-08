package misclicks.aischmark1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static android.R.id.message;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String curDate,f_date1;
    FloatingActionButton fab;
    private DataSource datasource;
    TextView det;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        det = (TextView) findViewById(R.id.textView2);

        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("dd/M/yyyy");
        f_date1 = df1.format(c1.getTime());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, event_create.class);
                intent.putExtra("message", curDate);
                startActivity(intent);
            }

        });

        CalendarView calendarView = (CalendarView) findViewById(R.id.cd);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                String cur_Date = String.valueOf(d);
                int m = month + 1;
                int y = year;
                String cur_month = String.valueOf(m);
                String cur_year = String.valueOf(y);
                curDate = d + "/" + m + "/" + y;

                Cursor crs = datasource.get_event_name(curDate);
                if (crs.getCount() == 0) {
                    det.setText("No Events");
                }
                else {
                    StringBuffer cf = new StringBuffer();
                    while (crs.moveToNext()) {
                        cf.append("Event Name: " + crs.getString(1) + "\n");
                    }
                    det.setText(cf.toString());
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        datasource = new DataSource(this);
        datasource.open();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
        String f_date = df.format(c.getTime());
        Cursor cr1 = datasource.get_event_name(f_date);
        if (cr1.getCount() != 0) {
            StringBuffer cf = new StringBuffer();
            cf.append("Event Name: " );
            while (cr1.moveToNext()) {
                cf.append(cr1.getString(1) + ", ");
            }
            addNotification(cf.toString());
        }
    }

    private void addNotification(String t) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cal)
                        .setContentTitle("Event")
                        .setContentText(t);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_events)
        {
            Intent i = new Intent(this , View_Events.class);
            startActivity(i);
        }
        else if (id == R.id.nav_notifiaction) {

        }
        else if (id == R.id.nav_delete) {
            Intent intent1 = new Intent(this,event_delete.class);
            startActivity(intent1);
        }
        else if (id == R.id.nav_select) {
            Intent intent1 = new Intent(this,completed_events.class);
            startActivity(intent1);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
