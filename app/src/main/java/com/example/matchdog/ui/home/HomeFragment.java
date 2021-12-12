package com.example.matchdog.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.matchdog.R;
import com.example.matchdog.database.DogFavoriteRoom;
import com.example.matchdog.database.UsuarioRoom;
import com.example.matchdog.database.dao.DogFavoriteDao;
import com.example.matchdog.databinding.FragmentHomeBinding;
import com.example.matchdog.models.Breed;
import com.example.matchdog.models.Dog;
import com.example.matchdog.models.DogFavorite;
import com.example.matchdog.models.Usuario;
import com.example.matchdog.retrofit.RetrofitConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView textViewNameDog;
    private FragmentHomeBinding binding;
    private Handler handler = new Handler();
    private View root = null ;
    private LinearLayout btnNext ;
    private LinearLayout btnFavorite ;
    private LinearLayout btnLike ;
    private DogFavoriteDao dogFavoriteDao ;
    private List<Dog> dogList ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        btnNext = root.findViewById(R.id.btnNext);
        btnFavorite = root.findViewById(R.id.btnFavorite);
        btnLike = root.findViewById(R.id.btnLike);

        textViewNameDog = root.findViewById(R.id.textViewNameDog);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDogByApi();
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Favoritado com sucesso!", Toast.LENGTH_SHORT).show();
                toSaveBd();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Você gostou deste post!", Toast.LENGTH_SHORT).show();
            }
        });

        getDogByApi();

        startBdFavorite();

        return root;
    }

    private void toSaveBd() {
        // persistir no banco de dados
        DogFavorite dogFavorite = new DogFavorite();
        dogFavorite.setIdImage(dogList.get(0).getId());
        dogFavorite.setName(dogList.get(0).getBreeds().get(0).getName());
        dogFavorite.setDesc(dogList.get(0).getBreeds().get(0).getTemperament());
        dogFavorite.setYears(dogList.get(0).getBreeds().get(0).getLifeSpan());
        dogFavorite.setUrlImage(dogList.get(0).getUrl());
        dogFavorite.setIdUser(getIdUserSessao());

        dogFavoriteDao.salvar(dogFavorite);
    }

    private String getIdUserSessao() {
        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences("dadosUser", Context.MODE_PRIVATE);
        String idUser =  sharedPreferences.getString("isUser","1");

        return idUser;
    }

    public void startBdFavorite(){
        dogFavoriteDao = Room.databaseBuilder(getContext().getApplicationContext(),
                DogFavoriteRoom.class, "dogfavorite.db")
                .allowMainThreadQueries()
                .build()
                .getRoomDogFavoriteDao();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getDogByApi(){
        Call<List<Dog>> call = new RetrofitConfig().getCEPService().getApiAleatoryDog();
        call.enqueue(new Callback<List<Dog>>() {
            @Override
            public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                dogList = response.body();
                String urlImage = dogList.get(0).getUrl();
                loadImg(urlImage);

                if(dogList.get(0).getBreeds().size() == 0) {
                    textViewNameDog.setText("Raça indefinida");
                }else{
                    textViewNameDog.setText(dogList.get(0).getBreeds().get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<Dog>> call, Throwable throwable) {
                Log.e("DogService   ", "Erro ao buscar o dog:" + throwable.getMessage());

            }
        });
    }

    public void loadImg(String urlImage){
        new Thread(){
            public void run() {
                Bitmap img = null;

                URL url = null;
                try {
                    url = new URL(urlImage);
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
                        ImageView imageView = (ImageView) root.findViewById(R.id.imageViewDog);
                        imageView.setImageBitmap(imgAux);
                    }
                });
            }
        }.start();
    }// loadimg
}