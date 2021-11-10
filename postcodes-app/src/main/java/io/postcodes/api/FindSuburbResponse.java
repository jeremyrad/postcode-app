package io.postcodes.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class FindSuburbResponse {

    private List<String> suburbs = new ArrayList<>();
    private long characterCount;
    private List<String> validations = new ArrayList<>();

}
