package org.jesusm.data;

import java.util.*;


public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Map<String, Integer> courses;

    private Set<String> completedCourses;

    public Student(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.courses = new HashMap<>(Map.of("Java",0,"DSA",0,"Databases",0,"Spring",0));
        this.completedCourses = new HashSet<>();
    }
    @Override
    public String toString() {
        return  id+" points: Java=%d; DSA=%d; Databases=%d; Spring=%d:".formatted(courses.values().toArray());
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, Integer> getCourses() {
        return courses;
    }

    public Collection<Integer> getPoints() {
        return courses.values();
    }
    public void setCourses(Map<String,Integer> points) {
        this.courses = points;
    }


    public Set<String> getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(Set<String> completedCourses) {
        this.completedCourses = completedCourses;
    }
}
