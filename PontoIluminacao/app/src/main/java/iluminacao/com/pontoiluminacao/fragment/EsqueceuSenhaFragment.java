package iluminacao.com.pontoiluminacao.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import iluminacao.com.pontoiluminacao.R;

public class EsqueceuSenhaFragment extends DialogFragment {

    private static final String DIALOG_TAG = "esqueceu_senha";

    private Button btn_cancelar;
    private Button btn_ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.esqueceu_senha_fragment, container, false);


        btn_ok = view.findViewById(R.id.IdEsqueceu_senha_btn);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fecha o Dialog
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void abrir(FragmentManager fragmentManager){
        show(fragmentManager,DIALOG_TAG);
    }



}
