package com.example.showcase.service.impl;

import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.entity.Tag;
import com.example.showcase.repository.TagRepository;
import com.example.showcase.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagById(int tagId) {
        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        return tag;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag updateTag(int tagId, Tag updateTag) {
        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tag.setName(updateTag.getName());
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(int tagId) {
        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }

    @Override
    public Iterable<Tag> save(List<Tag> tags) {
        return tagRepository.saveAll(tags);
    }

    @Override
    public boolean existsByName(String tagName) {return tagRepository.existsByName(tagName);}

    @Override
    public Tag getTagByName(String tagName){return tagRepository.getTagByName(tagName);}
}
