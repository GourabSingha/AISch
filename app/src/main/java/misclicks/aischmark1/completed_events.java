package misclicks.aischmark1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class completed_events extends AppCompatActivity {

        DataSource datasource;
        String name[],loc[],date[],time[];
        ListView l1;
        Button btn;
        List<misclicks.aischmark1.completed_events.Xyz> lis;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view__events);

            l1 = (ListView)findViewById(R.id.list_view1);
            datasource = new DataSource(this);
            datasource.open();

            ArrayList<String> name_ar = new ArrayList<String>();
            ArrayList<String> loc_ar = new ArrayList<String>();
            ArrayList<String> date_ar = new ArrayList<String>();
            ArrayList<String> time_ar = new ArrayList<String>();

            Cursor crs = datasource.getAllData();
            List<misclicks.aischmark1.completed_events.Xyz> list=new ArrayList<>();
            while (crs.moveToNext())
            {
                misclicks.aischmark1.completed_events.Xyz xyz=new misclicks.aischmark1.completed_events.Xyz();
                xyz.name=crs.getString(1);
                xyz.date=crs.getString(3);
                xyz.location=crs.getString(2);
                xyz.time=crs.getString(4);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date strDate = null;
                try {
                    strDate = sdf.parse(xyz.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if ((new Date().after(strDate))){
                    list.add(xyz);
                }
                //name_ar.add(crs.getString(1));
                // loc_ar.add(crs.getString(2));
                // date_ar.add(crs.getString(3));
                // time_ar.add(crs.getString(4));
            }

            name = new String[name_ar.size()];
            name = name_ar.toArray(name);

            loc = new String[loc_ar.size()];
            loc = loc_ar.toArray(loc);

            date = new String[date_ar.size()];
            date = date_ar.toArray(date);

            time = new String[time_ar.size()];
            time = time_ar.toArray(time);


            misclicks.aischmark1.completed_events.CustomAdapter customAdapter = new
                    misclicks.aischmark1.completed_events.CustomAdapter(this,list);
            l1.setAdapter(customAdapter);
            edit_event();
        }

        public void edit_event(){
            l1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub
                    Intent intent=new Intent(getApplicationContext(),event_update.class);
                    intent.putExtra("name",lis.get(pos).name);
                    intent.putExtra("loc",lis.get(pos).location);
                    intent.putExtra("date",lis.get(pos).date);
                    intent.putExtra("time",lis.get(pos).time);
                    startActivity(intent);
                    return true;
                }
            });
        }


        class CustomAdapter extends BaseAdapter {


            Context context;
            LayoutInflater inflater;

            CustomAdapter(Context con,List<misclicks.aischmark1.completed_events.Xyz> list)
            {
                context=con;
                lis=list;
                inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }


            @Override
            public int getCount() {
                return lis.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View myview = inflater.inflate(R.layout.custom_layout,null);

                ImageView img = (ImageView)myview.findViewById(R.id.Image_dis);
                TextView txt1 = (TextView)myview.findViewById(R.id.dis_name);
                TextView txt2 = (TextView)myview.findViewById(R.id.dis_location);
                TextView txt3 = (TextView)myview.findViewById(R.id.dis_date);
                TextView txt4 = (TextView)myview.findViewById(R.id.dis_time);

                img.setImageResource(R.drawable.cal);
                txt1.setText(lis.get(i).name);
                txt2.setText(lis.get(i).location);
                txt3.setText(lis.get(i).date);
                txt4.setText(lis.get(i).time);

                return myview;
            }
        }


        class Xyz
        {
            String name;
            String location;
            String date;
            String time;
        }
    }
