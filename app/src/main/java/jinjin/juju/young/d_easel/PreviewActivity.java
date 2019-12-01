package jinjin.juju.young.d_easel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class PreviewActivity extends BaseActivity {

    ProgressDialog dlg ;


    private static final String TAG = "AndroidOpenCv";

    long pk;

    private Bitmap original_p;
    private Bitmap canvas_p;
    private Bitmap meanshift_p;
    private Bitmap canvas_p_a;

    private ImageView meanshift;
    private ImageView canvas;

    private boolean mIsOpenCVReady = false;
    private int SELECT = 100;

    //엣지 디텍트
    public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2);

    public native void MeanShiftFilteringJNI(long inputImage,  long outputImage,  double sp, double sr);


    public native void GaussianBlurJNI(long inputImage,  long outputImage,  int sp);

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }
    public void ProcessingUsingMeanShift(double i, double j ){


        if (!mIsOpenCVReady) {
            return;
        }
        if( i == 0 ){
            meanshift_p = original_p.copy(original_p.getConfig(),true);
            meanshift.setImageBitmap(meanshift_p);
        }
        else{

            double sp = i;
            double sr = j;

            //mean shift 연산
            Mat src = new Mat();
            Utils.bitmapToMat(original_p, src);
            Mat mean = new Mat();
            MeanShiftFilteringJNI(src.getNativeObjAddr(), mean.getNativeObjAddr(), sp, sr);
            Utils.matToBitmap(mean, meanshift_p);

            // 처리한 이미지 변경
            meanshift.setImageBitmap(meanshift_p);

        }

        //edge detect 연산 후 canvas_p에 저장
        Mat src2 = new Mat();
        Utils.bitmapToMat(meanshift_p, src2);
        Mat edge = new Mat();
        detectEdgeJNI(src2.getNativeObjAddr(), edge.getNativeObjAddr(), 75, 175);
        Utils.matToBitmap(edge, canvas_p);

        canvas.setImageBitmap(canvas_p);


        int sW = canvas_p.getWidth();
        int sH = canvas_p.getHeight();

        int[] pixels = new int[sW*sH];
        canvas_p.getPixels(pixels, 0, sW, 0, 0, sW, sH);
        for (int jj =0 ;jj < pixels.length; jj++) {
            if (pixels[jj] == Color.WHITE)
                pixels[jj] = Color.TRANSPARENT;
        }

        canvas_p_a  = Bitmap.createBitmap(pixels, 0, sW, sW, sH, Bitmap.Config.ARGB_8888);

        //처리한 이미지로 변경
        canvas.setImageBitmap(canvas_p_a);


    }

    public void ProcessingUsingGaussain(int i){

        if (!mIsOpenCVReady) {
            return;
        }
        if( i == 0 ){
            meanshift_p = original_p.copy(original_p.getConfig(),true);
            meanshift.setImageBitmap(meanshift_p);
        }
        else{


            // 가우시안 블러링
            Mat src = new Mat();
            Utils.bitmapToMat(original_p, src);
            Mat mean = new Mat();
            GaussianBlurJNI(src.getNativeObjAddr(), mean.getNativeObjAddr(), i);
            Utils.matToBitmap(mean, meanshift_p);

            // 처리한 이미지 변경
            meanshift.setImageBitmap(meanshift_p);

        }

        //edge detect 연산 후 canvas_p에 저장
        Mat src2 = new Mat();
        Utils.bitmapToMat(meanshift_p, src2);
        Mat edge = new Mat();
        detectEdgeJNI(src2.getNativeObjAddr(), edge.getNativeObjAddr(), 75, 175);
        Utils.matToBitmap(edge, canvas_p);

        canvas.setImageBitmap(canvas_p);


        int sW = canvas_p.getWidth();
        int sH = canvas_p.getHeight();

        int[] pixels = new int[sW*sH];
        canvas_p.getPixels(pixels, 0, sW, 0, 0, sW, sH);
        for (int jj =0 ;jj < pixels.length; jj++) {
            if (pixels[jj] == Color.WHITE)
                pixels[jj] = Color.TRANSPARENT;
        }

        canvas_p_a  = Bitmap.createBitmap(pixels, 0, sW, sW, sH, Bitmap.Config.ARGB_8888);

        //처리한 이미지로 변경
        canvas.setImageBitmap(canvas_p_a);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);


        Log.d("List","Befor"+ getactList().toString());
        getactList().add(this);

        Log.d("List",getactList().toString());


        dlg = new ProgressDialog(this);
        dlg.setMessage("잠시만 기다려주세요...");
        dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        if (OpenCVLoader.initDebug()) {
            mIsOpenCVReady = true;
        }

        meanshift = findViewById(R.id.mean_shift);
        canvas = findViewById(R.id.edge_canvas);

        Intent intent = getIntent();

        //스트링 데이터 가져오기
        String s = intent.getStringExtra("where");

        // 원본 이미지 가져오기 & 이미지 처리
        byte[] bytes = intent.getByteArrayExtra("image");

        if(bytes != null){


            original_p = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            // 카피
            canvas_p = original_p.copy(original_p.getConfig(),true);
            meanshift_p = original_p.copy(original_p.getConfig(),true);

            meanshift.setImageBitmap(meanshift_p);

            //이미지 처리
            ProcessingUsingMeanShift(3, 20.0);


        }


        // 이미지 처리 버튼 리스너들

        Button buttonO = findViewById(R.id.button_o);
        buttonO.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.show();
                new Thread(new Runnable() {
                    @Override public void run()
                    { // TODOAuto-generated method stub
                        if(original_p != null){
                            ProcessingUsingMeanShift(0, 20.0);
                        }
                        dlg.dismiss();
                    }
                }).start();


            }
        }) ;


        Button buttonA = findViewById(R.id.button_a);
        buttonA.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.show();
                new Thread(new Runnable() {
                    @Override public void run()
                    { // TODOAuto-generated method stub
                        if(original_p != null){
                            ProcessingUsingMeanShift(3, 20.0);
                        }
                        dlg.dismiss();
                    }
                }).start();

            }
        }) ;


        Button buttonB = findViewById(R.id.button_b);
        buttonB.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.show();
                new Thread(new Runnable() {
                    @Override public void run()
                    { // TODOAuto-generated method stub
                        if(original_p != null){
                            ProcessingUsingMeanShift(6, 20.0);
                        }
                        dlg.dismiss();
                    }
                }).start();

            }
        }) ;


        Button buttonC = findViewById(R.id.button_c);
        buttonC.setOnClickListener(new Button.OnClickListener() {

             @Override
             public void onClick(View view) {
                 dlg.show();
                 new Thread(new Runnable() {
                     @Override public void run()
                     { // TODOAuto-generated method stub
                         if(original_p != null){
                             ProcessingUsingMeanShift(9.0 ,20.0);
                         }
                         dlg.dismiss();
                     }
                 }).start();


     }

        }) ;


        Button buttonD = findViewById(R.id.button_d);
        buttonD.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.show();
                new Thread(new Runnable() {
                    @Override public void run()
                    { // TODOAuto-generated method stub
                        if(original_p != null){
                            ProcessingUsingMeanShift(12, 20.0);
                        }
                        dlg.dismiss();
                    }
                }).start();


            }
        }) ;


        Button buttonG = findViewById(R.id.button_g);
        buttonG.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                dlg.show();
                new Thread(new Runnable() {
                    @Override public void run()
                    { // TODOAuto-generated method stub
                        if(original_p != null){
                            ProcessingUsingGaussain(3);
                        }
                        dlg.dismiss();
                    }
                }).start();
            }
        }) ;

        // start 버튼

        Button startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                dlg.show();
                new Thread(new Runnable() {
                    @Override public void run()
                    { // TODOAuto-generated method stub


                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();
                        Number maxValue = realm.where(ImageDB.class).max("id");
                        pk = (maxValue != null) ? maxValue.longValue() + 1 : 0;
                        ImageDB imageDB = realm.createObject(ImageDB.class, pk);

                        //원본 이미지 저장
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        original_p.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        imageDB.setOriginal(bytes);

                        //mean_shift 처리 영상 저장
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        meanshift_p.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                        byte[] bytes1 = stream1.toByteArray();
                        imageDB.setMean_shift(bytes1);

                        //edge 영상 저장
                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                        canvas_p_a.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                        byte[] bytes2 = stream2.toByteArray();
                        imageDB.setBackground(bytes2);

                        //lines 투명 값
                        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                        Bitmap temp = Bitmap.createBitmap(canvas_p_a.getWidth(), canvas_p_a.getHeight(),Bitmap.Config.ARGB_8888);
                        temp.compress(Bitmap.CompressFormat.PNG, 100, stream3);
                        byte[] bytes3 = stream3.toByteArray();
                        imageDB.setLines(bytes3);


                        //그림그린 영상 저장
                        imageDB.setImage(bytes2);


                        realm.commitTransaction();


                        realm.close();

                        dlg.dismiss();
                    }
                }).start();




                Intent intent = new Intent(PreviewActivity.this, MasterpieceActivity.class);

                intent.putExtra("id", pk);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);




            }
        }) ;

    }



    @Override
    protected void onDestroy() {

        Log.d("List","distroy"+ getactList().toString());
        super.onDestroy();
        getactList().remove(this);
    }
}


