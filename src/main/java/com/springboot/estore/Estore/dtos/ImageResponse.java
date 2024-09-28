package com.springboot.estore.Estore.dtos;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

    private String message;

    private boolean success;

    private HttpStatus status;

    private String imageName;
}
