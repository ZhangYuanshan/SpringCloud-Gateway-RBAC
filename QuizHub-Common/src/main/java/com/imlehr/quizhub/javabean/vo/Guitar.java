package com.imlehr.quizhub.javabean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lehr
 * @create: 2020-03-22
 */
@Data
public class Guitar implements Serializable {

    private String name;
    private String brand;

    public Guitar(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }

    public Guitar()
    {

    }

}
