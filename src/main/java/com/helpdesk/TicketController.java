package com.helpdesk;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TicketController {

    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // SHOW CREATE TICKET PAGE + MY TICKETS
    @GetMapping("/create-ticket")
    public String showTickets(
            HttpSession session,
            Model model) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        List<Ticket> tickets = ticketRepository.findTicketsByUserId(userId);

        model.addAttribute("tickets", tickets);

        return "create-ticket";
    }

    // CREATE TICKET
    @PostMapping("/create-ticket")
    public String createTicket(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam String priority,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        Ticket ticket = new Ticket();

        ticket.setUser_id(userId);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus("Open");
        ticket.setPriority(priority);
        ticket.setCreated_at(LocalDateTime.now());

        ticketRepository.save(ticket);

        redirectAttributes.addFlashAttribute(
                "success",
                "Ticket created successfully!"
        );

        return "redirect:/create-ticket";
    }

    // DELETE TICKET
    @GetMapping("/delete-ticket")
    public String deleteTicket(
            @RequestParam Integer id,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        Ticket ticket = ticketRepository.findById(id).orElse(null);

        if (ticket != null && ticket.getUser_id().equals(userId)) {
            ticketRepository.delete(ticket);
        }

        return "redirect:/create-ticket";
    }
}
