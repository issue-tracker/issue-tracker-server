package com.ahoo.issuetrackerserver.label.domain;

import com.ahoo.issuetrackerserver.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Label extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String backgroundColorCode;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TextColor textColor;

    public static Label of(String title, String backgroundColorCode, String description,
        TextColor textColor) {
        return new Label(null, title, backgroundColorCode, description, textColor);
    }

    public void update(String title, String backgroundColorCode, String description,
        TextColor textColor) {
        this.title = title;
        this.backgroundColorCode = backgroundColorCode;
        this.description = description;
        this.textColor = textColor;
    }
}
