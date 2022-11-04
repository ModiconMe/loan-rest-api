package ru.popov.loanrestapi.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.popov.loanrestapi.domain.Loan;
import ru.popov.loanrestapi.dto.LoanDTO;
import ru.popov.loanrestapi.dto.LoanResponse;
import ru.popov.loanrestapi.services.LoanServiceImpl;
import ru.popov.loanrestapi.util.ErrorResponse;
import ru.popov.loanrestapi.util.ErrorsUtil;
import ru.popov.loanrestapi.util.exceptions.CountryNotFoundException;
import ru.popov.loanrestapi.util.exceptions.LoanNotFoundException;
import ru.popov.loanrestapi.util.exceptions.PersonIsInBlacklistException;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;

import javax.validation.Valid;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanServiceImpl loanService;
    private final ModelMapper modelMapper;

    @Autowired
    public LoanController(LoanServiceImpl loanService, ModelMapper modelMapper) {
        this.loanService = loanService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> addLoan(@RequestBody @Valid LoanDTO loanDTO,
                                              BindingResult bindingResult
    ) {
        Loan loan = convertLoanDTOToLoan(loanDTO);
        if(bindingResult.hasErrors())
            ErrorsUtil.returnErrorsToClient(bindingResult);

        loanService.addLoan(loan);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/approved/{id}")
    public ResponseEntity<HttpStatus> approveLoan(@PathVariable("id") int id) {
        loanService.approvedLoanById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/approved")
    public LoanResponse getApprovedLoans(@RequestParam(name = "person_name", required = false) String name) {
        LoanResponse loanResponse = new LoanResponse();

        if (name != null) {
            loanResponse.setLoans(loanService.getAllApprovedLoansByPerson(name).stream().map(this::convertLoanToLoanDTO).toList());
        } else {
            loanResponse.setLoans(loanService.getAllApprovedLoans().stream().map(this::convertLoanToLoanDTO).toList());
        }

        return loanResponse;
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse loanErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(loanErrorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonIsInBlacklistException e) {
        ErrorResponse loanErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(loanErrorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CountryNotFoundException e) {
        ErrorResponse loanErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(loanErrorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(LoanNotFoundException e) {
        ErrorResponse loanErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(loanErrorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    private LoanDTO convertLoanToLoanDTO(Loan loan) {
        return modelMapper.map(loan, LoanDTO.class);
    }

    private Loan convertLoanDTOToLoan(LoanDTO loanDTO) {
        return modelMapper.map(loanDTO, Loan.class);
    }
}
