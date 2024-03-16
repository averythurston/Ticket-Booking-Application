package com.ontariotechu.sofe3980U;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.time.LocalDateTime;

public class BookingControllerTest {

    @Test
    public void testCreateSampleFlights() {
        BookingController controller = new BookingController();
        List<BookingController.Flight> flights = controller.createSampleFlights("Toronto", "Vancouver");

        // Assert that the correct number of sample flights are created
        assertEquals(3, flights.size(), "Should create 3 sample flights");
    }

    @Test
    public void testFilterFlights() {
        BookingController controller = new BookingController();
        List<BookingController.Flight> allFlights = controller.createSampleFlights("Toronto", "Vancouver");
        List<BookingController.Flight> filteredFlights = controller.filterFlights(allFlights, "Toronto", "Vancouver", "Direct");

        // Assert that all filtered flights match the criteria
        assertTrue(filteredFlights.stream().allMatch(flight ->
        flight.getOrigin().equalsIgnoreCase("Toronto") &&
        flight.getDestination().equalsIgnoreCase("Vancouver") &&
        flight.getFlightType().equalsIgnoreCase("Direct")),
        "Filtered flights should match criteria");
    }

    @Test
    public void testFormatFlightTimes() {
        BookingController controller = new BookingController();
        List<BookingController.Flight> flights = controller.createSampleFlights("Toronto", "Vancouver");
        controller.formatFlightTimes(flights, "12-hour");

        // Iterate through each flight to check and log the formatted time
        for (BookingController.Flight flight : flights) {
            String departureTime = flight.getFormattedDepartureTime();
            String arrivalTime = flight.getFormattedArrivalTime();

            System.out.println("Departure Time: " + departureTime + ", Arrival Time: " + arrivalTime); // Log the times

            assertTrue(departureTime.contains("a.m.") || departureTime.contains("p.m."),
            "Departure time should be formatted to 12-hour format. Found: " + departureTime);
            assertTrue(arrivalTime.contains("a.m.") || arrivalTime.contains("p.m."),
            "Arrival time should be formatted to 12-hour format. Found: " + arrivalTime);
        }
    }

    @Test
    public void testCalculateTotalFlightTime() {
        BookingController controller = new BookingController();
        LocalDateTime departure = LocalDateTime.of(2024, 3, 15, 8, 0);
        LocalDateTime arrival = LocalDateTime.of(2024, 3, 15, 10, 0);
        String totalFlightTime = controller.calculateTotalFlightTime(departure, arrival);

        // Assert that the total flight time is calculated correctly
        assertEquals("02:00", totalFlightTime, "Total flight time should be 02:00 hours");
    }
}
