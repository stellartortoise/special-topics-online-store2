package com.nscc.onlinestore2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GameDTO {
    @NotNull
    @NotBlank(message = "Name is Required")
    private String Name;

    @NotNull
    @NotBlank(message = "Description is Required")
    private String Description;

    @NotNull
    @NotBlank(message = "Category is Required")
    private String Category;

    @NotNull
    private double Price;

    @NotNull
    @NotBlank(message = "Image is Required")
    private String Image;

    @NotNull
    @NotBlank(message = "Create Date is Required")
    private String Developer;

    @NotNull
    @NotBlank(message = "Create Date is Required")
    private String Platform;

    @NotNull
    @NotBlank(message = "Create Date is Required")
    private String EsrbRating;

    //@NotNull
    //@NotBlank(message = "Create Date is Required")
    private String CreateDate;
}
