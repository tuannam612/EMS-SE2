document.addEventListener("DOMContentLoaded", function() {
    var semesterSelect = document.getElementById("semesterSelect");
    var courseSelect = document.getElementById("courseSelect");

    // Event listener for when a semester is selected
    semesterSelect.addEventListener("change", function() {
        var semesterId = semesterSelect.value;

        // Clear the current options in the course select element
        courseSelect.innerHTML = '';
         var selectOption = document.createElement("option");
                selectOption.value = "";
                selectOption.textContent = "Select Course";
                courseSelect.appendChild(selectOption);
        // Fetch courses for the selected semester from the server
        fetchCoursesForSemester(semesterId)
            .then(function(courses) {
                // Populate the course select element with the fetched courses
                courses.forEach(function(course) {
                    var option = document.createElement("option");
                    option.value = course.courseId;
                    option.textContent = course.courseName + "-" + course.courseCode;
                    courseSelect.appendChild(option);
                });
            })
            .catch(function(error) {
                console.error("Error fetching courses:", error);
            });
    });

    // Function to fetch courses for the selected semester from the server
    function fetchCoursesForSemester(semesterId) {
        return fetch('/admin/courses/' + semesterId)
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Failed to fetch courses');
                }
                return response.json();
            })
            .then(function(data) {
                             console.log("Courses:", data);
                             return data;
                         });;
    }

});
