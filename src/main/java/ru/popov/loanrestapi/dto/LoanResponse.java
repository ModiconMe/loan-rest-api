package ru.popov.loanrestapi.dto;

import java.util.List;

public class LoanResponse {

    private List<LoanDTO> loansDTO;

    public LoanResponse() {
    }

    public List<LoanDTO> getLoans() {
        return loansDTO;
    }

    public void setLoans(List<LoanDTO> loans) {
        this.loansDTO = loans;
    }
}
