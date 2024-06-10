package SE2.Project.Backend.controller;

import SE2.Project.Backend.model.*;
import SE2.Project.Backend.repository.*;
import SE2.Project.Backend.service.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @ModelAttribute("teacher")
    public Teacher teacher(HttpSession session){
        CustomUserDetails userDetails = (CustomUserDetails) session.getAttribute("userDetails");
        User user=userRepository.findByUsername(userDetails.getUsername());
        return teacherRepository.findByUserId(user.getUserID());
    }

    @GetMapping("/courses")
    public String showCourses(HttpSession session, Model model) {
        List<Course> courses = courseRepository.findByTeacher(teacher(session));
        List<Timetable>timetables=new ArrayList<>();
        for (Course course : courses) {
            timetables.add(timetableRepository.findByCourse(course));
        }
        model.addAttribute("teacher", teacher(session));
        model.addAttribute("courses", courses);
        model.addAttribute("timetables", timetables);
        return "teacher-course";
    }
    @RequestMapping("/courses/{courseId}")
    public String showStudentsForCourse(@PathVariable Long courseId,
                                        Model model) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseCourseId(courseId);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "teacher-grade-management";
    }
    @GetMapping("/courses/students/{enrollmentId}")
    public String updateGrade(
            @PathVariable Long enrollmentId,
            Model model) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new RuntimeException("Enrollment not found"));
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("enrollment", enrollment);
        return "teacher-edit-grade";
    }

    @PostMapping(value = "/saveGrade")
    public String saveGrade(@Valid Enrollment enrollment, BindingResult result){
        System.out.println(result);
        if(result.hasErrors()){
            return "teacher-edit-grade";
        }
        Long courseId = enrollment.getCourse().getCourseId();
        enrollmentRepository.save(enrollment);
        return "redirect:/teacher/courses/" + courseId;
    }



    @RequestMapping(value="/details")
    public String teacherDetails(Model model, HttpSession session){
        CustomUserDetails userDetail = (CustomUserDetails) session.getAttribute("userDetails");
        User user = userRepository.findByUsername(userDetail.getUsername());
        Teacher teacher = teacherRepository.findByUser(user);
        model.addAttribute("teacherDetails",teacher);
        System.out.println(teacher.getUser().getUsername());
        return "teacher-profile";
    }
    @RequestMapping(value = "/timetableDetails")
    public String timetableDetails(@RequestParam(name="semester", required = false,defaultValue = "1")Long semesterId, Model model, HttpSession session) {
        List<Timetable> teacherTimetable = new ArrayList<>();
        List<Semester> semesters = semesterRepository.findAll();
        List<Course> teacherCourses = courseRepository.findByTeacher(teacher(session));
        Timetable[][] timetables = new Timetable[6][6];
        for (Course e : teacherCourses) {
            Timetable timetable = timetableRepository.findByCourse(e);
            if(Objects.equals(e.getSemester().getSemesterId(), semesterId)){
                teacherTimetable.add(timetable);
            }
        }
        teacherTimetable(teacherTimetable, timetables);
        model.addAttribute("teacherTimetable", timetables);
        model.addAttribute("semesterId",semesterId);
        model.addAttribute("semesters",semesters);
        System.out.println(Arrays.deepToString(timetables));
        return "teacher-view-schedule";
    }

    static void teacherTimetable(List<Timetable> teacherTimetable, Timetable[][] timetables) {
        for (Timetable timetable : teacherTimetable) {
            switch ((timetable.getTeachingDay()).toLowerCase()) {
                case "monday":
                    switch (timetable.getStartTime().getHour()) {
                        case 7:
                            timetables[0][0] = timetable;
                            break;
                        case 9:
                            timetables[1][0] = timetable;
                            break;
                        case 12:
                            timetables[2][0] = timetable;
                            break;
                        case 14:
                            timetables[3][0] = timetable;
                            break;
                        case 17:
                            timetables[4][0] = timetable;
                            break;
                        case 19:
                            timetables[5][0] = timetable;
                            break;
                    }
                    break;

                case "tuesday":
                    switch (timetable.getStartTime().getHour()) {
                        case 7:
                            timetables[0][1] = timetable;
                            break;
                        case 9:
                            timetables[1][1] = timetable;
                            break;
                        case 12:
                            timetables[2][1] = timetable;
                            break;
                        case 14:
                            timetables[3][1] = timetable;
                            break;
                        case 17:
                            timetables[4][1] = timetable;
                            break;
                        case 19:
                            timetables[5][1] = timetable;
                            break;
                    }
                    break;

                case "wednesday":
                    switch (timetable.getStartTime().getHour()) {
                        case 7:
                            timetables[0][2] = timetable;
                            break;
                        case 9:
                            timetables[1][2] = timetable;
                            break;
                        case 12:
                            timetables[2][2] = timetable;
                            break;
                        case 14:
                            timetables[3][2] = timetable;
                            break;
                        case 17:
                            timetables[4][2] = timetable;
                            break;
                        case 19:
                            timetables[5][2] = timetable;
                            break;
                    }
                    break;

                case "thursday":
                    switch (timetable.getStartTime().getHour()) {
                        case 7:
                            timetables[0][3] = timetable;
                            break;
                        case 9:
                            timetables[1][3] = timetable;
                            break;
                        case 12:
                            timetables[2][3] = timetable;
                            break;
                        case 14:
                            timetables[3][3] = timetable;
                            break;
                        case 17:
                            timetables[4][3] = timetable;
                            break;
                        case 19:
                            timetables[5][3] = timetable;
                            break;
                    }
                    break;

                case "friday":
                    switch (timetable.getStartTime().getHour()) {
                        case 7:
                            timetables[0][4] = timetable;
                            break;
                        case 9:
                            timetables[1][4] = timetable;
                            break;
                        case 12:
                            timetables[2][4] = timetable;
                            break;
                        case 14:
                            timetables[3][4] = timetable;
                            break;
                        case 17:
                            timetables[4][4] = timetable;
                            break;
                        case 19:
                            timetables[5][4] = timetable;
                            break;
                    }
                    break;

                case "saturday":
                    switch (timetable.getStartTime().getHour()) {
                        case 7:
                            timetables[0][5] = timetable;
                            break;
                        case 9:
                            timetables[1][5] = timetable;
                            break;
                        case 12:
                            timetables[2][5] = timetable;
                            break;
                        case 14:
                            timetables[3][5] = timetable;
                            break;
                        case 17:
                            timetables[4][5] = timetable;
                            break;
                        case 19:
                            timetables[5][5] = timetable;
                            break;
                    }
                    break;

            }
        }
    }
}