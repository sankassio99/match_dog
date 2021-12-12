package com.example.matchdog.ui.favorite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.matchdog.AdapterListView;
import com.example.matchdog.CardViewDog;
import com.example.matchdog.R;
import com.example.matchdog.database.DogFavoriteRoom;
import com.example.matchdog.database.dao.DogFavoriteDao;
import com.example.matchdog.databinding.FragmentFavoriteBinding;
import com.example.matchdog.models.Dog;
import com.example.matchdog.models.DogFavorite;
import com.example.matchdog.models.Usuario;
import com.example.matchdog.retrofit.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {

    private FavoriteViewModel favoriteViewModel;
    private FragmentFavoriteBinding binding;
    private ListView listViewFavorites;
    private ArrayList<CardViewDog> listFavorites ;
    private AdapterListView adapter ;
    private DogFavoriteDao dogFavoriteDao ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoriteViewModel =
                new ViewModelProvider(this).get(FavoriteViewModel.class);

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewFavorites = root.findViewById(R.id.listViewFavorites);

        startBdFavorite();
        feedList();
        feedAdapter();


        return root;
    }

    private void feedList() {
        listFavorites = new ArrayList<>();

        // buscar favortos do usuario logado
        List<DogFavorite> dogsFavorites = dogFavoriteDao.todosByUser(getIdUserSessao());

        for(int i=0; i < dogsFavorites.size(); i++){

            CardViewDog cardViewDog = new CardViewDog();
            cardViewDog.setTitle(dogsFavorites.get(i).getName());
            cardViewDog.setDesc(dogsFavorites.get(i).getDesc());
            cardViewDog.setYears(dogsFavorites.get(i).getYears());
            cardViewDog.setUrlImage(dogsFavorites.get(i).getUrlImage());

            listFavorites.add(cardViewDog);
        }

    }//preencherLista

    private String getIdUserSessao() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dadosUser", Context.MODE_PRIVATE);
        String idUser =  sharedPreferences.getString("isUser","1");

        return idUser;
    }

    private void feedAdapter() {
        adapter = new AdapterListView(getContext(),listFavorites);
        listViewFavorites.setAdapter(adapter);
    }

    public void getDogByApi(String idDog){
        Call<Dog> call = new RetrofitConfig().getCEPService().getApiDogById(idDog);
        call.enqueue(new Callback<Dog>() {
            @Override
            public void onResponse(Call<Dog> call, Response<Dog> response) {
                Dog dog = response.body();
                String bread = dog.getBreeds().get(0).getName();
                Toast.makeText(getContext(), "dogResponse:" +bread, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Dog> call, Throwable throwable) {
                Log.e("DogService   ", "Erro ao buscar o dog:" + throwable.getMessage());

            }
        });
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
}