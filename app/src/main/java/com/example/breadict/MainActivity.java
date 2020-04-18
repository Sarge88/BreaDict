package com.example.breadict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button addBtn;
    EditText input;
    Button colorizeBtn;
    EditText input2;
    TextView myTextView;
    int count=0;

    private boolean
    table_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView=(TextView)findViewById(R.id.timer);

        addDialog();
        colorizeDialog();

        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        myTextView.setText("How many seconds did I study: "+count);
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

    public void addNewWord(String input1, String input2){

        TextView txt1 = new TextView(this);
        TextView txt2 = new TextView(this);

        txt1.setText(input1);
        txt2.setText(input2);

        //TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableLayout table = findViewById(R.id.table);

        TableRow newRow = new TableRow(this);// add views to the row
        newRow.addView(txt1);
        newRow.addView(txt2);

        newRow.addView(new TextView(this)); // you would actually want to set properties on this before adding it

        // add the row to the table layout
        //table.addView(params1, new TableLayout.LayoutParams());
        table.addView(newRow);
    }

    public void addDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("New word");
        dialog.setIcon(R.drawable.ic_launcher_background);
        dialog.setMessage("[German word] - [Hungarian word]");

        input = new EditText(this);
        dialog.setView(input);

        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)



        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String szo = input.getText().toString();
                String[] tomb = szo.split("-");
                addNewWord(tomb[0],tomb[1]);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog ad = dialog.create();

        addBtn = (Button) findViewById(R.id.addNewWord);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show();
            }
        });
    }

    public void colorize(String word, String color){
        TableLayout table = findViewById(R.id.table);
        int n = 0;
        for (int i = 1; i < table.getChildCount(); i++) {
            View child = table.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 1; x < row.getChildCount(); x=x+2) {
                    View view = row.getChildAt(x);
                    View view2 = row.getChildAt(x-1);
                    TextView text = (TextView) view;
                    String s = (String) text.getText().toString();
                    boolean correct = word.equals(s);
                    if (correct){
                        if (color.equals("red")){
                            view.setBackgroundColor(0xFFFA8072);
                            view2.setBackgroundColor(0xFFFA8072);
                        }
                        else if (color.equals("green")){
                            view.setBackgroundColor(0xFF00FF00);
                            view2.setBackgroundColor(0xFF00FF00);
                        }
                        else if(color.equals("yellow")){
                            view.setBackgroundColor(0xFFFFFF00);
                            view2.setBackgroundColor(0xFFFFFF00);
                        }
                    }

                }
            }
        }
    }
    public void colorizeDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Colorize");
        dialog.setIcon(R.drawable.ic_launcher_background);
        dialog.setMessage("[Word] - [Color]");

        input2 = new EditText(this);
        dialog.setView(input2);

        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)



        dialog.setPositiveButton("Colorize", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String szo = input2.getText().toString();
                String[] tomb = szo.split("-");
                colorize(tomb[0],tomb[1]);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog ad = dialog.create();

        colorizeBtn = (Button) findViewById(R.id.colorize);
        colorizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show();
            }
        });
    }

}
