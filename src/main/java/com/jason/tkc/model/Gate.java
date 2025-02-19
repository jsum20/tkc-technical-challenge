package com.jason.tkc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "gate")
public class Gate {
    @Id
    @Column(name = "id", length = 3)
    private String id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "connections", columnDefinition = "json")
    @Convert(converter = ConnectionConverter.class)
    private List<Connection> connections;
}
