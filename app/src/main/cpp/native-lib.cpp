#include <jni.h>
#include <string>

#include <opencv2/opencv.hpp>



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

}