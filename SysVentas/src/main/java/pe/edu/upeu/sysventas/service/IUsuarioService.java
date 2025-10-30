package pe.edu.upeu.sysventas.service;

import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.model.VentCarrito;

import java.util.List;

public interface IUsuarioService extends ICrudGenericoService<Usuario,Long>{
    Usuario loginUsuario(String user, String clave);



}
