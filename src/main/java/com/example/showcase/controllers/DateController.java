package com.example.showcase.controllers;

import com.example.showcase.entity.Date;
import com.example.showcase.service.DateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://showcase-pd.ru"})
@RestController
@RequestMapping("/dates")
public class DateController {
    private DateService dateService;

    @PostMapping
    public ResponseEntity<Date> createDate(@RequestBody Date date) {
        Date saveDate = dateService.createDate(date);
        return new ResponseEntity<>(saveDate, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Date> getDate(@PathVariable("id")  int dateId) {
        Date date = dateService.getDateById(dateId);
        return ResponseEntity.ok(date);
    }

    @GetMapping
    public ResponseEntity<List<Date>> getAllDates() {
        List<Date> dates = dateService.getAllDates();
        return ResponseEntity.ok(dates);
    }

    @PutMapping("{id}")
    public ResponseEntity<Date> updateDate(@PathVariable("id") int dateId, @RequestBody Date updateDate) {
        Date date = dateService.updateDate(dateId, updateDate);
        return ResponseEntity.ok(date);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDate(@PathVariable("id") int dateId) {
        dateService.deleteDate(dateId);
        return ResponseEntity.ok("Date is deleted");
    }
}
