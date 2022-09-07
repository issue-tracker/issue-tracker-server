package com.ahoo.issuetrackerserver.issue.domain;

public enum Emoji {

    THUMBS_UP("U+1F44D"),
    THUMBS_DOWN("U+1F44E"),
    LAUGH("U+1F604"),
    PARTY_POPPER("U+1F389"),
    CONFUSED("U+1F615"),
    HEART("U+2764 U+FE0F"),
    ROCKET("U+1F680"),
    EYES("U+1F440");

    String unicode;

    Emoji(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode() {
        return unicode;
    }
}
