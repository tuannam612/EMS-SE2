document.addEventListener("DOMContentLoaded", function() {
    var courseSelect = document.getElementById("courseSelect");
      var addButton = document.getElementById("confirm-button");
    courseSelect.addEventListener("change", function() {
        var courseId = courseSelect.value;

         fetch('/admin/checkCapacity/' + courseId)
                    .then(function(response) {
                        if (!response.ok) {
                            addButton.disabled = true;
                            throw new Error('Failed to check capacity');
                        }
                        return response.json();
                    })
                    .then(function(data) {
                        if (data.errorMessage) {
                            alert(data.errorMessage); // Display the error message
                            addButton.disabled = true;
                        } else {
                            console.log(data)
                            if (data.exceedsCapacity) {
                                alert('There is no capacity left for this course.');
                                addButton.disabled = true;
                            } else {
                                addButton.disabled = false;
                            }
                        }
                    })
                    .catch(function(error) {
                        console.error('Error checking capacity:', error);
                        alert('Error no timetable for this course. Please add!');
                    });
    });
});
