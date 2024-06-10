document.addEventListener("DOMContentLoaded", function () {
    console.log(document.getElementsByTagName("input"))
});

function checkEmptyFields(fields, errorMessage) {
    for (let i = 0; i < fields.length; i++) {
        const element = fields[i];
        if (element.tagName === 'INPUT') {
            const inputType = element.getAttribute('type');
            if (inputType === 'text' || inputType === 'date' || inputType === 'tel' || inputType === "email"||inputType==="time") {
                if (element.value.trim() === '') {
                    errorMessage = "Please fill all fields"
                    break;
                }
            } else if (inputType === 'number') {
                console.log(element.value)
                if (isNaN(element.valueAsNumber)||element.valueAsNumber<=0) {
                    errorMessage = "Please fill all fields"
                    break;
                }
            }
        }
    }
    return errorMessage
}

function checkFieldsValue(fields, errorMessage) {

}

function validateInputForm() {
    var fields = document.getElementsByTagName("input");
    if (fields) {
        console.log(fields)
    } else {console.log("Error")}
    // Clear previous error messages
    document.getElementById("error-message").innerText = "";
    closeErrorPopup(); // Close the error popup

    // Perform validation checks
    var errorMessage = "";

    // check empty field
    // try {
    //     var majorSelect = document.getElementsByTagName("select")
    //     if (majorSelect.length > 0) {
    //         for (let i = 0; i < majorSelect.length; i++) {
    //             const element = majorSelect[i];
    //             if (element.options.length === 0) {
    //                 errorMessage = "Selection is empty"
    //             }
    //         }
    //     }
    // } catch (error) {
    //
    // }
    errorMessage = checkEmptyFields(fields, errorMessage)

    if (errorMessage !== "") {
        // Display error messages in the popup
        document.getElementById("error-message").innerText = errorMessage;
        openErrorPopup();
        console.log(errorMessage)
        return false; // Prevent form submission
    }

    return true; // Allow form submission if all checks pass
}

function openErrorPopup() {
    document.getElementById("error-popup").style.display = "block";
}

function closeErrorPopup() {
    document.getElementById("error-popup").style.display = "none";
}