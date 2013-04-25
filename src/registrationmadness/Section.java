package registrationmadness;

import java.util.LinkedList;
import java.util.Vector;

/**
 * A class to represent class sections. Each section has a course number, time
 * (morning or afternoon), a roster of up to 25 students, and a waitlist of
 * unlimited size.
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version Spring 2013
 *
 */
public class Section {
    int course;
    boolean morning;
    Vector<Student> roster;
    LinkedList<Student> waitlist;
    
    /**
     * Constructor for the section. Creates a section with a course number and
     * a time (morning or afternoon). Each section can have up to 25 students
     * registered and unlimited students on the waitlist.
     * 
     * @param course The course number of this section.
     * @param morning True if this is a morning section, false if afternoon.
     */
    public Section(int course, boolean morning) {
        this.course = course;
        this.morning = morning;
        roster = new Vector<Student>(25);
        waitlist = new LinkedList<Student>();
    }
    
    /**
     * Returns the string representation of the section. Uses the format:
     * 
     * courseNumber timePeriod
     * 
     * e.g.
     * 
     * 101 Morning
     * 107 Afternoon
     * 
     * @return The string representation of this section.
     */
    @Override
    public String toString() {
        return (morning == true) ? course + " Morning" : course + " Afternoon"; 
    }
    
    /**
     * Returns true if the other object is a section with the same course
     * number and same time period as this object.
     * 
     * @param obj The object to test for equality.
     * @return True if the object is equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Section)) return false;
        if (obj == this) return true;
        Section other = (Section)obj;
        return (this.course == other.course && this.morning == other.morning) ? true : false;
    }
    
    /**
     * Returns whether or not there's still room in the section.
     * 
     * @return True if less than 25 students are registered for the section.
     */
    synchronized public boolean inquire() {
        return (roster.size() < 25);
    }
    
    /**
     * Registers a student in the section. If there is no room left in the
     * section, the student is waitlisted for this section.
     * 
     * @param student The student to register in the section.
     * @return True if the student is registered in the section, false if the student is waitlisted.
     */
    synchronized public boolean register(Student student) {
        if (roster.size() < 25) {
            roster.add(student);
            return true;
        } else {
            waitlist.add(student);
            return false;
        }
    }
    
    /**
     * Withdraws a student from a section or its waitlist. If there are other
     * students on the waitlist and this student is removed from the roster,
     * the section attempts to add students from the waitlist until the spot
     * is filled. 
     * 
     * @param student The student to withdraw from the section.
     */
    synchronized public void withdraw(Student student) {
        int rosterIndex;
        // Remove from waitlist first
        rosterIndex = waitlist.indexOf(student);
        if (rosterIndex > -1) { waitlist.remove(rosterIndex); }
        // Then remove from roster
        rosterIndex = roster.indexOf(student);
        if (rosterIndex > -1) { 
            roster.remove(rosterIndex);
            if (waitlist.size() > 0) {
                boolean spotFilled = false;
                while (spotFilled == false) {
                    Student nextStudent = waitlist.removeFirst();
                    try {
                        spotFilled = nextStudent.addFromWaitlist(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (spotFilled == true) { roster.add(nextStudent); }
                }
            }
        }
    }
}
