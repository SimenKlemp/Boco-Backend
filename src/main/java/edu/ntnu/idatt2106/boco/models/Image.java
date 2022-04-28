package edu.ntnu.idatt2106.boco.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "image", uniqueConstraints = { @UniqueConstraint(columnNames = "imageId") })
public class Image
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageId")
    private Long imageId;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "content")
    @Lob
    private byte[] content;

    public Image(String name, byte[] content)
    {
        this.name = name;
        this.content = content;
    }
}
