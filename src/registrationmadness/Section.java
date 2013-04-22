package registrationmadness;

import java.util.LinkedList;
import java.util.Vector;

public class Section {
    int course;
    boolean morning;
    Vector<Student> roster;
    LinkedList<Student> waitlist;
    
    public Section(int course, boolean morning) {
        this.course = course;
        this.morning = morning;
        roster = new Vector<Student>(25);
        waitlist = new LinkedList<Student>();
    }
    
    @Override
    public String toString() {
        return (morning == true) ? course + " Morning" : course + " Afternoon"; 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Section)) return false;
        if (obj == this) return true;
        Section other = (Section)obj;
        return (this.course == other.course && this.morning == other.morning) ? true : false;
    }
    
    synchronized public boolean inquire() {
        return (roster.size() < 25);
    }
    
    synchronized public boolean register(Student student) {
        if (roster.size() < 25) {
            roster.add(student);
            return true;
        } else {
            waitlist.add(student);
            return false;
        }
    }
    
    synchronized public void withdraw(Student student) {
        int rosterIndex;
        // Remove from wait list first
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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (spotFilled == true) { roster.add(nextStudent); }
                }
            }
        }
    }
}
