package com.proyectointegrado.reina_cabrera_david.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyectointegrado.reina_cabrera_david.bean.Student;
import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.bean.UserRequest;
import com.proyectointegrado.reina_cabrera_david.constants.ApiConstants;
import com.proyectointegrado.reina_cabrera_david.service.StudentService;
import com.proyectointegrado.reina_cabrera_david.service.TeacherService;
import com.proyectointegrado.reina_cabrera_david.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ApiConstants.ENDPOINT)
public class Controller {

	private UserService userService;
	
	private StudentService studentService;
	
	private TeacherService teacherService;

	protected Controller(UserService userService, StudentService studentService, TeacherService teacherService) {
		this.userService = userService;
		this.studentService = studentService;
		this.teacherService = teacherService;
	}

	// Users end points
	@GetMapping("login")
	public User login(@RequestParam String corporateMail, @RequestParam String password) {
		return userService.login(corporateMail, password);
	}
	
	@PostMapping("sign-up")
	public ResponseEntity<String> signUp(@RequestBody UserRequest request) {
		userService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("users-list")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@PutMapping("update-user")
	public ResponseEntity<String> updateUser(@RequestBody User request) {
		userService.updateUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// Students end points
	
	@PostMapping("save-student")
	public ResponseEntity<String> saveStudent(@RequestBody Student request) {
		studentService.saveStudent(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("students-list")
	public List<Student> getAllStudents() {
		return studentService.getAllStudents();
	}
	
	@PutMapping("update-student")
	public ResponseEntity<String> updateStudent(@RequestBody Student request) {
		studentService.updateStudent(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// Teachers end points
	
	@PostMapping("save-teacher")
	public ResponseEntity<String> saveTeacher(@RequestBody Teacher request) {
		teacherService.saveTeacher(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("teachers-list")
	public List<Teacher> getAllTeachers() {
		return teacherService.getAllTeachers();
	}
	
	@PutMapping("update-teacher")
	public ResponseEntity<String> updateTeacher(@RequestBody Teacher request) {
		teacherService.updateTeacher(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
