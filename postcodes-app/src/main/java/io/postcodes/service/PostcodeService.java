package io.postcodes.service;

import io.postcodes.api.FindSuburbResponse;
import io.postcodes.api.PostCodeDto;
import io.postcodes.api.PostCodeResponseDto;
import io.postcodes.api.UpdatePostCode;
import io.postcodes.api.UpdatePostCodeResponse;
import io.postcodes.db.model.PostCode;
import io.postcodes.db.PostCodeRepository;
import io.postcodes.db.model.Suburb;
import io.postcodes.db.SuburbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class PostcodeService {

    private PostCodeRepository postCodeRepository;

    private SuburbRepository suburbRepository;

    @Autowired
    public PostcodeService(final PostCodeRepository postCodeRepository, final SuburbRepository suburbRepository) {
        this.postCodeRepository = postCodeRepository;
        this.suburbRepository = suburbRepository;
    }

    public UpdatePostCodeResponse updatePostCode(final UpdatePostCode updatePostCode) {
        UpdatePostCodeResponse response = new UpdatePostCodeResponse();
        if (!updatePostCode.getPostCodes().isEmpty()) {
            response.setPostCodes(new ArrayList<>());
            updatePostCode.getPostCodes().forEach(postCodeDto -> {
                PostCodeResponseDto postCodeResponseDto = new PostCodeResponseDto();
                final List<String> validations = updatePostCodeAndSuburbs(postCodeDto.getPostCode(), postCodeDto.getSuburbs());
                postCodeResponseDto.setPostCodeDto(postCodeDto);
                postCodeResponseDto.setValidations(validations);
                response.getPostCodes().add(postCodeResponseDto);
            });
        }
        return response;
    }

    private List<String> updatePostCodeAndSuburbs(final Integer postCode, final List<String> suburbNames) {
        final List<String> validations = validatePostCode(postCode);
        validations.addAll(validateSuburbNames(postCode, suburbNames));
        if (validations.isEmpty()) {
            final Optional<PostCode> byId = postCodeRepository.findById(postCode);
            final PostCode persistedPostCode =
                    byId.orElseGet(() -> postCodeRepository.save(new PostCode(postCode)));
            if (suburbNames != null && !suburbNames.isEmpty()) {
                final List<Suburb> suburbByPostCodeCode = suburbRepository.findSuburbByPostCodeCode(postCode);
                final List<Suburb> suburbs = suburbNames.stream().map(name -> {
                    if (suburbByPostCodeCode.stream().noneMatch(suburb -> suburb.getName().equals(name))) {
                        final Suburb suburb = new Suburb();
                        suburb.setName(name);
                        suburb.setPostCode(persistedPostCode);
                        return suburb;
                    } else {
                        return null;
                    }
                }).filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!suburbs.isEmpty()) {
                    suburbRepository.saveAll(suburbs);
                }
            }
        }
        return validations;
    }

    private List<String> validateRange(final Integer from, final Integer to) {
        final List<String> validations = new ArrayList<>();
        if (to < from) {
            validations.add("The postcode range is invalid. The postcode range end " + to
                    + " is before the postcode range start " + from + ".");
        }
        return validations;
    }

    private List<String> validateSuburbNames(final Integer postcode, final List<String> suburbNames) {
        final List<String> validations = new ArrayList<>();
        suburbNames.forEach(s -> {
            if (s == null) {
                validations.add("The suburb name can not be null. A valid non null suburb name is required to be associated to postcode "
                        + postcode + ".");
            } else if (s.length() > 50) {
                validations.add("The suburb length of " + s.length() + " for suburb name " + s + " is invalid."
                        + " The required suburb name length can be no more than 250 characters.");
            }
        });
        return validations;
    }

    private List<String> validatePostCode(final Integer postCode) {
        final List<String> validations = new ArrayList<>();
        if (postCode == null) {
            validations.add("The postcode can not be null, postcode is required to update a postcode");
        } else {
            if (postCode < 1000 || postCode > 9999) {
                validations.add("The post code " + postCode + " with length of " + String.valueOf(postCode).length()
                        + " is invalid. The required postcode length is 4 digits.");
            }
        }
        return validations;
    }

    public FindSuburbResponse findSuburbsByPostCodeRange(final Integer postcodeFrom, final Integer postcodeTo) {
        final FindSuburbResponse response = new FindSuburbResponse();
        final List<String> validations = validatePostCode(postcodeFrom);
        validations.addAll(validatePostCode(postcodeTo));
        validations.addAll(validateRange(postcodeFrom, postcodeTo));
        if (validations.isEmpty()) {
            final List<Suburb> byCodeCode = suburbRepository.findByPostCode_CodeIsBetween(postcodeFrom, postcodeTo);
            final List<String> names = byCodeCode.stream().map(Suburb::getName).collect(Collectors.toList());
            response.getSuburbs().addAll(names);
            final long count = names.stream().map(String::length).mapToInt(value -> value).sum();
            response.setCharacterCount(count);
        }
        response.setValidations(validations);
        return response;
    }

    public void deletePostCode(final Integer postcode) {
        postCodeRepository.findById(postcode).ifPresent(postCode -> postCodeRepository.delete(postCode));
    }

    public PostCodeDto deletePostCodeSuburb(final Integer postcode, final String suburb) {
        final Suburb suburbByPostCodeCode = suburbRepository.findSuburbByPostCodeCodeAndName(postcode, suburb);
        suburbRepository.delete(suburbByPostCodeCode);
        return findPostCode(postcode);
    }

    public PostCodeDto findPostCode(final Integer postcode) {
        final Optional<PostCode> byId = postCodeRepository.findById(postcode);
        if (byId.isPresent()) {
            final PostCodeDto postCodeDto = new PostCodeDto();
            postCodeDto.setPostCode(postcode);
            final List<Suburb> suburbByPostCodeCode = suburbRepository.findSuburbByPostCodeCode(postcode);
            postCodeDto.setSuburbs(suburbByPostCodeCode.stream().map(Suburb::getName).collect(Collectors.toList()));
            return postCodeDto;
        }
        return null;
    }
}
