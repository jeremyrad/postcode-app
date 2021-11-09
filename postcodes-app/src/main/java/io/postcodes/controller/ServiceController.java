package io.postcodes.controller;

import io.postcodes.service.UpdatePostCode;
import io.postcodes.service.FindSuburbResponse;
import io.postcodes.service.PostcodeService;
import io.postcodes.service.UpdatePostCodeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postcode")
@Api(value = "Postcode API REST endpoint")
public class ServiceController {

    PostcodeService postcodeService;

    public ServiceController(@Autowired final PostcodeService postcodeService) {
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

    @PostMapping(value = "/postcode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Update postcode details")
    public ResponseEntity<UpdatePostCodeResponse> updatePostCode(@RequestBody final UpdatePostCode updatePostCode) {
        final UpdatePostCodeResponse response = postcodeService.updatePostCode(updatePostCode);
        return ResponseEntity.ok(response);
    }
}
