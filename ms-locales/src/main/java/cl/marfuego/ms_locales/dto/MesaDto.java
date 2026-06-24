package cl.marfuego.ms_locales.dto;

import cl.marfuego.ms_locales.enums.EstadoMesa;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "MesaDto", description = "Estructura de datos requerida para registrar o actualizar una mesa física en una sucursal")
public class MesaDto {

    @NotNull(message = "El número de mesa es obligatorio")
    @Positive(message = "El número de mesa debe ser mayor a 0")
    @Schema(description = "Número identificador físico de la mesa dentro del local", example = "12")
    private Integer numeroMesa;

    @NotNull(message = "La capacidad de mesa es obligatoria")
    @Min(value = 1, message = "La capacidad mínima es de 1 persona")
    @Max(value = 20, message = "Por seguridad, el máximo son 20 personas por mesa")
    @Schema(description = "Cantidad máxima de clientes permitidos en esta mesa", example = "6")
    private Integer capacidad;

    @Schema(description = "Estado inicial de la mesa", example = "LIBRE", allowableValues = {"LIBRE", "OCUPADA", "RESERVADA"})
    private EstadoMesa estado;

    @NotNull(message = "El id del local al que pertenece es obligatorio")
    @Schema(description = "ID numérico del local de MarFuego donde se instalará físicamente la mesa", example = "1")
    private Long localId;

    public MesaDto() {}

    public MesaDto(Integer numeroMesa, Integer capacidad, EstadoMesa estado, Long localId) {
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.estado = estado;
        this.localId = localId;
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    public void setEstado(EstadoMesa estado) {
        this.estado = estado;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }
}
