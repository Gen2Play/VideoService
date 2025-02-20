package com.Gen2Play.VideoService.model.entity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.Gen2Play.VideoService.model.entity.enumeration.MediaTypeEnum;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    // @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private String name;
    private int views = 0;
    private int downloads = 0;
    private MediaTypeEnum mediaType;//enum
    private int FPS;
    private LocalDateTime createAt;
    private boolean isPublic;
    @Nullable
    private LocalDateTime publishAt;
    private UUID owner;
    private String sourceLink;
    private String viewLink;
    private String resolution;
    //relationship
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private Set<VideoHashtag> videoHashtags;
    //1 - n save
    //1 - n video-playlists
    //1 - n hashtag
}
