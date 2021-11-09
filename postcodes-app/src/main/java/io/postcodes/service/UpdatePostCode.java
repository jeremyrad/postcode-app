package io.postcodes.service;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdatePostCode {

    private List<PostCodeDto> postCodes;
}
