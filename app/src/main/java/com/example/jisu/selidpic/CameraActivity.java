package com.example.jisu.selidpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "HelloCamera";
    private static final int IN_SAMPLE_SIZE = 8;

    private Camera mCamera;
    private ImageView mImage;
    private boolean mInProgress;
    private SurfaceHolder.Callback mSurfaceListener = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mCamera = Camera.open();
            Log.i(TAG, "Camera opened");
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(width, height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            Log.i(TAG, "Camera preview started");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mCamera.release();
            mCamera = null;
            Log.i(TAG, "Camera released");
        }
    };

    /*
    private View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mCamera!=null && mInProgress==false){
                mCamera.takePicture(mShutterListener, null, mPictureListener);
                mInProgress = true;
            }
        }
    };*/

    private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback(){
        public void onShutter(){
            Log.i(TAG,"onShutter");
        }
    };

    private Camera.PictureCallback mPictureListener = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG, "Picture Taken");
            if(data!=null){
                Log.i(TAG, "JPEG Picture Taken");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = IN_SAMPLE_SIZE;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                mImage.setImageBitmap(bitmap);
                camera.startPreview();
                mInProgress = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mImage = (ImageView) findViewById(R.id.image_view);
        SurfaceView surface = (SurfaceView) findViewById(R.id.surface_view);
        Canvas shoulderline = new Canvas();
        Paint pnt = new Paint();
        pnt.setStyle(Paint.Style.STROKE);
        pnt.setStrokeWidth(3);
        pnt.setColor(Color.BLACK);
        pnt.setAntiAlias(true);
        RectF rect = new RectF();
        rect.set(57, 57, 183, 183);
        shoulderline.drawArc(rect, 30, 120, false, pnt);
        surface.draw(shoulderline);
        SurfaceHolder holder = surface.getHolder();
        holder.addCallback(mSurfaceListener);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ImageButton button = (ImageButton) findViewById(R.id.shutter);
        button.setImageResource(R.drawable.arrow);
        button.setOnClickListener(mButtonListener);
    }
}
