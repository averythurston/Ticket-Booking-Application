package com.ontariotechu.sofe3980U;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

@Controller
public class BookingController {

    @GetMapping("/")
    public String showFlightSearchForm() {
        return "booking";
    }

    @PostMapping("/search")
    public String searchFlights(@RequestParam("origin") String origin,
                                @RequestParam("destination") String destination,
                                @RequestParam("trip-type") String tripType,
                                @RequestParam("flight-type") String flightType,
                                @RequestParam("ticket-format") String ticketFormat,
                                Model model) {
        // Hardcoded list of possible flights
        List<Flight> flights = createSampleFlights(origin, destination);

        // Format departure and arrival times based on ticket format
        formatFlightTimes(flights, ticketFormat);

        // Filter the flights based on form criteria
        List<Flight> matchingFlights = filterFlights(flights, origin, destination, flightType);

        // Add the list of filtered flights to the model
        model.addAttribute("flights", matchingFlights);

        return "results";
    }

    public List<Flight> createSampleFlights(String origin, String destination) {
        List<Flight> flights = new ArrayList<>();
        // Hardcoded sample flights
        flights.add(new Flight("Toronto", "Vancouver", LocalDateTime.of(2024, 3, 15, 8, 0), LocalDateTime.of(2024, 3, 15, 10, 0), "Direct"));
        flights.add(new Flight("Toronto", "Ottawa", LocalDateTime.of(2024, 3, 15, 12, 0), LocalDateTime.of(2024, 3, 15, 14, 0), "Direct"));
        flights.add(new Flight("Vancouver", "Toronto", LocalDateTime.of(2024, 3, 15, 16, 0), LocalDateTime.of(2024, 3, 15, 18, 0), "Direct"));
        return flights;
    }

    public void formatFlightTimes(List<Flight> flights, String ticketFormat) {
        DateTimeFormatter formatter;
        if (ticketFormat.equals("12-hour")) {
            formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a");
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        }
        for (Flight flight : flights) {
            flight.setFormattedDepartureTime(flight.getDepartureTime().format(formatter));
            flight.setFormattedArrivalTime(flight.getArrivalTime().format(formatter));
            flight.setTotalFlightTime(calculateTotalFlightTime(flight.getDepartureTime(), flight.getArrivalTime()));
        }
    }
    
    public String calculateTotalFlightTime(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        Duration duration = Duration.between(departureTime, arrivalTime);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

    public List<Flight> filterFlights(List<Flight> flights, String origin, String destination, String flightType) {
        List<Flight> matchingFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getOrigin().equalsIgnoreCase(origin)
                    && flight.getDestination().equalsIgnoreCase(destination)
                    && flight.getFlightType().equalsIgnoreCase(flightType)) {
                matchingFlights.add(flight);
            }
        }
        return matchingFlights;
    }

    // Flight class representing a flight
public static class Flight {
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String flightType;
    private String formattedDepartureTime;
    private String formattedArrivalTime;
    private String totalFlightTime; 

    public Flight(String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, String flightType) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightType = flightType;
    }

    // Getters and setters
    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public String getFlightType() {
        return flightType;
    }

    public String getFormattedDepartureTime() {
        return formattedDepartureTime;
    }

    public void setFormattedDepartureTime(String formattedDepartureTime) {
        this.formattedDepartureTime = formattedDepartureTime;
    }

    public String getFormattedArrivalTime() {
        return formattedArrivalTime;
    }

    public void setFormattedArrivalTime(String formattedArrivalTime) {
        this.formattedArrivalTime = formattedArrivalTime;
    }

    public String getTotalFlightTime() {
        return totalFlightTime;
    }

    public void setTotalFlightTime(String totalFlightTime) {
        this.totalFlightTime = totalFlightTime;
    }
}
}
