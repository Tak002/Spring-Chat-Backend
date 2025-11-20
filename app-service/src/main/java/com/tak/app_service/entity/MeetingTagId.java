package com.tak.app_service.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTagId implements Serializable {

    private Long meetingId;
    private Long tagId;
}
