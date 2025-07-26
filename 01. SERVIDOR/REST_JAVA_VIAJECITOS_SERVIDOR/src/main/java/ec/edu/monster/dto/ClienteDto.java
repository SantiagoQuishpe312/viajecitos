package ec.edu.monster.dto;

import ec.edu.monster.modelo.Cliente;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ClienteDto {
    private Integer clienteId;
    private Integer clienteReferenteId;
    private String clienteNombre;
    private String clienteEmail;
    private String clienteTelefono;
    //private String clienteContrasena;
    private String clienteUsuario;
    private String clienteRol;
    private String clienteCedula;
    private String clienteDireccion;
    private Genero clienteGenero;
    private LocalDate clienteFechaNacimiento;

    // No incluyo la contraseña ni las listas de facturas, boletos ni tarjetas para no exponer info sensible ni generar ciclos.
    public enum Genero {
        M, F
    }
}

