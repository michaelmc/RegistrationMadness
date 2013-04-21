package registrationmadness;

import java.util.Vector;

public class Student implements Runnable {
    Vector<Section> desiredSections;
    Vector<Section> alternateSections;
    Vector<Section> rosteredSections;
    boolean prefersMornings;
    Vector<Section> waitlistedSections;
    
    public Student(Vector<Section> desired, boolean mornings) {
        desiredSections = desired;
        // create alternate sections here
        prefersMornings = mornings;
        rosteredSections = new Vector<Section>(3);
        waitlistedSections = new Vector<Section>(50);
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
    
    boolean inquire(Section section) {
        return section.inquire();
    }
    
    synchronized boolean register(Section section) {
        if (section.register(this)) {
            rosteredSections.add(section);
            return true;
        } else {
            waitlistedSections.add(section);
            return false;
        }
    }
    
    void withdraw(Section section) {
        if (rosteredSections.contains(section)) {
            rosteredSections.remove(section);
        } else if (waitlistedSections.contains(section)) {
            waitlistedSections.remove(section);
        }
        section.withdraw(this);
    }
    
    synchronized boolean addFromWaitlist(Section section) {
        if (rosteredSections.size() < 3) {
            rosteredSections.add(section);
            return true;
        } else {
            for (int i = 0; i < rosteredSections.size(); i++) {
                if (!desiredSections.contains(rosteredSections.get(i))
                        && !alternateSections.contains(rosteredSections.get(i))) {
                    rosteredSections.remove(i);
                    rosteredSections.add(section);
                    return true;
                }
            }
            return false;
        }
    }
}
