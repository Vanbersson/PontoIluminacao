package iluminacao.com.pontoiluminacao.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import iluminacao.com.pontoiluminacao.EquipamentoAcaoQtdFragment;
import iluminacao.com.pontoiluminacao.R;
import iluminacao.com.pontoiluminacao.objetos.Bairro;
import iluminacao.com.pontoiluminacao.objetos.Equipamento;

public class EquipamentoAdapter extends BaseAdapter {

    private Activity act;

    private List<Equipamento> lista;
    private  Equipamento equipamento;

    private TextView tv_nome;
    private Button btn_delete;
    private ImageView img_acao;

    public EquipamentoAdapter(Activity act){

        this.act = act;
    }

    public void setLista(List<Equipamento> lista) {
        this.lista = lista;
    }

    public void clear(){
        lista.clear();
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        LayoutInflater inf = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.equipamento_adapter,viewGroup,false);

        equipamento = lista.get(position);

        tv_nome = v.findViewById(R.id.Idtv_adpter_nome);
        tv_nome.setText(equipamento.getNome());

        img_acao = v.findViewById(R.id.Idimg_adpter_acao);
        if(equipamento.getAcao() == EquipamentoAcaoQtdFragment.TIPO_ACAO_IN){
            img_acao.setBackground(act.getResources().getDrawable(R.drawable.ic_arrow_downward));
        }else{
            img_acao.setBackground(act.getResources().getDrawable(R.drawable.ic_arrow_upward));
        }

        btn_delete = v.findViewById(R.id.Idbtn_adpter_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista.remove(position);


                notifyDataSetChanged();
            }
        });

        return v;
    }
}
