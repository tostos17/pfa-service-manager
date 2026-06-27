package com.pfa.app.dto.request;

import com.pfa.app.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayerCategoryRequest {
    private String playerId;
    private Category category;
}
