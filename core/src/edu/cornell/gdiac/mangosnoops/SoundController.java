package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import edu.cornell.gdiac.mangosnoops.Menus.SettingsMenu;
import edu.cornell.gdiac.mangosnoops.hudentity.Child;
import edu.cornell.gdiac.mangosnoops.hudentity.DvdPlayer;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio;
import edu.cornell.gdiac.mangosnoops.hudentity.TouchScreen;

public class SoundController {
    private SettingsMenu settings;
    /** All played music */
    private Array<Music> music;
    private Array<Sound> effects;

    private final Music carAmbience = Gdx.audio.newMusic(Gdx.files.internal("sounds/carAmbience.mp3"));
    private final Music radioStatic = Gdx.audio.newMusic(Gdx.files.internal("sounds/radioStatic.mp3"));
    private final Music menuSong = Gdx.audio.newMusic(Gdx.files.internal("OtherSongs/bensound-ukulele.mp3"));
    private final Music levelSelectSong = Gdx.audio.newMusic(Gdx.files.internal("OtherSongs/bensound-retrosoul.mp3"));
    private final Music rearGnomeCrawl = Gdx.audio.newMusic(Gdx.files.internal("sounds/eerieRearGnome.mp3"));

    private final Sound carBeep = Gdx.audio.newSound(Gdx.files.internal("sounds/beepbeep.mp3"));
    private final Sound gnomeDeath1 = Gdx.audio.newSound(Gdx.files.internal("sounds/gnomeGrunt_1.mp3"));
    private final Sound gnomeDeath2 = Gdx.audio.newSound(Gdx.files.internal("sounds/gnomeGrunt_2.mp3"));
    private final Sound gnomeDeath3 = Gdx.audio.newSound(Gdx.files.internal("sounds/gnomeGrunt_3.mp3"));
    private final Sound gnomeDeath4 = Gdx.audio.newSound(Gdx.files.internal("sounds/gnomeGrunt_4.mp3"));
    private final Sound flamingoFlap = Gdx.audio.newSound(Gdx.files.internal("sounds/flamingosFlap.mp3"));
    private final Sound flamingoCollision = Gdx.audio.newSound(Gdx.files.internal("sounds/flamingoCollision.mp3"));
    private final Sound grillCollision = Gdx.audio.newSound(Gdx.files.internal("sounds/grillCollision.mp3"));
    private final Sound carIgnition = Gdx.audio.newSound(Gdx.files.internal("sounds/carStartUp.mp3"));
    private final Sound click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"));
    private final Sound hoverMouse = Gdx.audio.newSound(Gdx.files.internal("sounds/mousePassOver.mp3"));
    private final Sound rearGnomeDefeated = Gdx.audio.newSound(Gdx.files.internal("sounds/rearGnomeDisappears.mp3"));
    private final Sound snackEat = Gdx.audio.newSound(Gdx.files.internal("sounds/snackEating.mp3"));


    private final Array<Sound> noshDialogue = new Array<Sound>();
    private final Array<Sound> nedDialogue = new Array<Sound>();

