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
            "♡ 원하는 사진을 나만의 감각으로 색칠해 보세요 ♡",
            "카메라버튼을 누르고 사진을 촬영하거나 \n갤러리 버튼을 눌러 원하는 사진을 선택하세요!",
            R.drawable.one_intro ,
            Color.parseColor("#FFDDA0DD")
        )
        val second_fragment = AppIntroFragment.newInstance(
            "♡ 원하는 스케치북을 선택하세요 ♡",
            " ㄷ , ㅣ , ㅈ , ㅔ , ㄹ  버튼을 이용해\n원하는 도안을 고르세요!",
            R.drawable.two_intro,
            Color.parseColor("#FFDDA0DD")
        )
        val third_fragment = AppIntroFragment.newInstance(
            "♡ Coloring Book에서 그리던 그림을 확인할 수 있어요 ♡",
            "그림을 클릭하면 이어서 그릴 수 있어요!",
            R.drawable.three_intro,
            Color.parseColor("#FFDDA0DD")
        )
        val fourth_fragment = AppIntroFragment.newInstance(
            "♡ 그림을 꾹 누르면 \n 삭제할수 있어요 ♡",
            "Coloring Book 메뉴 에서 선택할 수 있어요!",
            R.drawable.four_intro,
            Color.parseColor("#FFDDA0DD")
        )
        val fifth_fragment = AppIntroFragment.newInstance(
            "♡ 이제 접근을 허용해주세요 ♡",
            "접근을 허용하지 않으면 D-Easel을 이용할 수 없어요",
            R.drawable.five_intro,
            Color.parseColor("#FFDDA0DD")
        )


        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
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
        setBarColor(Color.parseColor("#FFD8BFD8"))
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