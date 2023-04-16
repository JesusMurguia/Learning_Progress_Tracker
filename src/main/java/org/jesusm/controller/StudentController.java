package org.jesusm.controller;

import org.jesusm.data.Student;
import org.jesusm.data.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;


public class StudentController {
    StudentRepository studentRepository = StudentRepository.getInstance();

    public List<String> getStudentIds(){
        return studentRepository.getStudentList().stream().map(Student::getId).collect(Collectors.toList());
    }
    public boolean save(String credentials){
        Student student = getStudentObjectFromCredentials(credentials);
        if(isStudentValid(student)){
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    public boolean update(String progressData){
            if(isProgressDataValid(progressData)){
                String[] data = progressData.split(" ");
                String id = data[0];
                int[] points = Arrays.stream(data).skip(1).mapToInt(Integer::parseInt).toArray();
                Student student = findById(id).get();
                for(int i = 0; i < CourseController.courseNames.length; i++){
                    Integer value = student.getCourses().get(CourseController.courseNames[i]);
                    student.getCourses().put(CourseController.courseNames[i], value+points[i]);
                }
                studentRepository.update(student);
                return true;
            }
        return false;
    }

    private boolean isProgressDataValid(String progressData){
        var input = progressData.split(" ");

        if(!existsById(input[0])){
            System.out.printf("No student is found for id=%s\n", input[0]);
            return false;
        }
        try{
            if(input.length != 5 || Arrays.stream(input).skip(1).anyMatch(s -> Integer.parseInt(s) < 0)){
                System.out.println("Incorrect points format.");
                return false;
            }
        }catch(NumberFormatException exception){
            System.out.println("Incorrect points format.");
            return false;
        }

        return true;
    }

    public Optional<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    public boolean existsById(String id){
        return findById(id).isPresent();
    }

    public boolean isEmailAlreadyInUse(String email){
        return studentRepository.getStudentList().stream().anyMatch(student -> student.getEmail().equals(email));
    }

    private boolean isStudentValid(Student student){
        if(student == null){
            System.out.println("Incorrect credentials");
            return false;
        }
        if(!isNameValid(student.getFirstName())){
            System.out.println("Incorrect first name");
            return false;
        }
        if(!Arrays.stream(student.getLastName().split(" ")).allMatch(this::isNameValid)){
            System.out.println("Incorrect last name");
            return false;
        }
        if(!isEmailValid(student.getEmail())){
            System.out.println("Incorrect email");
            return false;
        }
        if(isEmailAlreadyInUse(student.getEmail())){
            System.out.println("This email is already taken.");
            return false;
        }

        return true;
    }

    private Student getStudentObjectFromCredentials(String credentials){
        String[] credentialsArr = credentials.split(" ");
        if(credentialsArr.length < 3){
            return null;
        }

        String firstName = credentialsArr[0];
        String lastName = String.join(" ",Arrays.copyOfRange(credentialsArr,1, credentialsArr.length-1));
        String email = credentialsArr[credentialsArr.length-1];

        return new Student(String.valueOf(studentRepository.getStudentList().size()+1),firstName, lastName, email);
    }

    private boolean isNameValid(String name) {
        return name.matches("^[A-Za-z](?!.*?[ '-]{2,})[A-Za-z '-]*[A-Za-z]$");
    }

    private boolean isEmailValid(String email) {
        return email.matches("^[^@]+@[^@]+\\.[^@]+$");
    }


}
