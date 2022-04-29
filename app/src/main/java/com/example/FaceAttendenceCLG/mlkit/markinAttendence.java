package com.example.FaceAttendenceCLG.mlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.FaceAttendenceCLG.AttendenceActivity;
import com.example.FaceAttendenceCLG.DataClass.MyPreference;
import com.example.FaceAttendenceCLG.ExamListActivity;
import com.example.FaceAttendenceCLG.MainActivity;
import com.example.FaceAttendenceCLG.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class markinAttendence extends AppCompatActivity {


    protected Interpreter tflite;
    private  int imageSizeX;
    private  int imageSizeY;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    public static final int REQUEST_CODE_PERMISSION = 101;
    public static final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public Bitmap oribitmap,testbitmap;
    public static Bitmap cropped;
    Uri imageuri;

    ImageView oriImage,testImage;
    Button buverify;
    TextView result_text;

    float[][] ori_embedding = new float[1][128];
    float[][] test_embedding = new float[1][128];

    Bitmap imageBitmap2;
    String exam;
    String attendece;
    private FirebaseAuth mAuth;
    private FirebaseDatabase reference;
    private MyPreference myPreference;
    private FirebaseUser firebaseUser;

    private ArrayList<String> permissionsToRequestFor;
     String url;
    private ProgressDialog progressDialog;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markin_attendence);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
          oriImage=(ImageView)findViewById(R.id.image1);

        progressDialog = new ProgressDialog(markinAttendence.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait…");


        initComponents();

        myPreference = new MyPreference(this);
        permissionsToRequestFor = new ArrayList<>();


         url = myPreference.getUrl();




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(REQUIRED_PERMISSIONS,REQUEST_CODE_PERMISSION);
        }


            }


    @Override
    protected void onResume() {
        super.onResume();

        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)
                oriImage.setImageBitmap(bitmap);
                face_detector(bitmap,"original");


            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionsGranted()) {

                dispatchTakePictureIntent();

            } else {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initComponents() {

       // oriImage=(ImageView)findViewById(R.id.image1);
        testImage=(ImageView)findViewById(R.id.image2);
        buverify=(Button)findViewById(R.id.verify);
        result_text=(TextView)findViewById(R.id.result);


        Intent intent = getIntent();
        exam = intent.getStringExtra("examscreen");
       // attendece = intent.getStringExtra("attendece");


        try{
            tflite=new Interpreter(loadmodelfile(this));
        }catch (Exception e) {
            e.printStackTrace();
        }




        buverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        double distance=calculate_distance(ori_embedding,test_embedding);
                        if(distance<6.0) {
                            result_text.setText("Result : Same Faces");

                            if(exam.equals("examscreen")){
                                Intent send = new Intent(markinAttendence.this, ExamListActivity.class);
                                startActivity(send);
                                finish();
                            }else {
                                Intent send = new Intent(markinAttendence.this, AttendenceActivity.class);
                                startActivity(send);
                                finish();
                            }

                        }
                        else{
                            result_text.setText("Result : Different Faces");
                            Toast.makeText(markinAttendence.this, "Unable to connect try Again", Toast.LENGTH_LONG).show();
                            Intent send = new Intent(markinAttendence.this, MainActivity.class);
                            startActivity(send);
                            finish();
                        }
                    }
                }, 8000);

            }

        });



    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            startActivityForResult(takePictureIntent, 13);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private double calculate_distance(float[][] ori_embedding, float[][] test_embedding) {
        double sum =0.0;
        for(int i=0;i<128;i++){
            sum=sum+Math.pow((ori_embedding[0][i]-test_embedding[0][i]),2.0);
        }
        return Math.sqrt(sum);
    }



    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer ) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }



    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }



    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

// test image load
         if(requestCode==13 && resultCode==RESULT_OK && data!=null) {


             Bundle extras = data.getExtras();
             imageBitmap2 = (Bitmap) extras.get("data");
            testImage.setImageBitmap(imageBitmap2);
             face_detector(imageBitmap2,"test");

        }
    }



    public void face_detector(final Bitmap bitmap, final String imagetype){

        final InputImage image = InputImage.fromBitmap(bitmap,0);
        FaceDetector detector = FaceDetection.getClient();
        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                // Task completed successfully
                                for (Face face : faces) {
                                    Rect bounds = face.getBoundingBox();
                                    cropped = Bitmap.createBitmap(bitmap, bounds.left, bounds.top, bounds.width(), bounds.height());
                                    get_embaddings(cropped,imagetype);
                                }
                            }

                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
    }
    public void get_embaddings(Bitmap bitmap,String imagetype){

        TensorImage inputImageBuffer;
        float[][] embedding = new float[1][128];

        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);

        inputImageBuffer = loadImage(bitmap,inputImageBuffer);

        tflite.run(inputImageBuffer.getBuffer(),embedding);

        if(imagetype.equals("original"))
            ori_embedding=embedding;
        else if (imagetype.equals("test"))
            test_embedding=embedding;
    }



}