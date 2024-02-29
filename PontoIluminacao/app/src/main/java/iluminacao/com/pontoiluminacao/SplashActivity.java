package iluminacao.com.pontoiluminacao;

import androidx.appcompat.app.AppCompatActivity;
import iluminacao.com.pontoiluminacao.objetos.Usuario;
import iluminacao.com.pontoiluminacao.sqlite.UsuarioSQLite;
import iluminacao.com.pontoiluminacao.util.NetworkUtil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class SplashActivity extends AppCompatActivity {

    private final String mURL_LOGIN = "https://www.gestaoip.com.br/ws/pontoiluminacao/api/user/login.php?";
    public static final String EXTRA_USUARIO ="usuario";

    private Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        verificaLogin();
    }

    private void verificaLogin(){
        UsuarioSQLite usuarioSQLite = new UsuarioSQLite(this);

        if(usuarioSQLite.getUsuario() != null){

            mUsuario = new Usuario();
            mUsuario = usuarioSQLite.getUsuario();

            new loginAsyncTask().execute(mUsuario);

        }else {
            startLogin();
        }

    }

    private void startLogin(){
        Intent it = new Intent(this,LoginActivity.class);
        startActivity(it);
        finishAffinity();
    }

    private void startApp(Usuario usuario){

        Intent it = new Intent(this,ActivityMapa.class);
        it.putExtra(EXTRA_USUARIO,usuario);
        startActivity(it);
        finishAffinity();

    }

    private void excluirUsuario(){

        UsuarioSQLite usuarioSQLite = new UsuarioSQLite(this);
        usuarioSQLite.excluir();

    }

    public class loginAsyncTask extends AsyncTask<Usuario,Void,String> {

        @Override
        protected String doInBackground(Usuario... usuarios) {

            String obj = "";
            try {

                HttpURLConnection conexao = NetworkUtil.getConexao(mURL_LOGIN+"nome="+ usuarios[0].getNome()+"&senha="+usuarios[0].getSenha(),"GET",false);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(inputStream);
                    conexao.disconnect();

                }

                return obj;

            } catch (Exception e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String obj) {

            try {

                Gson gson = new Gson();
                Usuario usuario = gson.fromJson(obj,Usuario.class);

                if(usuario.getStatus()){

                    startApp(usuario);

                    Toast.makeText(SplashActivity.this, "Bem vindo " + usuario.getNome_completo(), Toast.LENGTH_LONG).show();
                }else{

                    excluirUsuario();
                    startLogin();

                }

            }catch (Exception e){
                excluirUsuario();
                startLogin();
                Toast.makeText(SplashActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(obj);

        }

    }






}
