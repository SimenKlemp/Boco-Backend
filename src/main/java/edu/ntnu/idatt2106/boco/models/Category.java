package edu.ntnu.idatt2106.boco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "categoryId")
        })
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    Long categoryId;

    @NotBlank
    @Column(name = "category")
    String category;

    @ManyToOne
    @JoinColumn(name="itemId")
    private Item item;
}


