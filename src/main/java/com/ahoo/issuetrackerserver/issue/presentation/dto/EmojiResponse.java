package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.Emoji;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이모지 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmojiResponse {

    @Schema(description = "이모지 이름")
    private String name;

    @Schema(description = "이모지 유니코드")
    private String unicode;

    public static EmojiResponse from(Emoji emoji) {
        return new EmojiResponse(emoji.name(), emoji.getUnicode());
    }
}
