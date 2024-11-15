package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.AutorRepository;
import com.aluracursos.screenmatch.repository.LibroRepository;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final String API_KEY = "&apikey=a513f626";
    private ConvierteDatos conversor = new ConvierteDatos();


    private AutorRepository autorRepository;


    private List<Libro> libro;
    private List<Autor> autor;
    private Optional<Serie> serieBuscada;

    public Principal(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un año determinado
                    5 - Listar libros por idioma
                                 
                    0 - Salir
                    """;
            System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibrosWeb();
                        break;
                    case 2:
                        mostrarLibrosBuscados();
                        break;
                    case 3:
                        mostrarAutoresRegistrados();
                        break;
                    case 4:
                        mostrarAutoresVivos();
                        break;
                    case 5:
                        mostrarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }

        }

    }

    private void buscarLibrosWeb() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        try {
            var nombreLibro = teclado.nextLine();
            var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
            System.out.println(json);


            DatosLibro datosLibro = conversor.obtenerDatos(json, DatosLibro.class);
            System.out.println(datosLibro);



            String nombreAutor = datosLibro.libros().get(0).autor().get(0).nombreAutor();
            Autor autor = autorRepository.findByNombreAutorIgnoreCase(nombreAutor).orElseGet(() -> {
                // Si el autor no existe, crearlo y guardarlo en la base de dat
                Autor nuevoAutor = new Autor(datosLibro);
                return nuevoAutor;
            });

            System.out.println(autor);
            Libro libro = new Libro(datosLibro);

            try {
                autorRepository.guardarLibroYAutor(libro, autor);
            } catch (DataIntegrityViolationException e) {
                // Loguear el error o notificar que el libro ya existe
                System.out.println("El libro con el título '" + libro.getTitulo() + "' ya está registrado. Continuando...");
            } catch (Exception e) {
                // Manejar cualquier otro tipo de excepción
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }
        } catch (IndexOutOfBoundsException | NullPointerException e){
            System.out.println("No se encontraron libros. Error: "+e.getMessage());
        } catch (Exception ignored) {}

        //repositorio.save(libro);

    }

    private void mostrarLibrosBuscados() {
        libro = autorRepository.listarTodosLosLibros();

        libro.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }
    private void mostrarAutoresRegistrados() {
        autor = autorRepository.findAll();

        autor.stream()
                .sorted(Comparator.comparing(Autor::getNombreAutor))
                .forEach(a -> {System.out.println(a);
                    a.getLibros().forEach(libro -> System.out.println("  - " + libro.getTitulo()));});
        //List<Libro> librosPorAutor = autorRepository.encontrarLibrosPorAutorId(2L);
        //librosPorAutor.forEach(System.out::println);
    }
    private void mostrarAutoresVivos(){
        System.out.print("Introduce el año para buscar autores vivos: ");
        int anio = teclado.nextInt();

        List<Autor> autoresVivos = autorRepository.encontrarAutoresVivosEnAnio(anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores vivos en ese año.");
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(a -> {System.out.println(a);
                a.getLibros().forEach(libro -> System.out.println("  - " + libro.getTitulo()));});
        }
    }
    private void mostrarLibrosPorIdioma(){
        var menu = """
                    Introduce el idioma para buscar los libros:
                    es- español
                    en- Ingles
                    fr- Frances
                    pt- Portugues
                    """;
        System.out.print(menu);
        String idioma = teclado.nextLine();

        List<Libro> librosPorIdioma = autorRepository.encontrarLibrosPorIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros en ese idioma.");
        } else {
            System.out.println("Libros en " + idioma + ":");
            librosPorIdioma.forEach(System.out::println);
        }
    }

    /*
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }



    }*/

    /*
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }



    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if(serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }

    }
    private void buscarTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s ->
                System.out.println("Serie: " + s.getTitulo() + " Evaluacion: " + s.getEvaluacion()) );
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Escriba el genero/categoría de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoría " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }
    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Com evaluación apartir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemparadaYEvaluacion(totalTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void  buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

    }

    private void buscarTop5Episodios(){
        buscarSeriesPorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluación %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));

        }
    }*/
}

