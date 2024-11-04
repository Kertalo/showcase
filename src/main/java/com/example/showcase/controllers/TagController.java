package com.example.showcase.controllers;

import com.example.showcase.entity.Tag;
import com.example.showcase.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/tags")
public class TagController {
    private TagService tagService;

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.createTag(tag);
        return new ResponseEntity<>(savedTag, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") int tagId) {
        Tag tag = tagService.getTagById(tagId);
        return ResponseEntity.ok(tag);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @PutMapping("{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable("id") int tagId, @RequestBody Tag updatetag) {
        Tag tag = tagService.updateTag(tagId, updatetag);
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTag(@PathVariable("id") int tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("Tag is deleted");
    }
}
