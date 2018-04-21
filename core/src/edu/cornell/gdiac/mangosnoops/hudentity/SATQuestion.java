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
    // hasn't been
    private JSONObject currQuestion; // Index of the active question.
    private HashMap<String, Texture> images;
    private Texture bubble;
    Random rand = new Random();

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

    /** Checks if
     *
     * @return 0 = area was not pressed, 1 = left area pressed, 2 = right area pressed */
    private int isInArea(Vector2 p) {
        return 0;
    }

    public void update(Vector2 p, int numPressed, Child nosh) {
        if(active) {
            if(currQuestion.getString("imageB").equals("N/A")) {
                //numerical question
                if(numPressed == currQuestion.getInt("correct")) {
                    active = false;
                    //TODO: make the question flash green
                    nosh.setMood(Child.Mood.HAPPY);
                } else if(numPressed != -1) { //inputted wrong answer
                    active = false;
                    //TODO: make the question flash red
                    nosh.setMoodShifting(true, false);
                }
            } else {
                //choose the picture question

            }
        }
    }

    public void draw(GameCanvas canvas) {
        if(active) {
            canvas.draw(bubble, Color.WHITE, 0, bubble.getHeight(), 0.8f*canvas.getWidth(), 0.9f*canvas.getHeight(), 0,
                            0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth(), 0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth());
        }
    }

}
