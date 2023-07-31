package com.example.codingcafe.adaptithackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class ImageScanner extends AppCompatActivity {
    private  static  final int Request_Camera_Code = 100;
    Button btn_Capture, btn_copy, btn_sub;
    ImageView img;
    Bitmap imageBitmap;
    TextView txt_view, txt_V;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scanner);
        btn_Capture = findViewById(R.id.button_capture);
        btn_copy = findViewById(R.id.button_copy);
        btn_sub = findViewById(R.id.button);
        txt_view = findViewById(R.id.text_data);
        txt_V = findViewById(R.id.text_data);
        img = (ImageView) findViewById(R.id.image);
        FirebaseApp.initializeApp(this);
        //check if camera permissions are set, if not set them
        if(ContextCompat.checkSelfPermission(ImageScanner.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ImageScanner.this, new String[]{
                    Manifest.permission.CAMERA
            }, Request_Camera_Code);
        }
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DATA = txt_V.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(), profile.class);
                intent.putExtra("ID", DATA);
                startActivity(intent);
                //startActivity(new Intent(ImageScanner.this,profile.class));
            }
        });
        btn_Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectTxt();
            }
        });
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //fetches the image captured from camera
    private void dispatchTakePictureIntent() {
        // in the method we are displaying an intent to capture our image.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // on below line we are calling a start activity
        // for result method to get the image captured.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //Return with image data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // calling on activity result method.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // on below line we are getting
            // data from our bundles. .
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            // below line is to set the
            // image bitmap to our image.
            img.setImageBitmap(imageBitmap);
        }
    }
    //process and produce text from image data
    private void detectTxt() {
        // this is a method to detect a text from image.
        // below line is to create variable for firebase
        // vision image and we are getting image bitmap.
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        // below line is to create a variable for detector and we
        // are getting vision text detector from our firebase vision.
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getCloudTextRecognizer();

        // adding on success listener method to detect the text from image.
        Task<FirebaseVisionText> task = detector.processImage(image);
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                // calling a method to process
                // our text after extracting.
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // handling an error listener.
                Toast.makeText(ImageScanner.this, "Fail to detect the text from image..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //returns text from image processed
    private void processTxt(FirebaseVisionText text) {
        // below line is to create a list of vision blocks which
        // we will get from our firebase vision text.
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();

        // checking if the size of the
        // block is not equal to zero.
        if (blocks.size() == 0) {
            // if the size of blocks is zero then we are displaying
            // a toast message as no text detected.
            Toast.makeText(ImageScanner.this, "No Text ", Toast.LENGTH_LONG).show();
            return;
        }
        // extracting data from each block using a for loop.
        for (FirebaseVisionText.TextBlock block : text.getTextBlocks()) {
            // below line is to get text
            // from each block.
            String txt = block.getText();

            // below line is to set our
            // string to our text view.
            txt_view.setText(txt);
        }
    }
}