package es.adif.tlg.bean;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public class NumerarBean {
 private SurtidorBean surtidor; public SurtidorBean getSurtidor(){return surtidor;} public void setSurtidor(SurtidorBean v){surtidor=v;}
 private TanqueBean tanque; public TanqueBean getTanque(){return tanque;} public void setTanque(TanqueBean v){tanque=v;}
 private Date fechaRegistro; public Date getFechaRegistro(){return fechaRegistro;} public void setFechaRegistro(Date v){fechaRegistro=v;}
 private Date fechaRegUltimo; public Date getFechaRegUltimo(){return fechaRegUltimo;} public void setFechaRegUltimo(Date v){fechaRegUltimo=v;}
 private Date fechaRegPrimer; public Date getFechaRegPrimer(){return fechaRegPrimer;} public void setFechaRegPrimer(Date v){fechaRegPrimer=v;}
 private Integer numUltimoSuministro; public Integer getNumUltimoSuministro(){return numUltimoSuministro;} public void setNumUltimoSuministro(Integer v){numUltimoSuministro=v;}
 private Integer numPrimerSuministro; public Integer getNumPrimerSuministro(){return numPrimerSuministro;} public void setNumPrimerSuministro(Integer v){numPrimerSuministro=v;}
 private Integer diferencia; public Integer getDiferencia(){return diferencia;} public void setDiferencia(Integer v){diferencia=v;}
 private Integer faltan; public Integer getFaltan(){return faltan;} public void setFaltan(Integer v){faltan=v;}
 private Integer numSuministros; public Integer getNumSuministros(){return numSuministros;} public void setNumSuministros(Integer v){numSuministros=v;}
 private Integer difLitros; public Integer getDifLitros(){return difLitros;} public void setDifLitros(Integer v){difLitros=v;}
 private Integer difTotal; public Integer getDifTotal(){return difTotal;} public void setDifTotal(Integer v){difTotal=v;}
 private Integer sumLitros; public Integer getSumLitros(){return sumLitros;} public void setSumLitros(Integer v){sumLitros=v;}
 private Integer totalInicial; public Integer getTotalInicial(){return totalInicial;} public void setTotalInicial(Integer v){totalInicial=v;}
 private Integer totalFinal; public Integer getTotalFinal(){return totalFinal;} public void setTotalFinal(Integer v){totalFinal=v;}
 private Integer numOp; public Integer getNumOp(){return numOp;} public void setNumOp(Integer v){numOp=v;}
 private Integer ultimoNivelRegistrado; public Integer getUltimoNivelRegistrado(){return ultimoNivelRegistrado;} public void setUltimoNivelRegistrado(Integer v){ultimoNivelRegistrado=v;}
 private Integer ultimoNivelFecha; public Integer getUltimoNivelFecha(){return ultimoNivelFecha;} public void setUltimoNivelFecha(Integer v){ultimoNivelFecha=v;}
 private Integer primerNivelFecha; public Integer getPrimerNivelFecha(){return primerNivelFecha;} public void setPrimerNivelFecha(Integer v){primerNivelFecha=v;}
 private Integer volumenDescargado; public Integer getVolumenDescargado(){return volumenDescargado;} public void setVolumenDescargado(Integer v){volumenDescargado=v;}
 private Integer numDescargas; public Integer getNumDescargas(){return numDescargas;} public void setNumDescargas(Integer v){numDescargas=v;}
 private Integer volumenInicial; public Integer getVolumenInicial(){return volumenInicial;} public void setVolumenInicial(Integer v){volumenInicial=v;}
 private Integer volumenFinal; public Integer getVolumenFinal(){return volumenFinal;} public void setVolumenFinal(Integer v){volumenFinal=v;}
}
