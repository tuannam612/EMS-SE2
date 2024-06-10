package SE2.Project.Backend.controller;

import SE2.Project.Backend.model.*;
import SE2.Project.Backend.repository.*;
import SE2.Project.Backend.service.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private TimetableRepository timetableRepository;
    @Autowired
    private HttpSession httpSession;

    @ModelAttribute("student")
    public Student student(HttpSession session){
        CustomUserDetails userDetails = (CustomUserDetails) session.getAttribute("userDetails");
        User user=userRepository.findByUsername(userDetails.getUsername());
        return studentRepository.findByUserId(user.getUserID());
    }

    @GetMapping("/enroll/courses")
    public String getAvailableCourses(Model model, HttpSession session) {
        List<Course> availableCourses = courseRepository.findAllByCapacityGreaterThan(0);
        List<Enrollment> enrolledCourse = enrollmentRepository.findAllByStudent(student(session));
        List<Course> enrolled=enrolledCourse.stream().map(Enrollment::getCourse).toList();
        List<Timetable>timetables=new ArrayList<>();
        for(Course course:enrolled){
            timetables.add(timetableRepository.findByCourse(course));
        }
        timetables=timetables.stream().filter(timetable -> timetable.getEndDate().getMonthValue()>= LocalDate.now().getMonthValue()).toList();
        List<Course>unfinishedCourse=timetables.stream().map(Timetable::getCourse).toList();
        availableCourses.removeAll(unfinishedCourse);
        List<Enrollment> PassedCourse = enrolledCourse.stream()
                .filter(enrollment -> enrollment.getTotalGrade() >5)
                .toList();
        List<Course>Passed=PassedCourse.stream().map(Enrollment::getCourse).toList();
        availableCourses.removeAll(Passed);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("timetable", timetableRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        return "enrollment";
    }

    @PostMapping("/enroll/courses/{courseId}/enroll")
    public String enrollCourse(
            @PathVariable Long courseId,
            HttpSession session
    ) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        if (enrollmentRepository.existsByStudentAndCourseCourseName(student(session), course.getCourseName())) {
            return "redirect:/student/enroll/courses?error=alreadyEnrolled";
        }
        if (course.getCapacity() <= 0) {
            return "redirect:/student/enroll/courses?error=courseFull";
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setSemester(course.getSemester());
        enrollment.setStudent(student(session));
        enrollmentRepository.save(enrollment);
        course.setCapacity(course.getCapacity() - 1);
        courseRepository.save(course);
        return "redirect:/student/enrollments";
    }

    @GetMapping("/enrollments")
    public String getEnrollments(HttpSession session, Model model) {
        CustomUserDetails userDetail = (CustomUserDetails) session.getAttribute("userDetails");
        User user = userRepository.findByUsername(userDetail.getUsername());
        Student student = studentRepository.findByUser(user);

        Set<Enrollment> enrollments = student.getEnrollments();
        Integer numCourse = 0;
        Integer totalCredit = 0;

        for(Enrollment enrollment: enrollments){
            totalCredit += enrollment.getCourse().getCredit();
            numCourse += 1;

        }
        model.addAttribute("numCourse", numCourse);
        model.addAttribute("totalCredit", totalCredit);
        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments);
        return "enrolledCourses";
    }

    @RequestMapping(value = "/enroll/deleteEnrollment/{enrollmentId}")
    public String deleteEnrollment(@PathVariable Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        enrollment.getCourse().setCapacity(enrollment.getCourse().getCapacity() + 1);
        enrollmentRepository.delete(enrollment);

        return "redirect:/student/enrollments";
    }

    @RequestMapping("/details")
    public String details(Model model, HttpSession session) {
        model.addAttribute("student", student(session));
        return "student-profile";
    }

    @RequestMapping(value = "/timetableDetails")
    public String timetableDetails(@RequestParam(name="semester", required = false,defaultValue = "1")Long semesterId, Model model, HttpSession session) {
        List<Timetable> studentTimetable = new ArrayList<>();
        List<Semester> semesters = semesterRepository.findAll();
        List<Enrollment> studentEnrolledCourse = enrollmentRepository.findAllByStudent(student(session));
        Timetable[][] timetables = new Timetable[6][6];
        for (Enrollment e : studentEnrolledCourse) {
            System.out.println("Enrol: " + e);
            Timetable timetable = timetableRepository.findByCourse(e.getCourse());
            if(semesterId==0){
                studentTimetable.add(timetable);
            }
            else if(Objects.equals(timetable.getCourse().getSemester().getSemesterId(), semesterId))
                studentTimetable.add(timetable);
        }
        TeacherController.teacherTimetable(studentTimetable, timetables);
        model.addAttribute("studentTimetable", timetables);
        model.addAttribute("semesterId",semesterId);
        model.addAttribute("semesters",semesters);
        System.out.println(Arrays.deepToString(timetables));
        return "student-view-schedule";
    }

    @RequestMapping(value="/tuition")
    public String viewTuition(@RequestParam(name="semester", required = false,defaultValue = "0")Long semesterId,Model model,HttpSession session){
        List<Enrollment>enrollments=enrollmentRepository.findAllByStudent(student(session));
        List<Semester> semesters = semesterRepository.findAll();

        List<Course>courses=new ArrayList<>();
        for(Enrollment enrollment:enrollments){
            if(semesterId==0){
                courses.add(enrollment.getCourse());
            }
            else if(Objects.equals(enrollment.getCourse().getSemester().getSemesterId(), semesterId)) {
                courses.add(enrollment.getCourse());
            }
        }
        double totalFee=0;
        for(Course course:courses){
            totalFee+=course.getFee();
        }
        model.addAttribute("courses", courses);
        model.addAttribute("semesterId",semesterId);
        model.addAttribute("semesters",semesters);
        model.addAttribute("totalFee",totalFee);
        return "student-view-tuition";
    }

    @RequestMapping(value = "/mark")
    public String mark(Model model, HttpSession session) {
        List<Enrollment> enrollments = enrollmentRepository.findAllByStudent(student(session));
        List<Semester>semesters=semesterRepository.findAll();
        double totalGpa = 0.0;
        Integer totalCredit = 0;
        for (Enrollment enrollment : enrollments) {
            totalGpa += enrollment.getTotalGrade() * enrollment.getCourse().getCredit();
            totalCredit += enrollment.getCourse().getCredit();
        }
        if (totalCredit > 0) {
        totalGpa /= totalCredit;}

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("semesters",semesters);
        model.addAttribute("totalGpa", totalGpa);
        return "student-view-mark";
    }
    @RequestMapping("/timetableForCourse/{courseId}")
    @ResponseBody
    public Timetable getTimetableForCourse(@PathVariable Long courseId){
        return timetableRepository.findByCourseCourseId(courseId);
    }
}