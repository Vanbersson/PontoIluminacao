package iluminacao.com.pontoiluminacao.objetos;

import java.io.Serializable;

public class Bairro implements Serializable {

    private int bai_codigo;
    private String bai_nome;
    private int codigo_Distrito;
    private boolean status;

    public int getBai_codigo() {
        return bai_codigo;
    }

    public void setBai_codigo(int bai_codigo) {
        this.bai_codigo = bai_codigo;
    }

    public String getBai_nome() {
        return bai_nome;
    }

    public void setBai_nome(String bai_nome) {
        this.bai_nome = bai_nome;
    }

    public boolean isStatus() {
        return status;
    }

    public int getCodigo_Distrito() {
        return codigo_Distrito;
    }

    public void setCodigo_Distrito(int codigo_Distrito) {
        this.codigo_Distrito = codigo_Distrito;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
