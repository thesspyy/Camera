package com.company.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class mainCamera extends Activity implements View.OnClickListener{
    Button btnCam;

    private static  final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);
        btnCam = (Button)findViewById(R.id.btnCamera);
        btnCam.setOnClickListener(this);

       // android 7.0 system to solve the problem of camera
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();

        //ตรวจสอบการมีข้อมูลอยู่
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            try{
                File f = Environment.getExternalStoragePublicDirectory("myPhoto");
                File[] sd = f.listFiles();

                LinearLayout content = (LinearLayout)this.findViewById(R.id.LyLO);
                content.removeAllViews();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=8;

                for(int i=0 ; i < sd.length ; i++){
                    ImageView img = new ImageView(this);
                    Bitmap bMap = BitmapFactory.decodeFile(sd[i].toString(),options);
                    img.setImageBitmap(bMap);
                    content.addView(img);

                    TextView textV = new TextView(this);
                    textV.setText(sd[i].toString());
                    content.addView(textV);
                }
            }catch (Exception e){
                Log.d("Error",e.toString());
            }
        }
    }
    @Override
    public void onClick(View v){
        startCameraActivity();
    }

    protected void startCameraActivity(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String picTime = sdf.format(new Date());

        String _path_pic = Environment.getExternalStorageDirectory()+"/myPhoto/"+picTime+".jpg";

        File file = new File(_path_pic);

       // Uri outputFileUri = Uri.fromFile(file);

        Uri outputFileUri = FileProvider.getUriForFile(this,
                "com.example.android.fileprovider", file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
                Toast.makeText(this,"Image saved.", Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "User cancel", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Can't save", Toast.LENGTH_LONG).show();
            }
        }
    }
}
