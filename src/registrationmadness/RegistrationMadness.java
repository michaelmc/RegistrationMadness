package registrationmadness;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationMadness {
    Vector<Section> sections = new Vector<Section>(24);
    Vector<Student> students = new Vector<Student>(190);

    /**
     * @param args
     */
    public static void main(String[] args) {
        RegistrationMadness registration = new RegistrationMadness();
        registration.register();
        System.out.println("registration complete.");
    }
    
    public RegistrationMadness() {
        generateSections();
        generateStudents();
    }
    
    void register() {
        int threads = Runtime.getRuntime().availableProcessors();
        threads = (threads < 1) ? 1 : threads;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i).desiredClasses());
            pool.execute(students.get(i));
            System.out.println("Executing student " + students.get(i).id);
        }
        // pool.shutdown();
        pool.shutdownNow();        
    }
    
    void generateSections() {
        for (int i = 101; i < 113; i++) {
            sections.add(new Section(i, true));
        }
        for (int i = 101; i < 113; i++) {
            sections.add(new Section(i, false));
        }
        System.out.println("Sections: " + sections.size());
    }
    
    void generateStudents() {
        Random rand = new Random(); 
        boolean morning;
        int courseInt;
        ArrayList<Integer> checkedInts;
        Vector<Section> desiredCourses;
        // build for each student
        for (int i = 1; i <= 5; i++) {
            morning = (rand.nextInt(10) > 5) ? false : true;
            checkedInts = new ArrayList<Integer>();
            desiredCourses = new Vector<Section>(3);
            while (desiredCourses.size() < 3) {
                courseInt = (Integer)rand.nextInt(12);
                if (! checkedInts.contains(courseInt)) {
                    checkedInts.add(courseInt);
                    desiredCourses.add(sections.get(courseInt));
                }
            }
            students.add(new Student(desiredCourses, morning, i));
        }
        System.out.println("Students: " + students.size());
    }
}
