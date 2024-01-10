package br.com.afinal.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.afinal.DAO.ProdutoDAO;
import br.com.afinal.Model.Produto;
import br.com.afinal.R;

public class FormularioProdutoActivity extends AppCompatActivity {

    private EditText edit_Produto;
    private EditText edit_estoque;
    private EditText edit_valor;
    private ProdutoDAO produtoDAO;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_produto);

        produtoDAO = new ProdutoDAO(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            produto = (Produto) bundle.getSerializable("produto");
            editaProduto();
        }

        edit_Produto = findViewById(R.id.edit_Produto);
        edit_estoque = findViewById(R.id.edit_estoque);
        edit_valor = findViewById(R.id.edit_valor);
        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.imageButton_voltar).setOnClickListener(view -> finish());
    }
    private void editaProduto() {
        if (edit_Produto != null && edit_estoque != null && edit_valor != null && produto != null) {
            String nome = produto.getNome();
            String estoque = String.valueOf(produto.getEstoque());
            String valor = String.valueOf(produto.getValor());

            edit_Produto.setText(nome != null ? nome : "");
            edit_estoque.setText(estoque != null ? estoque : "");
            edit_valor.setText(valor != null ? valor : "");
        }
    }


    public void salvarProduto(View view) {
        String nome = edit_Produto.getText().toString().trim();
        String quantidade = edit_estoque.getText().toString().trim();
        String valor = edit_valor.getText().toString().trim();

        if (nome.isEmpty()) {
            edit_Produto.setError("Informe o nome do produto.");
            edit_Produto.requestFocus();
            return;
        }

        if (quantidade.isEmpty()) {
            edit_estoque.setError("Digite a quantidade em estoque.");
            edit_estoque.requestFocus();
            return;
        }

        int qtd;
        try {
            qtd = Integer.parseInt(quantidade);
        } catch (NumberFormatException e) {
            edit_estoque.setError("Digite uma quantidade válida.");
            edit_estoque.requestFocus();
            return;
        }

        if (valor.isEmpty()) {
            edit_valor.setError("Digite um valor para o produto.");
            edit_valor.requestFocus();
            return;
        }

        double valorProduto;
        try {
            valorProduto = Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            edit_valor.setError("Digite um valor válido para o produto.");
            edit_valor.requestFocus();
            return;
        }

        if (produto == null) produto = new Produto();
        produto.setNome(nome);
        produto.setEstoque(qtd);
        produto.setValor(valorProduto);

        // Salvar produto no banco de dados
        if (produto.getId() != 0) {
            produtoDAO.atualizarProduto(produto);
        } else {
            produtoDAO.salvarProduto(produto);
        }

        // Limpar os campos após a inserção
        //limparCampos();

        // Exibir mensagem de sucesso
        Toast.makeText(this, "Produto salvo com sucesso.", Toast.LENGTH_SHORT).show();

        finish();
    }

    private void limparCampos() {
        edit_Produto.getText().clear();
        edit_estoque.getText().clear();
        edit_valor.getText().clear();
    }
}