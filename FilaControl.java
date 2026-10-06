public class FilaControl {
    String nombre;
    int ntF, ntI;
    int numSum, numSumCab, numDes, numDesCon;
    double tlD, tlDPP, tlS, tlSPP;
    double difLitros;                    // CON signo: NT_F - (NT_I + TL_D + TL_D_PP - TL_S - TL_S_PP)
    double totIni, totFin;
    double etIni, etFin;
    // Desde CUB (fechaCub == null si no hay CUB)
    Date fechaCub;
    int ntICub;
    double etIniCub;
    double tlDCub, tlDPPCub, tlSCub, tlSPPCub, difCub;

    public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public int getNtF() {
    return ntF;
}

public void setNtF(int ntF) {
    this.ntF = ntF;
}

public int getNtI() {
    return ntI;
}

public void setNtI(int ntI) {
    this.ntI = ntI;
}

public int getNumSum() {
    return numSum;
}

public void setNumSum(int numSum) {
    this.numSum = numSum;
}

public int getNumSumCab() {
    return numSumCab;
}

public void setNumSumCab(int numSumCab) {
    this.numSumCab = numSumCab;
}

public int getNumDes() {
    return numDes;
}

public void setNumDes(int numDes) {
    this.numDes = numDes;
}

public int getNumDesCon() {
    return numDesCon;
}

public void setNumDesCon(int numDesCon) {
    this.numDesCon = numDesCon;
}

public double getTlD() {
    return tlD;
}

public void setTlD(double tlD) {
    this.tlD = tlD;
}

public double getTlDPP() {
    return tlDPP;
}

public void setTlDPP(double tlDPP) {
    this.tlDPP = tlDPP;
}

public double getTlS() {
    return tlS;
}

public void setTlS(double tlS) {
    this.tlS = tlS;
}

public double getTlSPP() {
    return tlSPP;
}

public void setTlSPP(double tlSPP) {
    this.tlSPP = tlSPP;
}

public double getDifLitros() {
    return difLitros;
}

public void setDifLitros(double difLitros) {
    this.difLitros = difLitros;
}

public double getTotIni() {
    return totIni;
}

public void setTotIni(double totIni) {
    this.totIni = totIni;
}

public double getTotFin() {
    return totFin;
}

public void setTotFin(double totFin) {
    this.totFin = totFin;
}

public double getEtIni() {
    return etIni;
}

public void setEtIni(double etIni) {
    this.etIni = etIni;
}

public double getEtFin() {
    return etFin;
}

public void setEtFin(double etFin) {
    this.etFin = etFin;
}

public Date getFechaCub() {
    return fechaCub;
}

public void setFechaCub(Date fechaCub) {
    this.fechaCub = fechaCub;
}

public int getNtICub() {
    return ntICub;
}

public void setNtICub(int ntICub) {
    this.ntICub = ntICub;
}

public double getEtIniCub() {
    return etIniCub;
}

public void setEtIniCub(double etIniCub) {
    this.etIniCub = etIniCub;
}

public double getTlDCub() {
    return tlDCub;
}

public void setTlDCub(double tlDCub) {
    this.tlDCub = tlDCub;
}

public double getTlDPPCub() {
    return tlDPPCub;
}

public void setTlDPPCub(double tlDPPCub) {
    this.tlDPPCub = tlDPPCub;
}

public double getTlSCub() {
    return tlSCub;
}

public void setTlSCub(double tlSCub) {
    this.tlSCub = tlSCub;
}

public double getTlSPPCub() {
    return tlSPPCub;
}

public void setTlSPPCub(double tlSPPCub) {
    this.tlSPPCub = tlSPPCub;
}

public double getDifCub() {
    return difCub;
}

public void setDifCub(double difCub) {
    this.difCub = difCub;
}
}
