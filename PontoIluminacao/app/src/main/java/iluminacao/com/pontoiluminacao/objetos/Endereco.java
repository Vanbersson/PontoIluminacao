package iluminacao.com.pontoiluminacao.objetos;

import java.io.Serializable;

public class Endereco implements Serializable {

    private int codigo_municipio;
    private String nome_municipio;
    private int codigo_bairro;
    private String nome_bairro;
    private int codigo_logradouro;
    private String nome_logradouro;

    public int getCodigo_municipio() {
        return codigo_municipio;
    }

    public void setCodigo_municipio(int codigo_municipio) {
        this.codigo_municipio = codigo_municipio;
    }

    public String getNome_municipio() {
        return nome_municipio;
    }

    public void setNome_municipio(String nome_municipio) {
        this.nome_municipio = nome_municipio;
    }

    public int getCodigo_bairro() {
        return codigo_bairro;
    }

    public void setCodigo_bairro(int codigo_bairro) {
        this.codigo_bairro = codigo_bairro;
    }

    public String getNome_bairro() {
        return nome_bairro;
    }

    public void setNome_bairro(String nome_bairro) {
        this.nome_bairro = nome_bairro;
    }

    public int getCodigo_logradouro() {
        return codigo_logradouro;
    }

    public void setCodigo_logradouro(int codigo_logradouro) {
        this.codigo_logradouro = codigo_logradouro;
    }

    public String getNome_logradouro() {
        return nome_logradouro;
    }

    public void setNome_logradouro(String nome_logradouro) {
        this.nome_logradouro = nome_logradouro;
    }
}
