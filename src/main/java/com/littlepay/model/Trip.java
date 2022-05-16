package com.littlepay.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.time.LocalDateTime;

public class Trip {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "Started")
    private LocalDateTime started;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "Finished")
    private LocalDateTime finished;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "DurationSecs")
    private Long durationSecs;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "FromStopId")
    private String fromStopId;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "ToStopId")
    private String toStopId;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "chargeAmount")
    private Double chargeAmount;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "CompanyId")
    private String companyId;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "BusId")
    private String busId;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "PAN")
    private String pan;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "Status")
    private String status;

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(final LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public void setFinished(final LocalDateTime finished) {
        this.finished = finished;
    }

    public Long getDurationSecs() {
        return durationSecs;
    }

    public void setDurationSecs(final Long durationSecs) {
        this.durationSecs = durationSecs;
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public void setFromStopId(final String fromStopId) {
        this.fromStopId = fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public void setToStopId(String toStopId) {
        this.toStopId = toStopId;
    }

    public Double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(final Double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final String companyId) {
        this.companyId = companyId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String  getPan() {
        return pan;
    }

    public void setPan(final String pan) {
        this.pan = pan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "started=" + started +
                ", finished=" + finished +
                ", durationSecs=" + durationSecs +
                ", fromStopId='" + fromStopId + '\'' +
                ", toStopId='" + toStopId + '\'' +
                ", chargeAmount=" + chargeAmount +
                ", companyId='" + companyId + '\'' +
                ", busId='" + busId + '\'' +
                ", pan=" + pan +
                ", status='" + status + '\'' +
                '}';
    }
}
