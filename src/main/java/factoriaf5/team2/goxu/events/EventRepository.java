package factoriaf5.team2.goxu.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Acceso a la tabla events
 * Además de los métodos heredados de JpaRepository (save, findAll...),
 * declara dos query methods que Spring traduce a consultas a partir de su nombre
 */
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    /* Todos los eventos, del más antiguo al más reciente. Para el calendario anual */
    List<EventEntity> findAllByOrderByEventDateAsc();

    /* Solo los eventos destacados, ordenados por fecha. Para "Próximos eventos" de la Home */
    List<EventEntity> findByFeaturedTrueOrderByEventDateAsc();
}