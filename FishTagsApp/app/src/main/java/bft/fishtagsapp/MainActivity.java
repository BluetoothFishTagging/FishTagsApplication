package bft.fishtagsapp;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import bft.fishtagsapp.GPS.GPS;
import bft.fishtagsapp.ParseFile.ParseFileActivity;
import bft.fishtagsapp.bft.fishtagsapp.Client.Uploader;

public class MainActivity extends AppCompatActivity {
    private GPS gps;

    Uploader uploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        /* GPS Code Start */
        gps = new GPS(this);
        Button GPSBtn = (Button) findViewById(R.id.GPS_GPSBtn);
        GPSBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Location location = gps.getGPS();
                if(location != null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String s = String.format("LAT:%f,LONG:%f",latitude,longitude);
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                }
            }
        });
        /* GPS End */

        /* Client Code Start */
        // change here as well if using placeholder
        String url = "http://192.168.16.73:8000/";
        uploader = new Uploader(this,url);
        Button testBtn = (Button) findViewById(R.id.testBtn);
        testBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                testClient();
            }
        });
        /* Client Code End */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


	/* TEST CODE FOR PARSEFILE START */
    /**
     * Called when user clicks Parse File button
     * @param view
     */
    public void parseFile(View view){
        Intent intent = new Intent(this, ParseFileActivity.class);
        startActivity(intent);
    }
	/* TEST CODE FORE PARSEFILE END */

    /* TEST CODE FOR CLIENT START */

    private static final int GET_TEST_FILE_CODE = 100;

    public void testClient() {
        /* Testing Purposes*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), GET_TEST_FILE_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_TEST_FILE_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        uploader.send(uri, "PARAM1", "PARAM2");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /* TEST CODE FOR CLIENT END */
}
