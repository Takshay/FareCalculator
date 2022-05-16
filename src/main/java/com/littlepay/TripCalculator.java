package com.littlepay;

import com.littlepay.model.Stop;
import com.littlepay.model.TapType;
import com.littlepay.model.Taps;
import com.littlepay.model.Trip;
import com.littlepay.service.FareMatrix;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TripCalculator {

    private FareMatrix fareMatrix = new FareMatrix();

    public static void main(final String[] args) {
        TripCalculator tripCalculator = new TripCalculator();
        Map<String, List<Trip>> trips = new HashMap<>();

        System.out.println("Stating...");
        if (args[0] == null || args[0].trim().isEmpty()
                || args[1] == null || args[1].trim().isEmpty()) {
            System.out.println("You need to specify a path for taps.csv");
        } else {
            try {
                // Read taps file
                List<Taps> taps = tripCalculator.readTaps(args[0], Taps.class);

                //Sort list by id to make sure we receive taps in sequence
                taps.sort(Comparator.comparing(Taps::getId));

                // Grouping by PAN as its the only unique column to identify single user
                System.out.println("Grouping Taps by PAN");
                Map<String, List<Taps>> groupedTaps = taps
                        .stream()
                        .collect(Collectors.groupingBy(Taps::getPan));

                //Creating Trip
                System.out.println("Creating Trip");
                tripCalculator.createTrip(trips, groupedTaps);

                //Writing trip to file
                tripCalculator.writeTrips(args[1], trips.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue().stream()).collect(Collectors.toList()));

            } catch (IOException | URISyntaxException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void createTrip(final Map<String, List<Trip>> trips, final Map<String, List<Taps>> groupedTaps) {
        for (String key : groupedTaps.keySet()) {
            List<Taps> groupedTapsList = groupedTaps.get(key);
            groupedTapsList.forEach(row -> {
                Trip trip = this.getTrip(trips, row.getPan());
                if (Objects.nonNull(row.getTap()) && TapType.ON.equals(row.getTap())) {
                    trip.setStarted(row.getDateTimeUtc());
                    trip.setFromStopId(row.getStopId());
                    trip.setBusId(row.getBusId());
                    trip.setPan(row.getPan());
                    trip.setCompanyId(row.getCompanyId());
                } else if (Objects.nonNull(row.getTap()) && TapType.OFF.equals(row.getTap())) {
                    trip.setFinished(row.getDateTimeUtc());
                    trip.setToStopId(row.getStopId());
                } else {
                    System.out.println("Invalid taps....");
                }
                if (groupedTapsList.size() == 1
                        || (Objects.nonNull(trip.getFinished()) || Objects.nonNull(trip.getToStopId()))) {
                    this.calculateTrip(trip);
                }
                if (!trips.containsKey(row.getPan())) {
                    trips.put(row.getPan(), Arrays.asList(trip));
                }
            });
        }
    }

    public List<Taps> readTaps(final String tapsFilePath, final Class<Taps> beanClass) throws Exception {
        Path path = Paths.get(tapsFilePath);
        System.out.println("Reading Taps " + path.toAbsolutePath());
        Reader reader = Files.newBufferedReader(path);
        ColumnPositionMappingStrategy<Taps> mappingStrategy = new ColumnPositionMappingStrategy<>();
        mappingStrategy.setType(beanClass);
        CsvToBean<Taps> csvToTaps = new CsvToBeanBuilder<Taps>(reader)
                .withType(Taps.class)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .withMappingStrategy(mappingStrategy)
                .build();
        return csvToTaps.parse();
    }

    public final void writeTrips(final String tripFilePath, final List<Trip> trip)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, URISyntaxException {
        File tripFile = new File(tripFilePath);
        if (!tripFile.exists()) {
            System.out.println("File Created : " + tripFile.getPath());
            tripFile.createNewFile();
        }
        Path path = Paths.get(tripFile.getPath());
        System.out.println("Writing Trips " + path.toAbsolutePath());
        Writer writer = new FileWriter(path.toString(), false);

        StatefulBeanToCsv<Trip> statefulBeanToCsv = new StatefulBeanToCsvBuilder<Trip>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withApplyQuotesToAll(false)
                .build();

        statefulBeanToCsv.write(trip);
        writer.close();
    }

    public Trip getTrip(final Map<String, List<Trip>> trips, final String pan) {
        if (trips.containsKey(pan)) {
            return trips.get(pan)
                    .stream()
                    .filter(trip -> Objects.isNull(trip.getStatus()))
                    .findFirst().orElse(new Trip());
        } else {
            return new Trip();
        }
    }

    public void calculateTrip(final Trip trip) {

        double chargeAmount = 0.0;
        if (trip.getFromStopId().equals(trip.getToStopId())) {
            trip.setStatus("Cancelled");
            trip.setDurationSecs(ChronoUnit.SECONDS.between(trip.getStarted(), trip.getFinished()));
        } else if (trip.getToStopId() == null) {
            trip.setStatus("Incomplete");
            chargeAmount = fareMatrix.getPossibleFare(Stop.valueOf(trip.getFromStopId()).getIndex());
        } else {
            trip.setDurationSecs(ChronoUnit.SECONDS.between(trip.getStarted(), trip.getFinished()));
            trip.setStatus("Completed");
            chargeAmount = fareMatrix.getFare(Stop.valueOf(trip.getFromStopId()).getIndex(),
                    Stop.valueOf(trip.getToStopId()).getIndex());
        }
        trip.setChargeAmount(chargeAmount);
    }
}
