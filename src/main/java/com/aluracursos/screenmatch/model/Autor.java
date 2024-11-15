package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String nombreAutor;
    private int fechaDeNacimiento;
    private int fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosLibro datosLibro){
        this.nombreAutor = datosLibro.libros().get(0).autor().get(0).nombreAutor();
        this.fechaDeNacimiento = datosLibro.libros().get(0).autor().get(0).nacimiento();
        this.fechaDeFallecimiento = datosLibro.libros().get(0).autor().get(0).deceso();
    }

    @Override
    public String toString() {
        return  '\n' + "Autor: " + nombreAutor + '\n' +
                "Fecha de nacimiento: " + fechaDeNacimiento + '\n' +
                "Fecha de fallecimiento: " + fechaDeFallecimiento + '\n'+
                "Libros: ";

    }

    public void setId(Long id) {
        Id = id;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public void setFechaDeNacimiento(int fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public void setFechaDeFallecimiento(int fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Long getId() {
        return Id;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public int getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public int getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }
}
