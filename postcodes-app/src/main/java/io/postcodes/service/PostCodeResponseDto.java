package io.postcodes.service;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostCodeResponseDto {

    private PostCodeDto postCodeDto;
    private List<String> validations;
}
