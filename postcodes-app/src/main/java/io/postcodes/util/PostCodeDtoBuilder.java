package io.postcodes.util;

import io.postcodes.service.PostCodeDto;

import java.util.ArrayList;
import java.util.List;

public class PostCodeDtoBuilder {

    private Integer postCode;
    private List<String> suburbs = new ArrayList<>();

    public static PostCodeDtoBuilder aPostCodeDto() {
        return new PostCodeDtoBuilder();
    }

    public PostCodeDtoBuilder withAPostCode(final Integer postCode) {
        this.postCode = postCode;
        return this;
    }

    public PostCodeDtoBuilder withASuburb(final String suburbName) {
        this.suburbs.add(suburbName);
        return this;
    }

    public PostCodeDto build() {
        final PostCodeDto postCodeDto = new PostCodeDto();
        postCodeDto.setPostCode(this.postCode);
        postCodeDto.setSuburbs(this.suburbs);
        return postCodeDto;
    }

}
