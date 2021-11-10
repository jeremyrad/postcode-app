package io.postcodes.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostCodeDto {

    private Integer postCode;
    private List<String> suburbs;
}
