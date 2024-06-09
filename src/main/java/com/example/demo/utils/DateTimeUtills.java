package com.example.demo.utils;

import com.example.demo.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtills {

    //cắt chuỗi ngày giờ thành ngày và giờ
    public static String[] dateTimeSplit(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
            String date = dateTime.toLocalDate().toString();
            String time = dateTime.toLocalTime().toString();
            return new String[]{date, time};
        } catch (Exception e) {
            throw new BadRequestException("khong the xu ly string: " + input);
        }

    }

    // so sánh thời gian ở parameter và thời gian hiện tại
    public static boolean isExpired(String date, String time) {

        String[] times = time.split("-");
        if(times.length != 2){
            throw new BadRequestException("dinh dang khong hop le!");
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(times[1].trim(), timeFormatter);

        LocalDateTime input = LocalDateTime.of(localDate, localTime);
        LocalDateTime current = LocalDateTime.now();
        if (input.isBefore(current)) {
            return true;
        }
        return false;
    }
}
