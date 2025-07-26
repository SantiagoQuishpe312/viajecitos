package ec.edu.monster.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CLIENTE")
@Data // Incluye @Getter, @Setter, @ToString, @EqualsAndHashCode

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENTE_ID")
    private Integer clienteId;

    @Column(name = "CLIENTE_NOMBRE")
    private String clienteNombre;

    @Column(name = "CLIENTE_EMAIL")
    private String clienteEmail;

    @Column(name = "CLIENTE_TELEFONO")
    private String clienteTelefono;

    @Column(name = "CLIENTE_CONTRASENA")
    private String clienteContrasena;

    @Column(name = "CLIENTE_USUARIO")
    private String clienteUsuario;

    @Column(name = "CLIENTE_ROL")
    private String clienteRol;

    @Column(name = "CLIENTE_CEDULA")
    private String clienteCedula;

    @Column(name = "CLIENTE_DIRECCION")
    private String clienteDireccion;
    @Column(name = "CLIENTE_GENERO")
    @Enumerated(EnumType.STRING)
    private Genero clienteGenero;

    @Column(name = "CLIENTE_FECHA_NACIMIENTO")
    private LocalDate clienteFechaNacimiento;

    public enum Genero {
        M, F
    }

    @ManyToOne
    @JoinColumn(name = "CLIENTE_REFERENTE_ID")
    private Cliente clienteReferente;

    @OneToMany(mappedBy = "clienteReferente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cliente> clientesReferidos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asiento> asientos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Factura> facturas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Boleto> boletos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClienteTarjeta> clienteTarjetas;
}
