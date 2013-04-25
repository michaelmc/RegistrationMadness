package registrationmadness;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class to run a student registration simulation. 190 students at the same
 * time attempt to register for their randomly-selected desired courses and
 * sections, three courses per student.
 * 
 * @author Michael McLaughlin, mvm@isc.upenn.edu
 * @version Spring 2013
 */
public class RegistrationMadness {
    Vector<Section> sections = new Vector<Section>(24);
    Vector<Student> students = new Vector<Student>(190);
    long startTime;
    long endTime;

    /**
     * The registration simulation's main class. Runs the actual student
     * registration simulation.
     * 
     * @param args
     */
    public static void main(String[] args) {
        RegistrationMadness registration = new RegistrationMadness();
        registration.register();
        System.out.println("registration complete.");
        registration.statistics();
    }
    
    /**
     * Constructor for the RegistrationMadness class. Generates the course
     * sections and students for the simulation.
     */
    public RegistrationMadness() {
        generateSections();
        generateStudents();
    }
    
    /**
     * Runs the details of the simulation. Times the process, creates the 
     * process pool, and calls the student runnables. In addition, will display
     * messages if a course registers more than 25 students and if a student 
     * doesn't register for three courses. Finally prints the elapsed time. 
     */
    void register() {
        startTime = System.nanoTime();
        int threads = 190;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < students.size(); i++) {
            pool.execute(students.get(i));
        }
        pool.shutdown();
        try {
            pool.awaitTermination(60, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).roster.size() > 25) { System.out.println("More than 25!"); }
        }
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).rosteredSections.size() != 3) { System.out.println("Not 3 classes!"); }
        }
        endTime = System.nanoTime();
        System.out.println("Time: " + ((endTime - startTime) / 1e9) + " seconds.\n");
    }
    
    /**
     * Creates the sections for the simulation. Generates twelve morning
     * sections and twelve afternoon sections, each series numbered 101-112. 
     */
    void generateSections() {
        for (int i = 101; i < 113; i++) {
            sections.add(new Section(i, true));
        }
        for (int i = 101; i < 113; i++) {
            sections.add(new Section(i, false));
        }
    }
    
    /**
     * Creates the students for the simulation. Generates 190 students each
     * with a randomly selected course time preference (60% probability of 
     * preferring the morning) and three randomly generated desired sections. 
     */
    void generateStudents() {
        Random rand = new Random(); 
        boolean morning;
        int courseInt;
        ArrayList<Integer> checkedInts;
        Vector<Section> desiredCourses;
        Vector<Section> alternateCourses;
        // build each student
        for (int i = 1; i <= 190; i++) {
            morning = (rand.nextInt(10) > 5) ? false : true;
            checkedInts = new ArrayList<Integer>();
            desiredCourses = new Vector<Section>(3);
            alternateCourses = new Vector<Section>(3);
            while (desiredCourses.size() < 3) {
                courseInt = (Integer)rand.nextInt(12);
                if (! checkedInts.contains(courseInt)) {
                    checkedInts.add(courseInt);
                    desiredCourses.add(sections.get(courseInt));
                    alternateCourses.add(sections.get(courseInt += 12));
                }
            }
            if (morning) {
                students.add(new Student(desiredCourses, alternateCourses, morning, i, sections));                
            } else {
                students.add(new Student(alternateCourses, desiredCourses, morning, i, sections));
            }
        }
    }
    
    /**
     * Generates a set of statistics for student enrollment once the
     * simulation completes. These include number of students with:
     * 
     * -Perfect schedules (all desired classes at the desired times)
     * -All three desired courses, regardless of time
     * -Two of three desired courses, regardless of time
     * -One of three desired courses, regardless of time
     * -All three courses at the preferred time
     * -No perfect courses
     * -No desired courses, regardless of time
     * -Worst possible schedules (no wanted classes, unwanted times)
     * 
     */
    void statistics() {
        int perfectSchedules = 0;
        int gotAllClasses = 0;
        int gotTwoClasses = 0;
        int gotOneClass = 0;
        int gotAllTimeframe = 0;
        int gotNoPerfectSections = 0;
        int gotNoWantedClasses = 0;
        int terribleSchedules = 0;
        
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            int perfectSections = 0;
            int rightSections = 0;
            int rightTimeFrame = 0;
            for (int j = 0; j < student.rosteredSections.size(); j++) {
                if (student.desiredSections.contains(student.rosteredSections.get(j))) {
                    perfectSections += 1;
                    rightSections += 1;
                    rightTimeFrame += 1;
                } else if (student.alternateSections.contains(student.rosteredSections.get(j))) {
                    rightSections += 1;
                } else {
                    if (student.rosteredSections.get(j).morning == student.prefersMornings) {
                        rightTimeFrame += 1;
                    }
                }
            }   
            if (perfectSections == 3) { perfectSchedules += 1; }
            if (rightSections == 3) { gotAllClasses += 1; }
            if (rightSections == 2) { gotTwoClasses += 1; }
            if (rightSections == 1) { gotOneClass += 1; }
            if (rightSections == 0) { gotNoWantedClasses += 1; }
            if (perfectSections == 0) { gotNoPerfectSections += 1; }
            if (rightTimeFrame == 3) { gotAllTimeframe += 1; }
            if (rightTimeFrame == 0 && rightSections == 0) { terribleSchedules += 1; }
        }
        
        System.out.println("Perfect schedules: " + perfectSchedules);
        System.out.println("Got all three wanted courses: " + gotAllClasses);
        System.out.println("Got two wanted courses: " + gotTwoClasses);
        System.out.println("Got one wanted course: " + gotOneClass);
        System.out.println("Got all wanted times: " + gotAllTimeframe);
        System.out.println("Got no perfect sections: " + gotNoPerfectSections);
        System.out.println("Got no wanted courses: " + gotNoWantedClasses);
        System.out.println("Worst possible schedules: " + terribleSchedules);
    }
}
