package es.adif.tlg.dao.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IDescargaTanqueDAO {
List<DescargaTanqueBean> obtenerDescargaTanqueDetallePorIdDescarga(Integer id);
}
