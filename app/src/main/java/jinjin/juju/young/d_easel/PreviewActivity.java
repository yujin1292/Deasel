package jinjin.juju.young.d_easel;

import androidx.appcompat.app.AppCompatActivity;

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

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "AndroidOpenCv";

    private Bitmap original_p;
    private Bitmap canvas_p;
    private Bitmap meanshift_p;

    private ImageView meanshift;
    private ImageView canvas;
    private TextView textView;

    private boolean mIsOpenCVReady = false;

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

        Button buttonO = (Button) findViewById(R.id.button_o) ;
        buttonO.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                meanshift_p = original_p.copy(original_p.getConfig(),true);

                meanshift.setImageBitmap(meanshift_p);

                Mat src2 = new Mat();
                Utils.bitmapToMat(meanshift_p, src2);
                Mat edge = new Mat();
                detectEdgeJNI(src2.getNativeObjAddr(), edge.getNativeObjAddr(), 75, 175);
                Utils.matToBitmap(edge, canvas_p);

                //처리한 이미지로 변경
                canvas.setImageBitmap(canvas_p);
                textView.setText("original");

            }
        }) ;


        Button buttonA = (Button) findViewById(R.id.button_a) ;
        buttonA.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(original_p != null){
                    ProcessingUsingJNL(1, 20);
                }
            }
        }) ;


        Button buttonB = (Button) findViewById(R.id.button_b) ;
        buttonB.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(original_p != null){
                    ProcessingUsingJNL(3, 20);
                }
            }
        }) ;


        Button buttonC = (Button) findViewById(R.id.button_c) ;
        buttonC.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(original_p != null){
                    ProcessingUsingJNL(6, 20.0);
                }
            }
        }) ;


        Button buttonD = (Button) findViewById(R.id.button_d) ;
        buttonD.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(original_p != null){
                    ProcessingUsingJNL(9, 20.0);
                }
            }
        }) ;


        // start 버튼

        Button startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;

    }


}
