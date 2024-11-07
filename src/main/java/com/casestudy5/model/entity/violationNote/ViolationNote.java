package com.casestudy5.model.entity.violationNote;

import com.casestudy5.model.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "violation_notes")
public class ViolationNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user ;

    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

}
