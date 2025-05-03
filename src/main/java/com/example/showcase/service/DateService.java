package com.example.showcase.service;

import com.example.showcase.entity.Date;

import java.util.List;

public interface DateService {

    Date createDate(Date date);

    Date getDateById(int dateId);

    List<Date> getAllDates();

    Date updateDate(int dateId, Date updateDate);

    void deleteDate(int dateId);

    Iterable<Date> save(List<Date> dates);

    boolean existsByName(String dateName);

    Date getDateByName(String dateName);
}
