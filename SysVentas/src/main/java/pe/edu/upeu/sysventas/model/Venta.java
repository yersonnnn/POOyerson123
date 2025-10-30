/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.upeu.sysventas.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;
    @Column(name = "preciobase", nullable = false)
    private Double precioBase;
    @Column(name = "igv", nullable = false)
    private Double igv;
    @Column(name = "preciototal", nullable = false)
    private Double precioTotal;
    @ManyToOne
    @JoinColumn(name = "dniruc", referencedColumnName = "dniruc",
            nullable = false, foreignKey = @ForeignKey(name
                    = "FK_CLIENTE_VENTA"))
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario",
            nullable = false, foreignKey = @ForeignKey(name
                    = "FK_USUARIO_VENTA"))
    private Usuario usuario;
    @Column(name = "num_doc", nullable = false, length = 20)
    private String numDoc;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_gener", nullable = false)
    private LocalDateTime fechaGener;
    @Column(name = "serie", nullable = false, length = 20)
    private String serie;
    @Column(name = "tipo_doc", nullable = false, length = 10)
    private String tipoDoc;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval
            = true)
    private List<VentaDetalle> ventaDetalles;
}
