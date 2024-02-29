package iluminacao.com.pontoiluminacao;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import iluminacao.com.pontoiluminacao.adapters.EquipamentoAdapter;
import iluminacao.com.pontoiluminacao.objetos.Equipamento;
import iluminacao.com.pontoiluminacao.util.NetworkUtil;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class EquipamentoListaFragment extends Fragment {

    private final String mURL_Equi = "https://www.gestaoip.com.br/ws/pontoiluminacao/api/user/equipamentos.php?";

    public static final String TAG_FRAGMENT = "EquipamentoListaFragment";
    private OnFragmentInteractionListener mListener;

    private EditText edt_pesquisa;
    private ListView lv_equipamentos;
    private Button btn_avancar;

    private ArrayList<Equipamento> lista_Equi;

    private ArrayAdapter adapter;

    public static boolean voltar = true;

    public EquipamentoListaFragment(){
        PontoIluminacaoActivity.ponto_sair = true;
        new EquiAsyncTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.equipamento_lista_fragment, container, false);

        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_single_choice);

        btn_avancar = view.findViewById(R.id.idbtn_equi_avancar);
        btn_avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemselecionado();
            }
        });

        lv_equipamentos = view.findViewById(R.id.Idlv_equi_lista);
        lv_equipamentos.setAdapter(adapter);
        lv_equipamentos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        edt_pesquisa = view.findViewById(R.id.idedt_equi_pesquisa);
        edt_pesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;

    }

    private void itemselecionado(){

        if(lv_equipamentos.getCheckedItemPosition() >= 0){

            String obj = (String) adapter.getItem(lv_equipamentos.getCheckedItemPosition());

            for (int i = 0; i < lista_Equi.size(); i++){

                String item = lista_Equi.get(i).getCodigo() + " / " +lista_Equi.get(i).getNome();

                if(item.trim().equals(obj.trim())){

                    startAvancar(lista_Equi.get(i));
                }
            }

        }else{
            Toast.makeText(getContext(), "Item não selecionado!", Toast.LENGTH_SHORT).show();
        }

    }

    private void startAvancar(Equipamento equi){

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        EquipamentoAcaoQtdFragment fragment = new EquipamentoAcaoQtdFragment(equi);
        ft.addToBackStack(EquipamentoAcaoQtdFragment.TAG_FRAGMENT);
        ft.add(R.id.idPonto_Iluminacao,fragment,EquipamentoAcaoQtdFragment.TAG_FRAGMENT).commit();
    }

    private void carregaLista(ArrayList<Equipamento> lista){

        for(int i = 0; i < lista.size(); i++){
            adapter.add(""+lista.get(i).getCodigo()+" / "+lista.get(i).getNome());
        }

        adapter.notifyDataSetChanged();

    }

    public class EquiAsyncTask extends AsyncTask<Equipamento,Void,String> {


        @Override
        protected String doInBackground(Equipamento... equipamentos) {

            String obj = "";
            try {

                HttpURLConnection conexao = NetworkUtil.getConexao(mURL_Equi,"GET",false);

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
            super.onPostExecute(obj);

            try {

                Gson gson = new Gson();
                Equipamento[] lista = gson.fromJson(obj,Equipamento[].class);
                lista_Equi = new ArrayList<Equipamento>(Arrays.asList(lista));

                if(lista_Equi.get(0).getStatus()){

                    carregaLista(lista_Equi);

                }else{
                    // não possue equipamentos cadastrados
                }

            }catch (Exception e){
                Toast.makeText(getActivity(), "Erro de conexão", Toast.LENGTH_SHORT).show();
            }



        }
    }

    public void onButtonPressed(boolean result) {
        if (mListener != null) {
            mListener.onFragmentInteraction(result);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(boolean result);
    }

}
