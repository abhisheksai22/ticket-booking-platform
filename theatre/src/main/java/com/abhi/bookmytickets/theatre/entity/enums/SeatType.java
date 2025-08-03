package com.abhi.bookmytickets.theatre.entity.enums;

import lombok.Getter;

@Getter
public enum SeatType {

    REGULAR("Regular"),
    PREMIUM("Premium");

    final String value;

    SeatType(String value){
        this.value = value;
    }

}
