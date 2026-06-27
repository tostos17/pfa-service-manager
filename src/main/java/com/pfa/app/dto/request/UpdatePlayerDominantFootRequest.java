package com.pfa.app.dto.request;

import com.pfa.app.enums.DominantFoot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayerDominantFootRequest {
    private String playerId;
    private DominantFoot dominantFoot;
}