    /**
     * Object Constructor
     */
    public SoundController(SettingsMenu settings){
        this.settings = settings;
        music = new Array<Music>();
        music.addAll(carAmbience,radioStatic,menuSong,levelSelectSong, rearGnomeCrawl);
        effects = new Array<Sound>();
        effects.addAll(carBeep,gnomeDeath1,gnomeDeath2,gnomeDeath3,gnomeDeath4,flamingoFlap,grillCollision,carIgnition,click,hoverMouse,rearGnomeDefeated, snackEat);
        carAmbience.setLooping(true);
        radioStatic.setLooping(true);

        noshDialogue.addAll(Gdx.audio.newSound(Gdx.files.internal("sounds/tut0A.mp3")),
                            Gdx.audio.newSound(Gdx.files.internal("sounds/tut0C.mp3")));
        nedDialogue.addAll(Gdx.audio.newSound(Gdx.files.internal("sounds/tut0B.mp3")),
                            Gdx.audio.newSound(Gdx.files.internal("sounds/tut0D.mp3")));

        noshDialogue.addAll(Gdx.audio.newSound(Gdx.files.internal("sounds/tut1A.mp3")));

        noshDialogue.addAll(Gdx.audio.newSound(Gdx.files.internal("sounds/tut2A.mp3")),
                            Gdx.audio.newSound(Gdx.files.internal("sounds/tut2D.mp3")),
                            Gdx.audio.newSound(Gdx.files.internal("sounds/tut2E.mp3")));
        nedDialogue.addAll(Gdx.audio.newSound(Gdx.files.internal("sounds/tut2B.mp3")),
                            Gdx.audio.newSound(Gdx.files.internal("sounds/tut2C.mp3")),
                            Gdx.audio.newSound(Gdx.files.internal("sounds/tut2F.mp3")),
                            Gdx.audio.newSound(Gdx.files.internal("sounds/tut2G.mp3")));

        effects.addAll(noshDialogue);
        effects.addAll(nedDialogue);
    }

    public void startAmbience() {
        carAmbience.setVolume(settings.getMusicVolume());
        carAmbience.play();
    }

    boolean wasStatic = false;
    /**
     * Given a radio r, the SoundController creates Music files for its most current station
     * and plays them. Additionally, it will stop audio for and dispose of the Music files
     * for the last played station.
     * @param r
     */
    public void playRadio(Radio r) {
        if (r.getNumStations() > 0) {
            Radio.Station currentStation = r.getCurrentStation();
            if (r.shouldPlayStatic()) {
                if (currentStation.getAudio().isPlaying()) currentStation.getAudio().setVolume(0);
                radioStatic.setVolume(settings.getEffectsVolume());
                radioStatic.play();
            } else {
                radioStatic.stop();
                Radio.Station lastStation = r.getLastStation();

                if (lastStation != null) {
                    lastStation.getAudio().setVolume(0);
                }
                if (currentStation != null) {
                    if(!r.soundOn())
                        currentStation.getAudio().setVolume(0);
                    else if (currentStation.getAudio().isPlaying())
                        currentStation.getAudio().setVolume(settings.getMusicVolume());
                    else {
                        currentStation.getAudio().play();
                        currentStation.getAudio().setVolume(settings.getMusicVolume());
                    }
                    music.add(currentStation.getAudio());
                    wasStatic = r.shouldPlayStatic();
                }
            }
        }
    }

    public void muteRadio(Radio r) {
        if(r != null && r.getCurrentStation() != null)
            r.getCurrentStation().getAudio().stop();
    }

    public void playDvd(DvdPlayer m) {
        // make sure to set volume to settings.getMusicVolume()
        // TODO : add audio functionality
    }

    /** Called in resolveActions when horn is honked */
    public void beepSound() {
        carBeep.stop();
        carBeep.play(settings.getEffectsVolume());
    }

    /** Called in GameplayController/resolveItemDrop **/
    public void playSnackEating(){
        snackEat.stop();
        snackEat.play();
    }

    /** Called in resolveActions when horn is honked */
    public void flamingoFlapSound() {
        flamingoFlap.play(settings.getEffectsVolume());
    }

    /** Called in CollisionController **/
    public void playGrillCollision() {
        grillCollision.play(settings.getEffectsVolume());
    }

    /** Called in CollisionController **/
    public void playFlamingoCollision() {
        flamingoCollision.play(settings.getEffectsVolume());
    }

