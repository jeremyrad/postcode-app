package io.postcodes.service;

import io.postcodes.db.PostCode;
import io.postcodes.db.PostCodeRepository;
import io.postcodes.db.Suburb;
import io.postcodes.db.SuburbRepository;
import io.postcodes.matcher.IsIterableOfSize;
import io.postcodes.util.PostCodeDtoBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.postcodes.util.PostCodeDtoBuilder.aPostCodeDto;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PostcodeServiceTest {

    @MockBean
    private PostCodeRepository postCodeRepository;
    @MockBean
    private SuburbRepository suburbRepository;

    private PostcodeService postcodeService;

    @Before
    public void setUp() {
        postcodeService = new PostcodeService(postCodeRepository, suburbRepository);
    }

    @Test
    public void updatePostCode_Unsaved_NoSuburbs_shouldSavePostCodeOnly() {
        final Integer postCode = 1234;
        final UpdatePostCode updatePostCode = buildUpdatePostCodePayload(postCode);

        when(postCodeRepository.findById(postCode)).thenReturn(Optional.empty());

        final PostCode savedPostCode = new PostCode(postCode);
        when(postCodeRepository.save(any(PostCode.class))).thenReturn(savedPostCode);

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertFalse(response.getPostCodes().isEmpty());
        assertEquals(1, response.getPostCodes().size());
        assertThat(response.getPostCodes().iterator().next().getPostCodeDto().getPostCode(), equalTo(postCode));
        assertTrue(response.getPostCodes().iterator().next().getValidations().isEmpty());
    }

    @Test
    public void updatePostCode_Saved_NoSuburbs_shouldSavePostCodeOnly() {
        final Integer postCode = 1234;
        final UpdatePostCode updatePostCode = buildUpdatePostCodePayload(postCode);

        final PostCode persistedPostCode = new PostCode(postCode);
        when(postCodeRepository.findById(postCode)).thenReturn(Optional.of(persistedPostCode));

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertFalse(response.getPostCodes().isEmpty());
        assertEquals(1, response.getPostCodes().size());
        assertThat(response.getPostCodes().iterator().next().getPostCodeDto().getPostCode(), equalTo(postCode));
        assertTrue(response.getPostCodes().iterator().next().getValidations().isEmpty());
    }

    @Test
    public void updatePostCode_Saved_WithNewSuburbs_shouldSavePostCodesAndSuburbs() {
        final Integer postCode = 1234;

        final UpdatePostCode updatePostCode = buildUpdatePostCodePayload(postCode, asList("sun", "moon", "star"));

        final PostCode persistedPostCode = new PostCode(postCode);
        when(postCodeRepository.findById(postCode)).thenReturn(Optional.of(persistedPostCode));

        final PostCode savedPostCode = new PostCode(postCode);
        when(postCodeRepository.save(any(PostCode.class))).thenReturn(savedPostCode);

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertNotNull(response.getPostCodes());
        assertEquals(1, response.getPostCodes().size());
        assertThat(response.getPostCodes().iterator().next().getPostCodeDto().getPostCode(), equalTo(postCode));
        assertTrue(response.getPostCodes().iterator().next().getValidations().isEmpty());
    }

    @Test
    public void updatePostCode_Saved_WithNewAndOldSuburbs_shouldSavePostCodesAndOnlyNewSuburbs() {
        final Integer postCode = 1234;

        final UpdatePostCode updatePostCode = buildUpdatePostCodePayload(postCode, asList("sun", "moon", "star"));

        final PostCode persistedPostCode = new PostCode(postCode);
        final Suburb persistedSuburb1 = new Suburb();
        persistedSuburb1.setPostCode(persistedPostCode);
        persistedSuburb1.setName("moon");

        final Suburb persistedSuburb2 = new Suburb();
        persistedSuburb2.setPostCode(persistedPostCode);
        persistedSuburb2.setName("mars");
        final List<Suburb> returnedSuburbs = new ArrayList<>();
        returnedSuburbs.add(persistedSuburb1);
        returnedSuburbs.add(persistedSuburb2);

        when(postCodeRepository.findById(postCode)).thenReturn(Optional.of(persistedPostCode));
        when(suburbRepository.findSuburbByPostCodeCode(postCode)).thenReturn(returnedSuburbs);

        final PostCode savedPostCode = new PostCode(postCode);
        when(postCodeRepository.save(any(PostCode.class))).thenReturn(savedPostCode);

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        verify(suburbRepository).saveAll(argThat(new IsIterableOfSize(2)));

        assertNotNull(response.getPostCodes());
        assertEquals(1, response.getPostCodes().size());
        assertThat(response.getPostCodes().iterator().next().getPostCodeDto().getPostCode(), equalTo(postCode));
        assertTrue(response.getPostCodes().iterator().next().getValidations().isEmpty());
    }

    @Test
    public void updatePostCode_PostCodeNull_shouldReturnValidationMessage() {
        final UpdatePostCode updatePostCode = buildUpdatePostCodePayload(null);

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertNotNull(response.getPostCodes());
        assertEquals(1, response.getPostCodes().size());
        assertEquals(1, response.getPostCodes().iterator().next().getValidations().size());
        assertTrue(response.getPostCodes().iterator().next().getValidations().iterator().next().startsWith("The postcode can not be null"));
    }

    @Test
    public void updatePostCode_SuburbNull_shouldReturnValidationMessage() {
        final UpdatePostCode updatePostCode = buildUpdatePostCodePayload(1234, asList(null, "moon", "star"));

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertNotNull(response.getPostCodes());
        assertEquals(1, response.getPostCodes().size());
        assertEquals(1, response.getPostCodes().iterator().next().getValidations().size());
        assertTrue(response.getPostCodes().iterator().next().getValidations().iterator().next().startsWith("The suburb name can not be null"));
    }

    @Test
    public void updatePostCode_PostCodeSizeLessThan4_shouldReturnValidationMessage() {
        final Integer postCode = 123;
        final UpdatePostCode updatePostCode = new UpdatePostCode();
        final List<PostCodeDto> postCodeDtoList = new ArrayList<>();
        postCodeDtoList.add(aPostCodeDto().withAPostCode(postCode).build());
        updatePostCode.setPostCodes(postCodeDtoList);

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertNotNull(response.getPostCodes());
        assertEquals(1, response.getPostCodes().size());
        assertEquals(1, response.getPostCodes().iterator().next().getValidations().size());
        assertTrue(response.getPostCodes().iterator().next().getValidations().iterator().next().startsWith("The post code length of"));
    }

    @Test
    public void updatePostCode_PostCodeSizeMoreThan4_shouldReturnValidationMessage() {
        final Integer postCode = 12345;
        final UpdatePostCode updatePostCode = new UpdatePostCode();
        final List<PostCodeDto> postCodeDtoList = new ArrayList<>();
        postCodeDtoList.add(aPostCodeDto().withAPostCode(postCode).build());
        updatePostCode.setPostCodes(postCodeDtoList);

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertNotNull(response.getPostCodes());
        assertEquals(1, response.getPostCodes().size());
        assertEquals(1, response.getPostCodes().iterator().next().getValidations().size());
        assertTrue(response.getPostCodes().iterator().next().getValidations().iterator().next().startsWith("The post code length of"));
    }

    @Test
    public void updatePostCode_SuburbSizeMoreThanMax_shouldReturnValidationMessage() {
        final Integer postCode = 1234;
        final UpdatePostCode updatePostCode = buildUpdatePostCodePayload(postCode,
                asList("averylongsuburbnameaverylongsuburbnameaverylongsuburbname",
                        "moon", "star"));

        final PostCode persistedPostCode = new PostCode(postCode);
        when(postCodeRepository.findById(postCode)).thenReturn(Optional.of(persistedPostCode));

        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);

        assertNotNull(response.getPostCodes());
        assertEquals(1, response.getPostCodes().size());
        assertEquals(1, response.getPostCodes().iterator().next().getValidations().size());
        assertTrue(response.getPostCodes().iterator().next().getValidations().iterator().next().startsWith("The suburb length of"));
    }

    @Test
    public void findPostCode_PostCodeRangeInvalid_shouldReturnValidationMessage() {
        final int from = 3255;
        final int to = 1456;
        final FindSuburbResponse suburbsByPostCodeRange = postcodeService.findSuburbsByPostCodeRange(from, to);
        assertTrue(suburbsByPostCodeRange.getSuburbs().isEmpty());
        assertEquals(1, suburbsByPostCodeRange.getValidations().size());
        assertTrue(suburbsByPostCodeRange.getValidations().iterator().next().startsWith("The postcode range is invalid"));
    }

    @Test
    public void findPostCode_PostCodeRangeValid_shouldReturnExpectedResponse() {
        final int from = 1235;
        final int to = 1236;

        List<Suburb> suburbs = new ArrayList<>();
        final PostCode postCode1234 = new PostCode(1234);
        suburbs.add(buildSuburb(1L, "sun", postCode1234));
        suburbs.add(buildSuburb(2L, "mars", postCode1234));
        suburbs.add(buildSuburb(3L, "moon", postCode1234));
        final PostCode postCode1235 = new PostCode(1235);
        suburbs.add(buildSuburb(4L, "moon", postCode1235));
        suburbs.add(buildSuburb(5L, "saturn", postCode1235));
        suburbs.add(buildSuburb(6L, "jupiter", postCode1235));
        final PostCode postCode1236 = new PostCode(1236);
        suburbs.add(buildSuburb(7L, "pluto", postCode1236));
        suburbs.add(buildSuburb(8L, "mercury", postCode1236));
        suburbs.add(buildSuburb(9L, "moon", postCode1236));

        when(suburbRepository.findByPostCode_CodeIsBetween(from, to)).thenReturn(suburbs);

        final FindSuburbResponse suburbsByPostCodeRange = postcodeService.findSuburbsByPostCodeRange(from, to);

        assertFalse(suburbsByPostCodeRange.getSuburbs().isEmpty());
        assertEquals(9, suburbsByPostCodeRange.getSuburbs().size());
        assertEquals(44, suburbsByPostCodeRange.getCharacterCount());

    }

    private Suburb buildSuburb(final long id, final String sun, final PostCode postCode) {
        final Suburb suburb = new Suburb();
        suburb.setName(sun);
        suburb.setId(id);
        suburb.setPostCode(postCode);
        return suburb;
    }

    private UpdatePostCode buildUpdatePostCodePayload(final Integer postCode) {
        return buildUpdatePostCodePayload(postCode, null);
    }

    private UpdatePostCode buildUpdatePostCodePayload(final Integer postCode, final List<String> suburbs) {
        final UpdatePostCode updatePostCode = new UpdatePostCode();
        final List<PostCodeDto> postCodeDtoList = new ArrayList<>();
        final PostCodeDtoBuilder postCodeDtoBuilder = aPostCodeDto().withAPostCode(postCode);
        if (suburbs != null && !suburbs.isEmpty()) {
            suburbs.forEach(postCodeDtoBuilder::withASuburb);
        }
        postCodeDtoList.add(postCodeDtoBuilder.build());
        updatePostCode.setPostCodes(postCodeDtoList);
        return updatePostCode;
    }


}