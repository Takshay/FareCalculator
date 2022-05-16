package com.littlepay.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDateTime;


public class Taps {

    /*ID,DateTimeUTC,TapType,StopId,CompanyId,BusID,PAN*/
    @CsvBindByPosition(position = 0)// Name(column = "ID")
    private int id;

    @CsvDate(value = "dd-MM-yyyy HH:mm:ss")
    @CsvBindByPosition(position = 1)//Name(column = "DateTimeUTC")//22-01-2018 13:05:00
    private LocalDateTime dateTimeUtc;

    @CsvBindByPosition(position = 2)//Name(column = "TapType")
    private TapType tap;

    @CsvBindByPosition(position = 3)//Name(column = "StopId")
    private String stopId;

    @CsvBindByPosition(position = 4) //Name(column = "CompanyId")
    private String companyId;

    @CsvBindByPosition(position = 5) //Name(column = "BusID")
    private String busId;

    @CsvBindByPosition(position = 6) //Name(column = "PAN")
    private String pan;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeUtc() {
        return dateTimeUtc;
    }

    public void setDateTimeUtc(final LocalDateTime dateTimeUtc) {
        this.dateTimeUtc = dateTimeUtc;
    }

    public TapType getTap() {
        return tap;
    }

    public void setTap(final TapType tap) {
        this.tap = tap;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(final String stopId) {
        this.stopId = stopId;
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

    public void setBusId(final String busId) {
        this.busId = busId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(final String pan) {
        this.pan = pan;
    }

}
