package iluminacao.com.pontoiluminacao;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SairNovoPontoFragment extends DialogFragment {

    private static final String DIALOG_TAG = "SairPonto";
    private OnFragmentInteractionListener mListener;


    private Button btn_sair;
    private Button btn_cancelar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sair_novo_ponto_fragment, container, false);

        btn_sair = view.findViewById(R.id.Idbtn_ponto_sair);
        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(true);
                dismiss();
            }
        });
        btn_cancelar = view.findViewById(R.id.Idbtn_ponto_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


    public void abrir(FragmentManager fragmentManager){
        show(fragmentManager,DIALOG_TAG);

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
