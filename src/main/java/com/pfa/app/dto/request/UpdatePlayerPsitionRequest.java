package com.pfa.app.dto.request;

import com.pfa.app.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayerPsitionRequest {
    private String playerId;
    private Position position;
}
