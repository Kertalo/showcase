package com.example.showcase.service.impl;

import com.example.showcase.entity.Date;
import com.example.showcase.repository.DateRepository;
import com.example.showcase.service.DateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DateServiceImpl implements DateService {
    DateRepository dateRepository;

    @Override
    public Date createDate(Date date) {
        return dateRepository.save(date);
    }

    @Override
    public Date getDateById(int dateId) {
        Date date = dateRepository.findById(dateId)
                .orElseThrow(() -> new RuntimeException("Date not found"));
        return date;
    }

    @Override
    public List<Date> getAllDates() {
        return dateRepository.findAll();
    }

    @Override
    public Date updateDate(int dateId, Date updateDate) {
        Date Date = dateRepository.findById(dateId)
                .orElseThrow(() -> new RuntimeException("Date not found"));
        Date.setName(updateDate.getName());
        return dateRepository.save(Date);
    }

    @Override
    public void deleteDate(int dateId) {
        Date Date = dateRepository.findById(dateId)
                .orElseThrow(() -> new RuntimeException("Date not found"));
        dateRepository.delete(Date);
    }

    @Override
    public Iterable<Date> save(List<Date> dates) {
        return dateRepository.saveAll(dates);
    }

    @Override
    public boolean existsByName(String dateName){return dateRepository.existsByName(dateName);}

    @Override
    public Date getDateByName(String dateName){return dateRepository.getDateByName(dateName);}

}
