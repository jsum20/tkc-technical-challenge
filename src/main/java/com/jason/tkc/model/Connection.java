package com.jason.tkc.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class Connection implements Serializable {
    private String id;
    private int hu;
}
