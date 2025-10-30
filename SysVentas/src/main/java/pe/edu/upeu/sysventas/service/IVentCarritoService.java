package pe.edu.upeu.sysventas.service;


import pe.edu.upeu.sysventas.model.VentCarrito;

import java.util.List;

public interface IVentCarritoService extends  ICrudGenericoService<VentCarrito,Long>{
    List<VentCarrito> listaCarritoCliente(String dni);
    void deleteCarAll(String dniruc);
}
