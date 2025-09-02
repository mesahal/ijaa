package com.ijaa.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 3)
    private String iso3;

    @Column(name = "numeric_code", length = 3)
    private String numericCode;

    @Column(length = 2)
    private String iso2;

    @Column(length = 255)
    private String phonecode;

    @Column(length = 255)
    private String capital;

    @Column(length = 255)
    private String currency;

    @Column(name = "currency_name", length = 255)
    private String currencyName;

    @Column(length = 255)
    private String currencySymbol;

    @Column(length = 255)
    private String tld;

    @Column(name = "native", length = 255)
    private String nativeName;

    @Column(length = 255)
    private String region;

    @Column(name = "region_id")
    private Long regionId;

    @Column(length = 255)
    private String subregion;

    @Column(name = "subregion_id")
    private Long subregionId;

    @Column(length = 255)
    private String nationality;

    @Column(columnDefinition = "TEXT")
    private String timezones;

    @Column(columnDefinition = "TEXT")
    private String translations;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(length = 191)
    private String emoji;

    @Column(name = "\"emojiU\"", length = 191)
    private String emojiU;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "SMALLINT DEFAULT 1")
    private Integer flag = 1;

    @Column(name = "\"wikiDataId\"", length = 255)
    private String wikiDataId;

    // Relationship with cities
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<City> cities;
}
