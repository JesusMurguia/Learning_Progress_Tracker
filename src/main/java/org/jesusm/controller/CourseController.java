package org.jesusm.controller;
import org.jesusm.IndividualStats;
import org.jesusm.data.Email;
import org.jesusm.data.Stats;
import org.jesusm.data.Student;
import org.jesusm.data.StudentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


public class CourseController {
    StudentRepository studentRepository = StudentRepository.getInstance();


    public static final String[] courseNames = new String[]{"Java", "DSA","Databases","Spring"};
    public static final Map<String, Integer> maxPoints = new HashMap<>(Map.of(courseNames[0],600,courseNames[1],400,courseNames[2],480,courseNames[3],550));

    public Stats getStatistics() {
        Map<String, Integer> countedCourses = getCountedCourses();
        Map<String, Double> averageScores = getAverageScores();

        if (countedCourses.isEmpty() || averageScores.isEmpty()) {
            return new Stats("n/a", "n/a", "n/a", "n/a", "n/a", "n/a");
        }

        String mostPopular = getCoursesByValue(Collections.max(countedCourses.values()), countedCourses);
        String leastPopular = getCoursesByValue(Collections.min(countedCourses.values()), countedCourses);
        String highestActivity = mostPopular; // idk why they are the same
        String lowestActivity = leastPopular;  // but they are
        String easiestCourse = getCoursesByValue(Collections.max(averageScores.values()), averageScores);
        String hardestCourse = getCoursesByValue(Collections.min(averageScores.values()), averageScores);

        return new Stats(mostPopular, leastPopular.equals(mostPopular) ? "n/a" : leastPopular, highestActivity, lowestActivity.equals(highestActivity) ? "n/a" : lowestActivity, easiestCourse, hardestCourse.equals(easiestCourse) ? "n/a" : hardestCourse);
    }


    private Map<String, Double> getAverageScores() {
        return studentRepository.getStudentList().stream()
                .flatMap(student -> student.getCourses().entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.averagingInt(Map.Entry::getValue)
                ));
    }

    private Map<String, Integer> getCountedCourses(){
        return studentRepository.getStudentList().stream()
                .flatMap(student -> student.getCourses().entrySet().stream())
                .filter(course -> course.getValue() > 0)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingInt(e -> 1)
                ));
    }

    private <T> String getCoursesByValue(T value, Map<String, T> courses) {
        List<String> coursesWithValue = courses.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .toList();

        if (coursesWithValue.isEmpty()) {
            return "n/a";
        } else {
            return String.join(", ", coursesWithValue);
        }
    }

    public Optional<List<IndividualStats>> getIndividualStats(String course) {
        if(!maxPoints.containsKey(course)){
            return Optional.empty();
        }
        int requiredPoints = maxPoints.get(course);
        List<IndividualStats> individualStats = new ArrayList<>();

        for(Student student : studentRepository.getStudentList()){
            int points = student.getCourses().get(course);
            if(points == 0) continue;
            String completed = new BigDecimal((double)points / requiredPoints).setScale(3, RoundingMode.HALF_UP).scaleByPowerOfTen(2) + "%";
            individualStats.add(new IndividualStats(student.getId(), points, completed));
        }

        return Optional.of(individualStats.stream().sorted(Comparator.comparingInt(IndividualStats::points).reversed().thenComparing(IndividualStats::id)).toList());
    }

    public List<Email> getPendingNotifications(){
        List<Email> mailingList = new ArrayList<>();
        for(Student student : studentRepository.getStudentList()){
            for(Map.Entry<String,Integer> course : maxPoints.entrySet()){
                if(Objects.equals(student.getCourses().get(course.getKey()), course.getValue()) && !student.getCompletedCourses().contains(course.getKey())){
                    student.getCompletedCourses().add(course.getKey());
                    mailingList.add(new Email(
                            student.getEmail(),
                            "Your Learning Progress",
                            "Hello, %s! You have accomplished our %s course!".formatted(student.getFirstName()+" "+student.getLastName(), course.getKey())
                            ));
                }
            }
        }
        return mailingList;
    };


}
