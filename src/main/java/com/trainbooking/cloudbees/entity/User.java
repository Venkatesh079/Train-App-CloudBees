package com.trainbooking.cloudbees.entity;

import lombok.*;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private final String firstName;
    private final String lastName;
    private final String email;
}
