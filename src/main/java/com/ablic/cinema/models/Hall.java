package com.ablic.cinema.models;

import com.ablic.cinema.dtos.Row;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "halls")
@NoArgsConstructor
@Data
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    @Column(length = 1024)
    private String layout;

    @JsonIgnore
    @Transient
    private final ObjectMapper mapper = new ObjectMapper();
    @JsonIgnore
    @Transient
    private List<Row> rows;

    public List<Row> getRows() {
        try {
            return mapper.readValue(layout, new TypeReference<List<Row>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRows(List<Row> rows) {
        try {
            layout = mapper.writeValueAsString(rows);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
