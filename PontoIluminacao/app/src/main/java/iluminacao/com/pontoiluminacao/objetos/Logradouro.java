package iluminacao.com.pontoiluminacao.objetos;

import java.io.Serializable;

public class Logradouro implements Serializable {

    private int codigo;
    private String nome;
    private int codigo_bairro;
    private int codigo_municipio;
    private boolean status;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo_bairro() {
        return codigo_bairro;
    }

    public void setCodigo_bairro(int codigo_bairro) {
        this.codigo_bairro = codigo_bairro;
    }

    public int getCodigo_municipio() {
        return codigo_municipio;
    }

    public void setCodigo_municipio(int codigo_municipio) {
        this.codigo_municipio = codigo_municipio;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
