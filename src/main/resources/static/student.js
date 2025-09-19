// alert("Hii i am js");

$(document).ready(function () {

	selectedCourseIds = (selectedCourseIds || []).map(String);   
	   selectedSubjectIds = (selectedSubjectIds || []).map(String);
    console.log("Pre-selected courses:", selectedCourseIds);
    console.log("Pre-selected subjects:", selectedSubjectIds);

    // Function to load courses for a department
    function loadCourses(deptId) {  
        $('#courses').empty();
        $('#subjects').empty();

        if (!deptId) return;

        $.getJSON(`/admin/student/courses/${deptId}`)
            .done(function (courses) {
                if (!courses || courses.length === 0) {
                    $('#courses').append("<p class='text-muted'>No courses found</p>");
                    return;
                }

                $.each(courses, function (i, course) {
                    //  Pre-check course if selected
                    let checked = selectedCourseIds.includes(String(course.id)) ? "checked" : "";  // ★

                    $('#courses').append(
                        `<div>
                            <input type="checkbox" class="course-checkbox" name="courseIds[]" 
                                   value="${course.id}" id="course-${course.id}" 
                                   data-course-name="${course.courseName}" ${checked}> <!-- ★ added ${checked} -->
                            <label for="course-${course.id}">${course.courseName}</label>
                        </div>`
                    );

                    //  If course is pre-selected, load its subjects immediately
                    if (checked) {  
                        loadSubjectsForCourse(course.id, course.courseName);  
                    }  
                });
            })
            .fail(function (xhr) {
                console.error("Error fetching courses:", xhr.status, xhr.responseText);
            });
    } 

    // On department change
    $('#department').on('change', function () {
        loadCourses($(this).val());  
    });

    //let initialDeptId = $('#department').val();  
    //if (initialDeptId) {
    //    loadCourses(initialDeptId);
    //}
	
	if ($('#department').val()) {
	    setTimeout(function() {
	        $('#department').triggerHandler('change');   // ★ delayed trigger
	    }, 100);  // small delay to let backend vars load
	}

    // When course checkbox is checked/unchecked
    $(document).on('change', '.course-checkbox', function () {
        let courseId = $(this).val();
        let courseName = $(this).data("course-name");

        if (this.checked) {
            loadSubjectsForCourse(courseId, courseName);  
        } else {
            console.log("Course unchecked:", courseId, courseName, "→ removing its subjects");
            $(`.subject-group-${courseId}`).remove();
        }
    });

    function loadSubjectsForCourse(courseId, courseName) {  
        $.getJSON(`/admin/student/subjects/${courseId}`)
            .done(function (subjects) {
                console.log("Received subjects for", courseName, ":", subjects);

                if (!subjects || subjects.length === 0) {
                    $('#subjects').append(
                        `<p class="text-muted subject-group-${courseId}">
                            No subjects found for <b>${courseName}</b>
                        </p>`
                    );
                    return;
                }

                $.each(subjects, function (i, subject) {
                    let checked = selectedSubjectIds.includes(String(subject.id)) ? "checked" : "";  

                    $('#subjects').append(
                        `<div class="subject-group-${courseId}">
                            <input type="checkbox" name="subjectIds[]" value="${subject.id}" id="subject-${subject.id}" ${checked}> <!-- ★ added ${checked} -->
                            <label for="subject-${subject.id}">${subject.subjectName}</label>
                        </div>`
                    );
                });
            })
            .fail(function (xhr) {
                console.error("Error fetching subjects for", courseName, ":", xhr.status, xhr.responseText);
            });
    }  

});
