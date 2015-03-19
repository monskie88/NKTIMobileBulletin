package com.irm.nkti.nktimobilebulletin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class SignUp extends ActionBarActivity {
    Button Register;
    ParseImageView imgProfile;
    private static int RESULT_LOAD_IMAGE = 1;
    ParseUser newUser;
    EditText edtFullname,edtUsername,edtPosition,edtPassword,edtPassword2;
    Spinner spnAccountType,spnDepartment;
    AdminList details;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //ParseObject.registerSubclass("User");
        progressDialog=new ProgressDialog(SignUp.this);
        //Parse.initialize(this, "VRsvlABLhL1pm8zqcbuKS3RZxOTMfStrbkvVd6An", "1QONJCeJEKa9nFqyh3kn9z2m1EjfRlI1DZG4RIyr");
        List<String> department=new ArrayList<>();

        department.add("IRM");
        department.add("OPD");
        department.add("GSD");
        department.add("HRD");
        List<String> userType=new ArrayList<>();

        userType.add("admin");
        userType.add("user");
        userType.add("VIP");

        Register=(Button)findViewById(R.id.btnRegister);
        //imgProfile=(ParseImageView)findViewById(R.id.imgSignUp);
        newUser=new ParseUser();
        details=new AdminList();
        edtFullname=(EditText)findViewById(R.id.edtFullname);
        edtPassword=(EditText)findViewById(R.id.edtSignUpPassword);
        edtUsername=(EditText)findViewById(R.id.edtUsername);
        edtPosition=(EditText)findViewById(R.id.edtPosition);
        edtPassword=(EditText)findViewById(R.id.edtSignUpPassword);
        edtPassword2=(EditText)findViewById(R.id.edtSignUpPassword2);

        //spnAccountType=(Spinner)findViewById(R.id.spnAccountType);
        spnDepartment=(Spinner)findViewById(R.id.spnDepartment);
        ArrayAdapter<String> adpDept= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,department);
        adpDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adpUser= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,userType);
        adpUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDepartment.setAdapter(adpDept);
        //spnAccountType.setAdapter(adpUser);

        //executeTask();
        //SpinnerAdapter departmentList=new SpinnerAdapter();

    /*    imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });*/
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getText().toString().equals(edtPassword2.getText().toString())) {
                    if(edtUsername.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Every user must have username!",Toast.LENGTH_LONG).show();
                    }else{
                    registerUser();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Password did not match",Toast.LENGTH_LONG).show();
                    edtPassword.setText("");
                    edtPassword2.setText("");
                }

            }
        });


    }

    private void executeTask() {
       // new prepareSignUp().execute();
    }

    private void registerUser() {
        startLoading();

        details.setUsername(edtUsername.getText().toString());
        details.setfullname(edtFullname.getText().toString());
        details.setPosition(edtPosition.getText().toString());
        details.setDept(spnDepartment.getSelectedItem().toString());
        details.setLevel("User");
        details.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
        newUser.setUsername(edtUsername.getText().toString());
        newUser.setPassword(edtPassword.getText().toString());
        newUser.put("fullname",edtFullname.getText().toString());
        newUser.setEmail("edmundvhermogino@yahoo.com");
            newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    details.setfullname(edtFullname.getText().toString());
                    details.setPosition(edtPosition.getText().toString());
                    details.setDept(spnDepartment.getSelectedItem().toString());
                    details.setLevel("User");
                    Toast.makeText(getApplicationContext(),"Sign up Success!",Toast.LENGTH_SHORT).show();
                    stopLoading();
                    finish();


                }
                else{
                    Toast.makeText(getApplicationContext(),"Sign up Failed! "+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void saveAsParseFile(){
        //convert image to bitmap
       /* ByteArrayOutputStream stream=new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG,100,stream);
        data=stream.toByteArray();
        imgFile=new ParseFile("Photo",data);
        //convert image to bitmap
       ParseObject save=new ParseObject("Sample");
        save.put("photo",imgFile);
        save.saveInBackground();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
           /* img=BitmapFactory.decodeFile(picturePath);
            imgProfile.setImageBitmap(img);
            saveAsParseFile();
*/

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
    protected void onPostResume() {
        super.onPostResume();



    }
    private void startLoading() {
        progressDialog.setTitle("Signing up");
        progressDialog.setMessage("Registering user to database");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void stopLoading(){
        progressDialog.dismiss();
        progressDialog = null;
        Intent i = new Intent(SignUp.this, LogIn.class);
        startActivity(i);
        finish();
    }
}
