package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private int numeroDeDescargas;
    private String idioma;
    @ManyToOne
    private Autor autor;

    public Libro(){}
    public Libro(DatosLibro datosLibro){
        DatosLibroIndividual primerLibro = datosLibro.libros().get(0);
        this.titulo = primerLibro.titulo();
        this.numeroDeDescargas = primerLibro.numeroDeDescargas();
        this.idioma= primerLibro.idiomas().get(0);
    }
    @Override
    public String toString() {
        return  '\n' + "----- LIBRO -----" +'\n' +
                "Titulo: " + titulo + '\n' +
                "Autor: " + autor.getNombreAutor() + '\n' +
                "Numero de descargas: " + numeroDeDescargas + '\n' +
                "Idioma: " + idioma + '\n';

    }




    public Long getId() {
        return Id;
    }

    public String getTitulo() {
        return titulo;
    }




    public int getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setId(Long id) {
        Id = id;
    }



    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public void setNumeroDeDescargas(int numedoDeDescargas) {
        this.numeroDeDescargas = numedoDeDescargas;
    }



    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
// Getters y setters
}