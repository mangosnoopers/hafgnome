package edu.cornell.gdiac.mangosnoops.hudentity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import edu.cornell.gdiac.mangosnoops.GameCanvas;
import edu.cornell.gdiac.mangosnoops.Image;
import org.json.*;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class SATQuestions extends Image {

    private static final String file = "SatQuestions/questions.json";
    private JSONArray questions;
    private boolean active; // True when there is a question active that
                            // hasn't been answered yet
    private String currQuestion;
    private Texture currImageA;
    private Texture currImageB;
    private int currAns;
    private HashMap<String, Texture> images; // Keys are the file names of the textures, values are respective Textures
    private Texture bubble; // Texture of the background bubble
    Random rand = new Random();
    private int timer; // Used to time how long to do a right/wrong animation
    private static final int TIMER_MAX = 30; //duration of timer
    /** -1: Do not tint draw & tint bubble
     *  0: Incorrect answer, tint bubble red
     *  1: Correct answer, tint bubble green*/
    private int answered;

    public SATQuestions(HashMap<String, Texture> i, Texture b) {
        super();
        try{
            Scanner scanner = new Scanner(new File(file));
            JSONObject jsonob = new JSONObject(scanner.useDelimiter("\\A").next());
            scanner.close();
            questions = jsonob.getJSONArray("questions");
            images = i;
            bubble = b;
            answered = -1;
        } catch(Exception e) {
            System.out.println("JSON Parsing Error for SATQuestions");
        }
    }

    /** Called when an SAT event happens.
     * Picks a random question from the array display on the screen.*/
    public void askQuestion() {
        if(!active) {
            active = true;
            JSONObject curr = questions.getJSONObject(rand.nextInt(questions.length()));
            currQuestion = curr.getString("text");
            if(curr.getString("imageB").equals("N/A")){
                currImageA = images.get("SatQuestions/"+curr.getString("imageA"));
                currImageB = null;
            } else {
                currImageA = images.get("SatQuestions/"+curr.getString("imageA"));
                currImageB = images.get("SatQuestions/"+curr.getString("imageB"));
            }
            currAns = curr.getInt("correct");
        }
    }

    /**
     * @return -1 = question area was not pressed, 1 = left area pressed, 2 = right area pressed */
    private int isInArea(Vector2 p) {
        if (p.x > X*SCREEN_DIMENSIONS.x && p.x < (X+0.3f*0.45f)*SCREEN_DIMENSIONS.x
            && SCREEN_DIMENSIONS.y-p.y < Y*SCREEN_DIMENSIONS.y && SCREEN_DIMENSIONS.y-p.y > Y*SCREEN_DIMENSIONS.y-0.3f*SCREEN_DIMENSIONS.x*bubble.getHeight()/bubble.getWidth()) {
            return 1;
        }
        else if(p.x < (X+0.3f)*SCREEN_DIMENSIONS.x && p.x > (X+0.3f*0.45f)*SCREEN_DIMENSIONS.x
                && SCREEN_DIMENSIONS.y-p.y < Y*SCREEN_DIMENSIONS.y && SCREEN_DIMENSIONS.y-p.y > Y*SCREEN_DIMENSIONS.y-0.3f*SCREEN_DIMENSIONS.x*bubble.getHeight()/bubble.getWidth()) {
            return 2;
        }
        else {
            return -1;
        }
    }

    /**
     *  Helper function for update method which handles when the player gets the question WRONG.
     */
    private void handleRightAnswer(Child ned) {
        active = false;
        ned.setMood(Child.Mood.HAPPY);
        timer = -1;
        answered = 1;
    }

    /**
     *  Helper function for update method which handles when the player gets the question RIGHT.
     */
    private void handleWrongAnswer(Child ned) {
        active = false;
        ned.setMoodShifting(true, false);
        timer = -1;
        answered = 0;
    }

    public void update(Vector2 p, int numPressed, Child ned) {
        if(answered != -1) {
            timer ++;
            if(timer == TIMER_MAX) {
                timer = 0;
                answered = -1;
            }
        }
        if(active) {
            if(currImageB == null) { //numerical question
                if(numPressed == currAns) {
                    handleRightAnswer(ned);
                } else if(numPressed != -1) { //inputted wrong answer
                    handleWrongAnswer(ned);
                }
            } else { //choose the picture question
                if(p!=null && isInArea(p) == currAns) {
                    handleRightAnswer(ned);
                } else if(p!=null && isInArea(p) != -1) {
                    handleWrongAnswer(ned);
                }
            }
        }
    }

    private static final float X = 0.35f;
    private static final float Y = 0.95f;
    private static final float TEXT_XOFFSET = 0.01f;
    private static final float TEXT_YOFFSET = 0.02f;

    public void reset() {
        active = false;
    }


    public void draw(GameCanvas canvas, BitmapFont font) {
        if(active){
            canvas.draw(bubble, Color.WHITE, 0, bubble.getHeight(), X*canvas.getWidth(), Y*canvas.getHeight(), 0,
                    0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth(), 0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth());
        } else if(answered == 1) {
            canvas.draw(bubble, Color.GREEN, 0, bubble.getHeight(), X*canvas.getWidth(), Y*canvas.getHeight(), 0,
                    0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth(), 0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth());
        } else if(answered == 0) {
            canvas.draw(bubble, Color.RED, 0, bubble.getHeight(), X*canvas.getWidth(), Y*canvas.getHeight(), 0,
                    0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth(), 0.3f*SCREEN_DIMENSIONS.x/bubble.getWidth());
        }
        if(active || answered != -1) { //question is present but hasn't been answered yet
            font.setColor(new Color(0, 0, 0, 1));
            float bubbleWidth = 0.3f*SCREEN_DIMENSIONS.x;
            float bubbleHeight = 0.3f*SCREEN_DIMENSIONS.x*bubble.getHeight()/bubble.getWidth();
            if(currImageB == null) { //numerical question
                canvas.draw(currImageA, Color.WHITE, 0.5f*currImageA.getWidth(), 0.5f*currImageA.getHeight(), (bubbleWidth*0.45f) + X*canvas.getWidth(), Y*canvas.getHeight() - (bubbleHeight*0.5f), 0,
                            bubbleHeight/currImageA.getWidth(), bubbleHeight/currImageA.getWidth());
                font.setColor(new Color(0, 0, 0, 1));
                canvas.drawText("Type to answer.", font, (X+TEXT_XOFFSET)*canvas.getWidth(), (1+TEXT_YOFFSET)*canvas.getHeight()-bubbleHeight);
            } else { //choose the picture question
                canvas.draw(currImageA, Color.WHITE, currImageA.getWidth(), 0.5f*currImageA.getHeight(), (bubbleWidth*0.45f) + X*canvas.getWidth(), Y*canvas.getHeight() - (bubbleHeight*0.5f), 0,
                        0.75f*bubbleHeight/currImageA.getWidth(), 0.75f*bubbleHeight/currImageA.getWidth());
                canvas.draw(currImageB, Color.WHITE, 0, 0.5f*currImageA.getHeight(), (bubbleWidth*0.45f) + X*canvas.getWidth(), Y*canvas.getHeight() - (bubbleHeight*0.5f), 0,
                        0.75f*bubbleHeight/currImageB.getWidth(), 0.75f*bubbleHeight/currImageB.getWidth());
                font.setColor(new Color(0, 0, 0, 1));
                canvas.drawText("Click to answer.", font, (X+TEXT_XOFFSET)*canvas.getWidth(), (1+TEXT_YOFFSET)*canvas.getHeight()-bubbleHeight);
            }
            canvas.drawText(currQuestion, font, (X+TEXT_XOFFSET)*canvas.getWidth(), (Y-TEXT_YOFFSET)*canvas.getHeight());
        }
    }

}
