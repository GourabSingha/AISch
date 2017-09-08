package misclicks.aischmark1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.SparseBooleanArray;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.Toast;

import java.util.ArrayList;

public class event_delete extends AppCompatActivity {

    private DataSource datasource;

    ListView listview ;
    ArrayList<String> where = new ArrayList<String>();
    ArrayList<String>  pos = new ArrayList<String>();
    SparseBooleanArray sparseBooleanArray ;
    Button b ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_delete);

        datasource=new DataSource(this);
        datasource.open();
        b= (Button)findViewById(R.id.button_delete);

        Cursor crs = datasource.event_delete();
        while (crs.moveToNext())
        {
            where.add(crs.getString(1));
        }
        final String[] ListViewItems = new String[ where.size() ];
        where.toArray( ListViewItems );

        listview = (ListView)findViewById(R.id.list1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, ListViewItems );
        listview.setAdapter(adapter);

       listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                sparseBooleanArray = listview.getCheckedItemPositions();
                int i = 0 ;
                while (i < sparseBooleanArray.size()) {
                    if (sparseBooleanArray.valueAt(i)) {
                        pos.add(ListViewItems [ sparseBooleanArray.keyAt(i)]);
                    }
                    i++ ;
                }
            }
        });
        delete();
    }

    public void delete(){
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] items = new String[ pos.size() ];
                pos.toArray( items );

                for(int i=0;i<items.length;i++)
                {
                datasource.delete(items[i]);
                }
                Toast.makeText(event_delete.this, "The Selected Data Deleted ", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            }

        });
    }

}