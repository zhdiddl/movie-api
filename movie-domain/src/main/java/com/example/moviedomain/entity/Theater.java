package com.example.moviedomain.entity;

import com.example.moviecommon.exception.CustomException;
import com.example.moviecommon.exception.ErrorCode;
import com.example.moviedomain.base.AuditingFields;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@Entity
public class Theater extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<Screening> screenings = new ArrayList<>();

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();


    protected Theater() {}

    private Theater(String name) {
        this.name = name;
    }

    public static Theater of(String name) {
        validate(name);
        return new Theater(name);
    }

    private static void validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_THEATER_NAME);
        }
    }

}
