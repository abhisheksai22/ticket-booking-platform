package com.abhi.bookmytickets.theatre.entity.enums;

import lombok.Getter;

@Getter
public enum SeatStatus {

    AVAILABLE("Available"),
    LOCKED("Locked"),
    BOOKED("Booked");

    final String value;

    SeatStatus(String value) {
        this.value = value;
    }

}
