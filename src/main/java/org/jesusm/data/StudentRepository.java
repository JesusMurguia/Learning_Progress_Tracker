package org.jesusm.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {

    private static StudentRepository INSTANCE;
    private final List<Student> studentList = new ArrayList<>();
    private StudentRepository(){}

    public List<Student> getStudentList(){
        return studentList;
    }

    public void save(Student student){
        studentList.add(student);
    }

    public void update(Student student){
        for(Student s : studentList){
            if(s.getId().equals(student.getId())){
                s.setCourses(student.getCourses());
                break;
            }
        }
    }

    public Optional<Student> findById(String id) {
        return studentList.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public static StudentRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new StudentRepository();
        }
        return INSTANCE;
    }




}
