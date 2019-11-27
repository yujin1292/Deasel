#include <jni.h>
#include <string>
#include <vector>

#include <opencv2/opencv.hpp>

using namespace cv;


extern "C"
JNIEXPORT void JNICALL
Java_jinjin_juju_young_d_1easel_SelectActivity_detectEdgeJNI(JNIEnv *env, jobject thiz,
                                                             jlong input_image, jlong output_image,
                                                             jint th1, jint th2) {
    // TODO: implement detectEdgeJNI()
    cv::Mat &inputMat = *(cv::Mat *) input_image;
    cv::Mat &outputMat = *(cv::Mat *) output_image;

    cvtColor(inputMat, outputMat, cv::COLOR_RGB2GRAY);
    Canny(outputMat, outputMat, th1, th2);
    bitwise_not(outputMat,outputMat);

}extern "C"
JNIEXPORT void JNICALL
Java_jinjin_juju_young_d_1easel_PreviewActivity_detectEdgeJNI(JNIEnv *env, jobject thiz,
                                                              jlong input_image, jlong output_image,
                                                              jint th1, jint th2) {
    // TODO: implement detectEdgeJNI()
    cv::Mat &inputMat = *(cv::Mat *) input_image;
    cv::Mat &outputMat = *(cv::Mat *) output_image;

    cvtColor(inputMat, outputMat, cv::COLOR_RGB2GRAY);
    Canny(outputMat, outputMat, th1, th2);
    bitwise_not(outputMat, outputMat);

}
extern "C"
JNIEXPORT void JNICALL
Java_jinjin_juju_young_d_1easel_PreviewActivity_MeanShiftFilteringJNI(JNIEnv *env, jobject thiz,
                                                                      jlong input_image,
                                                                      jlong output_image,
                                                                      jdouble sp, jdouble sr) {
    // TODO: implement MeanShiftFilteringJNI()
    cv::Mat &inputMat = *(cv::Mat *) input_image;
    cv::Mat &outputMat = *(cv::Mat *) output_image;

    cvtColor(inputMat, outputMat,cv::COLOR_BGR2Luv); // output = input 의 8비트 3채널 변환사진

    cv::pyrMeanShiftFiltering(outputMat,outputMat,sp,sr);

    cvtColor(outputMat, outputMat,cv::COLOR_Luv2BGR);


}extern "C"
JNIEXPORT void JNICALL
Java_jinjin_juju_young_d_1easel_PreviewActivity_GaussianBlurJNI(JNIEnv *env, jobject thiz,
                                                                jlong input_image,
                                                                jlong output_image, jint sp
                                                                ) {
    // TODO: implement GaussianBlurJNI()
    cv::Mat &inputMat = *(cv::Mat *) input_image;
    cv::Mat &outputMat = *(cv::Mat *) output_image;

    GaussianBlur(inputMat, outputMat, Size(3, 3), 0);


}