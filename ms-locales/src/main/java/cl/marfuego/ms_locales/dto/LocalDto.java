package cl.marfuego.ms_locales.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "LocalDto", description = "Estructura de datos requerida para crear o actualizar un local en el sistema MarFuego")
public class LocalDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100,  message = "El nombre debe tener entre 3 y 100 letras")
    @Schema(description = "Nombre de fantasía de la sucursal", example = "MarFuego Pelluco")
    private String nombre;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres")
    @Schema(description = "Dirección urbana exacta del local", example = "Av. Juan Soler Manfredini 1500")
    private String direccion;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(max = 100, message = "La ciudad no puede superar los 100 caracteres")
    @Schema(description = "Ciudad base donde se ubica el restaurante", example = "Puerto Montt")
    private String ciudad;

    LocalDto() {
    }

    public LocalDto(String nombre, String direccion, String ciudad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

}