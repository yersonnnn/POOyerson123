package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.model.CompCarrito;
import pe.edu.upeu.sysventas.repository.CompCarritoRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.ICompCarritoService;

@RequiredArgsConstructor
@Service
public class CompCarritoServiceImp extends CrudGenericoServiceImp<CompCarrito,Long> implements ICompCarritoService {

    private final CompCarritoRepository compCarritoRepository;

    @Override
    protected ICrudGenericoRepository<CompCarrito, Long> getRepo() {
        return compCarritoRepository;
    }
}
