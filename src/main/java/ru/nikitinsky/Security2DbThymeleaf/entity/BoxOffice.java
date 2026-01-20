package ru.nikitinsky.Security2DbThymeleaf.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "box_office")
public class BoxOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    private Double revenue;

    @Column(nullable = false)
    private String currency;

    @Column(name = "year_value", nullable = false)
    private Integer year;

    @Transient
    public String getRevenueFormatted() {
        if (revenue == null) {
            return "";
        }
        long value = Math.round(revenue);
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(value).replace(",", " ");
    }
}
