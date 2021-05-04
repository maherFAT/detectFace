package com.example.detectface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         imageView = (ImageView) findViewById(R.id.imageFace);
        button = findViewById(R.id.btnTest);

        Bitmap bm = getBitmapFromAsset("face_test2.jpg");
        imageView.setImageBitmap(bm);

        Paint boxPaint = new Paint();
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(Color.RED);
        boxPaint.setStyle(Paint.Style.STROKE);
        Bitmap tempbm = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),Bitmap.Config.RGB_565);
        final  Canvas canvas = new Canvas(tempbm);
        canvas.drawBitmap(bm,0,0,null);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if (!detector.isOperational()) {
                    Toast.makeText(MainActivity.this, "Face Detector won't work your device", Toast.LENGTH_SHORT).show();
                return;
                }
                Frame frame = new Frame.Builder().setBitmap(bm).build();
                SparseArray<Face> sparseArray = detector.detect(frame);
                for (int i=0; i<sparseArray.size();i++) {
                    Face face = sparseArray.valueAt(i);
                    float x1 = face.getPosition().x;
                    float y1 = face.getPosition().y;
                    float x2 = x1+face.getWidth();
                    float y2 = x1+face.getHeight();
                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF, 2, 2, boxPaint);
                }
                imageView.setImageDrawable(new BitmapDrawable(getResources(), tempbm));
            }
        });
    }
    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
             istr = assetManager.open(strName);

        } catch(IOException e){
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

}