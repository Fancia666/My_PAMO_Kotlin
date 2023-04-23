package com.example.tipper.quiz;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArraySet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class Quiz extends Fragment {

    private static final String TAG = "FlagQuiz Activity";

    private static final int FLAGS_IN_QUIZ = 2;

    private String correctAnswer; // correct country for the current flag
    private int totalGuesses; // number of guesses made
    private int correctAnswers; // number of correct guesses
    private int guessRows; // number of rows displaying guess Buttons
    private SecureRandom random; // used to randomize the quiz
    private Handler handler; // used to delay loading next flag
    private Animation shakeAnimation; // animation for incorrect guess

    private LinearLayout quizLinearLayout; // layout that contains the quiz
    private TextView questionNumberTextView; // shows current question #

    private TextView healthTextView; // displays a question
    private LinearLayout[] guessLinearLayouts; // rows of answer Buttons
    private TextView answerTextView; // displays correct answer

    Map<String, Map<String, Boolean>> listOfQuestions = new HashMap<>();
    //

    // configures the MainActivityFragment when its View is created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view =
                inflater.inflate(R.layout.fragment_quiz2, container, false);

        random = new SecureRandom();
        handler = new Handler();

        // load the shake animation that's used for incorrect answers
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times

        // get references to GUI components
        quizLinearLayout =
                (LinearLayout) view.findViewById(R.id.quizLinearLayout);
        questionNumberTextView =
                (TextView) view.findViewById(R.id.questionNumberTextView);

        healthTextView = (TextView) view.findViewById(R.id.guessHealthTextView);

        guessLinearLayouts = new LinearLayout[4];
        guessLinearLayouts[0] =
                (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] =
                (LinearLayout) view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] =
                (LinearLayout) view.findViewById(R.id.row3LinearLayout);
        guessLinearLayouts[3] =
                (LinearLayout) view.findViewById(R.id.row4LinearLayout);
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        // configure listeners for the guess Buttons
        for (LinearLayout row : guessLinearLayouts) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }

        // set questionNumberTextView's text
        questionNumberTextView.setText(
                getString(R.string.question, 1, FLAGS_IN_QUIZ));
        return view; // return the fragment's view for display
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateGuessRows();
        resetQuiz();

    }

    // update guessRows based on value in SharedPreferences
    public void updateGuessRows() {
        // get the number of guess buttons that should be displayed
        guessRows = 4 / 2;

        // hide all quess button LinearLayouts
        for (LinearLayout layout : guessLinearLayouts)
            layout.setVisibility(View.GONE);

        // display appropriate guess button LinearLayouts
        for (int row = 0; row < guessRows; row++)
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
    }

    // set up and start the next quiz
    public void resetQuiz() {

        AssetManager assets = getActivity().getAssets();

        listOfQuestions = new HashMap<>();
        try {

            InputStream is = assets.open("quiz.txt");
            Reader targetReader = new InputStreamReader(is);
            BufferedReader reader;

            reader = new BufferedReader(targetReader);
            String line = reader.readLine();

            int row = 1;
            String question = "";
            Map<String, Boolean> answers = new HashMap<>();
            while (line != null) {
                if(row == 1){
                    question = line;
                }
                if(row > 1 && row <= 5){
                    answers.put(
                            line.replace("- ","")
                                    .replace("<-",""), line.contains("<-"));
                }
                if(row > 4){
                    listOfQuestions.put(question, answers);
                    question = "";
                    answers = new HashMap<>();
                    row = 0;
                }
                // read next line
                line = reader.readLine();
                row++;
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        correctAnswers = 0; // reset the number of correct answers made
        totalGuesses = 0; // reset the total number of guesses the user made

        loadNextQuestion(); // start the quiz by loading the first flag
    }

    // after the user guesses a correct flag, load the next flag
    private void loadNextQuestion() {

        System.out.println(listOfQuestions);

        Optional<String> question = listOfQuestions.keySet().stream().findAny();
        System.out.println(question.get());
        Map<String,Boolean> answers = listOfQuestions.get(question.get());
        List<String> allAnswers = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : answers.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            allAnswers.add(entry.getKey());
            if(entry.getValue()){
                correctAnswer = entry.getKey();
            }
        }
        System.out.println(answers);
        System.out.println(correctAnswer);

        // get file name of the next flag and remove it from the list
        answerTextView.setText(""); // clear answerTextView

        // display current question number
        questionNumberTextView.setText(getString(
                R.string.question, (correctAnswers + 1), FLAGS_IN_QUIZ));

        healthTextView.setText(question.get());
        boolean ddd = listOfQuestions.remove(question.get(), answers);
        System.out.println(ddd);
        System.out.println(listOfQuestions);

        Collections.shuffle(allAnswers); // shuffle file names

        // put the correct answer at the end of fileNameList
        int correct = allAnswers.indexOf(correctAnswer);
        allAnswers.add(allAnswers.remove(correct));

        // add 2, 4, 6 or 8 guess Buttons based on the value of guessRows
        for (int row = 0; row < guessRows; row++) {
            // place Buttons in currentTableRow
            for (int column = 0;
                 column < guessLinearLayouts[row].getChildCount();
                 column++) {
                // get reference to Button to configure
                Button newGuessButton =
                        (Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);

                // get country name and set it as newGuessButton's text
                String filename = allAnswers.get((row * 2) + column);
                newGuessButton.setText(filename);
            }
        }
    }

    // animates the entire quizLinearLayout on or off screen
    private void animate(boolean animateOut) {
        // prevent animation into the the UI for the first flag
        if (correctAnswers == 0)
            return;

        // calculate center x and center y
        int centerX = (quizLinearLayout.getLeft() +
                quizLinearLayout.getRight()) / 2; // calculate center x
        int centerY = (quizLinearLayout.getTop() +
                quizLinearLayout.getBottom()) / 2; // calculate center y

        // calculate animation radius
        int radius = Math.max(quizLinearLayout.getWidth(),
                quizLinearLayout.getHeight());

        Animator animator;

        // if the quizLinearLayout should animate out rather than in
        if (animateOut) {
            // create circular reveal animation
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, radius, 0);
            animator.addListener(
                    new AnimatorListenerAdapter() {
                        // called when the animation finishes
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadNextQuestion();
                        }
                    }
            );
        }
        else { // if the quizLinearLayout should animate in
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, 0, radius);
        }

        animator.setDuration(500); // set animation duration to 500 ms
        animator.start(); // start the animation
    }

    // called when a guess Button is touched
    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = correctAnswer;
            ++totalGuesses; // increment number of guesses the user has made

            if (guess.equals(answer)) { // if the guess is correct
                ++correctAnswers; // increment the number of correct answers

                // display correct answer in green text
                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(
                        getResources().getColor(R.color.purple_200,
                                getContext().getTheme()));

                disableButtons(); // disable all guess Buttons

                // if the user has correctly identified FLAGS_IN_QUIZ flags
                if (correctAnswers == FLAGS_IN_QUIZ) {
                    onButtonShowPopupWindowClick();
                }
                else { // answer is correct but quiz is not over
                    // load the next flag after a 2-second delay
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    animate(true); // animate the flag off the screen
                                }
                            }, 2000); // 2000 milliseconds for 2-second delay
                }
            }
            else { // answer was incorrect
                healthTextView.startAnimation(shakeAnimation); // play shake

                // display "Incorrect!" in red
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.bmi_button, getContext().getTheme()));
                guessButton.setEnabled(false); // disable incorrect answer
            }
        }
    };

    public void onButtonShowPopupWindowClick() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView popupTextView = (TextView) popupView.findViewById(R.id.popupTextView);
        popupTextView.setText(
                getString(R.string.results,
                        totalGuesses,
                        (1000 / (double) totalGuesses)));

        Button popupButton = (Button) popupView.findViewById(R.id.popupButton);
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetQuiz();
                popupWindow.dismiss();
            }
        });

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);

    }

    // utility method that disables all answer Buttons
    private void disableButtons() {
        for (int row = 0; row < guessRows; row++) {
            LinearLayout guessRow = guessLinearLayouts[row];
            for (int i = 0; i < guessRow.getChildCount(); i++)
                guessRow.getChildAt(i).setEnabled(false);
        }
    }

}