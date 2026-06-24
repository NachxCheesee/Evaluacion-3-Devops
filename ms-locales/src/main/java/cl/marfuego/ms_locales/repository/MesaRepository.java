package cl.marfuego.ms_locales.repository;

import cl.marfuego.ms_locales.enums.EstadoMesa;
import cl.marfuego.ms_locales.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {

    List<Mesa> findByLocalIdAndEstado(Long localId, EstadoMesa estado);


}
