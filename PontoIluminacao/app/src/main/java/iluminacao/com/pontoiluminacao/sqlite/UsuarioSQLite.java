package iluminacao.com.pontoiluminacao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import iluminacao.com.pontoiluminacao.objetos.Usuario;

public class UsuarioSQLite {

    private  IluminacaoDBSQLite iluminacaoDBSQLite;
    private SQLiteDatabase db;

    public UsuarioSQLite(Context context){
        iluminacaoDBSQLite = new IluminacaoDBSQLite(context);
        db = iluminacaoDBSQLite.getWritableDatabase();
    }

    public void salvar(Usuario usuario){

        ContentValues cv = new ContentValues();

        cv.put(IluminacaoDBSQLite.USUA_NOME,usuario.getNome());
        cv.put(IluminacaoDBSQLite.USUA_SENHA,usuario.getSenha());

        db.insert(IluminacaoDBSQLite.TABELA_USUARIO,null,cv);

    }


    public void excluir(){

        db.delete(IluminacaoDBSQLite.TABELA_USUARIO,null,null);
    }

    public Usuario getUsuario(){
        Usuario usuario = null;

        String[] colunas = new String[]{IluminacaoDBSQLite.USUA_NOME,IluminacaoDBSQLite.USUA_SENHA};

        Cursor cursor = db.query(IluminacaoDBSQLite.TABELA_USUARIO,colunas,null,null,null,null,null);

        if(cursor.moveToNext()){

            usuario = new Usuario();
            usuario.setNome(cursor.getString(0));
            usuario.setSenha(cursor.getString(1));

        }

        return usuario;
    }

}
