package com.tak.app_service.entity;

import com.tak.app_service.entity.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "join_form_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinFormQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // form_id FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private JoinForm form;

    @Column(nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(name = "order_no", nullable = false)
    private Integer orderNo;
}
