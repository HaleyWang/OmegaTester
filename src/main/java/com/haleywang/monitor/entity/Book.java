package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;


/**
 * @author haley
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "JDBC")
    private Long id;

    private String name;

    @Override
    public String toString() {
        return String.format("Book [id=%s, name=%s]", id, name);
    }

}