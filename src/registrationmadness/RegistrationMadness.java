package registrationmadness;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationMadness {
    Section[] sections = new Section[24];
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
            pool.execute(students.get(i));
            System.out.println("Executing student " + students.get(i).id);
        }
    }
    
    void generateSections() {
        for (int i = 101; i < 113; i++) {
            sections[i - 101] = new Section(i, true);
        }
        for (int i = 101; i < 113; i++) {
            sections[i - 89] = new Section(i, false);
        }
        System.out.println("Sections: " + sections.length);
    }
    
    void generateStudents() {
        Random rand = new Random(); 
        boolean morning;
        Vector<Section> desired = new Vector<Section>();
        for (int i = 1; i < 191; i++) {
            morning = (rand.nextInt(10) > 5) ? false : true;
            while (desired.size() < 3) {
                if (morning == true) {
                    desired.add(sections[rand.nextInt(12)]);
                } else {
                    desired.add(sections[rand.nextInt(12) + 12]);
                }
            }
            students.add(new Student(desired, morning, i));
        }
        System.out.println("Students: " + students.size());

    }
}
