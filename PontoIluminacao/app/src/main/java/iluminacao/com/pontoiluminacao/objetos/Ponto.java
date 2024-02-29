package iluminacao.com.pontoiluminacao.objetos;

import java.io.Serializable;
import java.util.Date;

public class Ponto implements Serializable {

    private int codigo;
    private int codigo_logradouro;
    private String barramento;
    private String medidor;
    private Double po_gps_s; //latitude
    private Double po_gps_w;  // longitude
    private String po_datacadastrado;
    private String acao; // ALTERACAO, INCLUSAO
    private String barramento_proximo;
    private String medidor_proximo;
    private String data_substituicao;
    private boolean status;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo_logradouro() {
        return codigo_logradouro;
    }

    public void setCodigo_logradouro(int codigo_logradouro) {
        this.codigo_logradouro = codigo_logradouro;
    }

    public String getBarramento() {
        return barramento;
    }

    public void setBarramento(String barramento) {
        this.barramento = barramento;
    }

    public String getMedidor() {
        return medidor;
    }

    public void setMedidor(String medidor) {
        this.medidor = medidor;
    }

    public Double getPo_gps_s() {
        return po_gps_s;
    }

    public void setPo_gps_s(Double po_gps_s) {
        this.po_gps_s = po_gps_s;
    }

    public Double getPo_gps_w() {
        return po_gps_w;
    }

    public void setPo_gps_w(Double po_gps_w) {
        this.po_gps_w = po_gps_w;
    }

    public String getPo_datacadastrado() {
        return po_datacadastrado;
    }

    public void setPo_datacadastrado(String po_datacadastrado) {
        this.po_datacadastrado = po_datacadastrado;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getBarramento_proximo() {
        return barramento_proximo;
    }

    public void setBarramento_proximo(String barramento_proximo) {
        this.barramento_proximo = barramento_proximo;
    }

    public String getMedidor_proximo() {
        return medidor_proximo;
    }

    public void setMedidor_proximo(String medidor_proximo) {
        this.medidor_proximo = medidor_proximo;
    }

    public String getData_substituicao() {
        return data_substituicao;
    }

    public void setData_substituicao(String data_substituicao) {
        this.data_substituicao = data_substituicao;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
