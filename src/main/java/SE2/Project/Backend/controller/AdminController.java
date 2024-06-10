package SE2.Project.Backend.controller;

import SE2.Project.Backend.model.*;
import SE2.Project.Backend.repository.*;
import SE2.Project.Backend.service.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.expression.Strings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountantRepository accountantRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private TimetableRepository timetableRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @ModelAttribute("admin")
    public Admin admin() {
        Admin admin= new Admin();
        User user = new User();
        user.setRole("Admin");
        admin.setUser(user);
        return admin;
    }

    @ModelAttribute("student")
    public Student student() {
        Student student= new Student();
        User user = new User();
        user.setRole("Student");
        student.setUser(user);
        return student;
    }

    @ModelAttribute("teacher")
    public Teacher teacher() {
        Teacher teacher=new Teacher();
        User user = new User();
        user.setRole("Teacher");
        teacher.setUser(user);
        return teacher;
    }

    @ModelAttribute("accountant")
    public Accountant accountant() {
        Accountant accountant=new Accountant();
        User user = new User();
        user.setRole("Accountant");
        accountant.setUser(user);
        return accountant;
    }

    @ModelAttribute("semester")
    public Semester semester() {
        return new Semester();
    }

    @ModelAttribute("major")
    public Major major() {
        return new Major();
    }
    @ModelAttribute("user")
    public User user(HttpSession session) {
        CustomUserDetails userDetail = (CustomUserDetails) session.getAttribute("userDetails");
        return userRepository.findByUsername(userDetail.getUsername());
    }

    @ModelAttribute("course")
    public Course course() {
        return new Course();
    }
    @ModelAttribute("classroom")
    public Classroom classroom() {
        return new Classroom();
    }
    @ModelAttribute("timetable")
    public Timetable timetable() {
        return new Timetable();
    }
    @ModelAttribute("enrollment")
    public Enrollment enrollment() {
        return new Enrollment();
    }


    // CRUD admin
    @GetMapping("/addUser")
    public String addUser(){
        return "admin-account-management";
    }
    @RequestMapping(value="/adminDetails/{adminCode}")
    public String adminDetails(@PathVariable Integer adminCode, Model model){
        Admin admin=adminRepository.findByAdminCode(adminCode);
        model.addAttribute("admin", admin);
        return "admin-account-details";
    }
    @RequestMapping(value = "/insertAdmin")
    public String insertAdmin(@Valid Admin admin, BindingResult result) {

        if(result.hasErrors()){
            return "admin-account-management";
        } else if (isDuplicateEntry(admin.getUser().getUsername())) {
            result.rejectValue("user.username", "duplicate.key", "Username already exists");
            return "admin-account-management";
        }
        User user = admin.getUser();
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        admin.setUser(user);
        userRepository.save(user);
        adminRepository.save(admin);

        return "redirect:/admin/listAdmin";
    }

    @GetMapping(value = "/listAdmin")
    public String showAllAdmin(Model model, @RequestParam(name = "adminCode", required = false) Integer adminCode,
                               @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Admin> admins;
        Admin admin;
        if (showAll != null) {
            admins = adminRepository.findAll();
            model.addAttribute("admins", admins);
            return "admin-account-management";
        }
        if(adminCode != null){
            admin = adminRepository.findByAdminCode(adminCode);
            if(admin != null){
                model.addAttribute("admins", admin);
            } else {
                model.addAttribute("notFoundMessage", "No Admin found with the provided admin code");
            }
        } else {
            admins = adminRepository.findAll();
            model.addAttribute("admins", admins);
        }

        return "admin-account-management";
    }

    @GetMapping(value = "/adminDetail/{adminCode}")
    public String showAdminDetail(@PathVariable Integer adminCode, Model model){
        Admin admin = adminRepository.findByAdminCode(adminCode);
        model.addAttribute("admin", admin);
        return "admin-detail";
    }

    @GetMapping(value = "/updateAdmin/{adminCode}")
    public String updateAdmin(@PathVariable Integer adminCode, Model model){
        Admin admin = adminRepository.findByAdminCode(adminCode);
        model.addAttribute("admin", admin);
        return "admin-detail-edit";
    }

    @PostMapping(value = "/saveAdmin")
    public String saveAdmin(@Valid Admin admin, BindingResult result){
        if(result.hasErrors()){
            return "admin-detail-edit";
        }
        User user = admin.getUser();
        userRepository.save(user);
        adminRepository.save(admin);
        return "redirect:/admin/listAdmin";
    }

    @RequestMapping(value = "/deleteAdmin/{adminCode}")
    public String deleteAdmin(@PathVariable Integer adminCode){
        Admin admin = adminRepository.findByAdminCode(adminCode);
        User user = admin.getUser();
        adminRepository.delete(admin);
        userRepository.delete(user);
        return "redirect:/admin/listAdmin";
    }


















    // CRUD student

    @RequestMapping(value = "/insertStudent")
    public String insertStudent(@Valid Student student, BindingResult result) {

        if(result.hasErrors()){
            return "student-account-management";
        } else if (isDuplicateEntry(student.getUser().getUsername())) {
            result.rejectValue("user.username", "duplicate.key", "Username already exists");
            return "student-account-management";
        }
        User user = student.getUser();
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setRole("Student");
        student.setUser(user);
        userRepository.save(user);
        studentRepository.save(student);

        return "redirect:/admin/listStudent";
    }

    @GetMapping(value = "/listStudent")
    public String showAllStudents(Model model, @RequestParam(name = "studentCode", required = false) Integer studentCode,
                                  @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Student> students;
        Student student;
        Iterable<Major> majors;
        if (showAll != null) {
            students = studentRepository.findAll();
            model.addAttribute("students", students);
            return "student-account-management";
        }
        if(studentCode != null){
            student = studentRepository.findByStudentCode(studentCode);
            if(student != null){
                model.addAttribute("students", student);
            } else {
                model.addAttribute("notFoundMessage", "No student found with the provided student code");
            }
        } else {
            majors=majorRepository.findAll();
            students = studentRepository.findAll();
            model.addAttribute("students", students);
            model.addAttribute("majors",majors);
        }

        return "student-account-management";
    }

    @GetMapping(value = "/studentDetail/{studentCode}")
    public String showStudentDetail(@PathVariable Integer studentCode, Model model){
        Student student = studentRepository.findByStudentCode(studentCode);
        model.addAttribute("student", student);
        return "student-detail";
    }



    @GetMapping(value = "/updateStudent/{studentCode}")
    public String updateStudent(@PathVariable Integer studentCode, Model model){
        Iterable<Major> majors;
        Major major;
        Student student = studentRepository.findByStudentCode(studentCode);
        model.addAttribute("student", student);
        majors = majorRepository.findAll();
        model.addAttribute("majors", majors);
        return "student-detail-edit";
    }


    @PostMapping("/saveStudent")
    public String saveStudent(@Valid @ModelAttribute("student") Student student, BindingResult result) {
        if (result.hasErrors()) {
            return "student-detail-edit"; // Return to the update form if there are validation errors
        }
        User user = student.getUser();
        userRepository.save(user);
        // Save the student
        studentRepository.save(student);

        // Redirect to the student list page
        return "redirect:/admin/listStudent";
    }

    @RequestMapping(value = "/deleteStudent/{studentCode}")
    public String deleteStudent(@PathVariable Integer studentCode){
        Student student = studentRepository.findByStudentCode(studentCode);
        User user = student.getUser();
        studentRepository.delete(student);
        userRepository.delete(user);
        return "redirect:/admin/listStudent";
    }
















    // CRUD teacher

    @RequestMapping(value = "/insertTeacher")
    public String insertTeacher(@Valid Teacher teacher, BindingResult result) {
        if(result.hasErrors()){
            return "teacher-account-management";
        } else if (isDuplicateEntry(teacher.getUser().getUsername())) {
            result.rejectValue("user.username", "duplicate.key", "Username already exists");
            return "redirect:/admin/listTeacher";
        }

        User user = teacher.getUser();
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        teacherRepository.save(teacher);

        return "redirect:/admin/listTeacher";
    }

    private boolean isDuplicateEntry(String username) {
        return userRepository.existsByUsername(username);
    }

    @GetMapping(value = "/listTeacher")
    public String showAllTeachers(Model model, @RequestParam(name = "teacherCode", required = false) Integer teacherCode,
                                  @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Teacher> teachers;
        Teacher teacher;
        Iterable<Major> majors;
        if (showAll != null) {
            teachers = teacherRepository.findAll();
            model.addAttribute("teachers", teachers);
            return "teacher-account-management";
        }
        if(teacherCode != null){
            teacher = teacherRepository.findByTeacherCode(teacherCode);
            if(teacher != null){
                model.addAttribute("teachers", teacher);
            } else {
                model.addAttribute("notFoundMessage", "No teacher found with the provided teacher id");
            }
        } else {
            majors = majorRepository.findAll();
            model.addAttribute("majors", majors);
            teachers = teacherRepository.findAll();
            model.addAttribute("teachers", teachers);
        }

        return "teacher-account-management";
    }

    @GetMapping(value = "/teacherDetail/{teacherCode}")
    public String showTeacherDetail(@PathVariable Integer teacherCode, Model model){
        Teacher teacher = teacherRepository.findByTeacherCode(teacherCode);
        model.addAttribute("teacher", teacher);
        return "teacher-detail";}

    @GetMapping(value = "/updateTeacher/{teacherCode}")
    public String updateTeacher(@PathVariable Integer teacherCode, Model model){
        Teacher teacher = teacherRepository.findByTeacherCode(teacherCode);
        Iterable<Major> majors=majorRepository.findAll();
        model.addAttribute("majors",majors);
        model.addAttribute("teacher", teacher);
        return "teacher-detail-edit";
    }

    @PostMapping(value = "/saveTeacher")
    public String saveTeacher(@Valid Teacher teacher, BindingResult result){
        if(result.hasErrors()){
            return "teacher-detail-edit";
        }
        User user = teacher.getUser();
        userRepository.save(user);
        teacherRepository.save(teacher);
        return "redirect:/admin/listTeacher";
    }

    @RequestMapping(value = "/deleteTeacher/{teacherCode}")
    public String deleteTeacher(@PathVariable Integer teacherCode){
        Teacher teacher = teacherRepository.findByTeacherCode(teacherCode);
        User user = teacher.getUser();
        teacherRepository.delete(teacher);
        userRepository.delete(user);
        return "redirect:/admin/listTeacher";
    }
















    // CRUD accountant

    @RequestMapping(value = "/insertAccountant")
    public String insertAccountant(@Valid Accountant accountant, BindingResult result) {
        if(result.hasErrors()){
            return "accountant-account-management";
        } else if (isDuplicateEntry(accountant.getUser().getUsername())) {
            result.rejectValue("user.username", "duplicate.key", "Username already exists");
            return "accountant-account-management";
        }

        User user = accountant.getUser();
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        accountant.setUser(user);
        userRepository.save(user);
        accountantRepository.save(accountant);

        return "redirect:/admin/listAccountant";
    }
    @GetMapping(value = "/listAccountant")
    public String showAllAccountant(Model model, @RequestParam(name = "accountantCode", required = false) Integer accountantCode,
                                    @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Accountant> accountants;
        Accountant accountant;
        if (showAll != null) {
            accountants = accountantRepository.findAll();
            model.addAttribute("accountants", accountants);
            return "accountant-account-management";
        }
        if(accountantCode != null){
            accountant = accountantRepository.findByAccountantCode(accountantCode);
            if(accountant != null){
                model.addAttribute("accountants", accountant);
            } else {
                model.addAttribute("notFoundMessage", "No accountant found with the provided teacher id");
            }
        } else {
            accountants = accountantRepository.findAll();
            model.addAttribute("accountants", accountants);
        }

        return "accountant-account-management";
    }
    @GetMapping(value = "/accountantDetail/{accountantCode}")
    public String showAccountantDetail(@PathVariable Integer accountantCode, Model model){
        Accountant accountant = accountantRepository.findByAccountantCode(accountantCode);
        model.addAttribute("accountant", accountant);
        return "accountant-detail";
    }
    @GetMapping(value = "/updateAccountant/{accountantCode}")
    public String updateAccountant(@PathVariable Integer accountantCode, Model model){
        Accountant accountant = accountantRepository.findByAccountantCode(accountantCode);
        model.addAttribute("accountant", accountant);
        return "accountant-detail-edit";
    }

    @PostMapping(value = "/saveAccountant")
    public String saveAccountant(@Valid Accountant accountant, BindingResult result){
        if(result.hasErrors()){
            return "accountant-detail-edit";
        }
        User user = accountant.getUser();
        userRepository.save(user);
        accountantRepository.save(accountant);
        return "redirect:/admin/listAccountant";
    }

    @RequestMapping(value = "/deleteAccountant/{accountantCode}")
    public String deleteAccountant(@PathVariable Integer accountantCode){
        Accountant accountant = accountantRepository.findByAccountantCode(accountantCode);
        User user = accountant.getUser();
        accountantRepository.delete(accountant);
        userRepository.delete(user);
        return "redirect:/admin/listAccountant";
    }











    // CRUD major

    @RequestMapping(value = "/insertMajor")
    public String insertMajor(@Valid Major major, BindingResult result) {
        if(result.hasErrors()){
            return "major-management";
        }else if (isDuplicateMajorName(major.getMajorName())) {
            result.rejectValue("majorName", "duplicate.key", "Major Name already exists");
            return "major-management";
        }
        majorRepository.save(major);
        return "redirect:/admin/listMajor";
    }

    private boolean isDuplicateMajorName(String majorName) {
        return majorRepository.existsByMajorName(majorName);
    }

    @GetMapping(value = "/listMajor")
    public String showAllMajor(Model model, @RequestParam(name = "majorId", required = false) Long majorId,
                               @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Major> majors;
        Major major;
        if (showAll != null) {
            majors = majorRepository.findAll();
            model.addAttribute("majors", majors);
            return "major-management";
        }
        if(majorId != null){
            major = majorRepository.findByMajorId(majorId);
            if(major != null){
                model.addAttribute("majors", major);
            } else {
                model.addAttribute("notFoundMessage", "No major found with the provided teacher id");
            }
        } else {
            majors = majorRepository.findAll();
            model.addAttribute("majors", majors);
        }

        return "major-management";
    }

    @GetMapping(value = "/updateMajor/{majorId}")
    public String updateMajor(@PathVariable Long majorId, Model model){
        Major major = majorRepository.findByMajorId(majorId);
        model.addAttribute("major", major);
        return "major-detail-edit";
    }

    @PostMapping(value = "/saveMajor")
    public String saveMajor(@Valid Major major, BindingResult result){
        if(result.hasErrors()){
            return "major-detail-edit";
        }
        majorRepository.save(major);
        return "redirect:/admin/listMajor";
    }
    @RequestMapping(value = "/deleteMajor/{majorId}")
    public String deleteMajor(@PathVariable Long majorId){
        Major major = majorRepository.findByMajorId(majorId);
        majorRepository.delete(major);
        return "redirect:/admin/listMajor";
    }







    // CRUD semester
    @GetMapping("/addSemester")
    public String addSemester(Model model) {
        model.addAttribute("semester", new Semester());

        return "semester-management";
    }
    @RequestMapping(value = "/insertSemester")
    public String insertSemester(@Valid Semester semester, BindingResult result) {
        if(result.hasErrors()){
            return "semester-management";
        }else if (isDuplicateSemester(semester.getStartYear(), semester.getEndYear(), semester.getSemesterNum())) {
            result.rejectValue("startYear", "duplicate.key", "Semester already exists");
            return "semester-management";
        }
        semesterRepository.save(semester);
        return "redirect:/admin/listSemester";
    }
    private boolean isDuplicateSemester(Integer startYear, Integer endYear, Integer semesterNum) {
        return semesterRepository.existsByStartYearAndEndYearAndSemesterNum(startYear, endYear, semesterNum);

    }

    @GetMapping(value = "/listSemester")
    public String showAllSemester(Model model, @RequestParam(name = "semesterId", required = false) Long semesterId,
                                  @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Semester> semesters;
        Semester semester;
        if (showAll != null) {
            semesters = semesterRepository.findAll();
            model.addAttribute("semesters", semesters);
            return "semester-management";
        }
        if(semesterId != null){
            semester = semesterRepository.findBySemesterId(semesterId);
            if(semester != null){
                model.addAttribute("semesters", semester);
            } else {
                model.addAttribute("notFoundMessage", "No semester found with the provided teacher id");
            }
        } else {
            semesters = semesterRepository.findAll();
            model.addAttribute("semesters", semesters);
        }

        return "semester-management";
    }


    @GetMapping(value = "/updateSemester/{semesterId}")
    public String updateSemester(@PathVariable Long semesterId, Model model){
        Semester semester = semesterRepository.findBySemesterId(semesterId);
        model.addAttribute("semester", semester);
        return "semester-detail-edit";
    }

    @PostMapping(value = "/saveSemester")
    public String saveSemester(@Valid Semester semester, BindingResult result){
        if(result.hasErrors()){
            return "semester-detail-edit";
        }
        semesterRepository.save(semester);
        return "redirect:/admin/listSemester";
    }
    @RequestMapping(value = "/deleteSemester/{semesterId}")
    public String deleteSemester(@PathVariable Long semesterId){
        Semester semester = semesterRepository.findBySemesterId(semesterId);
        semesterRepository.delete(semester);
        return "redirect:/admin/listSemester";
    }







    // CRUD classroom
    @GetMapping("/addClassroom")
    public String addClassroom(Model model) {
        model.addAttribute("classroom", new Classroom());
        return "classroom-management";
    }
    @RequestMapping(value = "/insertClassroom")
    public String insertClassroom(@Valid Classroom classroom, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "classroom-management";
        }else if (isDuplicateClassroom(classroom.getRoomNumber())) {
            result.rejectValue("roomNumber", "duplicate.key", "Classroom already exists");
            return "classroom-management";
        }
        classroomRepository.save(classroom);
        return "redirect:/admin/listClassroom";
    }
    private boolean isDuplicateClassroom(String roomNumber) {
        return classroomRepository.existsByRoomNumber(roomNumber);
    }

    @GetMapping(value = "/listClassroom")
    public String showAllClassroom(Model model, @RequestParam(name = "classroomId", required = false) Long classroomId,
                                   @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Classroom> classrooms;
        Classroom classroom;
        if (showAll != null) {
            classrooms = classroomRepository.findAll();
            model.addAttribute("classrooms", classrooms);
            return "classroom-management";
        }
        if(classroomId != null){
            classroom = classroomRepository.findByClassroomId(classroomId);
            if(classroom!= null){
                model.addAttribute("classrooms", classroom);
            } else {
                model.addAttribute("notFoundMessage", "No Classroom found with the provided teacher id");
            }
        } else {
            classrooms = classroomRepository.findAll();
            model.addAttribute("classrooms", classrooms);
        }

        return "classroom-management";
    }

    @GetMapping(value = "/updateClassroom/{classroomId}")
    public String updateClassroom(@PathVariable Long classroomId, Model model){
        Classroom classroom = classroomRepository.findByClassroomId(classroomId);
        model.addAttribute("classroom", classroom);
        return "classroom-detail-edit";
    }
    @PostMapping(value = "/saveClassroom")
    public String saveClassroom(@Valid Classroom classroom, BindingResult result){
        if(result.hasErrors()){
            return "classroom-detail-edit";
        }
        classroomRepository.save(classroom);
        return "redirect:/admin/listClassroom";
    }
    @RequestMapping(value = "/deleteClassroom/{classrooomId}")
    public String deleteClassroom(@PathVariable Long classrooomId){
        Classroom classroom = classroomRepository.findByClassroomId(classrooomId);
        classroomRepository.delete(classroom);
        return "redirect:/admin/listClassroom";

    }


    // CRUD course

    @GetMapping("/addCourse")
    public String addCourse(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("course", new Course());
        model.addAttribute("majors", majorRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        if (redirectAttributes.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", redirectAttributes.getAttribute("errorMessage"));
        }
        return "course-management";
    }

    @RequestMapping(value = "/insertCourse")
    public String insertCourse(@Valid Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "All field must be fill up");
            return "course-management";
        }else if (isDuplicateCourse(course.getCourseCode(), course.getCourseName())) {

            redirectAttributes.addFlashAttribute("errorMessage", "Course already exists");

            return "course-management";
        }
        courseRepository.save(course);
        return "redirect:/admin/listCourse";
    }

    private boolean isDuplicateCourse(String courseCode, String courseName) {
        return courseRepository.existsByCourseCodeAndCourseName(courseCode, courseName);
    }


    @GetMapping(value = "/listCourse")
    public String showAllCourse(Model model, @RequestParam(name = "courseId", required = false) Long courseId,
                                @RequestParam(name = "showAll", required = false) String showAll){
        Iterable<Course> courses;
        Course course;
        model.addAttribute("course", new Course());
        model.addAttribute("majors", majorRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        if (showAll != null) {
            courses = courseRepository.findAll();
            model.addAttribute("courses", courses);
            return "course-management";
        }
        if(courseId != null){
            course = courseRepository.findByCourseId(courseId);

            if(course != null){
                model.addAttribute("courses", course);
            } else {
                model.addAttribute("notFoundMessage", "No Course found with the provided course code");
            }
        } else {
            courses = courseRepository.findAll();
            model.addAttribute("courses", courses);
        }
        return "course-management";
    }
    @GetMapping("/courseDetail/{courseId}")
    public String showCourseDetail(@PathVariable Long courseId, Model model){
        Course course = courseRepository.findByCourseId(courseId);
        model.addAttribute("course", course);
        return "course-detail";
    }

    @GetMapping(value = "/updateCourse/{courseId}")
    public String updateCourse(@PathVariable Long courseId, Model model){
        Course course = courseRepository.findByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("majors", majorRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        return "course-detail-edit";
    }

    @PostMapping(value = "/saveCourse")
    public String saveCourse(@Valid Course course, BindingResult result){
        if(result.hasErrors()){
            return "course-detail-edit";
        }
        courseRepository.save(course);
        return "redirect:/admin/listCourse";
    }
    @RequestMapping(value = "/deleteCourse/{courseId}")
    public String deleteCourse(@PathVariable Long courseId){
        Course course = courseRepository.findByCourseId(courseId);
        courseRepository.delete(course);
        return "redirect:/admin/listCourse";

    }


    // CRUD timetable
    @GetMapping("/addTimetable")
    public String addTimetable(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("timetable", new Timetable());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("majors", majorRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        if (redirectAttributes.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", redirectAttributes.getAttribute("errorMessage"));
        }
        return "timetable-management";

    }

    @RequestMapping(value = "/insertTimetable")
    public String insertTimetable(@Valid Timetable timetable, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "All field must be fill up");
            return "timetable-management";
        }
        else if (isDuplicateTimetable(timetable.getCourse().getCourseId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Timetable already exists");

            return "timetable-management";
        }
        timetableRepository.save(timetable);
        return "redirect:/admin/listTimetable";
    }

    private boolean isDuplicateTimetable(Long courseId) {
        return timetableRepository.existsByCourseCourseId(courseId);
    }

    @GetMapping(value = "/listTimetable")
    public String showAllTimetable(Model model, @RequestParam(name = "timetableId", required = false) Long timetableId,
                                   @RequestParam(name = "showAll", required = false) String showAll){
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("majors", majorRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        Iterable<Timetable> timetables;
        Timetable timetable;
        if (showAll != null) {
            timetables = timetableRepository.findAll();
            model.addAttribute("timetables", timetables);
            return "timetable-management";
        }
        if(timetableId != null){
            timetable = timetableRepository.findByTimetableId(timetableId);
            if(timetable != null){
                model.addAttribute("timetables", timetable);
            } else {
                model.addAttribute("notFoundMessage", "No timetable found with the provided course code");
            }
        } else {
            timetables = timetableRepository.findAll();
            model.addAttribute("timetables", timetables);
        }
        return "timetable-management";
    }

    @GetMapping("/timetableDetail/{timetableId}")
    public String showTimetableDetail(@PathVariable Long timetableId, Model model){
        Timetable timetable = timetableRepository.findByTimetableId(timetableId);
        model.addAttribute("timetable", timetable);
        return "timetable-detail";
    }

    @GetMapping(value = "/updateTimetable/{timetableId}")
    public String updateTimetable(@PathVariable Long timetableId, Model model){
        Timetable timetable = timetableRepository.findByTimetableId(timetableId);
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("majors", majorRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        model.addAttribute("timetable",timetable);
        return "timetable-detail-edit";
    }
    @RequestMapping(value = "/deleteTimetable/{timetableId}")
    public String deleteTimetable(@PathVariable Long timetableId){
        Timetable timetable = timetableRepository.findByTimetableId(timetableId);
        timetableRepository.delete(timetable);
        return "redirect:/admin/listTimetable";
    }
    @RequestMapping(value="/saveTimetable")
    public String saveTimetable(@Valid Timetable timetable, BindingResult result){
        if(result.hasErrors()){
            return "timetable-detail-edit";
        }
        timetableRepository.save(timetable);
        return "redirect:/admin/listTimetable";
    }


    // CRUD timetable


    @RequestMapping(value = "/insertEnrollment")
    public String insertEnrollment(@Valid Enrollment enrollment, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "All field must be fill up");
            return "enrollment-management";
        }
        else if (isDuplicateEnrollment(enrollment.getCourse().getCourseId(), enrollment.getStudent().getStudentCode())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Timetable already exists");
            return "enrollment-management";
        }
        Timetable timetable = timetableRepository.findByCourseCourseId(enrollment.getCourse().getCourseId());

            Course course = timetable.getCourse();
            course.setEnrollmentCount(course.getEnrollmentCount() + 1);
            courseRepository.save(course);

        enrollmentRepository.save(enrollment);
        return "redirect:/admin/listEnrollment";
    }

    @GetMapping("/checkCapacity/{courseId}")
    @ResponseBody
    public ResponseEntity<Object> checkCapacity(@PathVariable Long courseId) {
        Map<String, Object> result = new HashMap<>();
        Timetable timetable = timetableRepository.findByCourseCourseId(courseId);
        if (timetable != null) {
            int enrollmentCount = timetable.getCourse().getEnrollmentCount();
            int capacity = timetable.getClassroom().getRoomCapacity();
            boolean exceedsCapacity = enrollmentCount >= capacity;
            result.put("exceedsCapacity", exceedsCapacity);
            return ResponseEntity.ok(result);
        } else {
            String errorMessage = "Timetable not found for course id: " + courseId;
            result.put("errorMessage", errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }


    @GetMapping("/courses/{semesterId}")
    @ResponseBody
    public List<Course> getCoursesBySemester(@PathVariable Long semesterId) {
        return courseRepository.findBySemesterSemesterId(semesterId);
    }

    private boolean isDuplicateEnrollment(Long courseId, Integer studentCode) {
        return enrollmentRepository.existsByCourseCourseIdAndStudentStudentCode(courseId, studentCode);
    }

    @GetMapping(value = "/listEnrollment")
    public String showAllEnrollment(Model model, @RequestParam(name = "enrollmentId", required = false) Long enrollmentId,
                                    @RequestParam(name = "showAll", required = false) String showAll){
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("enrollment", new Enrollment());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        Iterable<Enrollment> enrollments;
        Enrollment enrollment;
        if (showAll != null) {
            enrollments = enrollmentRepository.findAll();
            model.addAttribute("enrollments", enrollments);
            return "enrollment-management";
        }
        if(enrollmentId != null){
            enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
            if(enrollment != null){
                model.addAttribute("enrollments", enrollment);
            } else {
                model.addAttribute("notFoundMessage", "No enrollment found with the provided course code");
            }
        } else {
            enrollments = enrollmentRepository.findAll();
            model.addAttribute("enrollments", enrollments);
        }
        return "enrollment-management";
    }

    @GetMapping("/enrollmentDetail/{enrollmentId}")
    public String showEnrollmentDetail(@PathVariable Long enrollmentId, Model model){
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        Timetable timetable = timetableRepository.findByCourseCourseId(enrollment.getCourse().getCourseId());
        model.addAttribute("timetable", timetable);
        model.addAttribute("enrollment", enrollment);
        return "enrollment-detail";
    }

    @GetMapping(value = "/updateEnrollment/{enrollmentId}")
    public String updateEnrollment(@PathVariable Long enrollmentId, Model model){
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        List<Course> courses = courseRepository.findBySemesterSemesterId(enrollment.getSemester().getSemesterId());
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        return "enrollment-detail-edit";
    }
    @RequestMapping(value="/saveEnrollment")
    public String saveEnrollment(@Valid Enrollment enrollment, BindingResult result){
        if(result.hasErrors()){
            return "enrollment-detail-edit";
        }
        enrollmentRepository.save(enrollment);
        return "redirect:/admin/listEnrollment";
    }

    @RequestMapping(value = "/deleteEnrollment/{enrollmentId}")
    public String deleteEnrollment(@PathVariable Long enrollmentId){
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        enrollmentRepository.delete(enrollment);
        return "redirect:/admin/listEnrollment";
    }

    @RequestMapping(value="/details")
    public String adminDetails(Model model, HttpSession session){
        CustomUserDetails userDetail = (CustomUserDetails) session.getAttribute("userDetails");
        User user = userRepository.findByUsername(userDetail.getUsername());
        Admin admin = adminRepository.findByUser(user);
        model.addAttribute("adminDetails",admin);
        return "admin-profile";
    }


}