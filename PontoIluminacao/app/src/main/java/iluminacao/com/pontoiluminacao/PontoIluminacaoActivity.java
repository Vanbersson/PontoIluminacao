package iluminacao.com.pontoiluminacao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import iluminacao.com.pontoiluminacao.adapters.EquipamentoAdapter;
import iluminacao.com.pontoiluminacao.fragment.BairroListaFragment;
import iluminacao.com.pontoiluminacao.fragment.LogradouroListaFragment;
import iluminacao.com.pontoiluminacao.fragment.MapaConfiFragment;
import iluminacao.com.pontoiluminacao.objetos.Bairro;
import iluminacao.com.pontoiluminacao.objetos.Equipamento;
import iluminacao.com.pontoiluminacao.objetos.Logradouro;
import iluminacao.com.pontoiluminacao.objetos.Ponto;
import iluminacao.com.pontoiluminacao.util.MaskEditUtil;
import iluminacao.com.pontoiluminacao.util.NetworkUtil;

import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PontoIluminacaoActivity extends AppCompatActivity implements
                                    MapaConfiFragment.OnFragmentInteractionListener,
                                    BairroListaFragment.OnFragmentInteractionListener,
                                    LogradouroListaFragment.OnFragmentInteractionListener,
                                    EquipamentoAcaoQtdFragment.OnFragmentInteractionListener,
                                    EquipamentoListaFragment.OnFragmentInteractionListener,
                                    SairNovoPontoFragment.OnFragmentInteractionListener{


    private final String mURL_PONTO = "https://www.gestaoip.com.br/ws/pontoiluminacao/api/user/salvarponto.php?";
    private final String mURL_EQUI = "https://www.gestaoip.com.br/ws/pontoiluminacao/api/user/salvarpontoequi.php?";

    private EditText edt_datasub;
    private Button btn_bairro;
    private Button btn_logradouro;

    private EditText edt_Lat;
    private EditText edt_Long;

    private EditText edt_barramento;
    private EditText edt_medidor;
    private EditText edt_barramentoprox;
    private EditText edt_medidorprox;


    private Button btn_gps;
    private RadioButton rb_alteracao;
    private RadioButton rb_inclusao;
    private Button btn_equipamentos;
    private Button btn_salvar;

    private ProgressBar progressBar_cicle;

    private Ponto mPonto;
    private int codigo_distrito = 1;
    private int codigo_municipio = 1;

    private Bairro mBairro;
    private Logradouro mLogradouro;
    private Location mLocation;
    private Equipamento mEquipamento;



    private ListView lv_equipamentos;
    private List<Equipamento> lista_equipamentos;
    private EquipamentoAdapter adapter;
    private int total_lista_equi = 0;

    public static boolean ponto_sair = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_iluminacao);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edt_datasub = findViewById(R.id.idponto_edt_data);
        edt_datasub.addTextChangedListener(MaskEditUtil.mask(edt_datasub,MaskEditUtil.FORMAT_DATE));
        edt_datasub.setText(dateToString(new Date()));

        btn_bairro = findViewById(R.id.Idponto_btn_Bairro);
        btn_logradouro = findViewById(R.id.Idponto_btn_Logradouro);

        edt_barramento = findViewById(R.id.Idponto_edt_Barramento);
        edt_medidor = findViewById(R.id.Idponto_edt_Medidor);
        edt_barramentoprox = findViewById(R.id.Idponto_edt_BarramentoProx);
        edt_medidorprox = findViewById(R.id.Idponto_edt_MedidorProx);

        edt_Lat = findViewById(R.id.Idponto_edt_gpsLat);
        edt_Long = findViewById(R.id.Idponto_edt_gpsLong);
        btn_gps = findViewById(R.id.Idponto_btn_GPS);

        rb_alteracao = findViewById(R.id.Idponto_rb_alteracao);
        rb_inclusao = findViewById(R.id.Idponto_rb_inclusao);

        btn_equipamentos = findViewById(R.id.Idponto_btn_equipamentos);
        lv_equipamentos = findViewById(R.id.Idponto_lista_item);

        btn_salvar = findViewById(R.id.Idponto_btn_salvar);
        progressBar_cicle = findViewById(R.id.progressBar_cicle);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();

            }
        });

        btn_bairro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListaBairro();
            }
        });

        btn_logradouro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startListaLogradouros();

            }
        });

        rb_alteracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_inclusao.setChecked(false);

            }
        });

        rb_inclusao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_alteracao.setChecked(false);

            }
        });

        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragmentGPS();
            }
        });

        btn_equipamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEquipamento();
            }
        });

        adapter = new EquipamentoAdapter(this);
        lista_equipamentos = new ArrayList<Equipamento>();



    }

    @Override
    public void onBackPressed() {

        if(ponto_sair == false){

            SairNovoPontoFragment fragment = new SairNovoPontoFragment();
            fragment.abrir(getSupportFragmentManager());

        }else if(EquipamentoListaFragment.voltar) {
            ponto_sair = false;
            super.onBackPressed();
        }else{
            super.onBackPressed();
        }

    }

    private void startListaBairro(){

        if(mLogradouro != null){
            btn_logradouro.setText("Logradouros");
            mLogradouro = null;
        }

        btn_bairro.setText("Bairros");
        mBairro = null;

        BairroListaFragment fragment = new BairroListaFragment();
        fragment.abrir(getSupportFragmentManager(),codigo_distrito);
    }

    private void startListaLogradouros(){

        if(mBairro == null){
            Toast.makeText(this, "Bairro não selecionado", Toast.LENGTH_SHORT).show();
        }else{
            btn_logradouro.setText("Logradouros");
            mLogradouro = null;
            LogradouroListaFragment fragment = new LogradouroListaFragment();
            fragment.abrir(getSupportFragmentManager(),mBairro.getBai_codigo(),codigo_municipio);
        }


    }

    private void startFragmentGPS(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.addToBackStack(null);
        ft.replace(R.id.idPonto_Iluminacao,MapaConfiFragment.getInstancia(),null).commit();

    }

    private void startEquipamento(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        EquipamentoListaFragment fragment = new EquipamentoListaFragment();
        ft.addToBackStack(EquipamentoListaFragment.TAG_FRAGMENT);
        ft.replace(R.id.idPonto_Iluminacao,fragment,EquipamentoListaFragment.TAG_FRAGMENT).commit();
    }

    private String dateToString(Date data){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String data_string = dateFormat.format(data);

        return data_string;

    }

    private String convertDate(Date data_string){

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(data_string);

        return date;

    }

    private String convertDateString(String data_string){

        String data;
        String[] array =  new String[3];
              array = edt_datasub.getText().toString().split("/");

        data = array[2] +"-"+array[1]+"-"+array[0];

        return  data;
    }

    private void salvar(){
        if(validaDados()){
            dadosPonto();
            new SalvarPonto().execute(mPonto);
        }
    }

    private boolean validaDados(){

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setLenient(false);
            Date data = simpleDateFormat.parse(edt_datasub.getText().toString());

        } catch (ParseException e) {
            Toast.makeText(this, "Data inválida!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }

        if(edt_datasub.getText().toString().trim().equals("")){
            Toast.makeText(this, "Data inválida!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mBairro == null){
            Toast.makeText(this, "Bairro inválido!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mLogradouro == null){
            Toast.makeText(this, "Logradouro inválido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(edt_barramento.getText().toString().trim().equals("")){
            Toast.makeText(this, "Barramento inválido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(edt_Lat.getText().toString().trim().equals("")){
            Toast.makeText(this, "Localização não informada!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edt_Long.getText().toString().trim().equals("")){
            Toast.makeText(this, "Localização não informada!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(lista_equipamentos.size() == 0){
            Toast.makeText(this, "Equipamentos não selecionado!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void dadosPonto(){

        mPonto = new Ponto();

        mPonto.setPo_datacadastrado(convertDate(new Date()));
        mPonto.setData_substituicao(convertDateString(edt_datasub.getText().toString()));
        mPonto.setCodigo_logradouro(mLogradouro.getCodigo());
        mPonto.setBarramento(edt_barramento.getText().toString().trim());
        mPonto.setMedidor(edt_medidor.getText().toString().trim());
        mPonto.setBarramento_proximo(edt_barramentoprox.getText().toString().trim());
        mPonto.setMedidor_proximo(edt_medidorprox.getText().toString().trim());
        mPonto.setPo_gps_s(Double.parseDouble(edt_Lat.getText().toString().trim()));
        mPonto.setPo_gps_w(Double.parseDouble(edt_Long.getText().toString().trim()));

        if(rb_inclusao.isChecked()){
            mPonto.setAcao("INCLUSAO");
        }
        if(rb_alteracao.isChecked()){
            mPonto.setAcao("ALTERACAO");
        }

    }

    private void salvarEquipamentos(){

        new SalvarEqui().execute(lista_equipamentos.get(total_lista_equi));

    }

    class SalvarPonto extends AsyncTask<Ponto , Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_cicle.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Ponto... pontos) {
            String obj = "";

            try {

                HttpURLConnection conexao = NetworkUtil.getConexao(mURL_PONTO
                        +"codigo_logradouro=" + pontos[0].getCodigo_logradouro()
                        +"&medidor="+ pontos[0].getMedidor()
                        +"&barramento="+pontos[0].getBarramento()
                        +"&po_gps_s="+pontos[0].getPo_gps_s()
                        +"&po_gps_w="+pontos[0].getPo_gps_w()
                        +"&datacadastrado="+  pontos[0].getPo_datacadastrado()
                        +"&acao=" + pontos[0].getAcao()
                        +"&barramento_proximo="+pontos[0].getBarramento_proximo()
                        +"&medidor_proximo="+pontos[0].getMedidor_proximo()
                        +"&data_substituicao="+ pontos[0].getData_substituicao(),"GET",false);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(inputStream);
                    conexao.disconnect();

                }

            } catch (Exception e) {
                e.getMessage();
            }


            return obj;
        }

        @Override
        protected void onPostExecute(String obj) {
            super.onPostExecute(obj);

            try {

                Gson gson = new Gson();
                Ponto ponto = gson.fromJson(obj,Ponto.class);

                if(ponto.getStatus()){

                   mPonto.setCodigo(ponto.getCodigo());

                   salvarEquipamentos();

                }else {
                    progressBar_cicle.setVisibility(View.GONE);
                   Toast.makeText(PontoIluminacaoActivity.this, "Não foi possível salvar!", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                progressBar_cicle.setVisibility(View.GONE);
                Toast.makeText(PontoIluminacaoActivity.this, "Erro de conexão!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    class SalvarEqui extends AsyncTask<Equipamento , Void, String>{

        @Override
        protected String doInBackground(Equipamento... equipamentos) {

            String obj = "";

            try {

                HttpURLConnection conexao = NetworkUtil.getConexao(mURL_EQUI
                        +"codigo_po_ponto="+mPonto.getCodigo()
                        +"&codigo_po_equipamento="+equipamentos[0].getCodigo()
                        +"&quantidade_po="+equipamentos[0].getQuatidade()
                        +"&acao_po="+equipamentos[0].getAcao(),"GET",false);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(inputStream);
                    conexao.disconnect();

                }

            } catch (Exception e) {
                e.getMessage();
            }

            return obj;
        }

        @Override
        protected void onPostExecute(String obj) {
            super.onPostExecute(obj);

            try {
                Gson gson = new Gson();
                Equipamento equi = gson.fromJson(obj,Equipamento.class);

                if(equi.getStatus()){

                    if(total_lista_equi+1 < lista_equipamentos.size()){
                        total_lista_equi += 1;
                        salvarEquipamentos();
                    }else {
                        progressBar_cicle.setVisibility(View.GONE);
                        Toast.makeText(PontoIluminacaoActivity.this, "Sucesso!", Toast.LENGTH_SHORT).show();
                        limpaDados();
                    }

                }else {
                    progressBar_cicle.setVisibility(View.GONE);
                    Toast.makeText(PontoIluminacaoActivity.this, "Não foi possível salvar!", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                progressBar_cicle.setVisibility(View.GONE);
                Toast.makeText(PontoIluminacaoActivity.this, "Erro de conexão!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void limpaDados(){

        edt_datasub.setText("");
        edt_datasub.setText(dateToString(new Date()));
        edt_datasub.requestFocus();

        mBairro = null;
        btn_bairro.setText("Bairros");
        mLogradouro = null;
        btn_logradouro.setText("Logradouros");

        edt_barramento.setText("");
        edt_medidor.setText("");
        edt_barramentoprox.setText("");
        edt_medidorprox.setText("");

        edt_Lat.setText("");
        edt_Long.setText("");
        mLocation = null;

        mEquipamento = null;
        lista_equipamentos.clear();
        lv_equipamentos.setVisibility(View.GONE);
        adapter.clear();
        adapter.notifyDataSetChanged();

        ponto_sair = false;


    }

    private void carregarListaEquipamento(Equipamento equipamento){

        lista_equipamentos.add(equipamento);

        adapter.setLista(lista_equipamentos);

        lv_equipamentos.setVisibility(View.VISIBLE);
        lv_equipamentos.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onFragmentInteraction(Bairro bairro) {
            mBairro = bairro;
            btn_bairro.setText(mBairro.getBai_nome());
    }

    @Override
    public void onFragmentInteraction(Logradouro logradouro) {
        mLogradouro = logradouro;
        btn_logradouro.setText(mLogradouro.getNome());
    }

    @Override
    public void onFragmentInteraction(Location location) {
        mLocation = location;
        String lat = Double.toString(location.getLatitude()).trim();
        String lon = Double.toString(location.getLongitude()).trim();

        if(lat.length() > 12){
            edt_Lat.setText(lat.substring(0,11));
        }else{
            edt_Lat.setText(lat);
        }
        if(lon.length() > 12){
            edt_Long.setText(lon.substring(0,11));
        }else{
            edt_Long.setText(lon);
        }

        ponto_sair = true;

    }

    @Override
    public void onFragmentInteraction(Equipamento equipamento) {

        mEquipamento = equipamento;
        carregarListaEquipamento(equipamento);
    }

    @Override
    public void onFragmentInteraction(boolean result) {
        ponto_sair = result;
        onBackPressed();
    }

}
