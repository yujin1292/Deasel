package jinjin.juju.young.d_easel


import android.Manifest
import android.graphics.Color
import android.os.Bundle

import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntroFragment

class MyIntro : AppIntro2() {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val first_fragment = AppIntroFragment.newInstance(
            "직접 찍은 사진으로 그림을 그려보세요!",
            "카메라버튼을 누르고 사진을 촬영하거나  갤러리 버튼을 눌러보세요",
            R.drawable.temp ,
            Color.parseColor("#969696")
        )
        val second_fragment = AppIntroFragment.newInstance(
            "원하는 배경화면을 선택하세요",
            "숫자가 커질수록 상세합니다!",
            R.drawable.vila,
            Color.parseColor("#969696")
        )
        val third_fragment = AppIntroFragment.newInstance(
            "내 작품에서 그림을 선택해 색칠하세요!",
            "second page",
            R.drawable.deasel5,
            Color.parseColor("#969696")
        )
        val fourth_fragment = AppIntroFragment.newInstance(
            "꾹 누르면 삭제할수 있어요",
            "third page\n접근을 허용해주세요!",
            R.drawable.deasel5,
            Color.parseColor("#969696")
        )
        val fifth_fragment = AppIntroFragment.newInstance(
            "이제 접근을 허용해주세요",
            "Show me what you got",
            R.drawable.deasel5,
            Color.parseColor("#969696")
        )


        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(help_first.newInstance(R.layout.help_first_layout))
        addSlide(first_fragment)
        addSlide(second_fragment)
        addSlide(third_fragment)
        addSlide(fourth_fragment)
        addSlide(fifth_fragment)

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest

        //  addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS

        // Override bar/separator color
        setBarColor(Color.parseColor("#51E7DCFF"))
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // SHOW or HIDE the statusbar
        showStatusBar(true)

        // Edit the color of the nav bar on Lollipop+ devices
        //setNavBarColor(Color.parseColor("#3F51B5"));

        // Hide Skip/Done button
        showSkipButton(false)
        showDoneButton(true)

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest
        setVibrate(false)
        setVibrateIntensity(30)

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation() // OR
        //  setZoomAnimation(); // OR
        // setFlowAnimation(); // OR
        //   setSlideOverAnimation(); // OR
        //   setDepthAnimation(); // OR
        //  setCustomTransformer(yourCustomTransformer);

        // Permissions -- takes a permission and slide number
        askForPermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 5
        )
    }

    override fun onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    override fun onNextPressed() {
        // Do something when users tap on Next button.
    }

    override fun onDonePressed() {
        // Do something when users tap on Done button.
        finish()
    }

    override fun onSlideChanged() {
        // Do something when slide is changed
    }
}