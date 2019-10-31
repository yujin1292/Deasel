package jinjin.juju.young.d_easel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SelectActivity extends AppCompatActivity {

    private static final String TAG = "AndroidOpenCv";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int MY_PERMISSIONS_REQUEST_GALLERY = 200;
    private static final int REQ_CODE_CAMERA_IMAGE = 123;
    private static final int REQ_CODE_SELECT_IMAGE = 456;

    private Bitmap original;
    private Bitmap input;

    boolean finish = false;

    private boolean mIsOpenCVReady = false;


    // 이 Activity 에서 processing 한 후에 bitmap intent 로 전달.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(this,"카메라 권한이 필요합니다..",Toast.LENGTH_LONG).show();

            }
        }

        permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"사용을 위해 읽기 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_GALLERY);
                Toast.makeText(this,"사용을 위해 읽기 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            }
        }




    }

    //버튼 리스너
    public void TakePicClicked(View view) {
        //카메라 열고 파일 처리.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_CODE_CAMERA_IMAGE);

    }

    public void GetPicClicked(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

    }


    //엣지 디텍트
    public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2);

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }


    public void detectEdgeUsingJNI() {

        if (!mIsOpenCVReady) {
            return;
        }
        Mat src = new Mat();
        Utils.bitmapToMat(input, src);
        Mat edge = new Mat();
        detectEdgeJNI(src.getNativeObjAddr(), edge.getNativeObjAddr(), 50, 150);
        Utils.matToBitmap(edge, input);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            mIsOpenCVReady = true;
        }
    }

    public void goPreview(String string){


        Intent intent2 = new Intent(this, PreviewActivity.class);
        intent2.putExtra("where",string);
/*

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        intent2.putExtra("original",bytes);*/

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        input.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
        byte[] bytes2 = stream2.toByteArray();
        intent2.putExtra("image",bytes2);

        startActivity(intent2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQ_CODE_CAMERA_IMAGE && resultCode == Activity.RESULT_OK) {

            try {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                original= imageBitmap;
                input = original.copy(original.getConfig(),true);


                if (input != null) { // 가져온 이미지가 null 이 아니면 엣지 디텍팅
                    detectEdgeUsingJNI();
                    finish = true;

                    goPreview("From camera");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {

                String path = getImagePathFromURI(data.getData());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                original =  BitmapFactory.decodeFile(path, options);
                input =  BitmapFactory.decodeFile(path, options);

                if (input!= null) { // 가져온 이미지가 null 이 아니면 엣지 디텍팅
                    detectEdgeUsingJNI();
                    finish = true;

                    goPreview("From Gallery");

                   /* Intent intent2 = new Intent(this, PreviewActivity.class);
                    intent2.putExtra("where","From Gallery");
                   // intent2.putExtra("original",original);
                   // intent2.putExtra("image",input);
                    startActivity(intent2);*/


                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getImagePathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imgPath = cursor.getString(idx);
            cursor.close();
            return imgPath;
        }
    }



    public void onDestroy() {
        super.onDestroy();
/*
        input.recycle();
        if (input != null) {
            input = null;
        }*/
    }



}
