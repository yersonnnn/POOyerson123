package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.model.Emisor;
import pe.edu.upeu.sysventas.repository.EmisorRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.IEmisorService;
@RequiredArgsConstructor
@Service
public class EmisorServiceImp extends CrudGenericoServiceImp<Emisor, Long> implements IEmisorService {
    private final EmisorRepository emisorRepository;

    @Override
    protected ICrudGenericoRepository<Emisor, Long> getRepo() {
        return null;
    }
}
