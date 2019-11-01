package jinjin.juju.young.d_easel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
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

    public native void MeanShiftFilteringJNI(long inputImage,  long outputImage,  int sp, int sr);

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }
    public void ProcessingUsingJNL(){
        if (!mIsOpenCVReady) {
            return;
        }
        //mean shift 연산

        Mat src = new Mat();
        Utils.bitmapToMat(meanshift_p, src);
        Mat mean = new Mat();
        MeanShiftFilteringJNI(src.getNativeObjAddr(), mean.getNativeObjAddr(), 3, 10);
        Utils.matToBitmap(mean, meanshift_p);

        // 처리한 이미지 변경
        meanshift.setImageBitmap(meanshift_p);

        //edge detect 연산

        Mat src2 = new Mat();
        Utils.bitmapToMat(meanshift_p, src2);
        Mat edge = new Mat();
        detectEdgeJNI(src2.getNativeObjAddr(), edge.getNativeObjAddr(), 50, 150);
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

        // 원본 이미지 가져오기
        byte[] bytes = intent.getByteArrayExtra("image");

        if(bytes != null){


            original_p = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            // 카피
            canvas_p = original_p.copy(original_p.getConfig(),true);
            meanshift_p = original_p.copy(original_p.getConfig(),true);
            textView.setText(s);

            meanshift.setImageBitmap(meanshift_p);

            //이미지 처리
            ProcessingUsingJNL();

        }



    }



}
