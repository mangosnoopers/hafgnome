package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import org.json.*;

import java.util.HashMap;
import java.util.Random;

public class SATQuestion {

    private static final String file = "SatQuestions/questions.json";
    private JSONArray questions;
    private boolean active; // True when there is a question active that
    // hasn't been
    private JSONObject currQuestion; // Index of the active question.
    private HashMap<String, Texture> images;
    Random rand = new Random();

    public SATQuestion(HashMap<String, Texture> i) {
        JSONObject jsonob = new JSONObject(file);
        questions = jsonob.getJSONArray("questions");
        images = i;
    }

    /** Called when an SAT event happens.
     * Picks a random question from the array display on the screen.*/
    public void askQuestion() {
        if(!active) {
            active = true;
            currQuestion = questions.getJSONObject(rand.nextInt(questions.length()));
        }
    }

    private void isInArea() {

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

        }
    }

}
