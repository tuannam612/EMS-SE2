document.addEventListener("DOMContentLoaded", function () {

    var editModal = document.getElementById("edit-modal");
    var addModal = document.getElementById("add-modal");
// Get the button that opens the editModal
    var editBtn = document.getElementById("logo-edit");
    var addBtn = document.getElementById("add-button");
    if (editModal !== null) {
        // Element with the specified ID was found
        console.log("Element found:", editModal);
        console.log(addModal)
    } else {
        // Element with the specified ID was not found
        console.log("Element not found");
    }
    var editSpan = document.getElementById("close-edit");
    var addSpan = document.getElementById("close-add");


// When the user clicks the button, open the editModal
    editBtn.onclick = function () {
        console.log("HEllo")
        editModal.style.display = "block";
    }

    addBtn.onclick = function () {
        addModal.style.display = "block";
    }

// When the user clicks on <editSpan> (x), close the editModal
    editSpan.onclick = function () {
        editModal.style.display = "none";
    }

    addSpan.onclick = function () {
        addModal.style.display = "none";
    }


});
