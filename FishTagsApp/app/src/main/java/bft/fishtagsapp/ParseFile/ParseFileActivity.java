package bft.fishtagsapp.ParseFile;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import bft.fishtagsapp.R;

/**
 * Current RFID reader transfers:
 * CountryID:999
 * NationalID:00000000009934
 * Reserved:0
 * Current mode: Animal Tag
 * Click 'More' to change the reading type
 */
public class ParseFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        fillInInfoFromFile();
    }

    /**
     *
     * @return Path to storage where transmitted file can be found
     * External storage currently (sdcard)
     */
    private File getStoragePath(){
        //return Environment.getExternalStorageDirectory();
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     *
     * @return name of file where transmitted data is stored.
     * Currently hardcoded to message transmitted on 3/8/2016
     */
    private String getFileName(){
        return "2016_03_08_22_08_1157890.txt";
    }

    protected HashMap<String, String> getEntries(){
        File file = new File(getStoragePath(), getFileName());
        HashMap<String, String> entries = new HashMap<>();
        String line;
        int numLine = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((line = br.readLine()) != null){
                numLine++;
                if(numLine > 3){
                    break;
                }
                Log.i("Reading", line);
                String[] l = line.split(":");
                entries.put(l[0], l[1]);

            }
        }
        catch(IOException e){
            // Do some error handling
            //TextView test = (TextView)findViewById(R.id.testText);
            //test.setText("Error occurred");
        }

        return entries;
    }

    protected void fillInInfoFromFile(){
        HashMap<String, String> entries = getEntries();
        for(String key : entries.keySet()){
            // If key exists in textview, fill in corresponding text
            try {
                int textID = getResources().getIdentifier(key,
                        "id", getPackageName());
                TextView text = (TextView)findViewById(textID);
                text.setText(entries.get(key));
            }
            catch (Exception e){
                // Do error handling if the id is not found
            }
        }
    }
}