package com.spring.Docdoc.utilits.comprator;

import com.spring.Docdoc.dto.WorkTimesDto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class TimeSortComparator implements Comparator<WorkTimesDto> {

    @Override
    public int compare(WorkTimesDto o1, WorkTimesDto o2) {
        LocalTime time1 = parseTime(o1.getStart()) ;
        LocalTime time2 = parseTime(o2.getEnd()) ;

        return time1.compareTo(time2) ;

    }

    private LocalTime parseTime(String o1) {

        return LocalTime.parse(o1, DateTimeFormatter.ofPattern("hh:mm a"));
    }

}
