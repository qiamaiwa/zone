package com.example.zone.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;


@Primary
@Repository
public class AlphaDaoMybatisImpl implements AlphaDao {

    public String select() {
        return "MyBatis";
    }

}

