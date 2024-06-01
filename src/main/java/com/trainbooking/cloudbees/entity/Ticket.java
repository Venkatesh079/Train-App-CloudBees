package com.trainbooking.cloudbees.entity;

import lombok.*;

@Data
@AllArgsConstructor
public class Ticket {

    private final int id;
    private final String from;
    private final String to;
    private final User user;
    private final double price;
    private String section;
    private int seatNumber;
}
