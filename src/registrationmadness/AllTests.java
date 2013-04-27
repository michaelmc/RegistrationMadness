package registrationmadness;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

    @RunWith(value=Suite.class)
    @Suite.SuiteClasses(value = {
            StudentTest.class,
            SectionTest.class,
            RegistrationMadnessTest.class
    })
        
    public class AllTests { }