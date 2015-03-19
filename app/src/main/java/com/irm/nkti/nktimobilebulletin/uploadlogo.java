package com.irm.nkti.nktimobilebulletin;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class uploadlogo extends ActionBarActivity {
    private String selectedImagePath;
    Bitmap img;
    ParseFile imgFile;
    Spinner spnDepartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadlogo);
        List<String> department=new ArrayList<>();

        department.add("IRM");
        department.add("OPD");
        department.add("GSD");
        department.add("HRD");

        spnDepartment=(Spinner)findViewById(R.id.spnDepartment);
        ArrayAdapter<String> adpDept= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,department);
        adpDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnDepartment.setAdapter(adpDept);
        Button slctImage=(Button)findViewById(R.id.btnSelectImage);
        slctImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                img= BitmapFactory.decodeFile(selectedImagePath);
                //imgProfile.setImageURI(selectedImageUri);
                Bitmap scaledBitmap=Bitmap.createScaledBitmap(img,img.getWidth(),img.getHeight(),false);
                img=scaledBitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bit = stream.toByteArray();
                imgFile = new ParseFile("Photo", bit);
                ParseObject department=new ParseObject("Department");
                department.put("Name",spnDepartment.getSelectedItem().toString());
                department.put("Logo",imgFile);
                //convert image to bitmap




                //getKeyInfo();
                //setProfileImage();

            }
        }
    }
    private String getPath(Uri uri) {
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_uploadlogo, menu);
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

    @Override
    protected void onResume() {
        super.onPostResume();

    }
}
