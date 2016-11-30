package com.gopal.icu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by gopal on 30/11/16.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginScreen";

    private Animation alphaAnim;
    private boolean flag = true;
    private AnimatorSet mClockAnimSet = new AnimatorSet().setDuration(400);
    private AnimatorSet mAntiClockAnimSet = new AnimatorSet().setDuration(400);
    private static final float CLOCK_ROTATION = 0f;
    private static final float ANTI_CLOCK_ROTATION = 45f;

    private CardView mCard;
    private FrameLayout framlayoutGo, framlayoutDone, framlayoutCircle;
    private LinearLayout linerlayoutReg;
    private RelativeLayout reltivlayoutReg, reltivlayoutMain;
    private ImageView ivPlus;
    private EditText edtUsername, edtPsswd;
    private ImageButton imgBtnToggle;
    private double mCurrntLocLat, mCurrntLocLng;
    private CurrentLocationFetcher mLocation;
    private boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLocation = new CurrentLocationFetcher(getApplicationContext());

        mCard = (CardView) findViewById(R.id.cardview);
        framlayoutGo = (FrameLayout) findViewById(R.id.layoutGo);
        framlayoutDone = (FrameLayout) findViewById(R.id.layoutDone);
        framlayoutCircle = (FrameLayout) findViewById(R.id.frameCircle);
        linerlayoutReg = (LinearLayout) findViewById(R.id.linearReg);
        reltivlayoutReg = (RelativeLayout) findViewById(R.id.reltvlayoutReg);
        reltivlayoutMain = (RelativeLayout) findViewById(R.id.mainlayout);
        ivPlus = (ImageView) findViewById(R.id.iv_plus);
        edtUsername = (EditText) findViewById(R.id.etd_username);
        edtPsswd = (EditText) findViewById(R.id.etd_password);
        imgBtnToggle = (ImageButton) findViewById(R.id.ivBtnToggle);

        alphaAnim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        final OvershootInterpolator interpolator = new OvershootInterpolator();

        final ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(ivPlus, "rotation", ANTI_CLOCK_ROTATION, CLOCK_ROTATION);
        final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(ivPlus, "rotation", CLOCK_ROTATION, ANTI_CLOCK_ROTATION);

        collapseAnimator.setInterpolator(interpolator);
        expandAnimator.setInterpolator(interpolator);

        mClockAnimSet.play(expandAnimator);
        mAntiClockAnimSet.play(collapseAnimator);
        framlayoutGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrntLocLat = mLocation.getLat();
                mCurrntLocLng = mLocation.getLong();
                Log.d(TAG,"Lat: "+mCurrntLocLat);
                Log.d(TAG,"Lng: "+mCurrntLocLng);
                String s = edtUsername.getText().toString();
                if (!s.equals("")){
                    showDone(s,mCurrntLocLat,mCurrntLocLng);
                }else {
                    Snackbar.make(reltivlayoutMain,"Please enter Username!!!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void pssdOnOff(View view){

        if (isVisible) {
            edtPsswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgBtnToggle.setBackground(getDrawable(R.drawable.ic_visibility_off));
            isVisible = false;
        }else {
            edtPsswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgBtnToggle.setBackground(getDrawable(R.drawable.ic_visibility));
            isVisible = true;
        }
        edtPsswd.setSelection(edtPsswd.length());

    }

    public void showDone(final String title, final double lat, final double lng) {

        int x = framlayoutGo.getMeasuredWidth()/2;
        int y = framlayoutGo.getMeasuredHeight()/2;

        int hypotenuse = Math.max(framlayoutGo.getWidth(), framlayoutGo.getHeight())/2;


        FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams) framlayoutDone.getLayoutParams();
        parameters.height = framlayoutGo.getHeight();
        framlayoutDone.setLayoutParams(parameters);

        Animator anim = ViewAnimationUtils.createCircularReveal(framlayoutDone, x, y, 0, hypotenuse);
        anim.setDuration(700);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                framlayoutDone.setVisibility(View.VISIBLE);
                mCurrntLocLat = mLocation.getLat();
                mCurrntLocLng = mLocation.getLong();
                Log.d(TAG,"Lat: "+mCurrntLocLat);
                Log.d(TAG,"Lng: "+mCurrntLocLng);

                Intent intentMap = new Intent(getApplicationContext(), MapsActivity.class);
                intentMap.putExtra(Utility.NAME,title);
                intentMap.putExtra(Utility.LAT, lat);
                intentMap.putExtra(Utility.LNG, lng);
                startActivity(intentMap);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        framlayoutDone.setVisibility(View.VISIBLE);
        anim.start();


    }

    public void launchRegScreen(View view) {

        int x = mCard.getRight();
        int y = mCard.getTop();

        int hypotenuse = (int) Math.hypot(mCard.getWidth(), mCard.getHeight());

        if (flag) {

            RelativeLayout.LayoutParams parameters = (RelativeLayout.LayoutParams) linerlayoutReg.getLayoutParams();
            parameters.height = mCard.getHeight();
            linerlayoutReg.setLayoutParams(parameters);

            Animator anim = ViewAnimationUtils.createCircularReveal(linerlayoutReg, x, y, 0, hypotenuse);
            anim.setDuration(400);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    framlayoutCircle.setVisibility(View.GONE);

                    mAntiClockAnimSet.cancel();
                    mClockAnimSet.start();

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    reltivlayoutReg.setVisibility(View.VISIBLE);
                    reltivlayoutReg.startAnimation(alphaAnim);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            linerlayoutReg.setVisibility(View.VISIBLE);
            anim.start();

            flag = false;
        } else {

            Animator anim = ViewAnimationUtils.createCircularReveal(linerlayoutReg, x, y, hypotenuse, 0);
            anim.setDuration(400);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    reltivlayoutReg.setVisibility(View.GONE);
                    reltivlayoutReg.clearAnimation();
                    linerlayoutReg.setVisibility(View.GONE);
                    framlayoutCircle.setVisibility(View.VISIBLE);
                    mAntiClockAnimSet.start();
                    mClockAnimSet.cancel();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();
            flag = true;
        }
    }
}
