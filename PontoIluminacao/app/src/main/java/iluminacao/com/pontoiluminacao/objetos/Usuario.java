package iluminacao.com.pontoiluminacao.objetos;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {

    private Integer codigo_usuario;
    private String nome_completo;
    private String nome;
    private String senha;
    private String chave_acesso;
    private Date datacadastro;
    private boolean status;

    public Integer getCodigo_usuario() {
        return codigo_usuario;
    }

    public void setCodigo_usuario(Integer codigo_usuario) {
        this.codigo_usuario = codigo_usuario;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getchave_acesso() {
        return chave_acesso;
    }

    public void setchave_acesso(String chave_acesso) {
        this.chave_acesso = chave_acesso;
    }

    public Date getDatacadastro() {
        return datacadastro;
    }

    public void setDatacadastro(Date datacadastro) {
        this.datacadastro = datacadastro;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
