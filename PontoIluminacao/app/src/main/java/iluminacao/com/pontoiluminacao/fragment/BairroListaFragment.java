package iluminacao.com.pontoiluminacao.fragment;


import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import iluminacao.com.pontoiluminacao.R;
import iluminacao.com.pontoiluminacao.objetos.Bairro;
import iluminacao.com.pontoiluminacao.util.NetworkUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class BairroListaFragment extends DialogFragment {
    private final String mURL_bairro = "https://www.gestaoip.com.br/ws/pontoiluminacao/api/user/bairros.php?distrito=";

    private OnFragmentInteractionListener mListener;

    private static final String DIALOG_TAG = "lista_bairro";

    private int codigo_Distrito = 0;

    private ArrayAdapter adapter;
    private ArrayList<Bairro> listabairro;

    private ListView list_bairro;
    private Button btn_cancelar;
    private Button btn_confirmar;
    private Bairro bairro;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bairro = new Bairro();
        bairro.setCodigo_Distrito(codigo_Distrito);
        new BairroAsyncTask().execute(bairro);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bairro_lista_fragment, container, false);

        btn_cancelar = view.findViewById(R.id.Idlista_bairro_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btn_confirmar = view.findViewById(R.id.Idlista_bairro_btn_confirmar);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaSelecao();

            }
        });

        list_bairro = view.findViewById(R.id.Idlista_bairro);
        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_single_choice);
        list_bairro.setAdapter(adapter);
        list_bairro.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        return  view;
    }

    public void abrir(FragmentManager fragmentManager,int distrito){
        codigo_Distrito = distrito;
        show(fragmentManager,DIALOG_TAG);

    }

    public class BairroAsyncTask extends AsyncTask<Bairro,Void,String> {

        @Override
        protected String doInBackground(Bairro... bairros) {

            String obj = "";
            try {

                HttpURLConnection conexao = NetworkUtil.getConexao(mURL_bairro+bairros[0].getCodigo_Distrito(),"GET",false);

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
                Bairro[] lista = gson.fromJson(obj,Bairro[].class);
                listabairro = new ArrayList<Bairro>(Arrays.asList(lista));

                if(listabairro.get(0).getStatus()){

                    for(int i = 0; i < listabairro.size(); i++){
                        adapter.add(listabairro.get(i).getBai_nome());
                    }

                    adapter.notifyDataSetChanged();

                }else{
                    // Distrito não possue bairros cadastrados
                }

            }catch (Exception e){
                Toast.makeText(getActivity(), "Erro de conexão", Toast.LENGTH_SHORT).show();
            }



        }
    }


    private void verificaSelecao(){

        if(list_bairro.getCheckedItemPosition() >= 0){
            int position = list_bairro.getCheckedItemPosition();

            onButtonPressed(listabairro.get(position));

            dismiss();
        }else{
            Toast.makeText(getContext(), "Bairro não selecionado!", Toast.LENGTH_SHORT).show();
        }

    }


    public void onButtonPressed(Bairro bairro) {
        if (mListener != null) {
            mListener.onFragmentInteraction(bairro);
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

        void onFragmentInteraction(Bairro bairro);
    }

}
