package ru.popov.loanrestapi.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "amount")
    @NotNull
    private Double amount;

    @Column(name = "term")
    private LocalDate expiredDate;

    @Column(name = "approved")
    private boolean approved;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    public Loan() {
    }

    public Loan(Double amount, LocalDate expiredDate, boolean approved, Person person, Country country) {
        this.amount = amount;
        this.expiredDate = expiredDate;
        this.approved = approved;
        this.person = person;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return approved == loan.approved && Objects.equals(amount, loan.amount) && Objects.equals(person, loan.person) && Objects.equals(country, loan.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, expiredDate, approved, person, country);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", expiredDate=" + expiredDate +
                ", approved=" + approved +
                ", person=" + person +
                ", country=" + country +
                '}';
    }
}
