package iluminacao.com.pontoiluminacao;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import iluminacao.com.pontoiluminacao.objetos.Equipamento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class EquipamentoAcaoQtdFragment extends Fragment {

    public static final String TAG_FRAGMENT = "EquipamentoAcaoQtdFragment";

    private OnFragmentInteractionListener mListener;
    public static boolean voltar = true;

    public static final String TIPO_ACAO_IN = "INCLUSAO";
    public static final String TIPO_ACAO_EX = "EXCLUSAO";

    private int contador_qtd = 0;
    private Button btn_acao_add;
    private Button btn_acao_rem;


    private TextView tv_qtd;
    private Button btn_qtd_add;
    private Button btn_qtd_rem;

    private Button btn_confirmar;

    private Equipamento mEquipamento;

    private boolean sele_acao = false;

    public EquipamentoAcaoQtdFragment(Equipamento equipamento){
        this.mEquipamento = equipamento;
        PontoIluminacaoActivity.ponto_sair = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.equipamento_acao_qtd_fragment, container, false);

        btn_acao_add = view.findViewById(R.id.Idbtn_equi_acao_add);
        btn_acao_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionaAcao(TIPO_ACAO_IN);
            }
        });
        btn_acao_rem = view.findViewById(R.id.Idbtn_equi_acao_rem);
        btn_acao_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionaAcao(TIPO_ACAO_EX);
            }
        });

        tv_qtd = view.findViewById(R.id.Idtv_qtd);
        btn_qtd_add = view.findViewById(R.id.Idbtn_qtd_add);
        btn_qtd_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionaQuatidade(1,"+");
            }
        });
        btn_qtd_rem = view.findViewById(R.id.Idbtn_qtd_rem);
        btn_qtd_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionaQuatidade(1,"-");
            }
        });

        btn_confirmar = view.findViewById(R.id.Idbtn_confirmar);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               confirmaDados();
            }
        });

        return view;
    }

    private void confirmaDados(){

        if(validadados()){
            mEquipamento.setQuatidade(Integer.parseInt(tv_qtd.getText().toString()));

            onButtonPressed(mEquipamento);
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().popBackStack(EquipamentoListaFragment.TAG_FRAGMENT,1);
        }

    }

    private boolean validadados(){
        if(!sele_acao){
            Toast.makeText(getContext(), "Ação não selecionada!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.parseInt(tv_qtd.getText().toString()) == 0){
            Toast.makeText(getContext(), "Quantidade não informada!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void selecionaAcao(String tipo){

        switch (tipo){
            case TIPO_ACAO_IN:
                mEquipamento.setAcao(TIPO_ACAO_IN);
                btn_acao_add.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
                btn_acao_add.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_equi_acao_add));
                btn_acao_rem.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_equi_add_rem_not));
                btn_acao_rem.setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryText));

                break;
            case TIPO_ACAO_EX:
                mEquipamento.setAcao(TIPO_ACAO_EX);
                btn_acao_rem.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
                btn_acao_rem.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_equi_acao_rem));
                btn_acao_add.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_equi_add_rem_not));
                btn_acao_add.setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryText));

                break;
        }

        sele_acao = true;

    }

    private void selecionaQuatidade(int qtd,String tipo){

        if(tv_qtd.getText().toString().trim().equals("0")){

            switch (tipo){
                case "+":
                    contador_qtd = contador_qtd + qtd;
                    break;

            }

        }else  if(Integer.parseInt(tv_qtd.getText().toString()) >= 1 && Integer.parseInt(tv_qtd.getText().toString()) < 99){

            switch (tipo){
                case "+":
                    contador_qtd = contador_qtd + qtd;
                    break;
                case "-":
                    contador_qtd = contador_qtd - qtd;
                    break;

            }

        }else if(Integer.parseInt(tv_qtd.getText().toString()) == 99){
            switch (tipo){
                case "-":
                    contador_qtd = contador_qtd - qtd;
                    break;

            }
        }
        tv_qtd.setText(Integer.toString(contador_qtd));

    }

    public void onButtonPressed(Equipamento equipamento) {
        if (mListener != null) {
            mListener.onFragmentInteraction(equipamento);
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

        void onFragmentInteraction(Equipamento equipamento);
    }

}
