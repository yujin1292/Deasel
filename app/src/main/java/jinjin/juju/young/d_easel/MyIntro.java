package jinjin.juju.young.d_easel;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class MyIntro extends AppIntro {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        AppIntroFragment first_fragment = AppIntroFragment.newInstance("welcome!", "first page", R.drawable.paper,Color.parseColor("#3F51B5"));
        AppIntroFragment second_fragment= AppIntroFragment.newInstance("First", "second page", R.drawable.b,Color.parseColor("#3F51B5"));
        AppIntroFragment third_fragment= AppIntroFragment.newInstance("second", "third page\n접근을 허용해주세요!", R.drawable.vila,Color.parseColor("#3F51B5"));
        AppIntroFragment fourth_fragment= AppIntroFragment.newInstance("Show What you got", "fourth page", R.drawable.icon,Color.parseColor("#3F51B5"));


        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(first_fragment);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);


        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest

      //  addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS

        // Override bar/separator color
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Edit the color of the nav bar on Lollipop+ devices
        //setNavBarColor(Color.parseColor("#3F51B5"));

        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest
        setVibrate(false);
        setVibrateIntensity(30);

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation(); // OR
      //  setZoomAnimation(); // OR
      // setFlowAnimation(); // OR
     //   setSlideOverAnimation(); // OR
     //   setDepthAnimation(); // OR
      //  setCustomTransformer(yourCustomTransformer);

        // Permissions -- takes a permission and slide number
       askForPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}