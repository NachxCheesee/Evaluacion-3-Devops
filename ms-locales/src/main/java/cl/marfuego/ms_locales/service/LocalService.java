package cl.marfuego.ms_locales.service;


import cl.marfuego.ms_locales.dto.LocalDto;
import cl.marfuego.ms_locales.dto.MesaDto;
import cl.marfuego.ms_locales.enums.EstadoMesa;
import cl.marfuego.ms_locales.exception.custom.ErrorNoEncontrado;
import cl.marfuego.ms_locales.model.Local;
import cl.marfuego.ms_locales.model.Mesa;
import cl.marfuego.ms_locales.repository.LocalRepository;
import cl.marfuego.ms_locales.repository.MesaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class LocalService {

    private static final Logger log = LoggerFactory.getLogger(LocalService.class);

    private final LocalRepository localRepository;

    private final MesaRepository mesaRepository;

    public LocalService(LocalRepository localRepository, MesaRepository mesaRepository) {
        this.localRepository = localRepository;
        this.mesaRepository = mesaRepository;
    }

    public List<Local> listarLocales() {
        log.info("[Service] Solicitando todos los locales a la base de datos.");
        return localRepository.findAll();

    }

    public Local buscarLocalPorId(Long id) {
        log.info("[Service] Buscando local por ID: {}", id);
        return localRepository.findById(id).orElseThrow(() -> {
            log.warn("[Service] ⚠️ Fallo: No se encontró el local con ID: {}", id);
            return new ErrorNoEncontrado("No se encontró el local con ID: " + id);
        });

    }

    public Local guardarLocal(LocalDto dto) {
        log.info("[Service] Procesando guardado de un nuevo local: {}", dto.getNombre());
        // Convertimos el dto en una Entidad Local
        Local local = new Local();
        local.setNombre(dto.getNombre());
        local.setDireccion(dto.getDireccion());
        local.setCiudad(dto.getCiudad());

        return localRepository.save(local);

    }

    public void eliminarLocal(Long id) {
        log.info("[Service] Intentando eliminar local con ID: {}", id);
        if (!localRepository.existsById(id)) {
            log.warn("[Service] ⚠️ Fallo al eliminar: El local {} no existe en los registros.", id);
            throw new ErrorNoEncontrado("No se puede eliminar: El local " + id + " no existe.");
        }
        localRepository.deleteById(id);


    }


    // Funciones para mesa

    public List<Mesa> listarMesas() {
        log.info("[Service] Solicitando todas las mesas a la base de datos.");
        return mesaRepository.findAll();

    }

    public Mesa buscarMesaPorId(Long id) {
        log.info("[Service] Buscando mesa por ID: {}", id);
        return mesaRepository.findById(id).orElseThrow(() -> {
            log.warn("[Service] ⚠️ Fallo: No se encontró la mesa con ID: {}", id);
            return new ErrorNoEncontrado("No se encontró la mesa con ID: " + id);
        });

    }

    public Mesa guardarMesa(MesaDto dto) {
        log.info("[Service] Iniciando flujo para crear mesa N°: {} para el local ID: {}", dto.getNumeroMesa(), dto.getLocalId());
        // 1. Buscamos el local al que pertenece la mesa (VITAL)
        Local local = localRepository.findById(dto.getLocalId()).orElseThrow(() -> {
            log.warn("[Service] ⚠️ Fallo al crear mesa: El local asociado ID {} no existe.", dto.getLocalId());
            return new ErrorNoEncontrado("No se pudo crear la mesa: Local " + dto.getLocalId() + " no existe."); // <-- Aquí le borramos el ) extra
        });

        // 2. Creamos la mesa y le "enchufamos" el local completo
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(dto.getNumeroMesa());
        mesa.setCapacidad(dto.getCapacidad());
        mesa.setLocal(local); // Aquí se crea la relación en la BD

        // El estado se asigna automáticamente como LIBRE en la entidad Mesa
        if (dto.getEstado() != null) {
            mesa.setEstado(dto.getEstado());
        }

        return mesaRepository.save(mesa);

    }


    public void eliminarMesa(Long id) {
        log.info("[Service] Intentando eliminar mesa con ID: {}", id);
        if (!mesaRepository.existsById(id)) {
            log.warn("[Service] ⚠️ Fallo al eliminar: La mesa {} no existe en los registros.", id);
            throw new ErrorNoEncontrado("No se puede eliminar: La mesa " + id + " no existe.");
        }
        mesaRepository.deleteById(id);
    }


    public List<Mesa> obtenerMesaEstadoPorLocal(Long localId, EstadoMesa estado) {
        log.info("[Service] Filtrando mesas en BD para el local ID: {} con estado: {}", localId, estado);
        return mesaRepository.findByLocalIdAndEstado(localId, estado);
    }


    public Mesa cambiarEstadoMesa(Long id, EstadoMesa nuevoEstado) {
        log.info("[Service] Intentando cambiar estado de mesa ID: {} a -> {}", id, nuevoEstado);
        Mesa mesa = mesaRepository.findById(id).orElseThrow(() -> {
            log.warn("[Service] ⚠️ Fallo al cambiar estado: La mesa {} no existe.", id);
            return new ErrorNoEncontrado("No se puede cambiar el estado: Mesa " + id + " no existe.");
        });
        mesa.setEstado(nuevoEstado);
        return mesaRepository.save(mesa);
    }


}
