package ru.popov.loanrestapi.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.popov.loanrestapi.domain.Country;
import ru.popov.loanrestapi.dto.CountryDTO;
import ru.popov.loanrestapi.services.CountryServiceImpl;
import ru.popov.loanrestapi.util.ErrorResponse;
import ru.popov.loanrestapi.util.ErrorsUtil;
import ru.popov.loanrestapi.util.exceptions.LoanNotFoundException;

import javax.validation.Valid;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryServiceImpl countryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CountryController(CountryServiceImpl countryService, ModelMapper modelMapper) {
        this.countryService = countryService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> addCountry(@RequestBody @Valid CountryDTO countryDTO,
                                                 BindingResult bindingResult
    ) {
        Country country = convertCountryDTOToCountry(countryDTO);
        if(bindingResult.hasErrors())
            ErrorsUtil.returnErrorsToClient(bindingResult);

        countryService.addCountry(country);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(LoanNotFoundException e) {
        ErrorResponse personErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    private CountryDTO convertCountryToCountryDTO(Country country) {
        return modelMapper.map(country, CountryDTO.class);
    }

    private Country convertCountryDTOToCountry(CountryDTO countryDTO) {
        return modelMapper.map(countryDTO, Country.class);
    }
}
