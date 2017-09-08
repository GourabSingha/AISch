package misclicks.aischmark1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class event_update extends AppCompatActivity {

    EditText editName , location ,date ,time ;
    private DataSource datasource;
    Button btnUpp, select_date, select_time;
    int year_x,month_x,day_x,minute_x,hour_x;
    static  final int DIALOG_ID=0;
    static  final int DIALOG_ID1=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);

        date = (EditText)findViewById(R.id.date_up);
        editName = (EditText)findViewById(R.id.event_up);
        location = (EditText)findViewById(R.id.location_up);
        time = (EditText)findViewById(R.id.time_up);
        btnUpp=(Button)findViewById(R.id.update);
        select_date=(Button)findViewById(R.id.button);
        select_time=(Button)findViewById(R.id.button2);
        datasource = new DataSource(this);
        datasource.open();

        Bundle bundle = getIntent().getExtras();
        String name_1 = bundle.getString("name");
        editName.setText(name_1);
        String loc_1 = bundle.getString("loc");
        location.setText(loc_1);
        String date_1 = bundle.getString("date");
        date.setText(date_1);
        String time_1 = bundle.getString("time");
        time.setText(time_1);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        update();
        showTimePickerDialog();
        showDialogOnButtonCLick();
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

    public void update()
    {
        btnUpp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean isInserted = datasource.updateData(
                        editName.getText().toString(),
                        location.getText().toString(),
                        date.getText().toString(),
                        time.getText().toString() );
                if(isInserted == true) {
                    Toast.makeText(event_update.this, "Data Updated", Toast.LENGTH_LONG).show();
                    Intent i= new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }

}
