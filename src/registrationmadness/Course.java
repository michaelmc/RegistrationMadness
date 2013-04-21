package registrationmadness;

import java.util.LinkedList;
import java.util.Vector;

public class Course {
    String courseName;
    Vector<Student> morningRoster;
    int morningRosterSize;
    Vector<Student> afternoonRoster;
    int afternoonRosterSize;
    LinkedList<Student> morningWaitlist;
    LinkedList<Student> afternoonWaitlist;
    
    public Course(String name) {
        courseName = name;
        morningRoster = new Vector<Student>(25);
        afternoonRoster = new Vector<Student>(25);
        morningWaitlist = new LinkedList<Student>();
        afternoonWaitlist = new LinkedList<Student>();
        morningRosterSize = 0;
        afternoonRosterSize = 0;
    }
    
    synchronized public boolean inquire(boolean morning) {
        if (morning) {
            return (morningRoster.size() < 25);
        } else {
            return (afternoonRoster.size() < 25);
        }
    }
    
    synchronized public String register(Student student, boolean morning) {
        if (morning) {
            if (morningRoster.size() < 25) {
                if (afternoonRoster.contains(student)) { afternoonRoster.removeElement(student); }
                morningRoster.add(student);
                return "Course added.";
            } else {
                morningWaitlist.add(student);
                return "Waitlisted.";
            }
        } else {
            if (afternoonRoster.size() < 25) {
                if (morningRoster.contains(student)) { morningRoster.removeElement(student); }
                afternoonRoster.add(student);
                return "Course added.";
            } else {
                afternoonWaitlist.add(student);
                return "Waitlisted.";
            }
        }
    }
    
    synchronized public String withdraw(Student student) {
        int rosterIndex;
        // Remove from waitlists first
        rosterIndex = afternoonWaitlist.indexOf(student);
        if (rosterIndex > -1) { morningWaitlist.remove(rosterIndex); }
        rosterIndex = morningWaitlist.indexOf(student);
        if (rosterIndex > -1) { morningWaitlist.remove(rosterIndex); }
        // Then remove from rosters
        rosterIndex = morningRoster.indexOf(student);
        if (rosterIndex > -1) { 
            morningRoster.remove(rosterIndex);
            Student newAdd = morningWaitlist.removeFirst();
            morningRoster.add(newAdd);
            newAdd.addCourse(courseName);
        }
        rosterIndex = afternoonRoster.indexOf(student);
        if (rosterIndex > -1) { 
            afternoonRoster.remove(rosterIndex);
            Student newAdd = afternoonWaitlist.removeFirst();
            afternoonRoster.add(newAdd);
            newAdd.addCourse(courseName);
        }
        return "Removed from all sections and waitlists.";
    }
}
