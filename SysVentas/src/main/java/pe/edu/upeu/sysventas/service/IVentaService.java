package pe.edu.upeu.sysventas.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import pe.edu.upeu.sysventas.model.Venta;

import java.io.File;
import java.sql.SQLException;

public interface IVentaService extends ICrudGenericoService<Venta,Long>{

    File getFile(String filex);
    JasperPrint runReport(Long idv) throws JRException, SQLException;

}
