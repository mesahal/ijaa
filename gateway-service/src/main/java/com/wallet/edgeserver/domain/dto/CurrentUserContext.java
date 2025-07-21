package com.wallet.edgeserver.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CurrentUserContext implements Serializable {
    private String username;
}
