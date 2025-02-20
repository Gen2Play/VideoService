package com.Gen2Play.VideoService.services.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.Gen2Play.VideoService.mappers.VideoMapper;
import com.Gen2Play.VideoService.model.dto.tags.HashtagResponse;
import com.Gen2Play.VideoService.model.entity.Hashtag;
import com.Gen2Play.VideoService.repositories.HashtagRepository;
import com.Gen2Play.VideoService.specification.HashtagSpecification;

@Service
public class TagService implements ITagService{
    @Autowired
    private HashtagRepository tagRepository;

    @Override
    public List<HashtagResponse> getRelatedTags(String tag) throws Exception {
        List<HashtagResponse> result = new ArrayList<>();
        try {
            Optional<Hashtag> hashtagOptional = tagRepository.findByName(tag);
            if (!hashtagOptional.isPresent()) {
                throw new NoSuchElementException("Tag with name not found");
            }
            Hashtag hashtag = hashtagOptional.get();
            Specification<Hashtag> spec = Specification
                        .where(HashtagSpecification.hasForeignKey(hashtag.getId(),"relatedHashtags","relatedHashtag"));
            // ✅ Thêm sắp xếp theo count giảm dần
            Sort sort = Sort.by(Sort.Direction.DESC, "relatedHashtags.count");

            List<Hashtag> hashtags = tagRepository.findAll(spec, sort);
            result = VideoMapper.INSTANCE.hashtagListToHashtagResponsesList(hashtags);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

}
