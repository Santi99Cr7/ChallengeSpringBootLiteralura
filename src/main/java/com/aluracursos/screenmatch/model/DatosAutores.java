package com.aluracursos.screenmatch.model;
import com.fasterxml.jackson.annotation.JsonAlias;

public record DatosAutores(
        @JsonAlias("name") String nombreAutor,
        @JsonAlias("birth_year") Integer nacimiento,
        @JsonAlias("death_year") Integer deceso) {
}
