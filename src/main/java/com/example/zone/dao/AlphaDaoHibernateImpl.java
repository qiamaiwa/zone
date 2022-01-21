package com.example.zone.dao;

import org.springframework.stereotype.Repository;

@Repository("Hibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {

    public String select(){
        return "Hibernate";
    };
}


