package com.trainbooking.cloudbees.impl;

import com.trainbooking.cloudbees.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceImplTest {

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl();
    }

    @Test
    void testPurchaseTicket_Success() {
        String result = ticketService.purchaseTicket("John", "Doe", "john.doe@example.com");
        assertTrue(result.contains("Ticket purchased successfully"));
    }

    @Test
    void testGetReceipt_Success() {
        ticketService.purchaseTicket("John", "Doe", "john.doe@example.com");
        String receipt = ticketService.getReceipt(1);
        assertNotNull(receipt);
    }

    @Test
    void testRemoveUser_Success() {
        ticketService.purchaseTicket("John", "Doe", "john.doe@example.com");
        String result = ticketService.removeUser(1);
        assertEquals("User removed successfully", result);
    }

    @Test
    void testModifyUserSeat_Success() {
        ticketService.purchaseTicket("John", "Doe", "john.doe@example.com");
        String result = ticketService.modifyUserSeat(1, "B");
        assertTrue(result.contains("User seat modified successfully"));
    }

}
