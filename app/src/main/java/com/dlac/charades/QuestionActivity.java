package com.dlac.charades;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dlac.charades.models.Category;
import com.dlac.charades.models.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView timer;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        timer = (TextView) findViewById(R.id.textview_timer);
        questions = loadQuestions();

        setListeners();
        initTimer();
    }

    protected void setListeners()
    {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void initTimer()
    {
        //TODO: Make game length configurable
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished)
            {
                timer.setText("Time remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                //TODO: Handle end of the game
                timer.setText("done!");
            }
        }.start();
    }

    private List<Question> loadQuestions()
    {
        List<Question> questionList = new ArrayList<>();
        Intent categoryIntent = getIntent();
        Category category = categoryIntent.getParcelableExtra("category");
        if (category != null)
        {
            DBHandler dbHandler = new DBHandler(this);
            questionList = dbHandler.getQuestions(category);
        }
        return  questionList;
    }

    @Override
    public void onDestroy()
    {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        sensorManager.unregisterListener(this);
        super.onBackPressed();
    }

    @Override
    public void onResume()
    {
        setListeners();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    float[] inclineGravity = new float[3];
    float[] gravity;
    float[] geomagnetic;
    float orientation[] = new float[3];
    float pitch;
    float roll;

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            gravity = event.values;
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            geomagnetic = event.values;

            if (isTiltDownward())
            {
                getWindow().getDecorView().setBackgroundColor(Color.RED);
                //TODO: Handle pass
            }
            else if (isTiltUpward())
            {
                getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                //TODO: Handle right answer
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private boolean isTiltDownward()
    {
        if (gravity != null && geomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = gravity.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 170))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    private boolean isTiltUpward()
    {
        if (gravity != null && geomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = gravity.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                // Normalize the accelerometer vector
                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 30 && inclination < 40))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }

}
