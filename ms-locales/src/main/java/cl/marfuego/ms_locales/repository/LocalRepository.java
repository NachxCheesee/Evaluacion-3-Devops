package cl.marfuego.ms_locales.repository;

import cl.marfuego.ms_locales.model.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository <Local, Long> {
}
