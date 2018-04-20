package edu.cornell.gdiac.mangosnoops;

public class Event {
    /** The y-coordinate where this event occurs */
    private float y;
    /** The type of event */
    private EventType type;

    /** Enumeration of different types of events that can occur */
    public enum EventType {
        REAR_ENEMY, SUN_START, SUN_END, NED_WAKES_UP, NOSH_WAKES_UP, SAT_QUESTION
    }

    /** Return the y-coordinate where this event occurs */
    public float getY() { return y; }

    /** Return the type of this event */
    public EventType getType() { return type; }

    /** Constructor for an event of the given type at the given y-coordinate */
    public Event(float y, EventType type) {
        this.y = y;
        this.type = type;
    }

}
