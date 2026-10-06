package es.adif.tlg.negocio.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.text.NumberFormat;

import es.adif.tlg.bean.*;
import es.adif.tlg.dao.interfase.IErrorNumeracionSuministroDAO;
import es.adif.tlg.dao.interfase.IExistenciasTeoricasDAO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.adif.tlg.bean.wrappers.FiltroNumerarWrapperBean;
import es.adif.tlg.dao.interfase.IDescargaTanqueDAO;
import es.adif.tlg.dao.interfase.ISuministroDAO;
import es.adif.tlg.dao.interfase.ITanqueDAO;
import es.adif.tlg.negocio.interfase.IGestorDescarga;
import es.adif.tlg.negocio.interfase.IGestorDescargaNodoConsola;
import es.adif.tlg.negocio.interfase.IGestorInformeSumPerdidas;
import es.adif.tlg.negocio.interfase.IGestorNivelTanque;
import es.adif.tlg.negocio.interfase.IGestorPersonal;
import es.adif.tlg.negocio.interfase.IGestorPuntoSuministro;
import es.adif.tlg.negocio.interfase.IGestorSuministro;
import es.adif.tlg.negocio.interfase.IGestorSuministroNodoCabezal;
import es.adif.tlg.negocio.interfase.IGestorSurtidor;
import es.adif.tlg.utils.GestorCorreo;

