document.addEventListener("DOMContentLoaded", function() {
    var majorSelect = document.getElementById("majorSelect");
        var courseTypeSelect = document.getElementById("courseType");
        var creditInput = document.getElementById("credit");
        var feeInput = document.getElementById("fee");

        majorSelect.addEventListener("change", function(event) {
            updateFee();
        });

        courseTypeSelect.addEventListener("change", function(event) {
            updateFee();
        });

        creditInput.addEventListener("input", function(event) {
            updateFee();
        });

        function updateFee() {
                var selectedMajorOption = majorSelect.options[majorSelect.selectedIndex];
                var specialFee = selectedMajorOption.getAttribute("data-special-fee");
                var basicFee = selectedMajorOption.getAttribute("data-basic-fee");

                var courseType = courseTypeSelect.value;
                var credit = parseFloat(creditInput.value);
                console.log(courseType)
                if (isNaN(credit)) {
                            credit = 0;
                        }

                        var fee;
                         if (courseType === "Special Course") {
                                    fee = credit * parseFloat(specialFee);
                                } else if (courseType === "Basic Course") {
                                    fee = credit * parseFloat(basicFee);
                                } else {
                                    fee = 0;
                                }
                                feeInput.value = fee;
                }
});