package com.trainbooking.cloudbees.impl;

import com.trainbooking.cloudbees.entity.*;
import com.trainbooking.cloudbees.exception.*;
import com.trainbooking.cloudbees.service.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TicketServiceImpl implements TicketService {

    private final Map<Integer, Ticket> tickets = new HashMap<>();
    private final Map<String, List<User>> sectionUsers = new HashMap<>();
    private final Map<String, Set<Integer>> availableSeats = new HashMap<>();
    private final AtomicInteger ticketIdGenerator = new AtomicInteger();
    private final AtomicInteger userIdGenerator = new AtomicInteger();
    private static final int MAX_SEATS = 20;
    private final String sectionA = "A";
    private final String sectionB = "B";

    public TicketServiceImpl() {
        availableSeats.put(sectionA, new HashSet<>());
        availableSeats.put(sectionB, new HashSet<>());
        for (int i = 1; i <= MAX_SEATS; i++) {
            availableSeats.get(sectionA).add(i);
            availableSeats.get(sectionB).add(i);
        }
    }

    public String purchaseTicket(String firstName, String lastName, String email) {
        User user = new User(userIdGenerator.incrementAndGet(), firstName, lastName, email);
        int ticketId = ticketIdGenerator.incrementAndGet();
        String section = allocateSection();

        if (section == null) {
           throw new NoSeatsAvailableException("No seats available in both sections");
        }

        int seatNumber = allocateSeat(section);
        Ticket ticket = new Ticket(ticketId, "London", "France", user, 20.0, section, seatNumber);

        tickets.put(ticketId, ticket);
        sectionUsers.computeIfAbsent(section, k -> new ArrayList<>()).add(user);

        return "Ticket purchased successfully with Ticket Id:" + ticketId + " in section " + section + " seat " + seatNumber;
    }

    public String getReceipt(int id)
    {
        Ticket ticket = tickets.get(id);
        return ticket != null ? ticket.toString() : null;
    }

    public String viewUsersBySection(String section)
    {
        List<User> users = sectionUsers.get(section);
        if (users != null)
        {
            List<Ticket> tickets = users.stream()
                    .map(user -> getTicketForUserInSection(user, section))
                    .filter(Objects::nonNull)
                    .toList();

            StringBuilder result = new StringBuilder();
            for (Ticket ticket : tickets)
            {
                result.append("Name: ").append(ticket.getUser().getFirstName()).append(" ").append(ticket.getUser().getLastName());
                result.append(", Email: ").append(ticket.getUser().getEmail());
                result.append(", Seat Number: ").append(ticket.getSeatNumber()).append("\n");
            }
            return result.toString();
        }
        else
        {
            return "No users in this section";
        }
    }

    public String removeUser(int id)
    {
        Ticket ticket = tickets.remove(id);
        if (ticket != null)
        {
            sectionUsers.get(ticket.getSection()).remove(ticket.getUser());
            availableSeats.get(ticket.getSection()).add(ticket.getSeatNumber());
            return "User removed successfully";
        }
        else
        {
            throw new TicketNotFoundException("Ticket Not Found");
        }
    }

    public String modifyUserSeat(int id, String section)
    {
        Ticket ticket = tickets.get(id);
        if (ticket != null)
        {
            sectionUsers.get(ticket.getSection()).remove(ticket.getUser());
            availableSeats.get(ticket.getSection()).add(ticket.getSeatNumber());

            String newSection = section.equals(sectionA) ? sectionB : sectionA;
            int newSeatNumber = allocateSeat(newSection);

            ticket.setSection(newSection);
            ticket.setSeatNumber(newSeatNumber);
            sectionUsers.computeIfAbsent(newSection, k -> new ArrayList<>()).add(ticket.getUser());
            return "User seat modified successfully to section " + newSection + " seat " + newSeatNumber;
        }
        else
        {
            return "Ticket not found";
        }
    }

    private String allocateSection()
    {
        if (!availableSeats.get(sectionA).isEmpty() && !availableSeats.get(sectionB).isEmpty())
        {
            return new Random().nextBoolean() ? sectionA : sectionB;
        }
        else if (!availableSeats.get(sectionA).isEmpty())
        {
            return sectionA;
        }
        else if (!availableSeats.get(sectionB).isEmpty())
        {
            return sectionB;
        }
        else
        {
            return null;
        }
    }

    private int allocateSeat(String section)
    {
        int seatNumber = availableSeats.get(section).iterator().next();
        availableSeats.get(section).remove(seatNumber);
        return seatNumber;
    }

    private Ticket getTicketForUserInSection(User user, String section)
    {
        for (Ticket ticket : tickets.values())
        {
            if (ticket.getUser().equals(user) && ticket.getSection().equals(section))
            {
                return ticket;
            }
        }
        return null;
    }
}

