package ru.popov.loanrestapi.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.dto.PersonDTO;
import ru.popov.loanrestapi.services.PersonService;
import ru.popov.loanrestapi.util.ErrorResponse;
import ru.popov.loanrestapi.util.ErrorsUtil;
import ru.popov.loanrestapi.util.exceptions.LoanNotFoundException;

import javax.validation.Valid;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> addPerson(@RequestBody @Valid PersonDTO personDTO,
                                              BindingResult bindingResult
    ) {
        Person person = convertPersonDTOToPerson(personDTO);
        if(bindingResult.hasErrors())
            ErrorsUtil.returnErrorsToClient(bindingResult);

        personService.addPerson(person);
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

    private PersonDTO convertPersonToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    private Person convertPersonDTOToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
