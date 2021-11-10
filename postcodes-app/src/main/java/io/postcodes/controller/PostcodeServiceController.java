package io.postcodes.controller;

import io.postcodes.api.PostCodeDto;
import io.postcodes.api.UpdatePostCode;
import io.postcodes.api.FindSuburbResponse;
import io.postcodes.service.PostcodeService;
import io.postcodes.api.UpdatePostCodeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/postcode")
@Api(value = "Postcode API REST endpoint")
public class PostcodeServiceController {

    PostcodeService postcodeService;

    public PostcodeServiceController(@Autowired final PostcodeService postcodeService) {
        this.postcodeService = postcodeService;
    }

    @GetMapping(path = "/from/{postcodeFrom}/to/{postcodeTo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve all suburbs associated to a postcode")
    public ResponseEntity<FindSuburbResponse> findSuburbsByCode(
            @PathVariable final Integer postcodeFrom,
            @PathVariable final Integer postcodeTo) {
        final FindSuburbResponse suburbsByPostCode = postcodeService.findSuburbsByPostCodeRange(postcodeFrom, postcodeTo);
        return ResponseEntity.ok(suburbsByPostCode);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Update postcode details")
    public ResponseEntity<UpdatePostCodeResponse> updatePostCode(@RequestBody final UpdatePostCode updatePostCode) {
        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{postcode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve a postcode with its associated suburbs")
    public ResponseEntity<PostCodeDto> findPostCode(
            @PathVariable final Integer postcode) {
        final PostCodeDto postCodeDto = postcodeService.findPostCode(postcode);
        if (postCodeDto != null) {
            return ResponseEntity.ok(postCodeDto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{postcode}")
    @ApiOperation(value = "Delete a postcode")
    public ResponseEntity<Object> deletePostCode(
            @PathVariable final Integer postcode) {
        postcodeService.deletePostCode(postcode);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{postcode}/suburb/{suburb}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete a postcode suburb")
    public ResponseEntity<Object> deletePostCodeSuburb(
            @PathVariable final Integer postcode,
            @PathVariable final String suburb) {
        final PostCodeDto postCodeDto = postcodeService.deletePostCodeSuburb(postcode, suburb);
        return ResponseEntity.ok(postCodeDto);
    }

}
