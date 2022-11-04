package ru.popov.loanrestapi.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CountryDTO {

    @NotEmpty(message = "Name should be not empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 30 characters")
    private String name;

    public CountryDTO() {
    }

    public CountryDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
