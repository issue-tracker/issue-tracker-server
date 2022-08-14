package com.ahoo.issuetrackerserver.label.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String backgroundColorCode;

    private String description;

    @Column(nullable = false)
    private TextBrightness textBrightness;

    //TODO
    // IssueLabel List

    public static Label of(String title, String backgroundColorCode, String description,
        TextBrightness textBrightness) {
        return new Label(null, title, backgroundColorCode, description, textBrightness);
    }
}
