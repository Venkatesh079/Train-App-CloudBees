package com.trainbooking.cloudbees.controller;


import com.trainbooking.cloudbees.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrainTicketControllerTest {

    private MockMvc mockMvc;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = Mockito.mock(TicketService.class);
        TrainTicketController trainTicketController = new TrainTicketController();
        trainTicketController.setTicketService(ticketService);
        mockMvc = MockMvcBuilders.standaloneSetup(trainTicketController).build();
    }

    @Test
    void testPurchaseTicket_Success() throws Exception {
        Mockito.when(ticketService.purchaseTicket("John", "Doe", "john.doe@example.com"))
                .thenReturn("Ticket purchased successfully");

        mockMvc.perform(post("/api/purchase")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket purchased successfully"));
    }

    @Test
    void testGetReceipt_Success() throws Exception {
        Mockito.when(ticketService.getReceipt(1)).thenReturn("Receipt for ticket ID 1");

        mockMvc.perform(get("/api/receipt")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Receipt for ticket ID 1"));
    }

    @Test
    void testGetReceipt_NotFound() throws Exception {
        Mockito.when(ticketService.getReceipt(66)).thenReturn(null);

        mockMvc.perform(get("/api/receipt")
                        .param("id", "87"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ticket not found"));
    }

    @Test
    void testViewUsersBySection_Success() throws Exception {
        Mockito.when(ticketService.viewUsersBySection("A")).thenReturn("Users in section A");

        mockMvc.perform(get("/api/view")
                        .param("section", "A"))
                .andExpect(status().isOk())
                .andExpect(content().string("Users in section A"));
    }

    @Test
    void testRemoveUser_Success() throws Exception {
        Mockito.when(ticketService.removeUser(1)).thenReturn("User removed successfully");

        mockMvc.perform(delete("/api/remove")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User removed successfully"));
    }

    @Test
    void testModifyUserSeat_Success() throws Exception {
        Mockito.when(ticketService.modifyUserSeat(1, "B")).thenReturn("User seat modified successfully");

        mockMvc.perform(put("/api/modify")
                        .param("id", "1")
                        .param("section", "B"))
                .andExpect(status().isOk())
                .andExpect(content().string("User seat modified successfully"));
    }

    @Test
    void testModifyUserSeat_NotFound() throws Exception {
        Mockito.when(ticketService.modifyUserSeat(39, "A")).thenReturn("Ticket not found");

        mockMvc.perform(put("/api/modify")
                        .param("id", "39")
                        .param("section", "A"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ticket not found"));
    }
}
