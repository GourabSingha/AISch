package misclicks.aischmark1;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class event_create extends AppCompatActivity {

    EditText editName , location ,date ,time ;
    private DataSource datasource;
    Button btnAdd,select_date,select_time;
    int year_x,month_x,day_x,minute_x,hour_x;
    static  final int DIALOG_ID=0;
    static  final int DIALOG_ID1=1;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        date = (EditText)findViewById(R.id.date);
        if (message!=null)
            date.setText(message);
        editName = (EditText)findViewById(R.id.event_name);
        location = (EditText)findViewById(R.id.location);
        time = (EditText)findViewById(R.id.time);
        btnAdd=(Button)findViewById(R.id.submit);
        select_date=(Button)findViewById(R.id.date_pick);
        select_time=(Button)findViewById(R.id.time_pick);
        datasource = new DataSource(this);
        datasource.open();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        add_data();
        showDialogOnButtonCLick();
        showTimePickerDialog();
    }

    public void showDialogOnButtonCLick()
    {
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id==DIALOG_ID)
            return new DatePickerDialog(this ,dpickerListener,year_x,month_x,day_x);
        else if (id == DIALOG_ID1)
            return new TimePickerDialog(this, TimePickerListener, hour_x, minute_x, true);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x=year;
            month_x = monthOfYear + 1;
            day_x= dayOfMonth;
            String y=String.valueOf(year_x);
            String m=String.valueOf(month_x);
            String d=String.valueOf(day_x);

            date.setText(d+"/"+m+"/"+y);
        }
    };

    public void showTimePickerDialog(){
        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID1);
            }
        });
    }

    protected  TimePickerDialog.OnTimeSetListener TimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    hour_x = hourOfDay;
                    minute_x = minute;

                    String h = String.valueOf(hour_x);
                    String m = String.valueOf(minute_x);

                    time.setText(h+":"+m);
                }
            };

    public void add_data()
    {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
                String f_date = df.format(c.getTime());

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date strDate = null;
                Date f_date1 = null;
                try {
                    strDate = sdf.parse(date.getText().toString());
                    f_date1 = sdf.parse(f_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!(new Date().after(strDate)) || (f_date1.compareTo(strDate) == 0)) {

                    if (!editName.getText().toString().equals("") && !location.getText().toString().equals("") &&
                            !date.getText().toString().equals("") && !time.getText().toString().equals("")) {
                        boolean isInserted = datasource.insert_data(
                                editName.getText().toString(),
                                location.getText().toString(),
                                date.getText().toString(),
                                time.getText().toString());
                        if (isInserted == true) {
                            Toast.makeText(event_create.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            noti(year_x, month_x, day_x, hour_x, minute_x);
                        } else
                            Toast.makeText(event_create.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                    } else
                        Toast.makeText(event_create.this, "Insert the Data", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(event_create.this, "Wrong Date Inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void noti(int year,int mon,int day,int hr,int min){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,mon,day,hr,min);

        Intent myIntent = new Intent(event_create.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(event_create.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}
