package es.adif.tlg.negocio.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IGestorNivelTanque {
NivelTanqueBean ultimoNivelTanque(Integer id); List<NivelTanqueBean> consultarUltimoNivelTanques(Date d, List<TanqueBean> t); List<NivelTanqueBean> consultarPrimerNivelTanques(Date d, List<TanqueBean> t);
}
