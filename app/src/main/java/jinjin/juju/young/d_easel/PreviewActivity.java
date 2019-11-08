package jinjin.juju.young.d_easel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class PreviewActivity extends AppCompatActivity {

    ProgressDialog dlg ;

    private Realm realm = Realm.getDefaultInstance();

    private static final String TAG = "AndroidOpenCv";

    private Bitmap original_p;
    private Bitmap canvas_p;
    private Bitmap meanshift_p;

    private ImageView meanshift;
    private ImageView canvas;
    private TextView textView;

    private boolean mIsOpenCVReady = false;
    private int SELECT = 100;

    //엣지 디텍트
    public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2);

    public native void MeanShiftFilteringJNI(long inputImage,  long outputImage,  double sp, double sr);

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }
    public void ProcessingUsingJNL(double i, double j ){


        if (!mIsOpenCVReady) {
            return;
        }
        if( i == 0 ){
            textView.setText("original img");
            meanshift_p = original_p.copy(original_p.getConfig(),true);
            meanshift.setImageBitmap(meanshift_p);
        }
        else{

            double sp = i;
            double sr = j;
            textView.setText("sp : " + sp + " sr : " + sr);

            //mean shift 연산
            Mat src = new Mat();
            Utils.bitmapToMat(original_p, src);
            Mat mean = new Mat();
            MeanShiftFilteringJNI(src.getNativeObjAddr(), mean.getNativeObjAddr(), sp, sr);
            Utils.matToBitmap(mean, meanshift_p);

            // 처리한 이미지 변경
            meanshift.setImageBitmap(meanshift_p);

        }

        //edge detect 연산

        Mat src2 = new Mat();
        Utils.bitmapToMat(meanshift_p, src2);
        Mat edge = new Mat();
        detectEdgeJNI(src2.getNativeObjAddr(), edge.getNativeObjAddr(), 75, 175);
        Utils.matToBitmap(edge, canvas_p);

        //처리한 이미지로 변경
        canvas.setImageBitmap(canvas_p);


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        dlg = new ProgressDialog(this);
        dlg.setMessage("Loading...");
        dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (OpenCVLoader.initDebug()) {
            mIsOpenCVReady = true;
        }

        meanshift = findViewById(R.id.mean_shift);
        canvas = findViewById(R.id.edge_canvas);
        textView = findViewById(R.id.where);

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
            textView.setText(s);

            meanshift.setImageBitmap(meanshift_p);

            //이미지 처리
            ProcessingUsingJNL(3, 20.0);


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
                            ProcessingUsingJNL(0, 20.0);
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
                            ProcessingUsingJNL(3, 20.0);
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
                            ProcessingUsingJNL(6, 20.0);
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
                            ProcessingUsingJNL(9 ,20.0);
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
                            ProcessingUsingJNL(12, 20.0);
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
                realm.beginTransaction();
                Number maxValue = realm.where(ImageDB.class).max("id");
                long pk = (maxValue != null) ? maxValue.longValue() + 1 : 0;
                ImageDB imageDB = realm.createObject(ImageDB.class, pk);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                original_p.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                imageDB.setImage(bytes);

                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                meanshift_p.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                byte[] bytes1 = stream1.toByteArray();
                imageDB.setMean_shift(bytes1);

                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                canvas_p.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                byte[] bytes2 = stream2.toByteArray();
                imageDB.setBackground(bytes2);

                realm.commitTransaction();

                Intent intent = new Intent(PreviewActivity.this, MasterpieceActivity.class);
                intent.putExtra("id", pk);
                startActivity(intent);




            }
        }) ;

    }



}




