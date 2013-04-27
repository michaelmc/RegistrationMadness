package registrationmadness;

import java.util.Random;
import java.util.Vector;

/**
 * A class to represent students in the registration simulation. Each student
 * has an ID number, desired sections, alternate sections (the same courses as
 * desired but in opposite times), preferred time (morning or afternoon), a
 * course catalog, a list of rostered sections, and a list of waitlisted 
 * sections.
 * 
 * The student also has the ability to find out if a class has open space,
 * register for a class, and withdraw from a class, as well as register for a
 * class from the waitlist.
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version Spring 2013
 */
public class Student implements Runnable {
    int id;
    Vector<Section> desiredSections;
    Vector<Section> alternateSections;
    Vector<Section> rosteredSections;
    Vector<Section> allSections;
    boolean prefersMornings;
    Vector<Section> waitlistedSections;
    
    /**
     * Constructor for the student class. Passes to the student the below
     * parameters:
     * 
     * @param desired The student's desired sections.
     * @param alternate The student's alternate sections.
     * @param mornings The student's preferred section time.
     * @param id The student's ID number.
     * @param allSections The course catalog of all sections.
     */
    public Student(Vector<Section> desired, Vector<Section> alternate, boolean mornings, int id, Vector<Section> allSections) {
        this.id = id;
        desiredSections = desired;
        alternateSections = alternate;
        prefersMornings = mornings;
        rosteredSections = new Vector<Section>(3);
        waitlistedSections = new Vector<Section>(50);
        this.allSections = allSections;
        
    }
    
    /**
     * Runs the student's section registration algorithm. Attempts to register
     * the student in desired courses and then alternate courses, and then
     * for any available section. Once the student has all three courses, they
     * withdraw from any extant waitlits.
     */
    @Override
    public void run() {
        Random rand = new Random();
        for (int i = 0; i < desiredSections.size(); i++) {
            try {
                if (!register(desiredSections.get(i))) { 
                    if (register(alternateSections.get(i))) {
                        withdraw(desiredSections.get(i)); 
                    }
                }
            } catch (InterruptedException e) { 
                e.printStackTrace();
            }
        }
        int courseInt;
        while (rosteredSections.size() < 3) {
            courseInt = rand.nextInt(24);
            if (! rosteredSections.contains(allSections.get(courseInt))) {
                try {
                    register(allSections.get(courseInt));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < waitlistedSections.size(); i++) {
            try {
                withdraw(waitlistedSections.get(i));
            } catch (InterruptedException e) { 
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Returns the list of the student's desired sections.
     * 
     * @return The list of the student's desired sections.
     */
    public String desiredClasses() {
        String classes = "";
        for (int i = 0; i < desiredSections.size(); i++) {
            classes = classes + desiredSections.get(i).toString() + " "; 
        }
        return classes;
    }
    
    /**
     * Asks a section whether it has room left to register. 
     * 
     * @param section The section to ask.
     * @return True if the section is not full.
     * @throws InterruptedException If the thread is interrupted.
     */
    boolean inquire(Section section) throws InterruptedException {
        Thread.sleep(1000);
        return section.inquire();
    }
    
    /**
     * Attempts to register the student in a section.
     *  
     * @param section The section in which to register the student.
     * @return True if the student is registered, false if he is waitlisted.
     * @throws InterruptedException If the thread is interrupted.
     */
    synchronized boolean register(Section section) throws InterruptedException {
        if (section.register(this)) {
            rosteredSections.add(section);
            Thread.sleep(1000);
            return true;
        } else {
            waitlistedSections.add(section);
            Thread.sleep(1000);
            return false;
        }
    }
    
    /**
     * Withdraws the student from the roster or waitlist of a section.
     * 
     * @param section The section from which to withdraw the student.
     * @throws InterruptedException If the thread is interrupted.
     */
    void withdraw(Section section) throws InterruptedException {
        if (rosteredSections.contains(section)) {
            rosteredSections.remove(section);
            Thread.sleep(1000);
        } else if (waitlistedSections.contains(section)) {
            waitlistedSections.remove(section);
            Thread.sleep(1000);
        }
        section.withdraw(this);
    }
    
    /**
     * Attempts to register the student for a section from its waitlist. If the
     * student doesn't yet have three courses, he is automatically registered. 
     * If he does, then he's registered if and only if one of his previously
     * registered sections is neither desired nor alternate.
     * 
     * @param section The section in which to enroll the student.
     * @return True if the student is registered for the section.
     * @throws InterruptedException If the thread is interrupted.
     */
    synchronized boolean addFromWaitlist(Section section) throws InterruptedException {
        if (rosteredSections.size() < 3) {
            rosteredSections.add(section);
            waitlistedSections.remove(section);
            Thread.sleep(1000);
            return true;
        } else {
            for (int i = 0; i < rosteredSections.size(); i++) {
                if (!desiredSections.contains(rosteredSections.get(i))
                        && !alternateSections.contains(rosteredSections.get(i))) {
                    rosteredSections.remove(i);
                    rosteredSections.add(section);
                    Thread.sleep(1000);
                    return true;
                }
            }
            return false;
        }
    }
}
