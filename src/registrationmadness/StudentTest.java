package registrationmadness;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class StudentTest {
    Vector<Student> students;
    Vector<Section> sections;
    Vector<Section> desired;
    Vector<Section> alternate;

    @Before
    public void setUp() throws Exception {
        sections = new Vector<Section>();
        sections.add(new Section(1, true));
        sections.add(new Section(1, false));
        sections.add(new Section(2, true));
        sections.add(new Section(2, false));
        desired = new Vector<Section>();
        desired.add(sections.get(0));
        desired.add(sections.get(1));
        desired.add(sections.get(2));
        students = new Vector<Student>();
        students.add(new Student(desired, desired, true, 0, sections));
        students.add(new Student(null, null, false, 0, sections));
        students.add(new Student(null, null, true, 1, sections));
        students.add(new Student(null, null, false, 1, sections));
        students.add(new Student(null, null, true, 2, sections));
        students.add(new Student(null, null, false, 2, sections));
    }

    @Test
    public final void testRun() {
        students.get(0).run();
        assertEquals(3, students.get(0).rosteredSections.size());
        assertEquals(0, students.get(0).waitlistedSections.size());
    }

    @Test
    public final void testDesiredClasses() {
        assertEquals("1 Morning 1 Afternoon 2 Morning ", students.get(0).desiredClasses());
    }

    @Test
    public final void testInquire() throws InterruptedException {
        assertTrue(students.get(0).inquire(sections.get(1)));
        assertTrue(students.get(0).inquire(sections.get(3)));
        assertTrue(students.get(2).inquire(sections.get(0)));
    }

    @Test
    public final void testRegister() throws InterruptedException {
        assertTrue(students.get(0).register(sections.get(1)));
        assertTrue(students.get(1).register(sections.get(3)));
        sections.get(2).roster.setSize(25);
        assertEquals(0, students.get(0).waitlistedSections.size());
        assertFalse(students.get(0).register(sections.get(2)));
        assertEquals(1, students.get(0).waitlistedSections.size());
    }

    @Test
    public final void testWithdraw() throws InterruptedException {
        assertTrue(students.get(0).register(sections.get(1)));
        assertTrue(students.get(0).register(sections.get(3)));
        assertEquals(2, students.get(0).rosteredSections.size());
        students.get(0).withdraw(sections.get(1));
        assertEquals(1, students.get(0).rosteredSections.size());
        sections.get(2).roster.setSize(25);
        assertEquals(0, students.get(0).waitlistedSections.size());
        assertFalse(students.get(0).register(sections.get(2)));
        assertEquals(1, students.get(0).waitlistedSections.size());
        students.get(0).withdraw(sections.get(2));
        assertEquals(0, students.get(0).waitlistedSections.size());
    }

    @Test
    public final void testAddFromWaitlist() {
        fail("Not yet implemented");
    }

}
