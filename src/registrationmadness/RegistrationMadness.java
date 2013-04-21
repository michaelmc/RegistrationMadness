package registrationmadness;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationMadness {
    Section[] sections = new Section[24];

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    
    void register() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
    }
    
    void generateSections() {
        for (int i = 101; i < 113; i++) {
            sections[i - 101] = new Section(course, true);
        }
        for (int i = 101; i < 113; i++) {
            sections[i - 89] = new Section(course, false);
        }
    }
    
    void generateStudents() {
        Random rand = new Random(); 
        boolean morning;
        Vector<Section> desired;
        for (int i = 0; i < 190; i++) {
            morning = (rand.nextInt(10) > 5) ? false : true;
            while (desired.size() < 3) {
                
            }
        }
    }
}
