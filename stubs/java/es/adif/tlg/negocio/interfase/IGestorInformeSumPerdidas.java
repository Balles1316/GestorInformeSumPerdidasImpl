package es.adif.tlg.negocio.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IGestorInformeSumPerdidas {
void crearInformeSumPerdidos() throws Exception; String construirBloqueResumenPerdidas(Set<Integer> ids);
}
