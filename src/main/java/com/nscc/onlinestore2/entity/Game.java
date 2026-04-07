package com.nscc.onlinestore2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 750)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String developer;

    @Column(nullable = false)
    private String platform;

    @Column(nullable = false)
    private String esrbRating;

    //@Column(nullable = false)
    private String createDate;

}
