package com.littlepay;

import com.littlepay.model.Taps;
import com.littlepay.model.Trip;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.List;

public class TripCalculatorTest {
    TripCalculator tripCalculator = new TripCalculator();

    @Test
    public void testMain() {
        String tapsFileName = "taps.csv";
        String tripsFileName = "trips.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File tapsfile = new File(classLoader.getResource(tapsFileName).getFile());
        File tripsfile = new File(classLoader.getResource(tripsFileName).getFile());
        String[] args = new String[2];
        args[0] = tapsfile.getPath();
        args[1] = tripsfile.getPath();
        TripCalculator.main(args);
    }

    @Test
    public void readTapsTest() throws Exception {
        String resourceName = "taps.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        String absolutePath = file.getAbsolutePath();
        List<Taps> taps= tripCalculator
                .readTaps(absolutePath, Taps.class);
        assertEquals(taps.size(), 11);
    }

    @Test
    public void readTapsTestNotFound() {
        String resourceName = "taps1.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        //File file = new File(classLoader.getResource(resourceName).getFile());
        assertThrows(NoSuchFileException.class,
                () -> tripCalculator.readTaps("src/test/resources/taps1.csv", Taps.class));
    }

    @Test
    public void calculateTripTest_cancelled() {
        Trip trip = new Trip();
        trip.setFromStopId("Stop1");
        trip.setToStopId("Stop1");
        trip.setStarted(LocalDateTime.now());
        trip.setFinished(LocalDateTime.now());
        tripCalculator.calculateTrip(trip);
        assertEquals(trip.getChargeAmount(), 0.0, 0.0);
        assertEquals(trip.getStatus(), "Cancelled");
    }

    @Test
    public void calculateTripTest_Incomplete() {
        Trip trip = new Trip();
        trip.setFromStopId("Stop2");
        trip.setStarted(LocalDateTime.now());
        trip.setFinished(LocalDateTime.now());
        tripCalculator.calculateTrip(trip);
        assertEquals(trip.getChargeAmount(), 5.5, 0.0);
        assertEquals(trip.getStatus(), "Incomplete");
    }

    @Test
    public void calculateTripTest_Complete() {
        Trip trip = new Trip();
        trip.setFromStopId("Stop2");
        trip.setToStopId("Stop3");
        trip.setStarted(LocalDateTime.now());
        trip.setFinished(LocalDateTime.now());
        tripCalculator.calculateTrip(trip);
        assertEquals(trip.getChargeAmount(), 5.5, 0.0);
        assertEquals(trip.getStatus(), "Completed");
    }
}