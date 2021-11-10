package io.postcodes.api;


import io.postcodes.api.PostCodeResponseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class UpdatePostCodeResponse {

    private List<PostCodeResponseDto> postCodes;
}
