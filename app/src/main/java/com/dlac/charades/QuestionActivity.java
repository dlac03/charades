package com.dlac.charades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dlac.charades.models.Category;
import com.dlac.charades.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView timer;
    private TextView questionDisplay;
    private TextView pointDisplay;
    private TextView progressDisplay;
    private Button restartButton;
    private Button finishButton;
    private Random random;
    private List<Question> questions;
    private ArrayList<String> totalPoints;
    private CountDownTimer countDownTimer;
    private int gameLength;
    private int totalQuestions;
    private int points = 0;
    private boolean canSkipToNextQuestion = true;
    private boolean skipToNextQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initGame();
    }

    private void initGame()
    {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        timer = (TextView) findViewById(R.id.textview_timer);
        questionDisplay = (TextView) findViewById(R.id.textview_question);
        pointDisplay = (TextView) findViewById(R.id.textview_points);
        progressDisplay = (TextView) findViewById(R.id.textview_progress);
        finishButton = (Button) findViewById(R.id.end_button);
        restartButton = (Button) findViewById(R.id.restart_button);
        random = new Random();
        questions = loadQuestions();
        totalQuestions = questions.size();
        totalPoints = new ArrayList<>();

        setButtonListeners();
        hideButtons();
        getGameLength();
        updatePointDisplay();
        updateProgressDisplay();
        setListeners();
        showNextQuestion();
        initTimer();
    }

    private void hideButtons()
    {
        restartButton.setVisibility(View.GONE);
        finishButton.setVisibility(View.GONE);
    }


    private void showButtons()
    {
        restartButton.setVisibility(View.VISIBLE);
        finishButton.setVisibility(View.VISIBLE);
    }

    private void setButtonListeners()
    {
        finishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showResults(view);
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                restartGame(view);
            }
        });
    }


    private void getGameLength() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gameLength = Integer.parseInt(sharedPreferences.getString("GAME_LENGTH","30000"));
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


    protected void setListeners()
    {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void initTimer()
    {
        countDownTimer = new CountDownTimer(gameLength, 1000) {

            public void onTick(long millisUntilFinished)
            {
                long seconds = millisUntilFinished / 1000;
                long minutes = 0;
                if (seconds > 60)
                {
                    minutes = seconds / 60;
                    seconds = seconds - (minutes *60);
                }

                String remainingTime = (minutes > 0 ? minutes + ":" : "") + seconds ;

                timer.setText(getString(R.string.time_remaining) + " " + remainingTime);
            }

            public void onFinish()
            {
                timer.setText(getString(R.string.time_noleft));
                endGame(false);
            }
        }.start();
    }

    private void stopTimer()
    {
        countDownTimer.cancel();
        endGame(false);
    }

    private void showNextQuestion() {
            if (questions != null && questions.size() > 0)
            {
                int idx = random.nextInt(questions.size());
                Question currentQuestion = questions.get(idx);
                questionDisplay.setText(currentQuestion.getText());
                questions.remove(idx);
                updateProgressDisplay();
            }
            else
                endGame(true);
    }

    private CountDownTimer nextQuestionTimer = new CountDownTimer((2000), 1000) {
        public void onTick(long millisUntilFinished) {  canSkipToNextQuestion = false;  }
        public void onFinish() {
            canSkipToNextQuestion = true;
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            if (skipToNextQuestion)
            {
                showNextQuestion();
                skipToNextQuestion = false;
            }
        }
    };

    private void updatePointDisplay()
    {
        pointDisplay.setText(getString(R.string.points) + points + "/" + totalQuestions);
    }

    private void updateProgressDisplay()
    {
        progressDisplay.setText( totalQuestions - questions.size() + getString(R.string.question_nr));
    }

    private void endGame(boolean stopTimer)
    {
        if (stopTimer)
            countDownTimer.cancel();
        totalPoints.add(totalPoints.size() + 1 + "." + getString(R.string.round_nr) + ": " + totalQuestions + "/" + points + " " + getString(R.string.point));
        questionDisplay.setText(getString(R.string.game_over));
        sensorManager.unregisterListener(this);
        showButtons();
    }

    private void showResults(View v)
    {
        Intent i = new Intent(this, ResultActivity.class);
        i.putStringArrayListExtra("points", totalPoints);
        v.getContext().startActivity(i);
    }

    private void restartGame(View v)
    {
        points = 0;
        questions = loadQuestions();
        showNextQuestion();
        updatePointDisplay();
        updateProgressDisplay();
        hideButtons();
        setListeners();
        initTimer();
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

            if (canSkipToNextQuestion)
            {
                if (isTiltDownward()) {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                    points++;
                    updatePointDisplay();
                    skipToNextQuestion = true;
                    nextQuestionTimer.start();
                } else if (isTiltUpward()) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    skipToNextQuestion = true;
                    nextQuestionTimer.start();
                }
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
