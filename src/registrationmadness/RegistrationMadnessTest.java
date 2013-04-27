package registrationmadness;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RegistrationMadnessTest {
    RegistrationMadness rm; 
    
    @Before
    public void setUp() throws Exception {
        rm = new RegistrationMadness();
    }

    @Test
    public final void testRegistrationMadness() {
        assertEquals(24, rm.sections.size());
        assertEquals(190, rm.students.size());
    }

    @Test
    public final void testRegister() {
        rm.register();
        for (int i = 0; i < 190; i++) {
            assertEquals(3, rm.students.get(i).rosteredSections.size());
        }
    }

    @Test
    public final void testGenerateSections() {
        rm.sections.setSize(0);
        rm.generateSections();
        assertEquals(24, rm.sections.size());
        for (int i = 0; i < 12; i++) {
            assertTrue(rm.sections.get(i).morning);
            assertFalse(rm.sections.get(i + 12).morning);
        }
    }

    @Test
    public final void testGenerateStudents() {
        rm.students.setSize(0);
        rm.generateStudents();
        assertEquals(190, rm.students.size());
        for (int i = 0; i < 190; i++) {
            assertEquals(3, rm.students.get(i).desiredSections.size());
            assertEquals(3, rm.students.get(i).alternateSections.size());
            assertEquals(0, rm.students.get(i).rosteredSections.size());
            assertEquals(0, rm.students.get(i).waitlistedSections.size());
            assertEquals(24, rm.students.get(i).allSections.size());
        }
    }
}
