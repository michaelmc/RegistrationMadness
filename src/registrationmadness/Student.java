package registrationmadness;

import java.util.Random;
import java.util.Vector;

public class Student implements Runnable {
    int id;
    Vector<Section> desiredSections;
    Vector<Section> alternateSections;
    Vector<Section> rosteredSections;
    Vector<Section> allSections;
    boolean prefersMornings;
    Vector<Section> waitlistedSections;
    
    public Student(Vector<Section> desired, Vector<Section> alternate, boolean mornings, int id, Vector<Section> allSections) {
        this.id = id;
        desiredSections = desired;
        alternateSections = alternate;
        prefersMornings = mornings;
        rosteredSections = new Vector<Section>(3);
        waitlistedSections = new Vector<Section>(50);
        this.allSections = allSections;
        
    }
    
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
        while (rosteredSections.size() < 3) {
            int courseInt = rand.nextInt(24);
//            if (!prefersMornings) { courseInt += 12; }
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
    
    public String desiredClasses() {
        String classes = "";
        for (int i = 0; i < desiredSections.size(); i++) {
            classes = classes + desiredSections.get(i).toString() + " "; 
        }
        return classes;
    }
    
    boolean inquire(Section section) throws InterruptedException {
        Thread.sleep(1000);
        return section.inquire();
    }
    
    synchronized boolean register(Section section) throws InterruptedException {
        if (section.register(this)) {
            rosteredSections.add(section);
//            System.out.println("Student " + id + " registered for " + section.toString());
            Thread.sleep(1000);
            return true;
        } else {
            waitlistedSections.add(section);
//            System.out.println("Student " + id + " waitlisted for " + section.toString());
            Thread.sleep(1000);
            return false;
        }
    }
    
    void withdraw(Section section) throws InterruptedException {
        if (rosteredSections.contains(section)) {
            rosteredSections.remove(section);
//            System.out.println("Student " + id + " removed from " + section.toString());
            Thread.sleep(1000);
        } else if (waitlistedSections.contains(section)) {
            waitlistedSections.remove(section);
//            System.out.println("Student " + id + " removed from " + section.toString() + " waitlist");
            Thread.sleep(1000);
        }
        section.withdraw(this);
    }
    
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
//                    System.out.println("Student " + id + " registered for " + section.toString() + " from waitlist");
                    Thread.sleep(1000);
                    return true;
                }
            }
            return false;
        }
    }
}
