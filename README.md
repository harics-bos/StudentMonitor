# StudentMonitor

Main Activity Screen Validation:

1) The buttons 'EDIT CHILD', 'SURVEY', 'VIEW REPORT' will be visible only if there exists atleast one registered student.

Student Registration Screen Validation:

1) The first name should not be empty. If empty, error with error text, “FirstName should not be empty” will be displayed.

2) The last name should not be empty. If empty, error with error text, “LastName should not be empty” will be displayed.

3) The Date Of Birth field has a minimum date of 5 years and maximum date of 12 years less than the current day. Since the app if for the students aged between 5 and 12, this restriction is enforced. Only dates between 5 years and 12 years from the current date will be displayed.

4)The submit button will be visible only when all the mandatory fields are completed by the user.

Note: While clicking on the ‘Upload’ button, please select images from the Gallery.

Survey Screen Validation:

1)If there has been no survey detail recorded for a student, then the Bed Date field and the Wake up Date field will have only the current date visible.

2) If there already exists at-least one survey for the student, then latest bed date will be fetched from the Survey table for the student. The Date Picker dialog for the Bed Date field will have date from the latest wake up date till the current date. The date picker dialog of the wake up field will have date selected in the Bed date field and the next date. Note: The next date will be visible only if doesn’t exceed the current date. 

3)The Bed date and Bed Time fields should not be empty. If empty, then appropriate errors will be displayed.

4)Validation errors will be thrown if the wakeup date and time is lesser than the bed date  and time values and vice versa.

5)The submit button will be visible only if all the fields are filled by the user.
