package com.booking.system.cs203.dto;

public class UserUpdateDto {
    private String email;
    private String name;
    private String number;
    private double attendedPercentage;
    private int bookingCount;
    private String hashPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getAttendedPercentage() {
        return attendedPercentage;
    }

    public void setAttendedPercentage(double attendedPercentage) {
        this.attendedPercentage = attendedPercentage;
    }

    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

}
