package com.springboot.estore.Estore.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  CategoryDto {


    private String categoryId;


    @NotBlank(message = "Title cant be blank.")
    @Size(min = 4,message = "Title must be of atleast 4 chars.")
    private String title;


    @NotBlank(message = "A description is required.")
    private String categoryDesc;

    //@NotBlank
    private String coverImage;
}
