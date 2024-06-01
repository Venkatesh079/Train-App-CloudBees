package com.trainbooking.cloudbees.service;

public interface TicketService {

    String purchaseTicket(String firstName, String lastName, String email);

    String getReceipt(int id);

    String viewUsersBySection(String section);

    String removeUser(int id);

    String modifyUserSeat(int id, String section);
}
