package registrationmadness;

import java.util.Vector;

public class Student implements Runnable {
    int id;
    Vector<Section> desiredSections;
    Vector<Section> alternateSections;
    Vector<Section> rosteredSections;
    boolean prefersMornings;
    Vector<Section> waitlistedSections;
    
    public Student(Vector<Section> desired, boolean mornings, int id) {
        this.id = id;
        desiredSections = desired;
        // create alternate sections here
        prefersMornings = mornings;
        rosteredSections = new Vector<Section>(3);
        waitlistedSections = new Vector<Section>(50);
    }
    
    @Override
    public void run() {
        for (int i = 0; i < desiredSections.size(); i++) {
            try {
                if (!register(desiredSections.get(i))) { register(alternateSections.get(i));}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        while (rosteredSections.size() < 3) {
//            register()
//        }
        
    }
    
    boolean inquire(Section section) {
        return section.inquire();
    }
    
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
    
    synchronized boolean addFromWaitlist(Section section) throws InterruptedException {
        if (rosteredSections.size() < 3) {
            rosteredSections.add(section);
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
