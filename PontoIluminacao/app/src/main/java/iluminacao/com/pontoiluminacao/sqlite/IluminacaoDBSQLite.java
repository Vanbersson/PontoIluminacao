package iluminacao.com.pontoiluminacao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IluminacaoDBSQLite extends SQLiteOpenHelper {

    private static final String NOME_DB = "iluminacao";
    private static  final int VERSAO_DB = 1;

    public static final String TABELA_USUARIO = "usuario";
    public static final String USUA_NOME = "nome";
    public static final String USUA_SENHA = "senha";


    public IluminacaoDBSQLite(Context context){
        super(context,NOME_DB,null,VERSAO_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+TABELA_USUARIO+"("
                +USUA_NOME+" text not null,"
                +USUA_SENHA+" text not null );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if(oldVersion != newVersion){
                db.execSQL("drop table if exists "+TABELA_USUARIO);
            }

    }

}
