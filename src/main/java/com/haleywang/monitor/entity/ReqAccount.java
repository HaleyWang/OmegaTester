package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;


/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReqAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator= "JDBC")
    private Long accountId;

    @Column(nullable = false)
    private String name;
    
    @Column(unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String akey;
    
    private String token;
    
	private String type;

}
