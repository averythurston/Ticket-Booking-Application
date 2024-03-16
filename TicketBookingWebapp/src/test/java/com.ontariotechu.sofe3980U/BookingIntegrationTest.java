package com.ontariotechu.sofe3980U;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

public class BookingIntegrationTest {

    @Test
    public void testSearchAndFilterIntegration() {
        BookingController controller = new BookingController();
        Model model = new BindingAwareModelMap();

        // Perform a search with criteria that should yield results
        String viewName = controller.searchFlights("Toronto", "Vancouver", "round-trip", "Direct", "24-hour", model);

        // Assert the correct view is returned
        assertEquals("results", viewName, "View name should be 'results'.");

        // Assert the model contains flights that match the criteria
        List<BookingController.Flight> flights = (List<BookingController.Flight>) ((BindingAwareModelMap) model).get("flights");
        assertFalse(flights.isEmpty(), "Filtered flights list should not be empty.");

        // Check all flights meet the search criteria
        assertTrue(flights.stream().allMatch(flight ->
            flight.getOrigin().equalsIgnoreCase("Toronto") &&
            flight.getDestination().equalsIgnoreCase("Vancouver") &&
            flight.getFlightType().equalsIgnoreCase("Direct")),
            "All flights should match the search and filter criteria.");
    }

    @Test
    public void testSearchAndTimeFormattingIntegration() {
        BookingController controller = new BookingController();
        Model model = new BindingAwareModelMap();

        // Perform a search that requires time formatting
        controller.searchFlights("Toronto", "Vancouver", "one-way", "Direct", "12-hour", model);

        List<BookingController.Flight> flights = (List<BookingController.Flight>) ((BindingAwareModelMap) model).get("flights");

        // Assert that flight times are formatted according to user preference (12-hour format)
        assertTrue(flights.stream().allMatch(flight ->
            flight.getFormattedDepartureTime().matches(".+ (a.m.|p.m.)$") &&
            flight.getFormattedArrivalTime().matches(".+ (a.m.|p.m.)$")),
            "Flight times should be formatted to 12-hour format.");
    }
}
