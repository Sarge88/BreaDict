package com.example.breadict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button addBtn;
    EditText input;

    TextView myTextView;
    int count=0;

    private boolean
    table_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView=(TextView)findViewById(R.id.timer);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New word");
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("Please add a word.");

        input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input.getText().toString();
                addNewWord(txt);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog ad = builder.create();

        addBtn = (Button) findViewById(R.id.addNewWord);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show();
            }
        });

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

    public void addNewWord(String txt){

        TextView txt1 = new TextView(this);

        txt1.setText(txt);

        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableLayout table = findViewById(R.id.table);

        TableRow newRow = new TableRow(this);// add views to the row
        newRow.addView(txt1);

        newRow.addView(new TextView(this)); // you would actually want to set properties on this before adding it

        // add the row to the table layout
        table.addView(newRow);
    }

}
