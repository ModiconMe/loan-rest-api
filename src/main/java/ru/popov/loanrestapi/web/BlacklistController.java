package ru.popov.loanrestapi.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.popov.loanrestapi.domain.Person;
import ru.popov.loanrestapi.dto.PersonDTO;
import ru.popov.loanrestapi.services.BlacklistService;
import ru.popov.loanrestapi.services.BlacklistServiceImpl;
import ru.popov.loanrestapi.util.ErrorResponse;
import ru.popov.loanrestapi.util.ErrorsUtil;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;

import javax.validation.Valid;

@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

    private final BlacklistService blacklistService;
    private final ModelMapper modelMapper;

    @Autowired
    public BlacklistController(BlacklistServiceImpl blacklistService, ModelMapper modelMapper) {
        this.blacklistService = blacklistService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addPersonToBlacklist(@RequestBody @Valid PersonDTO personDTO,
                                                           BindingResult bindingResult
    ) {
        Person person = convertPersonDTOToPerson(personDTO);
        if(bindingResult.hasErrors())
            ErrorsUtil.returnErrorsToClient(bindingResult);

        blacklistService.addPersonToBlackList(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<HttpStatus> removePersonFromBlacklist(@RequestBody @Valid PersonDTO personDTO,
                                                           BindingResult bindingResult
    ) {
        Person person = convertPersonDTOToPerson(personDTO);
        if(bindingResult.hasErrors())
            ErrorsUtil.returnErrorsToClient(bindingResult);

        blacklistService.removePersonFromBlackList(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private PersonDTO convertPersonToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    private Person convertPersonDTOToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
