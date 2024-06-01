package com.trainbooking.cloudbees.controller;

import com.trainbooking.cloudbees.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TrainTicketController {

    private TicketService ticketService;

    @Autowired
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestParam String firstName, @RequestParam String lastName,
                                                 @RequestParam String email) {
        String response = ticketService.purchaseTicket(firstName, lastName, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/receipt")
    public ResponseEntity<String> getReceipt(@RequestParam int id) {
        String response = ticketService.getReceipt(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.status(404).body("Ticket not found");
    }

    @GetMapping("/view")
    public ResponseEntity<String> viewUsersBySection(@RequestParam String section) {
        String response = ticketService.viewUsersBySection(section);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeUser(@RequestParam int id) {
        String response = ticketService.removeUser(id);
        return response.equals("Ticket not found") ? ResponseEntity.status(404).body(response) : ResponseEntity.ok(response);
    }

    @PutMapping("/modify")
    public ResponseEntity<String> modifyUserSeat(@RequestParam int id, @RequestParam String section) {
        String response = ticketService.modifyUserSeat(id, section);
        return response.equals("Ticket not found") ? ResponseEntity.status(404).body(response) : ResponseEntity.ok(response);
    }
}
