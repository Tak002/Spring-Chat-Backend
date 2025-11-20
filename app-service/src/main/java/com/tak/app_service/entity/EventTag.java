package com.tak.app_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventTag {

    @EmbeddedId
    private EventTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
