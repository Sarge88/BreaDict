package com.example.breadict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button addBtn;
    EditText input;
    Button colorizeBtn;
    EditText input2;
    TextView myTextView;
    ArrayList<String> words;
    int count=0;

    private boolean table_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        table_flg = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TableLayout table = findViewById(R.id.table);
        table.setColumnCollapsed(0, false);
        table.setColumnCollapsed(1, true);

        loadData();

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
                        myTextView.setText("Mennyit tanultam: "+count);
                        count++;
                    }
                });
            }
        }, 1000, 1000);

    }

    public void collapseTable(View view){
        TableLayout table = findViewById(R.id.table);
        Button showGermanBtn = findViewById(R.id.showGermanBtn);
        table.setColumnCollapsed(1, table_flg);

        if(table_flg){
            table_flg = false;
            showGermanBtn.setText("Német szavak mutatása");
        }
        else{
            table_flg = true;
            showGermanBtn.setText("Német szavak elrejtése");
        }
    }

    private void saveData() {
        SharedPreferences sh = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        Gson gson = new Gson();
        String json = gson.toJson(words);
        editor.putString("dict", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sh = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sh.getString("dict", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        words = gson.fromJson(json, type);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

        if(words == null) {
            words = new ArrayList<>();
        }

        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        TextView txtm = new TextView(this);
        TextView txtn = new TextView(this);
        txtm.setText("Magyar");
        txtn.setText("Német");
        txtm.setGravity(Gravity.CENTER);
        txtn.setGravity(Gravity.CENTER);
        txtm.setLayoutParams(params);
        txtn.setLayoutParams(params);
        txtm.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        txtn.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        TableLayout tableT = findViewById(R.id.table);
        TableRow newRowT = new TableRow(this);// add views to the row
        newRowT.addView(txtm);
        newRowT.addView(txtn);
        newRowT.addView(new TextView(this)); // you would actually want to set properties on this before adding it
        tableT.addView(newRowT, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for(int i=0; i<words.size(); i++) {
            String szo = words.get(i);
            String[] tomb = szo.split("-");
            TextView txt1 = new TextView(this);
            TextView txt2 = new TextView(this);
            txt1.setText(tomb[0]);
            txt2.setText(tomb[1]);
            txt1.setGravity(Gravity.CENTER);
            txt2.setGravity(Gravity.CENTER);
            txt1.setLayoutParams(params);
            txt2.setLayoutParams(params);
            TableLayout table = findViewById(R.id.table);
            TableRow newRow = new TableRow(this);// add views to the row
            newRow.addView(txt1);
            newRow.addView(txt2);
            newRow.addView(new TextView(this)); // you would actually want to set properties on this before adding it
            table.addView(newRow, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            String color = tomb[2];

            if (color.equals("p")){
                txt1.setBackgroundColor(0xFFFA8072);
                txt2.setBackgroundColor(0xFFFA8072);
                words.set(i, tomb[0]+"-"+tomb[1]+"-p");
            }
            else if (color.equals("z")){
                txt1.setBackgroundColor(0xFF00FF00);
                txt2.setBackgroundColor(0xFF00FF00);
                words.set(i, tomb[0]+"-"+tomb[1]+"-z");
            }
            else if(color.equals("s")){
                txt1.setBackgroundColor(0xFFFFFF00);
                txt2.setBackgroundColor(0xFFFFFF00);
                words.set(i, tomb[0]+"-"+tomb[1]+"-s");
            }
        }
    }

    public void addNewWord(String input1, String input2){
        TableLayout table = findViewById(R.id.table);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

        String word = input1 + "-" + input2 + "-0";

        clearTable();

        words.add(word);
        saveData();
        loadData();
    }

    public void addDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Új szó");
        dialog.setIcon(R.drawable.ic_launcher_background);
        dialog.setMessage("[Magyar szó]-[Német szó]");

        input = new EditText(this);
        dialog.setView(input);

        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)



        dialog.setPositiveButton("Hozzáadás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String szo = input.getText().toString();
                String[] tomb = szo.split("-");
                addNewWord(tomb[0],tomb[1]);
            }
        });

        dialog.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
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

    public void clearTable(){
        TableLayout table = findViewById(R.id.table);
        table.removeAllViews();
    }

    public void colorize(String word, String color){
        TableLayout table = findViewById(R.id.table);
        for (int i = 1; i < table.getChildCount(); i++) {
            View child = table.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 1; x < row.getChildCount(); x=x+2) {
                    View view = row.getChildAt(x);
                    View view2 = row.getChildAt(x-1);
                    TextView text = (TextView) view2;
                    String s = (String) text.getText().toString();
                    boolean correct = word.equals(s);
                    System.out.println(s+" "+word+" "+correct);
                    if (correct){
                        for(int z=0; z<words.size(); ++z){
                            String[] tomb = words.get(z).split("-");
                            System.out.println(tomb[0]+" "+word);
                            if(tomb[0].equals(word)){
                                if (color.equals("p")){
                                    view.setBackgroundColor(0xFFFA8072);
                                    view2.setBackgroundColor(0xFFFA8072);
                                    words.set(z, tomb[0]+"-"+tomb[1]+"-p");
                                }
                                else if (color.equals("z")){
                                    view.setBackgroundColor(0xFF00FF00);
                                    view2.setBackgroundColor(0xFF00FF00);
                                    words.set(z, tomb[0]+"-"+tomb[1]+"-z");
                                }
                                else if(color.equals("s")){
                                    view.setBackgroundColor(0xFFFFFF00);
                                    view2.setBackgroundColor(0xFFFFFF00);
                                    words.set(z, tomb[0]+"-"+tomb[1]+"-s");
                                }
                                else if(color.equals("0")){
                                    view.setBackgroundColor(0xFFFFFF);
                                    view2.setBackgroundColor(0xFFFFFF);
                                    words.set(z, tomb[0]+"-"+tomb[1]+"-0");
                                }
                                saveData();
                            }
                        }

                    }

                }
            }
        }
    }
    public void colorizeDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Színezés");
        dialog.setIcon(R.drawable.ic_launcher_background);
        dialog.setMessage("[Magyar szó] - [színkód] | színkódok: p (piros), z (zöld), s (sárga), 0 (töröl)");

        input2 = new EditText(this);
        dialog.setView(input2);

        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)



        dialog.setPositiveButton("Színezés", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String szo = input2.getText().toString();
                String[] tomb = szo.split("-");
                colorize(tomb[0],tomb[1]);
            }
        });
        dialog.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
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
