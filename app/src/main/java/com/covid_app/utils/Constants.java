package com.covid_app.utils;

public class Constants {

    public interface DbCollection {
        String COLLECTION_USERS = "users";
        String COLLECTION_ATTENDANCE = "attendance";
        String COLLECTION_USER_CONTACTS = "user_contacts";
        String COLLECTION_COVID_STATUS = "covid_status";

    }

    public interface UsersCollection {
        String name = "name";
        String studentId = "student_id";
        String email = "email";
        String courseName = "course_name";
    }

    public interface UserContacts {
        String userId = "id";
        String contactUserId = "contact_user_id";
        String timeStamp = "timestamp";
    }

    public interface CovidStatus {
        String userId = "user_id";
        String highTemperature = "high_temperature";
        String symptoms = "symptoms";
        String covidStatus = "covid_status";
    }

    public interface Attendance{
        String userId="user_id";
        String classId="class_id";
        String type="type";
        String timestamp="timestamp";
    }

}
