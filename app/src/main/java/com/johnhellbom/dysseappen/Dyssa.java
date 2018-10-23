package com.johnhellbom.dysseappen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by John Hellbom on 2016-03-06.
 */
public class Dyssa extends AppCompatActivity implements Animation.AnimationListener, NumberPicker.OnValueChangeListener, DyssaActiveQuestionCaller {

    Animation ageAnimBounce;
    Animation genAnimBounce;

    OffsetButton questionButton;
    OffsetButton allQuestionsButton;
    OffsetButton askButton;

    Integer activeQuestionID = -1;
    String activeQuestion = "";

    Boolean answeredActiveQuestion = false;

    public String newsCallerData;

    static Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyssa);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dyssa_toolbar);
        setSupportActionBar(toolbar);

        newsCallerData = getIntent().getStringExtra("newsCallerData");

        ageAnimBounce = AnimationUtils.loadAnimation(DysseAppenApplication.getAppContext(), R.anim.fastbounce);
        ageAnimBounce.setAnimationListener(this);

        genAnimBounce = AnimationUtils.loadAnimation(DysseAppenApplication.getAppContext(), R.anim.fastbounce);
        genAnimBounce.setAnimationListener(this);

        final LinearLayout ageDrop = (LinearLayout) findViewById(R.id.dyssa_age_drop);
        ageDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ageDrop.startAnimation(ageAnimBounce);
            }
        });

        final LinearLayout genDrop = (LinearLayout) findViewById(R.id.dyssa_gen_drop);
        genDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                genDrop.startAnimation(genAnimBounce);
            }
        });

        TextView ageText = (TextView) findViewById(R.id.dyssa_age_drop_text);
        TextView genText = (TextView) findViewById(R.id.dyssa_gendrop_text);

        String ageDisplay = DroidUtils.ReadString("age_displayval");
        if(ageDisplay != null)
        {
            ageText.setText(ageDisplay);
        }

        String genDisplay = DroidUtils.ReadString("gen_displayval");
        if(genDisplay != null)
        {
            genText.setText(genDisplay);
        }

        questionButton = (OffsetButton) findViewById(R.id.dyssa_question_button);
        questionButton.setVisibility(View.INVISIBLE);
        questionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!answeredActiveQuestion) {
                    Intent intent = new Intent(Dyssa.this, DyssaQuestion.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Dyssa.this, DyssaResult.class);
                    intent.putExtra("activeQuestionID", activeQuestionID);
                    intent.putExtra("activeQuestion", activeQuestion);
                    startActivity(intent);
                }
            }
        });

        allQuestionsButton = (OffsetButton) findViewById(R.id.dyssa_all_button);
        allQuestionsButton.setVisibility(View.INVISIBLE);
        allQuestionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Dyssa.this, DyssaAllQuestions.class);
                startActivity(intent);
            }
        });

        askButton = (OffsetButton) findViewById(R.id.dyssa_suggest_button);
        askButton.setVisibility(View.INVISIBLE);
        askButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Dyssa.this, DyssaAsk.class);
                startActivity(intent);
            }
        });

        String url = "http://dysseappen.se.preview.binero.se/ws/daws.asmx/Dyssa_SelectActiveQuestion";
        new DyssaQuestionTask(Dyssa.this, url).execute();
    }

    public void showAgePicker()
    {
        final Dialog d = new Dialog(Dyssa.this);
        d.setTitle("Hur gammal är du?");
        d.setContentView(R.layout.numberpicker_dialog);

        TextView title = (TextView) d.findViewById(R.id.dTitle);
        title.setText("Hur gammal är du?");

        Button okButton = (Button) d.findViewById(R.id.np_okbutton);
        Button cancelButton = (Button) d.findViewById(R.id.np_cancelbutton);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.np);
        np.setMaxValue(99);
        np.setMinValue(0);

        final ArrayList<String> displayValues = new ArrayList<>();
        for(int i = 1; i <= 100; i++) {
            displayValues.add(i + " år");
        }

        Object[] objVal = displayValues.toArray();
        String[] strVal = Arrays.copyOf(objVal, objVal.length, String[].class);

        np.setDisplayedValues(strVal);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        Integer defVal = 11;

        final TextView ageTextView = (TextView) findViewById(R.id.dyssa_age_drop_text);

        Integer storeVal = DroidUtils.ReadInteger("age_defval");
        if(storeVal > -1)
        {
            defVal = storeVal;
        }

        np.setValue(defVal);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageTextView.setText(displayValues.get(np.getValue()));
                ageTextView.setTag(np.getValue());
                DroidUtils.SaveInteger("age_defval", np.getValue());
                DroidUtils.SaveString(Dyssa.this, "age_displayval", ageTextView.getText().toString());
                d.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }

    public void showGenPicker()
    {
        final Dialog d = new Dialog(Dyssa.this);
        d.setTitle("Är du tjej/kille/annat?");
        d.setContentView(R.layout.numberpicker_dialog);

        TextView title = (TextView) d.findViewById(R.id.dTitle);
        title.setText("Är du tjej/kille/annat?");

        Button okButton = (Button) d.findViewById(R.id.np_okbutton);
        Button cancelButton = (Button) d.findViewById(R.id.np_cancelbutton);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.np);
        np.setMaxValue(2);
        np.setMinValue(0);

        final ArrayList<String> displayValues = new ArrayList<>();
        displayValues.add("Tjej");
        displayValues.add("Kille");
        displayValues.add("Annat");

        Object[] objVal = displayValues.toArray();
        String[] strVal = Arrays.copyOf(objVal, objVal.length, String[].class);

        np.setDisplayedValues(strVal);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        Integer defVal = 0;

        final TextView genTextView = (TextView) findViewById(R.id.dyssa_gendrop_text);

        Integer storeVal = DroidUtils.ReadInteger("gen_defval");
        if(storeVal > -1)
        {
            defVal = storeVal;
        }

        np.setValue(defVal);

        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                genTextView.setText(displayValues.get(np.getValue()));
                genTextView.setTag(np.getValue());
                DroidUtils.SaveInteger("gen_defval", np.getValue());
                DroidUtils.SaveString(Dyssa.this, "gen_displayval", genTextView.getText().toString());
                d.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }

    public void callBackActiveQuestion(Pair<Integer, String> result) {

        activeQuestionID = result.first;
        activeQuestion = result.second;

        if(DroidUtils.ReadString("dyssa_question_" + result.first) != null) {
            answeredActiveQuestion = true;
        }

        allQuestionsButton.setVisibility(View.VISIBLE);
        questionButton.setVisibility(View.VISIBLE);
        askButton.setVisibility(View.VISIBLE);

        if(newsCallerData != null)
        {
            Intent intent;
            switch (newsCallerData)
            {
                case "question":
                    if(!answeredActiveQuestion) {
                        intent = new Intent(Dyssa.this, DyssaQuestion.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(Dyssa.this, DyssaResult.class);
                        intent.putExtra("activeQuestionID", activeQuestionID);
                        intent.putExtra("activeQuestion", activeQuestion);
                        startActivity(intent);
                    }
                    break;

                case "ask":
                    intent = new Intent(Dyssa.this, DyssaAsk.class);
                    startActivity(intent);
                    break;

                case "all":
                    intent = new Intent(Dyssa.this, DyssaAllQuestions.class);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    }

    // region @ NumberPicker.OnValueChangeListener
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    }
    // endregion

    // region @ Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    // endregion

    //region @ AnimationListener

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation == ageAnimBounce)
        {
            showAgePicker();
        } else if(animation == genAnimBounce)
        {
            showGenPicker();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    // endregion
}

