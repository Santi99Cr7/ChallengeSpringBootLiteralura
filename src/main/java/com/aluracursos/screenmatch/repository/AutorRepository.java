package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Autor;
import com.aluracursos.screenmatch.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public interface AutorRepository extends JpaRepository<Autor,Long> {
    Optional<Autor> findByNombreAutorIgnoreCase(String nombreAutor);

    default void guardarLibroYAutor(Libro libro, Autor autor) {
        if (autor.getLibros() == null) {
            autor.setLibros(new ArrayList<>());  // Inicializa la lista si es null
        }
        autor.getLibros().add(libro); // Añade el libro al autor
        libro.setAutor(autor);       // Establece la relación bidireccional
        save(autor);                 // Guarda al autor, lo que automáticamente guarda el libro
    }
    @Query("SELECT l FROM Libro l")
    List<Libro> listarTodosLosLibros();
    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> encontrarLibrosPorIdioma(@Param("idioma") String idioma);

    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    List<Libro> encontrarLibrosPortitulo(@Param("titulo") String titulo);
   // @Query("SELECT l FROM Libro l WHERE l.id =:idAutor")
    //List<Libro> encontrarLibrosPorAutor(@Param("idAutor") Long idAutor);

    @Query("SELECT a.libros FROM Autor a WHERE a.Id = :idAutor")
    List<Libro> encontrarLibrosPorAutorId(@Param("idAutor") Long idAutor);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeFallecimiento >= :anio OR a.fechaDeFallecimiento = 0)")
    List<Autor> encontrarAutoresVivosEnAnio(@Param("anio") int anio);



}
