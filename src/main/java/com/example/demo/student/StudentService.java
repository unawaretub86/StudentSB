package com.example.demo.student;

import org.hibernate.internal.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//we use here a service , cauase its not a controller in controller we use controller :)
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll(); //this return a lit
    }

//    validacion para correo
    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.finStudentByEmail(student.getEmail());

        if(studentOptional.isPresent()){
            throw new IllegalMonitorStateException("email taken");
        }
        studentRepository.save(student);
        System.out.println(student);
    }

    public void deleteStudent(Long studentId) {
       boolean exists =  studentRepository.existsById(studentId);
       if(!exists){
           throw new IllegalStateException(("student with id" + studentId + "does not exist"));
       }
       studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow(()-> new IllegalStateException("student with id " +studentId + "does not exist"));

        if(name != null && name.length()>0 && !Objects.equals(student.getName(), name)){
            student.setName(name);
        }

        if(email != null && email.length()>0 && !Objects.equals(student.getEmail(), email)){
            Optional<Student> studentOptional = studentRepository.finStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }
}
