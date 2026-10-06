package es.adif.tlg.bean;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public class DescargaTanqueBean {
 private DescargaNodoConsolaBean descargaNodoConsola; public DescargaNodoConsolaBean getDescargaNodoConsola(){return descargaNodoConsola;} public void setDescargaNodoConsola(DescargaNodoConsolaBean v){descargaNodoConsola=v;}
 private TanqueBean tanque; public TanqueBean getTanque(){return tanque;} public void setTanque(TanqueBean v){tanque=v;}
 private Double volumen15TanqueInicio; public Double getVolumen15TanqueInicio(){return volumen15TanqueInicio;} public void setVolumen15TanqueInicio(Double v){volumen15TanqueInicio=v;}
 private Double volumen15TanqueFin; public Double getVolumen15TanqueFin(){return volumen15TanqueFin;} public void setVolumen15TanqueFin(Double v){volumen15TanqueFin=v;}
}
