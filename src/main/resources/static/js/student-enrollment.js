document.addEventListener("DOMContentLoaded", function() {
    const enrolls=document.querySelectorAll("#enrollId")
    const schedule=document.querySelectorAll(".enrollCourse")

    // Event listener for when a semester is selected
    enrolls.forEach((enroll,index)=>{
        const courseId=enroll.value;
        fetchTimetableforCourse(courseId)
            .then(function(timetable) {
                // Populate the course select element with the fetched courses
                schedule.item(index).textContent=timetable.teachingDay+' ('+timetable.startDate+ ' -> '+timetable.endDate+') ('+timetable.startTime+' -> '+timetable.endTime+') ';
            })
            .catch(function(error) {
                console.error("Error fetching courses:", error);
            });
    })
        // Fetch courses for the selected semester from the server



    // Function to fetch courses for the selected semester from the server
    function fetchTimetableforCourse(courseId) {
        return fetch('/student/timetableForCourse/' + courseId)
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
