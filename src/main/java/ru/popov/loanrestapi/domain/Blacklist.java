package ru.popov.loanrestapi.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "blacklist")
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public Blacklist() {
    }

    public Blacklist(Person person) {
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blacklist blacklist = (Blacklist) o;
        return id == blacklist.id && Objects.equals(person, blacklist.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person);
    }
}
