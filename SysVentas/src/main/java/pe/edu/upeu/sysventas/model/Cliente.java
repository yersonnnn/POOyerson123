package pe.edu.upeu.sysventas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysventas.enums.TipoDocumento;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_cliente")
public class Cliente {
    @Id
    @NotNull(message = "El DNI/RUC no puede estar vacío")
    @Size(min = 8, max = 12, message = "El DNI/RUC debe tener entre 8 y 12 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El DNI/RUC debe contener solo números")
    @Column(name = "dniruc", nullable = false, length = 12)
    private String dniruc;

    @NotNull(message = "Los nombres no pueden estar vacíos")
    @Size(min = 3, max = 160, message = "Los nombres deben tener entre 3 y 160 caracteres")
    @Column(name = "nombres", nullable = false, length = 160)
    private String nombres;

    @Column(name = "rep_legal", length = 160)
    private String repLegal;

    @NotNull(message = "El tipo de documento no puede estar vacío")
    @Column(name = "tipo_documento", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
}