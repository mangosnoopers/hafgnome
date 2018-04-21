package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import org.json.*;

import java.util.HashMap;
import java.util.Random;

public class SATQuestion extends Image {

    private static final String file = "SatQuestions/questions.json";
    private JSONArray questions;
    private boolean active; // True when there is a question active that
                            // hasn't been answered yet
    private JSONObject currQuestion; // Index of the active question.
    private HashMap<String, Texture> images; // Keys are the file names of the textures, values are respective Textures
    private Texture bubble; // Texture of the background bubble
    Random rand = new Random();
    private int timer; // Used to time how long to do a right/wrong animation

    public SATQuestion(HashMap<String, Texture> i, Texture b) {
        super();
        JSONObject jsonob = new JSONObject(file);
        questions = jsonob.getJSONArray("questions");
        images = i;
        bubble = b;
    }

    /** Called when an SAT event happens.
     * Picks a random question from the array display on the screen.*/
    public void askQuestion() {
        if(!active) {
            active = true;
            currQuestion = questions.getJSONObject(rand.nextInt(questions.length()));
        }
    }

    /**
     * @return -1 = question area was not pressed, 1 = left area pressed, 2 = right area pressed */
    private int isInArea(Vector2 p) {
        return -1;
    }

    /**
     *  Helper function for update method which handles when the player gets the question WRONG.
     */
    private void handleRightAnswer(Child ned) {
        active = false;
        ned.setMood(Child.Mood.HAPPY);
        timer = -1;
    }

    /**
     *  Helper function for update method which handles when the player gets the question RIGHT.
     */
    private void handleWrongAnswer(Child ned) {
        active = false;
        ned.setMoodShifting(true, false);
        timer = -1;
    }

    public void update(Vector2 p, int numPressed, Child ned) {
        if(active) {
            if(currQuestion.getString("imageB").equals("N/A")) {
                //numerical question
                if(numPressed == currQuestion.getInt("correct")) {
                    handleRightAnswer(ned);
                } else if(numPressed != -1) { //inputted wrong answer
                    handleWrongAnswer(ned);
                }
            } else {
                //choose the picture question
                if(isInArea(p) == currQuestion.getInt("correct")) {
                    handleRightAnswer(ned);
                } else if(isInArea(p) != -1) {
                    handleWrongAnswer(ned);
                }
            }
        }
    }

    public void draw(GameCanvas canvas) {
        if(active) { //question is present but hasn't been answered yet
            canvas.draw(bubble, Color.WHITE, 0, bubble.getHeight(), 0.8f*canvas.getWidth(), 0.9f*canvas.getHeight(), 0,
                            0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth(), 0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth());
        } else if(true) { //question has been answered correctly

        } else { //question has been answered incorrectly

        }
    }

}
