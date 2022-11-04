package ru.popov.loanrestapi.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonDTO {

    @NotEmpty(message = "Name should be not empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 30 characters")
    private String name;

    @NotEmpty(message = "Surname should be not empty")
    @Size(min = 2, max = 50, message = "Surname should be between 3 and 30 characters")
    private String surname;

    public PersonDTO() {
    }

    public PersonDTO(String name, String surname) {
        this();
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
