package br.com.afinal.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.tsuryo.swipeablerv.SwipeableRecyclerView;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import br.com.afinal.Adapter.AdapterProduto;
import br.com.afinal.DAO.ProdutoDAO;
import br.com.afinal.Model.Produto;
import br.com.afinal.R;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private AdapterProduto adapterProduto;
    private SwipeableRecyclerView rvProdutos;
    private ImageButton ibAdd;
    private ImageButton ibVerMais;
    private ProdutoDAO produtoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        produtoDAO = new ProdutoDAO(this);

        ibAdd = findViewById(R.id.ib_add);
        ibVerMais = findViewById(R.id.ib_ver_mais);
        rvProdutos = findViewById(R.id.rvProdutos);

        configRecyclerView();

        ouvinteCliques();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualizar a lista ao retomar a atividade
        configRecyclerView();
    }

    private void ouvinteCliques() {
        ibAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, FormularioProdutoActivity.class));
        });

        ibVerMais.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, ibVerMais);
            popupMenu.getMenuInflater().inflate(R.menu.menu_toolbar, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menu_sobre) {
                    startActivity(new Intent(this, EmpresaActivity.class));
                }
                return true;
            });

            popupMenu.show();
        });
    }

    private void configRecyclerView() {
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        List<Produto> produtos = produtoDAO.getListProdutos();
        adapterProduto = new AdapterProduto(produtos, this);
        rvProdutos.setAdapter(adapterProduto);

        rvProdutos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

                Produto produtoEditado = produtos.get(position);
                Intent intent = new Intent(MainActivity.this, FormularioProdutoActivity.class);
                intent.putExtra("produto", produtoEditado);
                startActivity(intent);
            }

            @Override
            public void onSwipedRight(int position) {
                // Remover produto do banco de dados
                Produto produtoRemovido = produtos.get(position);
                produtoDAO.removerProduto(produtoRemovido.getId());

                // Atualizar a lista no adaptador
                produtos.remove(position);
                adapterProduto.notifyItemRemoved(position);
            }
        });
    }

    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormularioProdutoActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }
}