    /** Called in CollisionController and Settings */
    public void gnomeDeathSound() {
        switch (settings.getGnomeSoundSelected()) {
            case 'A':
                gnomeDeath1.stop();
                gnomeDeath1.play(settings.getEffectsVolume());
                break;
            case 'B':
                gnomeDeath2.stop();
                gnomeDeath2.play(settings.getEffectsVolume());
                break;
            case 'C':
                gnomeDeath3.stop();
                gnomeDeath3.play(settings.getEffectsVolume());
                break;
            case 'D':
                gnomeDeath4.stop();
                gnomeDeath4.play(settings.getEffectsVolume());
                break;
            default: break;
        }
    }

    public void playRearViewGnomeCrawl(boolean playing){
        if(playing) {
            rearGnomeCrawl.stop();
            rearGnomeCrawl.setVolume(settings.getMusicVolume());
            rearGnomeCrawl.play();
        } else{
            rearGnomeCrawl.stop();
            rearGnomeDefeated.stop();
            rearGnomeDefeated.play(settings.getEffectsVolume());
        }
    }

    /** Called in TutorialController **/
    public void playTutorialDialogue(Child.ChildType c, int i){
        if(i == -1){
            return;
        }
        if(c == Child.ChildType.NOSH){
            if(i > 0){
                noshDialogue.get(i-1).stop();
            }
            noshDialogue.get(i).play(settings.getEffectsVolume());
        }else if(c == Child.ChildType.NED){
            if(i>0){
                nedDialogue.get(i-1).stop();
            }
            nedDialogue.get(i).play(settings.getEffectsVolume());
        }
    }

    /** Called in StartMenuMode Constructor and Dispose**/
    public void playMenuSong(boolean playing){
        if(playing) {
            menuSong.setVolume(settings.getMusicVolume());
            menuSong.play();
            menuSong.setLooping(true);
        } else{
            stopAudio(menuSong);
        }
    }
    /** Called in LevelMenuMode **/
    public void playLevelSelectSong(boolean playing){
        if(playing) {
            levelSelectSong.setVolume(settings.getMusicVolume());
            levelSelectSong.play();
            levelSelectSong.setLooping(true);
        } else{
            stopAudio(levelSelectSong);
        }
    }

    /**Called in LevelMenuMode and StartMenuMode**/
    public void playCarIgnition() {
        carIgnition.stop();
        carIgnition.play(settings.getEffectsVolume());
    }

    public void playClick(){
        click.stop();
        click.play(settings.getEffectsVolume());
    }
    public void playHoverMouse(){
        hoverMouse.stop();
        hoverMouse.play(settings.getEffectsVolume());
    }
    /** Stops audio and disposes the file **/
    private void stopAudio(Music m){
        if(m == null){
            return;
        }
        m.stop();
        music.removeValue(m,false);
    }
    private void stopAudio(Sound s){
        if(s == null) return;
        s.stop();
        effects.removeValue(s,false);
    }

    /** Universal play method, plays all audio based on game updates **/
    float lastVolume;
    public void play(TouchScreen t){
        if( lastVolume != settings.getMusicVolume()) {
            setAllMusicVolume();
        }
        lastVolume = settings.getMusicVolume();
        if(t != null) {
            if (t.getDvdPlayer().isPlayingDvd()) {
                muteRadio(t.getRadio());
                playDvd(t.getDvdPlayer());
            } else {
                playRadio(t.getRadio());
            }
        }
    }

    private void setAllMusicVolume(){
        for(Music m: music){
            m.setVolume(settings.getMusicVolume());
        }
    }


    /** Reset method **/
    public void reset() {
        for(Music m : music) {
            stopAudio(m);
        }
        music.addAll(carAmbience,radioStatic,menuSong,levelSelectSong, rearGnomeCrawl);
        for(Sound s : effects) {
            stopAudio(s);
        }
        effects.addAll(carBeep,gnomeDeath1,gnomeDeath2,gnomeDeath3,gnomeDeath4,flamingoFlap,grillCollision,carIgnition,click,hoverMouse,rearGnomeDefeated,snackEat);
        effects.addAll(noshDialogue);
        effects.addAll(nedDialogue);
    }
}