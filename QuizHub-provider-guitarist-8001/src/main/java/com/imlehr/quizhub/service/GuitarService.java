package com.imlehr.quizhub.service;

import com.imlehr.quizhub.javabean.vo.Guitar;
import org.springframework.stereotype.Service;

/**
 * @author Lehr
 * @create: 2020-03-22
 */
@Service
public class GuitarService {

    public Guitar buyGuitar(Integer money)
    {
        if(money>5000)
        {
            return new Guitar("Strat","Fender");
        }
            return new Guitar("Les Paul","Epiphone");
    }

}
