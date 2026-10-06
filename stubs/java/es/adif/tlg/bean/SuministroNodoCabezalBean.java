package es.adif.tlg.bean;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public class SuministroNodoCabezalBean {
 private SurtidorBean surtidor; public SurtidorBean getSurtidor(){return surtidor;} public void setSurtidor(SurtidorBean v){surtidor=v;}
 private Integer diferenciaTiempo; public Integer getDiferenciaTiempo(){return diferenciaTiempo;} public void setDiferenciaTiempo(Integer v){diferenciaTiempo=v;}
 private Date fechaInicioSuministro; public Date getFechaInicioSuministro(){return fechaInicioSuministro;} public void setFechaInicioSuministro(Date v){fechaInicioSuministro=v;}
 private Date fechaFinSuministro; public Date getFechaFinSuministro(){return fechaFinSuministro;} public void setFechaFinSuministro(Date v){fechaFinSuministro=v;}
 private Float litrosTemp15; public Float getLitrosTemp15(){return litrosTemp15;} public void setLitrosTemp15(Float v){litrosTemp15=v;}
 private Integer numSuministro; public Integer getNumSuministro(){return numSuministro;} public void setNumSuministro(Integer v){numSuministro=v;}
 private BigDecimal totInicialTemp15; public BigDecimal getTotInicialTemp15(){return totInicialTemp15;} public void setTotInicialTemp15(BigDecimal v){totInicialTemp15=v;}
 private BigDecimal totFinalTemp15; public BigDecimal getTotFinalTemp15(){return totFinalTemp15;} public void setTotFinalTemp15(BigDecimal v){totFinalTemp15=v;}
 private Integer checkLitros; public Integer getCheckLitros(){return checkLitros;} public void setCheckLitros(Integer v){checkLitros=v;}
 private Integer checkSaltos; public Integer getCheckSaltos(){return checkSaltos;} public void setCheckSaltos(Integer v){checkSaltos=v;}
 private Integer opEspecial; public Integer getOpEspecial(){return opEspecial;} public void setOpEspecial(Integer v){opEspecial=v;}
 private String idOperacion; public String getIdOperacion(){return idOperacion;} public void setIdOperacion(String v){idOperacion=v;}
}
