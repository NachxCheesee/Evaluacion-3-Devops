package cl.marfuego.ms_locales.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locales")
@Schema(name = "Local", description = "Entidad que representa un establecimiento o sucursal de la cadena MarFuego")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental (no se coloca de manera manual) del local en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre comercial o de fantasía de la sucursal", example = "MarFuego Costanera")
    private String nombre;

    @Schema(description = "Dirección física exacta donde se ubica el local", example = "Calle Falsa 123")
    private String direccion;

    @Schema(description = "Ciudad de ubicación del establecimiento", example = "Puerto Montt")
    private String ciudad;

    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Schema(description = "Lista de mesas que pertenecen y están físicamente distribuidas en este local (No se ingresa de manera manual se crea al momento de llamar a la entidad)")
    private List<Mesa> mesas = new ArrayList<>();

    public Local() {}

    public Local(Long id, String nombre, String direccion, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }
}
