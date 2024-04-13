package com.better.one_better_bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "usersDataTable")
public class User {
    @Id
    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private Timestamp joinDate;//дата начала

//    @Override
//    public String toString() {
//        return "User{" +
//                "chatId=" + chatId +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", userName='" + userName + '\'' +
//                ", joinDate=" + joinDate +
//                '}';
//    }
}
