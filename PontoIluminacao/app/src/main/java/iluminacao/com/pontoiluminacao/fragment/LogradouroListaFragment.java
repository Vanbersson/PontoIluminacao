package iluminacao.com.pontoiluminacao.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import iluminacao.com.pontoiluminacao.R;
import iluminacao.com.pontoiluminacao.objetos.Bairro;
import iluminacao.com.pontoiluminacao.objetos.Logradouro;
import iluminacao.com.pontoiluminacao.util.NetworkUtil;

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
public class LogradouroListaFragment extends DialogFragment {

    private int codigo_bairro = 0;
    private int codigo_municipio = 0;

    private final String mURL_Log = "https://www.gestaoip.com.br/ws/pontoiluminacao/api/user/logradouros.php?";

    private OnFragmentInteractionListener mListener;

    private static final String DIALOG_TAG = "lista_logradouro";

    private ArrayAdapter adapter;
    private ArrayList<Logradouro> listalogradouro;

    private ListView list_logradouro;
    private Button btn_cancelar;
    private Button btn_confirmar;
    private Logradouro logradouro;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logradouro = new Logradouro();
        logradouro.setCodigo_bairro(codigo_bairro);
        logradouro.setCodigo_municipio(codigo_municipio);

        new LogAsyncTask().execute(logradouro);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.logradouro_lista_fragment, container, false);

        btn_cancelar = view.findViewById(R.id.Idlista_log_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btn_confirmar = view.findViewById(R.id.Idlista_log_btn_confirmar);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaSelecao();
            }
        });


        list_logradouro = view.findViewById(R.id.Idlista_logradouros);
        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_single_choice);
        list_logradouro.setAdapter(adapter);
        list_logradouro.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        return view;
    }

    public void abrir(FragmentManager fragmentManager,int bairro,int municipio){
        show(fragmentManager,DIALOG_TAG);
        codigo_bairro = bairro;
        codigo_municipio = municipio;

    }

    public class LogAsyncTask extends AsyncTask<Logradouro,Void,String> {

        @Override
        protected String doInBackground(Logradouro... logradouros) {

            String obj = "";
            try {

                HttpURLConnection conexao = NetworkUtil.getConexao(mURL_Log +"bairro="+ logradouros[0].getCodigo_bairro() +"&municipio="+logradouros[0].getCodigo_municipio(),"GET",false);

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
                Logradouro[] lista = gson.fromJson(obj,Logradouro[].class);
                listalogradouro = new ArrayList<Logradouro>(Arrays.asList(lista));

                if(listalogradouro.get(0).getStatus()){

                    for(int i = 0; i < listalogradouro.size(); i++){
                        adapter.add(listalogradouro.get(i).getNome());
                    }

                    adapter.notifyDataSetChanged();

                }else{
                    // não possue logradouros cadastrados
                }

            }catch (Exception e){
                Toast.makeText(getActivity(), "Erro de conexão", Toast.LENGTH_SHORT).show();
            }



        }
    }

    private void verificaSelecao(){

        if(list_logradouro.getCheckedItemPosition() >= 0){

            int position = list_logradouro.getCheckedItemPosition();

            onButtonPressed(listalogradouro.get(position));

            dismiss();
        }else{
            Toast.makeText(getContext(), "Logradouro não selecionado", Toast.LENGTH_SHORT).show();
        }

    }


    public void onButtonPressed(Logradouro logradouro) {
        if (mListener != null) {
            mListener.onFragmentInteraction(logradouro);
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

        void onFragmentInteraction(Logradouro logradouro);
    }


}
