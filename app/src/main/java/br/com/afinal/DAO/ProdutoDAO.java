package br.com.afinal.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.afinal.Helper.DBHelper;
import br.com.afinal.Model.Produto;

public class ProdutoDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ProdutoDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.write = dbHelper.getWritableDatabase();
        this.read = dbHelper.getReadableDatabase();
    }

    public void salvarProduto(Produto produto) {
        ContentValues cv = new ContentValues();
        cv.put("nome", produto.getNome());
        cv.put("estoque", produto.getEstoque());
        cv.put("valor", produto.getValor());

        try {
            long result = write.insert(DBHelper.TB_PRODUTO, null, cv);

            if (result != -1) {
                // Sucesso ao salvar
                Log.d("ProdutoDAO", "Produto salvo com sucesso. ID: " + result);
            } else {
                // Falha ao salvar
                Log.e("ProdutoDAO", "Erro ao salvar o produto. ID: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProdutoDAO", "Erro ao salvar o produto: " + e.getMessage());
        }
    }

    public void removerProduto(int produtoId) {
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(produtoId)};

        try {
            write.delete(DBHelper.TB_PRODUTO, whereClause, whereArgs);
            Log.d("ProdutoDAO", "Produto removido com sucesso. ID: " + produtoId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProdutoDAO", "Erro ao remover o produto: " + e.getMessage());
        }
    }


    public List<Produto> getListProdutos() {
        List<Produto> produtoList = new ArrayList<>();

        String sql = "SELECT * FROM " + DBHelper.TB_PRODUTO + ";";
        Cursor c = read.rawQuery(sql, null);

        while (c.moveToNext()) {
            int idColumnIndex = c.getColumnIndex("id");
            int nomeColumnIndex = c.getColumnIndex("nome");
            int estoqueColumnIndex = c.getColumnIndex("estoque");
            int valorColumnIndex = c.getColumnIndex("valor");

            // Verificar se as colunas existem
            if (idColumnIndex >= 0 && nomeColumnIndex >= 0 && estoqueColumnIndex >= 0 && valorColumnIndex >= 0) {
                int id = c.getInt(idColumnIndex);
                String nome = c.getString(nomeColumnIndex);
                int estoque = c.getInt(estoqueColumnIndex);
                double valor = c.getDouble(valorColumnIndex);

                Produto produto = new Produto();
                produto.setId(id);
                produto.setNome(nome);
                produto.setEstoque(estoque);
                produto.setValor(valor);

                produtoList.add(produto);
            }
        }

        c.close(); // Fechar o cursor para evitar memory leaks
        return produtoList;
    }

    public void atualizarProduto(Produto produto) {
        ContentValues cv = new ContentValues();
        cv.put("nome", produto.getNome());
        cv.put("estoque", produto.getEstoque());
        cv.put("valor", produto.getValor());

        String where = "id=?";
        String[] args = {String.valueOf(produto.getId())};

        try {
            write.update(DBHelper.TB_PRODUTO, cv, where, args);

        } catch (Exception e) {
            Log.i("Erro", "Erro ao atualizar" + e.getMessage());
        }
    }



}
