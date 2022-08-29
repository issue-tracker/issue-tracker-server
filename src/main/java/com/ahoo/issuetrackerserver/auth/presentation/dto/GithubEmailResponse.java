package com.ahoo.issuetrackerserver.auth.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubEmailResponse {

    private String email;
    private Boolean primary;
}
