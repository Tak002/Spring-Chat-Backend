package com.tak.app_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meeting_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTag {

    @EmbeddedId
    private MeetingTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("meetingId")
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