@Component
public class GestorInformeSumPerdidasImpl implements IGestorInformeSumPerdidas {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GestorInformeSumPerdidasImpl.class);
	
	protected static final String ERROR = "ERROR, no coincide con el total final";
	protected static final String OK = "OK";
	
	private static final String GUION = "-";
	private static final String PUNTOS_SEGUIDOS = " : ";
	private static final String OPEN_HTML_BODY ="<html><body>";
	private static final String	CLOSE_HTML_BODY = "</body></html>";
	private static final String OPEN_LI = "<li>";
	private static final String OPEN_LI_STYLE =	"<li style=\"list-style-type: none;\">";
	private static final String	CLOSE_LI = "</li>";
	private static final String OPEN_UL = "<ul>";
	private static final String	CLOSE_UL = "</ul>";
	private static final String OPEN_TH_STYLE = "<th style=\"border: 1px solid black ;background-color: #f2f2f2;\">";
	private static final String OPEN_TH_STYLEYellow = "<th style=\"border: 1px solid black ;background-color: #FFEE8C;\">";
	private static final String OPEN_TH_STYLEOrange = "<th style=\"border: 1px solid black ;background-color: #ffe5b4;\">";
	private static final String CLOSE_TH = "</th>";
	private static final String OPEN_TR = "<tr>";			
	private static final String CLOSE_TR = "</tr>";
	private static final String OPEN_TD = "<td style=\"border: 1px solid black ;\">";
	private static final String OPEN_TD_STYLE = "<td style=\"border: 1px solid black ; text-align: right;\">";
	private static final String CLOSE_TD = "</td>";
	private static final String ETIQUETA_BR ="<br/>";
	private static final String OPEN_TABLE = "<table style=\"border: 1px solid black;\">";
	private static final String CLOSE_TABLE = "</table>";
	private static final String COLOR = "_color";
	private static final String COLOR_Red     = "#f8d7da";
	@Autowired
	private IGestorPersonal personalGestor;
	
	@Autowired
	private GestorCorreo gestorCorreo;
	
    @Autowired
    private ISuministroDAO suministroDAO;
    
    @Autowired
	private IGestorPuntoSuministro iGestorPuntoSuministro;

    @Autowired
	private IGestorSuministro iGestorSuministro;
    
    @Autowired
	private IGestorDescarga iGestorDescarga;

	@Autowired
	private IExistenciasTeoricasDAO iExistenciasTeoricasDAO;
    
    @Autowired
	private IDescargaTanqueDAO iDescargaTanqueDAO;

	@Autowired
	private ISuministroDAO iSuministroDAO;
    
	@Autowired
	private IGestorSurtidor iGestorSurtidor;
	
	@Autowired
	private ITanqueDAO iTanqueDAO;
	
	@Autowired
	private IGestorSuministroNodoCabezal iGestorSuministroNodoCabezal;
	
	@Autowired
	private IGestorDescargaNodoConsola iGestorDescargaNodoConsola;
	
	@Autowired
	private IGestorNivelTanque iGestorNivelTanque;

	@Autowired
	private IErrorNumeracionSuministroDAO iErrorNumeracionSuministroDAO;

	/* ======================================================================= */
	/* CREATE 	                                                               */
	/* ======================================================================= */

	/* ======================================================================= */
	/* READ 	                                                               */
	/* ======================================================================= */

	/* ======================================================================= */
	/* UPDATE 	                                                               */
	/* ======================================================================= */

	/* ======================================================================= */
	/* DELETE 	                                                               */
	/* ======================================================================= */

	/* ======================================================================= */
	/* LOGICA 	                                                               */
	/* ======================================================================= */
	

	/**
	* Genera y envía el informe diario de comprobaciones y posibles pérdidas.
	*/
	@Override
	public void crearInformeSumPerdidos() throws Exception {
		LOGGER.info("[crearInformeSumPerdidos] INICIO ");

		List<SuministroNodoCabezalBean> listaNodosConSaltos = new ArrayList<>();
		List<SuministroNodoCabezalBean> listaNodosSinMov = new ArrayList<>();
		List<SuministroBean> listaNumDAS = new ArrayList<>();
		
		listaNodosConSaltos = suministroDAO.consultarUltimosSumNodos();
		LOGGER.info("[crearInformeSumPerdidos] contarSurConSaltos {} ",listaNodosConSaltos);
		listaNumDAS = suministroDAO.obtenerSaltoNumeraciosDAS();
		LOGGER.info("[crearInformeSumPerdidos] contarDASConSaltos {} ",listaNumDAS);
	    listaNodosSinMov = suministroDAO.consultarSinMovSumNodos();
		LOGGER.info("[crearInformeSumPerdidos] contarSurSinUso {} ",listaNodosSinMov);

		// Alertas + las 4 tablas de control
		StringBuilder resultStringBuilder = new StringBuilder();
		createBodySaltosMail(listaNodosConSaltos, resultStringBuilder);
		createBodySaltosDASMail(listaNumDAS, resultStringBuilder);
		createBodySinMovMail(listaNodosSinMov, resultStringBuilder);
		createBodyControlMovMail(resultStringBuilder);

		LOGGER.info("[crearInformeSumPerdidos] enviarCorreo {} ",resultStringBuilder.toString());
		
		List<PersonalBean> listaPersonal = personalGestor.obtenerPersonasRemitentes();
		enviarCorreo(listaPersonal, resultStringBuilder.toString());		
		LOGGER.info("[crearInformeSumPerdidos] FIN ");
	}

	
	/**
	 * Construye el bloque HTML de resumen filtrado por puntos de suministro.
	 * @param idsPuntoSuministroFiltro
	 * @return
	 */
	@Override
	public String construirBloqueResumenPerdidas(Set<Integer> idsPuntoSuministroFiltro) {
		List<SuministroNodoCabezalBean> listaNodosConSaltos = suministroDAO.consultarUltimosSumNodos();
		List<SuministroNodoCabezalBean> listaNodosSinMov = suministroDAO.consultarSinMovSumNodos();
		List<SuministroBean> listaNumDAS = suministroDAO.obtenerSaltoNumeraciosDAS();

		if (idsPuntoSuministroFiltro != null && !idsPuntoSuministroFiltro.isEmpty()) {
			listaNodosConSaltos = filtrarNodosPorPuntos(listaNodosConSaltos, idsPuntoSuministroFiltro);
			listaNodosSinMov = filtrarNodosPorPuntos(listaNodosSinMov, idsPuntoSuministroFiltro);
			listaNumDAS = filtrarSuministrosPorPuntos(listaNumDAS, idsPuntoSuministroFiltro);
		}

		StringBuilder resultStringBuilder = new StringBuilder();
		createBodySaltosMail(listaNodosConSaltos, resultStringBuilder);
		createBodySaltosDASMail(listaNumDAS, resultStringBuilder);
		createBodySinMovMail(listaNodosSinMov, resultStringBuilder);
		createBodyControlMovMail(resultStringBuilder, idsPuntoSuministroFiltro);
		return limpiarTagsHtmlExternos(resultStringBuilder.toString());
	}

	/**
	 *Elimina las etiquetas html y body externas del contenido.
	 * @param html
	 * @return
	 */
	private String limpiarTagsHtmlExternos(String html) {
		return html.replace(OPEN_HTML_BODY, "").replace(CLOSE_HTML_BODY, "");
	}

	/**
	 *Filtra una lista de nodos por los puntos de suministro indicados.
	 * @param lista
	 * @param idsPuntoSuministroFiltro
	 * @return
	 */
	private List<SuministroNodoCabezalBean> filtrarNodosPorPuntos(List<SuministroNodoCabezalBean> lista,
			Set<Integer> idsPuntoSuministroFiltro) {
		List<SuministroNodoCabezalBean> result = new ArrayList<>();
		for (SuministroNodoCabezalBean item : lista) {
			if (item != null
					&& item.getSurtidor() != null
					&& item.getSurtidor().getPuntoSuministro() != null
					&& idsPuntoSuministroFiltro.contains(item.getSurtidor().getPuntoSuministro().getIdPuntoSuministro())) {
				result.add(item);
			}
		}
		return result;
	}

	/**
	 *Filtra una lista de suministros por los puntos de suministro indicados.
	 * @param lista
	 * @param idsPuntoSuministroFiltro
	 * @return
	 */
	private List<SuministroBean> filtrarSuministrosPorPuntos(List<SuministroBean> lista,
			Set<Integer> idsPuntoSuministroFiltro) {
		List<SuministroBean> result = new ArrayList<>();
		for (SuministroBean item : lista) {
			if (item != null
					&& item.getPuntoSuministro() != null
					&& idsPuntoSuministroFiltro.contains(item.getPuntoSuministro().getIdPuntoSuministro())) {
				result.add(item);
			}
		}
		return result;
	}

	/**
	 *Genera el bloque HTML de surtidores sin movimiento.
	 * @param listaNodosSinMov
	 * @param resultStringBuilder
	 */
	private void createBodySinMovMail(List<SuministroNodoCabezalBean> listaNodosSinMov,
			StringBuilder resultStringBuilder) {
		LOGGER.info("[createBodySinMovMail] INICIO");
		resultStringBuilder.append("<p><b>Surtidores - Días sin enviar movimientos: </b></p>");	
		if(null != listaNodosSinMov && !listaNodosSinMov.isEmpty()) {
			resultStringBuilder.append(OPEN_UL);
			for (SuministroNodoCabezalBean entry : listaNodosSinMov) {
			    resultStringBuilder.append(OPEN_LI)
			    			.append(entry.getSurtidor().getPuntoSuministro().getNombre()
			    				.concat(GUION+entry.getSurtidor().getNombre()))
			    			.append(PUNTOS_SEGUIDOS)
			    			.append(" Lleva "+entry.getDiferenciaTiempo()+" días sin enviar suministros.")
			    			.append(CLOSE_LI);
			}	
			resultStringBuilder.append(CLOSE_UL);
		}
		
		LOGGER.info("[createBodySinMovMail] FIN");
	}


	/**
	 *Genera las tablas de control completas para el correo.
	 * @param resultStringBuilder
	 */
	private void createBodyControlMovMail(StringBuilder resultStringBuilder) {
		createBodyControlMovMail(resultStringBuilder, null);
	}

	/**
	 *Genera las tablas de control filtradas por punto de suministro.
	 * @param resultStringBuilder
	 * @param idsPuntoSuministroFiltro
	 */
	private void createBodyControlMovMail(StringBuilder resultStringBuilder, Set<Integer> idsPuntoSuministroFiltro) {
		LOGGER.info("[createBodyControlMovMail] INICIO FILTRADO");


		List<FilaControl> filas = calcularFilas(idsPuntoSuministroFiltro);
		crearTablasControl(resultStringBuilder, filas);
		pie(resultStringBuilder);

		LOGGER.info("[createBodyControlMovMail] FIN FILTRADO");
	}

	/**
	 * Pinta las tablas de control a partir de las filas ya calculadas.
	 * Las tablas CUB solo se pintan si algún punto tiene CUB.
	 * @param resultStringBuilder
	 * @param filas
	 */
	void crearTablasControl(StringBuilder resultStringBuilder, List<FilaControl> filas) {
		cabeceraTablaTotalizadores(resultStringBuilder);
		cuerpoTablaTotalizadores(resultStringBuilder, filas);

		cabeceraTablaLitrosMovidos(resultStringBuilder);
		cuerpoTablaLitrosMovidos(resultStringBuilder, filas);

		if (hayCub(filas)) {
			cabeceraTablaLitrosMovidosCUB(resultStringBuilder);
			cuerpoTablaLitrosMovidosCUB(resultStringBuilder, filas);
		}

		cabeceraTablaMermasExcesos(resultStringBuilder);
		cuerpoTablaMermasExcesos(resultStringBuilder, filas);

		if (hayCub(filas)) {
			cabeceraTablaMermasExcesosCUB(resultStringBuilder);
			cuerpoTablaMermasExcesosCUB(resultStringBuilder, filas);
		}
	}

	/**
	 * Cabecera de la tabla de Totalizadores.
	 *
	 * @param resultStringBuilder HTML con la tabla comprobación diaria de los litros registrados
	 */
	private void cabeceraTablaTotalizadores(StringBuilder resultStringBuilder){

		resultStringBuilder.append(creacionTabla("Totalizadores","NT_F = NT_I + TL_D + TL_D_Por_Procesar - TL_S - TL_S_Por_Procesar"));

		Map<String, String> columnas = new LinkedHashMap<>();

		columnas.put("Punto de Suministro", OPEN_TH_STYLE);
		columnas.put("TL_S", OPEN_TH_STYLE);
		columnas.put("TL_S_Procesar", OPEN_TH_STYLE);
		columnas.put("SUM TL_S", OPEN_TH_STYLEYellow);
		columnas.put("TOT Ini", OPEN_TH_STYLE);
		columnas.put("TOT Fin", OPEN_TH_STYLE);
		columnas.put("Dif TOT", OPEN_TH_STYLEYellow);
		columnas.put("Dif TOT vs SUM", OPEN_TH_STYLEOrange);

		crearCabeceras(resultStringBuilder, columnas);

	}

	/**
	 * Cabecera de la tabla de Litros registrados Ayer.
	 * @param resultStringBuilder
	 */
	private void cabeceraTablaLitrosMovidos(StringBuilder resultStringBuilder){

		resultStringBuilder.append(creacionTabla("Litros Registrados","NT_F = NT_I + TL_D + TL_D_Por_Procesar - TL_S - TL_S_Por_Procesar"));

		Map<String,String> columnas = new LinkedHashMap<>();

		columnas.put("Punto de Suministro",OPEN_TH_STYLE);
		columnas.put("NumSUM",OPEN_TH_STYLE);
		columnas.put("NumSUMCab",OPEN_TH_STYLE);
		columnas.put("NumDES",OPEN_TH_STYLE);
		columnas.put("NumDESCon",OPEN_TH_STYLE);
		columnas.put("NT_F",OPEN_TH_STYLE);
		columnas.put("NT_I",OPEN_TH_STYLE);
		columnas.put("TL_D",OPEN_TH_STYLE);
		columnas.put("TL_D_Por_Procesar",OPEN_TH_STYLE);
		columnas.put("TL_S",OPEN_TH_STYLE);
		columnas.put("TL_S_Por_Procesar",OPEN_TH_STYLE);
		columnas.put("Diferencia de litros",OPEN_TH_STYLEYellow);
		columnas.put("AVG",OPEN_TH_STYLEOrange);

		crearCabeceras(resultStringBuilder, columnas);
	}

	/**
	 * Cabecera de la tabla de Litros registrados desde CUB.
	 * @param resultStringBuilder
	 */

	private void cabeceraTablaLitrosMovidosCUB(StringBuilder resultStringBuilder){

		resultStringBuilder.append(creacionTabla("Litros registrados desde CUB","NT_F = NT_I + TL_D + TL_D_Por_Procesar - TL_S - TL_S_Por_Procesar"));

		Map<String,String> columnas = new LinkedHashMap<>();

		columnas.put("Punto de Suministro",OPEN_TH_STYLE);
		columnas.put("NT_F",OPEN_TH_STYLE);
		columnas.put("NT_I",OPEN_TH_STYLE);
		columnas.put("TL_D",OPEN_TH_STYLE);
		columnas.put("TL_D_Por_Procesar",OPEN_TH_STYLE);
		columnas.put("TL_S",OPEN_TH_STYLE);
		columnas.put("TL_S_Por_Procesar",OPEN_TH_STYLE);
		columnas.put("Diferencia de litros",OPEN_TH_STYLEYellow);
		columnas.put("AVG",OPEN_TH_STYLEOrange);
		
		crearCabeceras(resultStringBuilder, columnas);
	}

	/**
	 * Cabecera de la tabla de Mermas - Excesos.
	 * @param resultStringBuilder
	 */

	private void cabeceraTablaMermasExcesos(StringBuilder resultStringBuilder){

		resultStringBuilder.append(creacionTabla("Mermas o Excesos","NT_F - NT_I = Dif litros | ET Fin -  ET Ini = Dif ET | Dif litros - Dif ET = Mermas o Excesos "));
		crearCabeceras(resultStringBuilder, cabecerasMermas());
		
	}

	/**
	 * Cabecera de la tabla de Mermas - Excesos desde CUB.
	 */
	private void cabeceraTablaMermasExcesosCUB(StringBuilder resultStringBuilder){
		
		resultStringBuilder.append(creacionTabla("Mermas o Excesos desde CUB",""));
		crearCabeceras(resultStringBuilder, cabecerasMermas());

	}

	// Mermas y Excesos llevan las mismas columnas
	private Map<String, String> cabecerasMermas() {
		Map<String, String> c = new LinkedHashMap<>();
		c.put("Punto de Suministro", OPEN_TH_STYLE);
		c.put("NT_F", OPEN_TH_STYLE);
		c.put("NT_I", OPEN_TH_STYLE);
		c.put("Dif litros", OPEN_TH_STYLEYellow);
		c.put("ET Fin", OPEN_TH_STYLE);
		c.put("ET Ini", OPEN_TH_STYLE);
		c.put("Dif ET", OPEN_TH_STYLEYellow);
		c.put("Mermas o Excesos", OPEN_TH_STYLEOrange);
		return c;
	}

	private void crearCabeceras(StringBuilder sb,
                            Map<String, String> columnas) {

		for (Map.Entry<String, String> columna : columnas.entrySet()) {
			sb.append(columna.getValue())
			.append(columna.getKey())
			.append(CLOSE_TH);
		}
		sb.append(CLOSE_TR);
	}
	
	/**
	* Cuerpo de la tabla de Totalizadores.
	*/
	private void cuerpoTablaTotalizadores(StringBuilder sb, List<FilaControl> filas) {
    NumberFormat nf = formatoNumero();

    for (FilaControl f : filas) {
        double sumTlS = f.tlS + f.tlSPP;
        double difTot = f.totFin - f.totIni;
        double difVsSum = difTot - sumTlS;

      	String difVsSumTexto = nf.format(difVsSum);

		if (Math.abs(difVsSum) > 3) {
		difVsSumTexto = "<span style='color:red;font-weight:bold;'>"
		+ difVsSumTexto
		+ "</span>";
		}

        fila(sb, null, f.nombre,nf.format(f.tlS),nf.format(f.tlSPP),nf.format(sumTlS),nf.format(f.totIni),nf.format(f.totFin),nf.format(difTot), difVsSumTexto);
    }

    cerrarTabla(sb);
}

	/**
	* Cuerpo de la tabla de Litros movidos Ayer.
	*/
	private void cuerpoTablaLitrosMovidos(StringBuilder sb, List<FilaControl> filas) {
		NumberFormat nf = formatoNumero();
		NumberFormat pct = formatoPorcentaje();
		for (FilaControl f : filas) {
			double avg = ratio(f.difLitros, f.tlS + f.tlSPP);

			String avgTexto = pct.format(avg);
			if (avg > 0.05) {
			avgTexto = "<span style='color:red;font-weight:bold;'>" + avgTexto + "</span>";
			}
		 // regla del Excel: AVG > 5 %

			fila(sb, null,f.nombre, String.valueOf(f.numSum),String.valueOf(f.numSumCab),
			String.valueOf(f.numDes),String.valueOf(f.numDesCon),nf.format(f.ntF),nf.format(f.ntI),nf.format(f.tlD),nf.format(f.tlDPP), nf.format(f.tlS),
        	nf.format(f.tlSPP), nf.format(f.difLitros),avgTexto);
		}
		cerrarTabla(sb);
	}

	/**
	* Cuerpo de la tabla de Litros movidos desde CUB.
	*/
	private void cuerpoTablaLitrosMovidosCUB(StringBuilder sb, List<FilaControl> filas) {
		NumberFormat nf = formatoNumero();
		NumberFormat pct = formatoPorcentaje();
		for (FilaControl f : filas) {
			if (f.fechaCub == null) {
				continue;                                                  // solo puntos con CUB
			}
		double avg = ratio(f.difCub, f.tlSCub + f.tlSPPCub);
		String avgTexto = pct.format(avg);
		if (avg > 0.05) {
		avgTexto = "<span style='color:red;font-weight:bold;'>" + avgTexto + "</span>";
		}
			fila(sb, null, f.nombre, nf.format(f.ntF), nf.format(f.ntICub),
					nf.format(f.tlDCub), nf.format(f.tlDPPCub), nf.format(f.tlSCub), nf.format(f.tlSPPCub),
					nf.format(f.difCub), avgTexto);
		}
		cerrarTabla(sb);
	}

	/**
	* Cuerpo de la tabla de Mermas - Excesos.
	*/
	private void cuerpoTablaMermasExcesos(StringBuilder sb, List<FilaControl> filas) {
		NumberFormat nf = formatoNumero();
		for (FilaControl f : filas) {
			String color = null;                                           // regla pendiente de decidir

			fila(sb, null, f.nombre, nf.format(f.ntF), nf.format(f.ntI), nf.format(f.ntF - f.ntI),
					nf.format(f.etFin), nf.format(f.etIni), nf.format(f.etFin - f.etIni),
					nf.format(f.ntF - f.etFin));
		}
		cerrarTabla(sb);
	}

	/**
	* Cuerpo de la tabla de Mermas - Excesos CUB.
	*/
	private void cuerpoTablaMermasExcesosCUB(StringBuilder sb, List<FilaControl> filas) {
		NumberFormat nf = formatoNumero();
		for (FilaControl f : filas) {
			if (f.fechaCub == null) {
				continue;
			}
			String color = null;

			fila(sb, color, f.nombre, nf.format(f.ntF), nf.format(f.ntICub), nf.format(f.ntF - f.ntICub),
					nf.format(f.etFin), nf.format(f.etIniCub), nf.format(f.etFin - f.etIniCub),
					nf.format(f.ntF - f.etFin));
		}
		cerrarTabla(sb);
	}
	
	private List<FilaControl> calcularFilas(Set<Integer> idsPuntoSuministroFiltro) {
		List<FilaControl> filas = new ArrayList<>();
		List<PuntoSuministroBean> listaPuntosSuministro = iGestorPuntoSuministro.consultarPuntosSuministro();

		for (PuntoSuministroBean puntoSuministro : listaPuntosSuministro) {
			if (idsPuntoSuministroFiltro != null
					&& !idsPuntoSuministroFiltro.isEmpty()
					&& !idsPuntoSuministroFiltro.contains(puntoSuministro.getIdPuntoSuministro())) {
				continue;
			}

			FiltroNumerarWrapperBean filtroNumerarBean = new FiltroNumerarWrapperBean();
			filtroNumerarBean.setIdPuntoSuministro(puntoSuministro.getIdPuntoSuministro());
			filtroNumerarBean.setPuntoSuministro(puntoSuministro);

			// Rango de fechas: el día de ayer completo (00:00:00 a 23:59:59)
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date diaAnteriorInicio = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date diaAnteriorFinal = calendar.getTime();

			filtroNumerarBean.setFechaInicioDesde(diaAnteriorInicio);
			filtroNumerarBean.setFechaInicioHasta(diaAnteriorFinal);

			puntoSuministro = iGestorPuntoSuministro.consultarDetallePuntoSuministro(puntoSuministro);

			List<SuministroNodoCabezalBean> listaSumNodoCab = new ArrayList<>();
			List<SurtidorBean> surtidores = iGestorSurtidor.consultarSurtidoresPuntoSuministro(puntoSuministro);
			List<DescargaNodoConsolaBean> listaDescNodoCon = new ArrayList<>();
			List<TanqueBean> tanques = iTanqueDAO.recuperarTanquesPuntoSuministro(puntoSuministro);
			int sumLitros = 0;

			// --- Suministros por surtidor (movimientos de los cabezales) ---
			for (SurtidorBean surt : surtidores) {
				NumerarBean numerarBean = new NumerarBean();
				numerarBean.setSurtidor(surt);

				SuministroNodoCabezalBean sumNodCab = new SuministroNodoCabezalBean();
				sumNodCab.setSurtidor(surt);
				sumNodCab.setFechaInicioSuministro(filtroNumerarBean.getFechaInicioDesde());
				sumNodCab.setFechaFinSuministro(filtroNumerarBean.getFechaInicioHasta());
				List<SuministroNodoCabezalBean> listaSuministroNodoCabezal =
						iGestorSuministroNodoCabezal.consultarSuministrosNodoCabezal(sumNodCab);
				numerarBean = verificarListaSum(numerarBean, listaSuministroNodoCabezal);

				Integer diferencia = numerarBean.getNumUltimoSuministro() - numerarBean.getNumPrimerSuministro();
				diferencia = comprobarDif(diferencia);
				numerarBean.setDiferencia(diferencia);
				numerarBean.setFaltan(numerarBean.getDiferencia() - numerarBean.getNumSuministros());
				numerarBean = recorrerListaListro(listaSuministroNodoCabezal, numerarBean);
				numerarBean.setDifLitros(numerarBean.getDifTotal() - numerarBean.getSumLitros());

				listaSuministroNodoCabezal = checkLitrosSaltos(listaSuministroNodoCabezal);
				listaSumNodoCab.addAll(listaSuministroNodoCabezal);   // para NumSUMCab
				sumLitros += numerarBean.getSumLitros();
			}

			// --- Descargas y niveles por tanque ---
			int volumenDescargadoTotal = 0;
			int nt_f = 0;
			int nt_i = 0;

			for (TanqueBean tanq : tanques) {
				NumerarBean numerarBeanTanques = new NumerarBean();
				numerarBeanTanques.setTanque(tanq);

				DescargaNodoConsolaBean descNodCon = new DescargaNodoConsolaBean();
				descNodCon.setFechaInicioDescarga(filtroNumerarBean.getFechaInicioDesde());
				Calendar c = Calendar.getInstance();
				c.setTime(filtroNumerarBean.getFechaInicioHasta());
				c.add(Calendar.DATE, 1);
				c.add(Calendar.MINUTE, -1);
				descNodCon.setFechaFinDescarga(c.getTime());
				descNodCon.setTanque(tanq);

				List<DescargaNodoConsolaBean> listaDescargaNodoConsola =
						iGestorDescargaNodoConsola.recuperarDescargasEntreFechasTanque(descNodCon);
				NivelTanqueBean nivelTanque = iGestorNivelTanque.ultimoNivelTanque(tanq.getIdTanque());
				numerarBeanTanques.setUltimoNivelRegistrado(nivelTanque.getVolumenNeto().intValue());
				numerarBeanTanques.setFechaRegistro(nivelTanque.getFechaNivelTanque());

				List<TanqueBean> listTanque = new ArrayList<>();
				listTanque.add(tanq);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

				// Nivel final del tanque (NT_F)
				List<NivelTanqueBean> list_ntf =
						iGestorNivelTanque.consultarUltimoNivelTanques(filtroNumerarBean.getFechaInicioHasta(), listTanque);
				numerarBeanTanques = obtenerUltimoNivelFecha(numerarBeanTanques, list_ntf);
				numerarBeanTanques = compruebaSiExisteNivelFinal(filtroNumerarBean, numerarBeanTanques, sdf);
				nt_f = getNt_f(nt_f, numerarBeanTanques);

				// Nivel inicial del tanque (NT_I)
				List<NivelTanqueBean> list_nti =
						iGestorNivelTanque.consultarPrimerNivelTanques(filtroNumerarBean.getFechaInicioDesde(), listTanque);
				for (NivelTanqueBean nti : list_nti) {
					numerarBeanTanques.setPrimerNivelFecha(nti.getVolumenNeto().intValue());
					numerarBeanTanques.setFechaRegPrimer(nti.getFechaNivelTanque());
					numerarBeanTanques = compruebaSiExisteNivel(filtroNumerarBean, numerarBeanTanques, sdf);
				}
				nt_i = getNt_i(nt_i, numerarBeanTanques);

				numerarBeanTanques = volumenDesc(listaDescargaNodoConsola, numerarBeanTanques);
				numerarBeanTanques = verificacionLista(numerarBeanTanques, listaDescargaNodoConsola);

				volumenDescargadoTotal += numerarBeanTanques.getVolumenDescargado();
				listaDescNodoCon.addAll(listaDescargaNodoConsola);    // para NumDESCon
			}

			// --- Suministros y descargas procesados (tablas de la BBDD) ---
			SuministroBean sum = new SuministroBean();
			sum.setPuntoSuministro(puntoSuministro);
			sum.setFechaInicio(filtroNumerarBean.getFechaInicioDesde());
			sum.setFechaFin(filtroNumerarBean.getFechaInicioHasta());

			List<SuministroBean> listaSum = iGestorSuministro.consultarSuministrosPuntoFechas(sum);
			List<SuministroNodoCabezalBean> listaSuministrosNodoNoProcesados =
					iGestorSuministroNodoCabezal.consultarSuministrosNoProcesados(puntoSuministro);
			Float litrosSuministroNodoNoProcesados = obtenerLitrosSuministroNodoNoProcesados(
					listaSuministrosNodoNoProcesados, 0f, filtroNumerarBean.getFechaInicioDesde());

			List<DescargaBean> listaDes = iGestorDescarga.descargasPuntoFechas(sum);
			DatosSumDesMovimientoBean datosSumDesMovimiento = recorrerListaSumDes(listaSum, listaDes);
			List<DescargaNodoConsolaBean> listaDescargasNodosNoProcesada =
					iGestorDescargaNodoConsola.consultarListaDescargasNodoConsolaSinProcesar(puntoSuministro);
			Double litrosDescargaNodoNoProcesados = obtenerLitrosDescargaNodoNoProcesados(
					listaDescargasNodosNoProcesada, 0d, filtroNumerarBean.getFechaInicioDesde());

			datosSumDesMovimiento.setTotalLitrosNodos(sumLitros);
			datosSumDesMovimiento.setDifLitrosSum(datosSumDesMovimiento.getTotalLitrosSum() - sumLitros);
			datosSumDesMovimiento.setTotalLitrosDesNodos(volumenDescargadoTotal);
			datosSumDesMovimiento.setDifLitrosDes(datosSumDesMovimiento.getTotalLitrosDes() - volumenDescargadoTotal);

			// (NT_I + TL_D + TL_D_PP - TL_S - TL_S_PP) - NT_F
			Integer excedenteLitros = (nt_i + datosSumDesMovimiento.getTotalLitrosDes()
					+ litrosDescargaNodoNoProcesados.intValue()
					- datosSumDesMovimiento.getTotalLitrosSum()
					- litrosSuministroNodoNoProcesados.intValue()) - nt_f;

			// --- Montamos la fila ---
			FilaControl f = new FilaControl();
			f.nombre = puntoSuministro.getNombre();
			f.ntF = nt_f;
			f.ntI = nt_i;
			f.tlD = datosSumDesMovimiento.getTotalLitrosDes();
			f.tlDPP = litrosDescargaNodoNoProcesados;
			f.tlS = datosSumDesMovimiento.getTotalLitrosSum();
			f.tlSPP = litrosSuministroNodoNoProcesados;
			f.difLitros = -excedenteLitros;               // con signo, como el Excel
			f.numSum = listaSum.size();
			f.numSumCab = listaSumNodoCab.size();
			f.numDes = listaDes.size();
			f.numDesCon = listaDescNodoCon.size();

			Integer id = puntoSuministro.getIdPuntoSuministro();
			f.totIni = iGestorSuministroNodoCabezal.obtenerTotalizadoresPorDia(diaAnteriorInicio, id);
			f.totFin = iGestorSuministroNodoCabezal.obtenerTotalizadoresPorDia(new Date(), id);
			f.etIni = iExistenciasTeoricasDAO.obtenerExistenciasTeoricas(puntoSuministro, diaAnteriorInicio);
			f.etFin = iExistenciasTeoricasDAO.obtenerExistenciasTeoricas(puntoSuministro, diaAnteriorFinal);

			// --- Desde CUB: solo si sabemos la fecha de la CUB ---
			Date fechaCub = obtenerFechaCub(puntoSuministro);
			if (fechaCub != null) {
				f.fechaCub = fechaCub;

				SuministroBean sumCub = new SuministroBean();
				sumCub.setPuntoSuministro(puntoSuministro);
				sumCub.setFechaInicio(fechaCub);
				sumCub.setFechaFin(diaAnteriorFinal);

				for (SuministroBean s : iGestorSuministro.consultarSuministrosPuntoFechas(sumCub)) {
					f.tlSCub += s.getLitrosSuministrados15().doubleValue();
				}
				for (DescargaBean d : iGestorDescarga.descargasPuntoFechas(sumCub)) {
					f.tlDCub += d.getLitrosDescargados().doubleValue();
				}
				f.tlSPPCub = litrosSumNoProcesadosEntre(listaSuministrosNodoNoProcesados, fechaCub, diaAnteriorFinal);
				f.tlDPPCub = litrosDesNoProcesadosEntre(listaDescargasNodosNoProcesada, fechaCub, diaAnteriorFinal);

				// NT_I de la CUB: nivel de cada tanque en la fecha de la CUB
				for (TanqueBean tanq : tanques) {
					List<TanqueBean> listTanque = new ArrayList<>();
					listTanque.add(tanq);
					int nivel = 0;
					for (NivelTanqueBean nti : iGestorNivelTanque.consultarPrimerNivelTanques(fechaCub, listTanque)) {
						nivel = nti.getVolumenNeto().intValue();
					}
					f.ntICub += nivel;
				}
				f.etIniCub = iExistenciasTeoricasDAO.obtenerExistenciasTeoricas(puntoSuministro, fechaCub);
				f.difCub = nt_f - (f.ntICub + f.tlDCub + f.tlDPPCub - f.tlSCub - f.tlSPPCub);
			}

			filas.add(f);
		}
		return filas;
	}

	/**
	 *
	 * @param titulo De la tabla
	 * @param formula utilizada ejemplo NT_F = NT_I + TL_D + TL_D_Por_Procesar - TL_S - TL_S_Por_Procesar
	 * @return 
	 */
	private String creacionTabla(String titulo, String formula) {
		StringBuilder sb = new StringBuilder();
		sb.append("<p><b>Comprobación diaria ").append(titulo).append(": </b></p>");
		if (formula != null) {
			sb.append("<p><b>").append(formula).append("</b></p>");
		}
		sb.append(OPEN_TABLE).append(OPEN_TR);

		return sb.toString();
	}

	// |dif| / base, y 0 si la base es 0 (como el AVG del Excel)
	private double ratio(double dif, double base) {
		return base == 0 ? 0 : Math.abs(dif) / base;
	}

	private void pie(StringBuilder resultStringBuilder){
		resultStringBuilder.append("<pre><br>"
				+ "<i>* NT_F: Nivel final de los tanques para el día anterior.</i><br>"
				+ "<i>* NT_I: Nivel inicial de los tanques para el día anterior.</i><br>"
				+ "<i>* TL_D: Litros descargados(Tabla descargas entre las fechas) en el día anterior.</i><br>"
				+ "<i>* TL_D_Por_Procesar: Litros descargados del nodo pendientes de procesar.</i><br>"
				+ "<i>* TL_S: Litros suministrados (Tabla suministros entre las fechas) en el día anterior.</i><br>"
				+ "<i>* TL_S_Por_Procesar: Litros suministrados del nodo pendientes de procesar.</i><br>"
				+ "<i>* Diferencia de litros: Diferencia de Litros Finales(NT_F) respecto a Litros Iniciales(NT_I) + Litros Descargas(TL_D) - Litros Suministros(TL_S).</i><br>"
				+ "<i>* NumSUM: Número de suministros.</i><br>"
				+ "<i>* NumSUMCab: Número de movimientos de suministro enviados por los Cabezales.</i><br>"
				+ "<i>* NumDES: Número de descargas.</i><br>"
				+ "<i>* NumDESCon: Número de movimientos de descarga enviados por las Consolas.</i><br>"
				+ "<i>* TOT litros SUM a CGD: Total de los litros de suministros enviados a CGD.</i><br>"
				+ "<i>* TOT litros SUM no CGD: Total de los litros de suministros no enviados a CGD.</i><br>"
				+ "<i>* TOT litros DES a CGD: Total de los litros de descargas enviados a CGD.</i><br>"
				+ "<i>* TOT litros DES no CGD: Total de los litros de descargas no enviados a CGD.</i><br>"
				+ "<i>* TOT Ini: Suma de los totalizadores de los Cabezales al inicio de la jornada.</i><br>"
				+ "<i>* TOT Fin: Suma de los totalizadores de los Cabezales al final de la jornada.</i><br>"
				+ "<i>* Dif TOT: Diferencia de los totalizadores (TOT Fin - TOT Ini, es decir, los litros realmente suministrados ese día, salvo problemas de los nodos).</i><br>"
				+ "<i>* ET Ini: Existencias Teóricas al inicio de la jornada.</i><br>"
				+ "<i>* ET Fin: Existencias Teóricas al final de la jornada.</i><br>"
				+ "<i>* Dif ET: Diferencia de las Existencias Teóricas (ET Fin - ET Ini).</i><br>"
				+ "<i>* Dif TOT vs SUM: Diferencia de los totalizadores vs Suministros (procesados o no) (debería cuadrar, salvo +/-2 por suministro).</i><br>"
				+ "<i>* de litros TOT: NT_F – ( NT_I + TL_D + TL_D_Por_Procesar - Dif. TOT ).</i><br>"
				+ "<i>* Merma y Exceso: Nivel consola final (volumen neto, NT_F) vs ET Fin (a 15º).</i><br>"
				+ "<i>* TL_D_Por_Procesar No tienen que ser los datos del albarán.</i><br>"
				+ "<br></pre><br>");

		resultStringBuilder.append(CLOSE_HTML_BODY);
	}

	private void createBodySaltosDASMail(List<SuministroBean> listaNumDAS,
									  StringBuilder resultStringBuilder) {
		LOGGER.info("[createBodySaltosDASMail] INICIO");
		resultStringBuilder.append("<p><b>Documentos de Suministro con Posibles Saltos de Numeración: </b></p>");

		// Obtener los puntos con salto de numeracion
		Map<String, List<SuministroBean>> listaSumPuntosDASConSaltos = obtenerPuntosDASConSalto(listaNumDAS);

		if (listaSumPuntosDASConSaltos != null && !listaSumPuntosDASConSaltos.isEmpty()) {
			for (Map.Entry<String, List<SuministroBean>> entry : listaSumPuntosDASConSaltos.entrySet()) {
				String punto = entry.getKey();
				List<SuministroBean> suministros = entry.getValue();

				resultStringBuilder.append("<p><b>Punto de Suministro:</b> ")
						.append(punto).append("</p>")
						.append(OPEN_UL);

				for (SuministroBean s : suministros) {
					String error = obtenerError(s.getIdSuministro());

					resultStringBuilder.append(OPEN_LI)
							.append("Número de documento faltante: ").append(s.getNumeroDocumento())
							.append(" - Motivo: ").append((error!=null) ? error : "No se encontró motivo")
							.append(CLOSE_LI);
				}

				resultStringBuilder.append(CLOSE_UL);
			}
		} else {
			resultStringBuilder.append(OPEN_UL)
					.append(OPEN_LI)
					.append("No se han detectado saltos de numeración de documento de suministro.")
					.append(CLOSE_LI)
					.append(CLOSE_UL);
		}

		LOGGER.info("[createBodySaltosDASMail] FIN");
	}

	private String obtenerError(Integer idSuministro){
		String error = null;
		List<ErrorNumeracionSuministroBean> listaError = iErrorNumeracionSuministroDAO.obtenerErroresPorIdSuministro(idSuministro);
		if (listaError!=null && !listaError.isEmpty())  {
			error = listaError.get(0).getMensaje();
		}
		return error;
	}

	private Map<String, List<SuministroBean>> obtenerPuntosDASConSalto(
			List<SuministroBean> listaNumDAS) {

		DecimalFormat formatter = crearFormatter();
		Map<String, List<SuministroBean>> grupos = agruparPorPuntoSuministro(listaNumDAS);
		List<SuministroBean> listaSumPuntosDASConSaltos = new ArrayList<>();

		for (List<SuministroBean> grupo : grupos.values()) {
			procesarGrupo(grupo, listaSumPuntosDASConSaltos, formatter);
		}

		return agruparPorPuntoSuministro(listaSumPuntosDASConSaltos);
	}

	/* ===================== MÉTODOS AUXILIARES ===================== */

	private NumberFormat formatoNumero() {
    	return NumberFormat.getNumberInstance(Locale.GERMANY);
	}

	private NumberFormat formatoPorcentaje() {
		NumberFormat pct = NumberFormat.getPercentInstance(Locale.GERMANY);
		pct.setMinimumFractionDigits(2);
		pct.setMaximumFractionDigits(2);
		return pct;
	}

	// Abre un <td> con borde, alineación y color de fondo opcional
	private String celda(boolean derecha, String color) {
		StringBuilder sb = new StringBuilder("<td style=\"border: 1px solid black ;");
		if (derecha) {
			sb.append(" text-align: right;");
		}
		if (color != null && !color.isEmpty()) {
			sb.append(" background-color: ").append(color).append(";");
		}
		return sb.append("\">").toString();
	}

	// Una fila: primera columna a la izquierda, el resto a la derecha. color null = sin color
	private void fila(StringBuilder sb, String color, String... valores) {
		sb.append(OPEN_TR);
		for (int i = 0; i < valores.length; i++) {
			sb.append(celda(i != 0, color)).append(valores[i]).append(CLOSE_TD);
		}
		sb.append(CLOSE_TR);
	}

	private void cerrarTabla(StringBuilder sb) {
		sb.append(CLOSE_TABLE).append(ETIQUETA_BR);
	}

	private boolean hayCub(List<FilaControl> filas) {
		for (FilaControl f : filas) {
			if (f.fechaCub != null) {
				return true;
			}
		}
		return false;
	}

	// TODO: devolver la fecha de la última CUB del punto. Mientras devuelva null no salen las tablas CUB.
	private Date obtenerFechaCub(PuntoSuministroBean punto) {
		return null;
	}

	private double litrosSumNoProcesadosEntre(List<SuministroNodoCabezalBean> lista, Date desde, Date hasta) {
		double total = 0;
		for (SuministroNodoCabezalBean s : lista) {
			Date fin = s.getFechaFinSuministro();
			if (fin != null && !fin.before(desde) && !fin.after(hasta)) {
				total += s.getLitrosTemp15();
			}
		}
		return total;
	}

	private double litrosDesNoProcesadosEntre(List<DescargaNodoConsolaBean> lista, Date desde, Date hasta) {
		double total = 0;
		for (DescargaNodoConsolaBean d : lista) {
			Date fin = d.getFechaFinDescarga();
			if (fin != null && !fin.before(desde) && !fin.after(hasta)) {
				total += (d.getVolumenNetoFin() - d.getVolumenNetoInicio());
			}
		}
		return total;
	}

	private void procesarGrupo(List<SuministroBean> grupo,
							   List<SuministroBean> resultado,
							   DecimalFormat formatter) {

		ordenarPorNumeroDocumento(grupo);

		for (int i = 0; i < grupo.size() - 1; i++) {
			agregarFaltantesSiExisten(grupo.get(i), grupo.get(i + 1), resultado, formatter);
		}
	}

	private void ordenarPorNumeroDocumento(List<SuministroBean> grupo) {
		Collections.sort(grupo, new Comparator<SuministroBean>() {
			@Override
			public int compare(SuministroBean o1, SuministroBean o2) {
				return Double.valueOf(o1.getNumeroDocumento())
						.compareTo(Double.valueOf(o2.getNumeroDocumento()));
			}
		});
	}

	private void agregarFaltantesSiExisten(SuministroBean actual,
										   SuministroBean siguiente,
										   List<SuministroBean> resultado,
										   DecimalFormat formatter) {

		double numeroActual = Double.parseDouble(actual.getNumeroDocumento());
		double numeroSiguiente = Double.parseDouble(siguiente.getNumeroDocumento());

		if (numeroSiguiente <= numeroActual + 1) {
			return;
		}

		crearSuministrosFaltantes(actual, numeroActual, numeroSiguiente, resultado, formatter);
	}

	private void crearSuministrosFaltantes(SuministroBean base,
										   double inicio,
										   double fin,
										   List<SuministroBean> resultado,
										   DecimalFormat formatter) {

		for (double faltante = inicio + 1; faltante < fin; faltante++) {
			SuministroBean nuevo = copiarSuministro(base);
			nuevo.setNumeroDocumento(formatter.format(faltante));
			resultado.add(nuevo);
		}
	}

	private DecimalFormat crearFormatter() {
		DecimalFormat formatter = new DecimalFormat("################");
		formatter.setGroupingUsed(false);
		return formatter;
	}

	private SuministroBean copiarSuministro(SuministroBean origen) {
		SuministroBean copia = new SuministroBean();
		copia.setIdSuministro(origen.getIdSuministro());
		copia.setPuntoSuministro(origen.getPuntoSuministro());
		copia.setNumeroDocumentoSuministro(origen.getNumeroDocumentoSuministro());

		return copia;
	}

	private Map<String, List<SuministroBean>> agruparPorPuntoSuministro(List<SuministroBean> lista){
		Map<String, List<SuministroBean>> grupos = new HashMap<String, List<SuministroBean>>();

		for (SuministroBean s : lista) {
			String punto = s.getPuntoSuministro().getNombre();

			if (!grupos.containsKey(punto)) {
				grupos.put(punto, new ArrayList<SuministroBean>());
			}

			grupos.get(punto).add(s);
		}

		return grupos;
	}

	private void createBodySaltosMail(List<SuministroNodoCabezalBean> listaNodosConSaltos,
			StringBuilder resultStringBuilder) {
		LOGGER.info("[createBodySaltosMail] INICIO");
		resultStringBuilder.append(OPEN_HTML_BODY);
		/*resultStringBuilder.append(OPEN_TABLE);
		resultStringBuilder.append(OPEN_TR);
		resultStringBuilder.append(OPEN_TD);*/
		resultStringBuilder.append("<b style='color:#0F4C81;'>COMPROBACIÓN DIARIA MOVIMIENTOS</b>");
		/*resultStringBuilder.append(CLOSE_TD);
		resultStringBuilder.append(CLOSE_TR);
		resultStringBuilder.append(CLOSE_TABLE);*/
		resultStringBuilder.append(ETIQUETA_BR)
		.append("<p><b>Surtidores con Posibles Saltos de Numeración: </b></p>");	

		if (listaNodosConSaltos != null && !listaNodosConSaltos.isEmpty()) {
		    resultStringBuilder.append(OPEN_UL);
		    for (SuministroNodoCabezalBean entry : listaNodosConSaltos) {
		        resultStringBuilder.append(OPEN_LI)
		                           .append(entry.getSurtidor().getPuntoSuministro().getNombre()
		                           .concat(GUION+entry.getSurtidor().getNombre()))
		                           .append(PUNTOS_SEGUIDOS)
		                           .append(" Tiene Saltos de Numeración")
		                           .append(CLOSE_LI);
		    }
		    resultStringBuilder.append(CLOSE_UL);
		} else {
			resultStringBuilder.append(OPEN_UL)
				.append(OPEN_LI)
				.append("No se han detectado saltos.")
				.append(CLOSE_LI)
				.append(CLOSE_UL);
		}

		LOGGER.info("[createBodySaltosMail] FIN");		
	}

	private void enviarCorreo(List<PersonalBean> listaPersonal, String cuerpoMensaje) {
		LOGGER.info("[enviarCorreo] INICIO");
		for (PersonalBean personal : listaPersonal) {			
			String asuntoMensajeParam = "[TEICO] Posibles Pérdidas del Día de Ayer";	

			if (!StringUtils.isEmpty(personal.getEmail()) && 
					!gestorCorreo.enviarEmailHtml(personal.getEmail(), cuerpoMensaje.toString(), asuntoMensajeParam, null, null)) {
				LOGGER.error("Error: No se logró enviar el email a " + personal.getEmail() + ".");
			}			
		}
		LOGGER.info("[enviarCorreo] FIN ");
	}
	
	private DatosSumDesMovimientoBean recorrerListaSumDes(List<SuministroBean> listaSum, List<DescargaBean> listaDes){
		DatosSumDesMovimientoBean datosMov = new DatosSumDesMovimientoBean();
		List<MovimientoBean> listaMovimientos = new ArrayList<>();
		int numSum = 0;
		int numDes = 0;
		int numSumWeb = 0;
		int numDesWeb = 0;
		int totaLitrosSum = 0;
		int totaLitrosDes = 0;

		for(SuministroBean sum:listaSum){
			List<SuministroSurtidorBean> listSumSurtidores = iSuministroDAO.obtenerSuministroSurtidorDetallePorIdSuministro(sum);
			listaMovimientos = comprobarListaSumWebOAuto(listaMovimientos, sum, listSumSurtidores);
			if(listaMovimientos.get(listaMovimientos.size()-1).getIdOperacion() == null || listaMovimientos.get(listaMovimientos.size()-1).getIdOperacion().isEmpty()){
				numSumWeb += 1;
			}
			numSum += 1;
			totaLitrosSum += sum.getLitrosSuministrados15();
		}

		for(DescargaBean des:listaDes){
			List<DescargaTanqueBean> listaDesTanques = iDescargaTanqueDAO.obtenerDescargaTanqueDetallePorIdDescarga(des.getIdDescarga());
			listaMovimientos = comprobarListaDescWebOAuto(listaMovimientos, des, listaDesTanques);
			if(listaMovimientos.get(listaMovimientos.size()-1).getIdOperacion() == null || listaMovimientos.get(listaMovimientos.size()-1).getIdOperacion().isEmpty()){
				numDesWeb += 1;
			}
			numDes += 1;
			totaLitrosDes += des.getLitrosDescargados();
		}

		Collections.sort(listaMovimientos);
		// Suministros
		datosMov.setMovimientos(listaMovimientos);
		datosMov.setNumSum(numSum);
		datosMov.setNumSumWeb(numSumWeb);
		datosMov.setTotalLitrosSum(totaLitrosSum);

		// Descargas
		datosMov.setNumDescargasReg(numDes);
		datosMov.setNumDescargasWeb(numDesWeb);
		datosMov.setTotalLitrosDes(totaLitrosDes);

		return datosMov;
	}

	private List<MovimientoBean> comprobarListaSumWebOAuto(List<MovimientoBean> listaMovimientos, SuministroBean sum, List<SuministroSurtidorBean> listSumSurtidores) {
		if(listSumSurtidores != null && !listSumSurtidores.isEmpty()) {
			for (SuministroSurtidorBean sumSurt : listSumSurtidores) {
				MovimientoBean mov = new MovimientoBean();
				mov.setFechaInicio(sum.getFechaInicio());
				mov.setFechaFin(sum.getFechaFin());
				mov.setTipo("S");
				mov.setSurtidor(sumSurt.getSurtidor());
				SuministroNodoCabezalBean sumNodo = sumSurt.getSuministroNodoCabezal();
				if (sumNodo != null) {
					mov.setIdOperacion(sumNodo.getIdOperacion());
					mov.setNumSuministro(sumNodo.getNumSuministro());
					mov.setLitrosTotalInicio(sumNodo.getTotInicialTemp15().intValue());
					mov.setLitrosTotalFin(sumNodo.getTotFinalTemp15().intValue());
				}
				mov.setNumDocumento(sum.getNumeroDocumento());
				mov.setLitrosSum(sum.getLitrosSuministrados15().intValue());
				mov.setIdSuministro(sum.getIdSuministro());

				listaMovimientos.add(mov);
			}
		}else{
			MovimientoBean mov = new MovimientoBean();
			mov.setFechaInicio(sum.getFechaInicio());
			mov.setFechaFin(sum.getFechaFin());
			mov.setTipo("S");
			mov.setNumDocumento(sum.getNumeroDocumento());
			mov.setLitrosSum(sum.getLitrosSuministrados15().intValue());
			mov.setIdSuministro(sum.getIdSuministro());

			listaMovimientos.add(mov);
		}
		return listaMovimientos;
	}
	
	private List<MovimientoBean> comprobarListaDescWebOAuto(List<MovimientoBean> listaMovimientos, DescargaBean des, List<DescargaTanqueBean> listaDesTanques) {
		if(listaDesTanques != null && !listaDesTanques.isEmpty()) {
			for (DescargaTanqueBean desTanq : listaDesTanques) {
				MovimientoBean mov = new MovimientoBean();
				mov.setFechaInicio(des.getFechaInicioDescarga());
				mov.setFechaFin(des.getFechaFinDescarga());
				mov.setIdOperacion(desTanq.getDescargaNodoConsola().getIdOperacion());
				mov.setTipo("D");
				mov.setTanque(desTanq.getTanque());
				mov.setVolumenTotalInicio(desTanq.getVolumen15TanqueInicio().intValue());
				mov.setVolumenTotalFin(desTanq.getVolumen15TanqueFin().intValue());
				mov.setIdDescarga(des.getIdDescarga());
				mov.setLitrosDes(des.getLitrosDescargados().intValue());

				listaMovimientos.add(mov);
			}
		}else{
			MovimientoBean mov = new MovimientoBean();
			mov.setFechaInicio(des.getFechaInicioDescarga());
			mov.setFechaFin(des.getFechaFinDescarga());
			mov.setTipo("D");
			mov.setIdDescarga(des.getIdDescarga());
			mov.setLitrosDes(des.getLitrosDescargados().intValue());
			LOGGER.info(des.toString());
			listaMovimientos.add(mov);
		}
		return listaMovimientos;
	}
	
	private NumerarBean verificarListaSum(NumerarBean numerarBean, List<SuministroNodoCabezalBean> lista) {
		if(lista.isEmpty()){
			numerarBean.setNumSuministros(0);
			numerarBean.setNumPrimerSuministro(0);
			numerarBean.setNumUltimoSuministro(0);
			numerarBean.setTotalInicial(0);
			numerarBean.setTotalFinal(0);
			numerarBean.setDifTotal(0);
			numerarBean.setNumOp(0);
		}else{
			numerarBean.setNumSuministros(lista.size());
			numerarBean.setNumPrimerSuministro(lista.get(0).getNumSuministro());
			numerarBean.setNumUltimoSuministro(lista.get(lista.size()-1).getNumSuministro());
			numerarBean.setTotalInicial(lista.get(0).getTotInicialTemp15().intValue());
			numerarBean.setTotalFinal(lista.get(lista.size() - 1).getTotFinalTemp15().intValue());
			numerarBean.setDifTotal(numerarBean.getTotalFinal() - numerarBean.getTotalInicial());
		}
		return numerarBean;
	}
	
	private Integer comprobarDif(Integer diferencia) {
		if(diferencia >0){
			diferencia = diferencia +1;
		}
		return diferencia;
	}
	
	private NumerarBean recorrerListaListro(List<SuministroNodoCabezalBean> lista, NumerarBean numerarBean){

		int totalLitros= 0;
		int numOp = 0;
		for(SuministroNodoCabezalBean sum: lista){
			totalLitros +=sum.getLitrosTemp15();
			if(sum.getOpEspecial() == 1){
				numOp += 1;
			}
		}
		numerarBean.setSumLitros(totalLitros);
		numerarBean.setNumOp(numOp);
		return numerarBean;
	}
	
	private List<SuministroNodoCabezalBean> checkLitrosSaltos(List<SuministroNodoCabezalBean> lista){
		SuministroNodoCabezalBean sumNodAnt = null;
		for(SuministroNodoCabezalBean sumNod : lista){
			Integer checkLitros = sumNod.getTotFinalTemp15().intValueExact()-sumNod.getTotInicialTemp15().intValueExact();
			sumNod.setCheckLitros((int) (sumNod.getLitrosTemp15()-checkLitros));
			if(sumNodAnt == null) {
				sumNod.setCheckSaltos(0);
			}else{
				sumNod.setCheckSaltos((sumNod.getNumSuministro()-sumNodAnt.getNumSuministro())-1);
			}
			sumNodAnt = sumNod;
		}
		return lista;
	}	
	
	private NumerarBean obtenerUltimoNivelFecha(NumerarBean numerarBeanTanques, List<NivelTanqueBean> list_ntf) {
		for(NivelTanqueBean ntf : list_ntf){
			numerarBeanTanques.setUltimoNivelFecha(ntf.getVolumenNeto().intValue());
			numerarBeanTanques.setFechaRegUltimo(ntf.getFechaNivelTanque());
		}
		return numerarBeanTanques;
	}
	
	private int getNt_f(int nt_f, NumerarBean numerarBeanTanques) {
		if(numerarBeanTanques.getUltimoNivelFecha() != null){
			nt_f += numerarBeanTanques.getUltimoNivelFecha();
		}
		return nt_f;
	}

	private int getNt_i(int nt_i, NumerarBean numerarBeanTanques) {
		if(numerarBeanTanques.getPrimerNivelFecha() != null) {
			nt_i += numerarBeanTanques.getPrimerNivelFecha();
		}
		return nt_i;
	}
	private NumerarBean compruebaSiExisteNivelFinal(FiltroNumerarWrapperBean filtroNumerarBean, NumerarBean numerarBeanTanques, SimpleDateFormat sdf) {
		boolean hayNivelFinal = false;
		LOGGER.info("FECHA FINAL NIVEL: "+ sdf.format(numerarBeanTanques.getFechaRegUltimo()));
		LOGGER.info("FECHA FINAL RANGO: "+ sdf.format(filtroNumerarBean.getFechaInicioHasta()));
		if(!sdf.format(numerarBeanTanques.getFechaRegUltimo()).equals(sdf.format(filtroNumerarBean.getFechaInicioHasta()))){
			hayNivelFinal = true;
			numerarBeanTanques.setUltimoNivelFecha(null);
			numerarBeanTanques.setFechaRegUltimo(null);
		}
		return numerarBeanTanques;
	}
	
	private NumerarBean compruebaSiExisteNivel(FiltroNumerarWrapperBean filtroNumerarBean, NumerarBean numerarBeanTanques, SimpleDateFormat sdf) {
		LOGGER.info("FECHA INICIAL NIVEL: "+ sdf.format(numerarBeanTanques.getFechaRegPrimer()));
		LOGGER.info("FECHA INICIAL RANGO: "+ sdf.format(filtroNumerarBean.getFechaInicioDesde()));
		if(!sdf.format(numerarBeanTanques.getFechaRegPrimer()).equals(sdf.format(filtroNumerarBean.getFechaInicioDesde()))){
			numerarBeanTanques.setPrimerNivelFecha(null);
			numerarBeanTanques.setFechaRegPrimer(null);
		}
		return numerarBeanTanques;
	}
	
	private NumerarBean volumenDesc(List<DescargaNodoConsolaBean> lista, NumerarBean numerarBean){
		int sumVolumen = 0;
		int numOp = 0;
		for(DescargaNodoConsolaBean desc : lista){
			desc.setDifLitros(desc.getVolumenNetoFin().intValue()-desc.getVolumenNetoInicio().intValue());
			sumVolumen += desc.getDifLitros();
			if(desc.getOpEspecial() == 1){
				numOp += 1;
			}
		}
		numerarBean.setVolumenDescargado(sumVolumen);
		numerarBean.setNumOp(numOp);
		return numerarBean;
	}
	
	private NumerarBean verificacionLista(NumerarBean numerarBeanTanques, List<DescargaNodoConsolaBean> lista) {
		if(lista.isEmpty()){
			numerarBeanTanques.setNumDescargas(0);
			numerarBeanTanques.setVolumenInicial(0);
			numerarBeanTanques.setVolumenFinal(0);
			numerarBeanTanques.setNumOp(0);
		}else{
			numerarBeanTanques.setNumDescargas(lista.size());
			numerarBeanTanques.setVolumenInicial(lista.get(0).getVolumenNetoInicio().intValue());
			numerarBeanTanques.setVolumenFinal(lista.get(lista.size()-1).getVolumenNetoFin().intValue());
		}
		return numerarBeanTanques;
	}
	
	private boolean compartenMismoDia(Date fecha1, Date fecha2) {
        LocalDate localDate1 = fecha1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = fecha2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return (localDate1.getDayOfMonth() == localDate2.getDayOfMonth()) &&
               (localDate1.getMonth() == localDate2.getMonth()) &&
               (localDate1.getYear() == localDate2.getYear());
    }
	
	private Double obtenerLitrosDescargaNodoNoProcesados(List<DescargaNodoConsolaBean> listaDescargasNodosNoProcesada ,Double litrosDescargaNodoNoProcesados, Date fechaInicioDesde) {
		
		for(DescargaNodoConsolaBean descargaNodoNoProcesada: listaDescargasNodosNoProcesada) {
			
			if(compartenMismoDia(descargaNodoNoProcesada.getFechaFinDescarga(),fechaInicioDesde)) {
			
				litrosDescargaNodoNoProcesados+=(descargaNodoNoProcesada.getVolumenNetoFin()-descargaNodoNoProcesada.getVolumenNetoInicio());	
			}
		}
		
		return litrosDescargaNodoNoProcesados;
	}
	
	private Float obtenerLitrosSuministroNodoNoProcesados(List<SuministroNodoCabezalBean> listaSuministrosNodoNoProcesados ,Float litrosSuministroNodoNoProcesados, Date fechaInicioDesde) {
		
		for(SuministroNodoCabezalBean suministroNodoNoProcesado:listaSuministrosNodoNoProcesados) {
			
			if(compartenMismoDia(suministroNodoNoProcesado.getFechaFinSuministro(),fechaInicioDesde)) {
				litrosSuministroNodoNoProcesados+=suministroNodoNoProcesado.getLitrosTemp15();
			}
		}
		
		return litrosSuministroNodoNoProcesados;
	}
}
