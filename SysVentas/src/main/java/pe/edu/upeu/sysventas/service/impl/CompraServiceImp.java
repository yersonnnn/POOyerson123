package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.model.Compra;
import pe.edu.upeu.sysventas.repository.CompraRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.ICompraService;

@RequiredArgsConstructor
@Service
public class CompraServiceImp extends CrudGenericoServiceImp<Compra,Long> implements ICompraService {

    private final CompraRepository compraRepository;

    @Override
    protected ICrudGenericoRepository<Compra, Long> getRepo() {
        return compraRepository;
    }
}
