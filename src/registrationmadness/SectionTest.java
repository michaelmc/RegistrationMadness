package registrationmadness;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SectionTest {
    Section oneAM;
    Section onePM;
    Section twoAM;
    Section twoPM;
    Student[] students;

    @Before
    public void setUp() throws Exception {
        oneAM = new Section(1, true);
        onePM = new Section(1, false);
        twoAM = new Section(2, true);
        twoPM = new Section(2, false);
        students = new Student[26];
        students[0] = new Student(null, null, true, 0, null);
        students[1] = new Student(null, null, true, 1, null);
        students[2] = new Student(null, null, true, 2, null);
        students[3] = new Student(null, null, true, 3, null);
        students[4] = new Student(null, null, true, 4, null);
        students[5] = new Student(null, null, true, 5, null);
        students[6] = new Student(null, null, true, 6, null);
        students[7] = new Student(null, null, true, 7, null);
        students[8] = new Student(null, null, true, 8, null);
        students[9] = new Student(null, null, true, 9, null);
        students[10] = new Student(null, null, true, 10, null);
        students[11] = new Student(null, null, true, 11, null);
        students[12] = new Student(null, null, true, 12, null);
        students[13] = new Student(null, null, true, 13, null);
        students[14] = new Student(null, null, true, 14, null);
        students[15] = new Student(null, null, true, 15, null);
        students[16] = new Student(null, null, true, 16, null);
        students[17] = new Student(null, null, true, 17, null);
        students[18] = new Student(null, null, true, 18, null);
        students[19] = new Student(null, null, true, 19, null);
        students[20] = new Student(null, null, true, 20, null);
        students[21] = new Student(null, null, true, 21, null);
        students[22] = new Student(null, null, true, 22, null);
        students[23] = new Student(null, null, true, 23, null);
        students[24] = new Student(null, null, true, 24, null);
        students[25] = new Student(null, null, true, 25, null);
    }

    @Test
    public final void testToString() {
        assertEquals("1 Morning", oneAM.toString());
        assertEquals("1 Afternoon", onePM.toString());
        assertEquals("2 Morning", twoAM.toString());
        assertEquals("2 Afternoon", twoPM.toString());
    }

    @Test
    public final void testEqualsObject() {
        assertFalse(oneAM.equals(onePM));
        assertFalse(oneAM.equals(twoAM));
        assertTrue(oneAM.equals(new Section(1, true)));
    }

    @Test
    public final void testInquire() throws InterruptedException {
        assertTrue(oneAM.inquire());
        assertTrue(twoPM.inquire());
        for (int i = 0; i < 25; i++) {
            assertTrue(students[i].inquire(oneAM));
            oneAM.register(students[i]);
        }
        assertFalse(students[25].inquire(oneAM));
    }

    @Test
    public final void testRegister() throws InterruptedException {
        for (int i = 0; i < 25; i++) {
            oneAM.register(students[i]);
        }
        assertFalse(students[25].inquire(oneAM));
    }

    @Test
    public final void testWithdraw() {
        assertEquals(0, oneAM.roster.size());
        for (int i = 0; i < 5; i++) {
            oneAM.register(students[i]);
        }
        assertEquals(5, oneAM.roster.size());
        for (int i = 0; i < 5; i++) {
            oneAM.withdraw(students[i]);
        }
        assertEquals(0, oneAM.roster.size());
    }

}
