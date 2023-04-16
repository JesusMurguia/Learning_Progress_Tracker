package org.jesusm;

import org.jesusm.controller.CourseController;
import org.jesusm.controller.StudentController;
import org.jesusm.data.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class StudentControllerTest {
    StudentController studentController;
    CourseController courseController;

    @BeforeEach
    public void init(){
        StudentRepository.getInstance().getStudentList().clear();
        studentController = new StudentController();
        courseController = new CourseController();
    }

    @Test
    void JohnDoeValidEmailShouldBeTrue() {
        Assertions.assertTrue(studentController.save("John Doe jdoe@mail.net"));
    }

    @Test
    void JaneDoeValidEmailShouldBeTrue() {
        Assertions.assertTrue(studentController.save("Jane Doe jane.doe@yahoo.com"));
    }

    @Test
    void randomCommandShouldBeFalse() {
        Assertions.assertFalse(studentController.save("help"));
    }

    @Test
    void invalidEmailShouldBeFalse() {
        Assertions.assertFalse(studentController.save("John Doe email"));
    }

    @Test
    void invalidNameShouldBeFalse() {
        Assertions.assertFalse(studentController.save("J. Doe name@domain.com"));
    }

    @Test
    void invalidLastNameShouldBeFalse() {
        Assertions.assertFalse(studentController.save("John D. name@domain.com"));
    }

    @Test
    void nameWithHyphensInTheMiddleShouldBeTrue() {
        Assertions.assertTrue(studentController.save("Jean-Clause van Helsing jc@google.it"));
    }

    @Test
    void MaryLuiseWithValidEmailShouldBeTrue() {
        Assertions.assertTrue(studentController.save("Mary Luise Johnson maryj@google.com"));
    }

    @Test
    void nonASCIICharsShouldBeFalse() {
        Assertions.assertFalse(studentController.save("陳 港 生"));
    }

    @Test
    void firstNamesWithHyphensInBeginningShouldBeFalse() {
        Assertions.assertFalse(studentController.save("-Mary Luise Johnson maryj@google.com"));
    }
    @Test
    void lastNamesWithHyphensInBeginningShouldBeFalse() {
        Assertions.assertFalse(studentController.save("-Mary Luise -Johnson maryj@google.com"));
    }

    @Test
    void moreThanOneHyphenConsecutivelyShouldBeFalse() {
        Assertions.assertFalse(studentController.save("Mary--Luise Johnson maryj@google.com"));
    }

    @Test
    void shouldBeTrue() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
    }
    @Test
    void isEmailWithNumbersNameValid() {
        Assertions.assertTrue(studentController.save("Mary Emelianenko 125367at@zzz90.z9"));
    }

    @Test
    void duplicatedEmailShouldBeFalse() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        Assertions.assertTrue(studentController.isEmailAlreadyInUse("jcda123@google.net"));
        Assertions.assertFalse(studentController.save("John Doe jcda123@google.net"));
    }

    @Test
    void uniqueEmailShouldBeTrue() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        Assertions.assertFalse(studentController.isEmailAlreadyInUse("jcda1234@google.net"));
        Assertions.assertTrue(studentController.save("John Doe jcda1234@google.net"));
    }

    @Test
    void validIdAndPointsShouldUpdate() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertTrue(studentController.update(id+ " 1 0 0 0"));
        var studentOptional = studentController.findById(id);
        Assertions.assertTrue(studentOptional.isPresent());

        Assertions.assertIterableEquals(List.of(1,0,0,0), studentOptional.get().getPoints());
    }

    @Test
    void invalidIdAndValidPointsShouldNotUpdate() {
        String id = "invalid_id";
        Assertions.assertFalse(studentController.update(id+ " 1 0 0 0"));
    }

    @Test
    void invalidIdAndInvalidPointsShouldNotUpdate() {
        String id = "invalid_id";
        Assertions.assertFalse(studentController.update(id+ " 1 0 0 0 0"));
    }

    @Test
    void validIdAndInvalidPointsShouldNotUpdate() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertFalse(studentController.update(id+ " 1 -1 0 0"));
    }

    @Test
    void mostPopularShouldBeJava() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertTrue(studentController.update(id+ " 4 0 0 0"));
        var studentOptional = studentController.findById(id);
        Assertions.assertTrue(studentOptional.isPresent());

        Map<String, Integer> expected = new HashMap<>(Map.of("Java",4,"DSA",0,"Databases",0,"Spring",0));

        Assertions.assertEquals(expected, studentOptional.get().getCourses());

        Assertions.assertEquals("Java", courseController.getStatistics().mostPopular());
    }


    @Test
    void mostPopularShouldBeSpring() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertTrue(studentController.update(id+ " 0 0 0 6"));
        var studentOptional = studentController.findById(id);
        Assertions.assertTrue(studentOptional.isPresent());

        Map<String, Integer> expected = new HashMap<>(Map.of("Java",0,"DSA",0,"Databases",0,"Spring",6));

        Assertions.assertEquals(expected, studentOptional.get().getCourses());

        Assertions.assertEquals("Spring", courseController.getStatistics().mostPopular());
    }

    @Test
    void mostPopularShouldBeJavaAndSpring() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertTrue(studentController.update(id+ " 6 0 0 6"));
        var studentOptional = studentController.findById(id);
        Assertions.assertTrue(studentOptional.isPresent());

        Map<String, Integer> expected = new HashMap<>(Map.of("Java",6,"DSA",0,"Databases",0,"Spring",6));

        Assertions.assertEquals(expected, studentOptional.get().getCourses());


        Assertions.assertEquals("Java, Spring", courseController.getStatistics().mostPopular());
    }

    @Test
    void JohnDoeShouldBeNo1InJava() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertTrue(studentController.update(id+ " 6 0 0 6"));
        var studentOptional = studentController.findById(id);
        Assertions.assertTrue(studentOptional.isPresent());

        Assertions.assertTrue(studentController.save("John Doe jcda1243@google.net"));
        String id2 = (String) studentController.getStudentIds().toArray()[1];
        Assertions.assertTrue(studentController.update(id2+ " 12 1 2 0"));
        var studentOptional2 = studentController.findById(id2);
        Assertions.assertTrue(studentOptional2.isPresent());


        Map<String, Integer> expected1 = new HashMap<>(Map.of("Java",6,"DSA",0,"Databases",0,"Spring",6));

        Assertions.assertEquals(expected1, studentOptional.get().getCourses());

        Map<String, Integer> expected2 = new HashMap<>(Map.of("Java",12,"DSA",1,"Databases",2,"Spring",0));

        Assertions.assertEquals(expected2, studentOptional2.get().getCourses());

        Optional<List<IndividualStats>> individualStats = courseController.getIndividualStats("Java");
        Assertions.assertTrue(individualStats.isPresent());
        Assertions.assertEquals(id2,courseController.getIndividualStats("Java").get().get(0).id());

    }

    @Test
    void JeanClaudeShouldBeNo1InSpring() {
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertTrue(studentController.update(id+ " 6 0 0 6"));
        var studentOptional = studentController.findById(id);
        Assertions.assertTrue(studentOptional.isPresent());

        Assertions.assertTrue(studentController.save("John Doe jcda1243@google.net"));
        String id2 = (String) studentController.getStudentIds().toArray()[1];
        Assertions.assertTrue(studentController.update(id2+ " 12 1 2 0"));
        var studentOptional2 = studentController.findById(id2);
        Assertions.assertTrue(studentOptional2.isPresent());


        Map<String, Integer> expected1 = new HashMap<>(Map.of("Java",6,"DSA",0,"Databases",0,"Spring",6));

        Assertions.assertEquals(expected1, studentOptional.get().getCourses());

        Map<String, Integer> expected2 = new HashMap<>(Map.of("Java",12,"DSA",1,"Databases",2,"Spring",0));

        Assertions.assertEquals(expected2, studentOptional2.get().getCourses());

        Optional<List<IndividualStats>> individualStats = courseController.getIndividualStats("Java");
        Assertions.assertTrue(individualStats.isPresent());
        Assertions.assertEquals(id,courseController.getIndividualStats("Spring").get().get(0).id());
    }

    @Test
    void shouldNotifyOneStudent(){
        Assertions.assertTrue(studentController.save("Jean-Claude O'Connor jcda123@google.net"));
        String id = (String) studentController.getStudentIds().toArray()[0];
        Assertions.assertTrue(studentController.update(id+ " 600 0 0 6"));
        var studentOptional = studentController.findById(id);
        Assertions.assertTrue(studentOptional.isPresent());

        Map<String, Integer> expected = new HashMap<>(Map.of("Java",600,"DSA",0,"Databases",0,"Spring",6));

        Assertions.assertEquals(expected, studentOptional.get().getCourses());

        var emailList = courseController.getPendingNotifications();
        System.out.println(emailList);
        Assertions.assertEquals(1, emailList.size());
        emailList = courseController.getPendingNotifications();
        Assertions.assertEquals(0, emailList.size());

    }
}