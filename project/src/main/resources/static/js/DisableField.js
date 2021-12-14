function disableField() {
    headlineField = document.getElementById("headline");
    writerField = document.getElementById("writer");
    
    if (headlineField.value !== "") {
        writerField.disabled = true;
    } else {
        writerField.disabled = false;
    }
    
    if (writerField.value !== "") {
        headlineField.disabled = true;
    } else {
        headlineField.disabled = false;
    }
}

