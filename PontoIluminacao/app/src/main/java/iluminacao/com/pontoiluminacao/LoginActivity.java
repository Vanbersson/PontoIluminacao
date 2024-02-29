package iluminacao.com.pontoiluminacao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import iluminacao.com.pontoiluminacao.fragment.EsqueceuSenhaFragment;
import iluminacao.com.pontoiluminacao.objetos.Usuario;
import iluminacao.com.pontoiluminacao.sqlite.UsuarioSQLite;
import iluminacao.com.pontoiluminacao.util.NetworkUtil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class LoginActivity extends AppCompatActivity {

    private final String mURL_LOGIN = "https://www.gestaoip.com.br/ws/pontoiluminacao/api/user/login.php?";
    public static final String EXTRA_USUARIO ="usuario";

    private EditText mEdt_login;
    private EditText mEdt_senha;
    private Button mBtn_login;
    private Button mBtn_esqueceusenha;
    private ProgressBar mProgressBar;
    private SwitchCompat mSwitch_login_manter;

    private Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mEdt_login = findViewById(R.id.idedt_login_login);
        mEdt_senha = findViewById(R.id.idedt_login_senha);
        mBtn_login = findViewById(R.id.idbtn_login);
        mProgressBar = findViewById(R.id.progressBar);
        mSwitch_login_manter = findViewById(R.id.idsw_login_manter);

        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });

        mBtn_esqueceusenha = findViewById(R.id.idbtn_login_esqueceusenha);
        mBtn_esqueceusenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esqueceuSenha();
            }
        });

    }

    private void salvarLoginLocal(Usuario usuario){

        UsuarioSQLite usuarioSQLite = new UsuarioSQLite(LoginActivity.this);

        usuarioSQLite.salvar(usuario);

    }

    private void login(){

        if(validaLogin()){
            dadosLogin();
            remotoLogin();
        }

    }

    private boolean validaLogin(){

        if( mEdt_login.getText().toString().trim().equals("")){
            Toast.makeText(this,"Login inválido", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mEdt_senha.getText().toString().trim().equals("")) {
            Toast.makeText(this,"Senha inválida", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void dadosLogin(){

        mUsuario = new Usuario();

        mUsuario.setNome(mEdt_login.getText().toString().trim());
        mUsuario.setSenha(mEdt_senha.getText().toString().trim());

    }

    private void remotoLogin(){
        new loginAsyncTask().execute(mUsuario);
    }

    private void esqueceuSenha(){
        EsqueceuSenhaFragment fragmentLogin = new EsqueceuSenhaFragment();
        fragmentLogin.abrir(getSupportFragmentManager());

    }

    public class loginAsyncTask extends AsyncTask<Usuario,Void,String> {

        int contador = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setMax(100);

            Thread t = new Thread(){
                public void run(){
                    while(contador < mProgressBar.getMax()){
                        try {
                            sleep(50);
                            contador  += 2;
                            mProgressBar.setProgress(contador);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }; t.start();

        }

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
            mProgressBar.setVisibility(View.GONE);

            try {

                Gson gson = new Gson();
                Usuario usuario = gson.fromJson(obj,Usuario.class);

                if(usuario.getStatus()){

                    startApp(usuario);

                    if(mSwitch_login_manter.isChecked()){
                        salvarLoginLocal(mUsuario);
                    }

                    Toast.makeText(LoginActivity.this, "Bem vindo " + usuario.getNome_completo(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Usuário ou senha inválida!", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Toast.makeText(LoginActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(obj);

        }
    }

    private void startApp(Usuario usuario){

        Intent it = new Intent(this,ActivityMapa.class);
        it.putExtra(EXTRA_USUARIO,usuario);
        startActivity(it);

        finishAffinity();

    }







}
