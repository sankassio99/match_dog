package com.example.matchdog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AdapterListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CardViewDog> itens;
    private Handler handler = new Handler();

    public AdapterListView(Context context, List<CardViewDog> itens) {
        //Itens do listview
        this.itens = itens;
        //Objeto responsável por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public CardViewDog getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (view == null) {
            //infla o layout para podermos pegar as views
            view = mInflater.inflate(R.layout.card_view_dog, null);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new ItemSuporte();
            itemHolder.textTitle = ((TextView) view.findViewById(R.id.textTitle));
            itemHolder.textDesc = ((TextView) view.findViewById(R.id.textDesc));
            itemHolder.textYear = ((TextView) view.findViewById(R.id.textYear));
            itemHolder.imgIcon = ((ImageView) view.findViewById(R.id.imagemViewDog));

            //define os itens na view;
            view.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (ItemSuporte) view.getTag();
        }

        //pega os dados da lista
        //e define os valores nos itens.
        CardViewDog item = itens.get(i);
        itemHolder.textTitle.setText(item.getTitle());
        itemHolder.textDesc.setText(item.getDesc());
        itemHolder.textYear.setText(item.getYears());
        new Thread(){
            public void run() {
                Bitmap img = null;

                URL url = null;
                try {
                    url = new URL(item.getUrlImage());
                    HttpsURLConnection conexao = (HttpsURLConnection) url.openConnection();
                    InputStream inputStream = conexao.getInputStream();
                    img = BitmapFactory.decodeStream(inputStream);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Bitmap imgAux = img;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        ImageView imageView = (ImageView) root.findViewById(R.id.imageViewDog);
//                        imageView.setImageBitmap(imgAux);
                        itemHolder.imgIcon.setImageBitmap(imgAux);
                    }
                });
            }
        }.start();

        //retorna a view com as informações
        return view;
    }


    private class ItemSuporte {

        ImageView imgIcon;
        TextView textTitle, textDesc, textYear;
    }
}
