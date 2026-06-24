package cl.marfuego.ms_locales.model;

import cl.marfuego.ms_locales.enums.EstadoMesa;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "mesas")
@Schema(name = "Mesa", description = "Entidad que representa una mesa física ubicada dentro de un local específico de MarFuego")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental (no se coloca de manera manual) de la mesa en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Número identificador físico de la mesa dentro del local", example = "5")
    private int numeroMesa;

    @Schema(description = "Cantidad máxima de comensales que pueden sentarse en la mesa", example = "4")
    private int capacidad;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual de disponibilidad de la mesa", example = "LIBRE", allowableValues = {"LIBRE", "OCUPADA", "RESERVADA"})
    private EstadoMesa estado = EstadoMesa.LIBRE;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    @Schema(description = "Local o sucursal al que pertenece físicamente esta mesa")
    private Local local;


    public Mesa() {}

    public Mesa(Long id, int numeroMesa, int capacidad, EstadoMesa estado, Local local) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.estado = estado;
        this.local = local;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    public void setEstado(EstadoMesa estado) {
        this.estado = estado;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}
