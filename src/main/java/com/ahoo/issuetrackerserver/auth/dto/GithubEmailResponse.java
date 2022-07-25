package com.ahoo.issuetrackerserver.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubEmailResponse {

    private String email;
    private Boolean primary;
}
