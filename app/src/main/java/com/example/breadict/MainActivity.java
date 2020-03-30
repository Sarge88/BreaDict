package com.example.breadict;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //test

    TextView myTextView;
    int count=0;

    private boolean
    table_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView=(TextView)findViewById(R.id.timer);

        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        myTextView.setText("How many seconds did I waste with running this app: "+count);
                        count++;
                    }
                });
            }
        }, 1000, 1000);

    }

    public void collapseTable(View view){
        TableLayout table = findViewById(R.id.table);
        Button showGermanBtn = findViewById(R.id.showGermanBtn);
        table.setColumnCollapsed(0, table_flg);

        if(table_flg){
            table_flg = false;
            showGermanBtn.setText("Show German");
        }
        else{
            table_flg = true;
            showGermanBtn.setText("Hide German");
        }
    }

    public void addNewWord(View view){

        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableLayout table = findViewById(R.id.table);
        Button add = findViewById(R.id.addNewWord);

        TextView txt1=new TextView(this);
        txt1.setText("asd");

        TableRow newRow = new TableRow(this);// add views to the row
        newRow.addView(txt1);

        newRow.addView(new TextView(this)); // you would actually want to set properties on this before adding it

        // add the row to the table layout
        table.addView(newRow);
    }

}
