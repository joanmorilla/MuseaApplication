package com.example.museaapplication.Classes.ViewModels;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Likes;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Dominio.Work;
import com.example.museaapplication.Classes.Json.AuxMuseo;
import com.example.museaapplication.Classes.Json.InfoValue;
import com.example.museaapplication.Classes.Json.LikesValue;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.Json.WorksValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.TimeClass;
import com.example.museaapplication.ui.ExpositionFragment;
import com.example.museaapplication.ui.MuseoFragment;
import com.example.museaapplication.ui.MuseuActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedViewModel extends ViewModel {

    private Museo curMuseum;
    private MutableLiveData<Museo> myMuseum = new MutableLiveData<>();
    private MutableLiveData<Exhibition> curExposition = new MutableLiveData<>();
    private MutableLiveData<Work> curWork = new MutableLiveData<>();

    private Fragment mMuseoFragment;
    private Fragment mExpositionFragment;
    private Fragment mCommentsFragment;

    private Likes[] favourites;
    private Fragment active;

    private FragmentManager fm;


    public Museo getCurMuseo() {
        return curMuseum;
    }

    public LiveData<Museo> getMuseum() {
        return myMuseum;
    }
    public void setMyMuseum(Museo m) {
        myMuseum.postValue(m);
    }
    public void setCurMuseum(Museo m){
        curMuseum = m;
    }

    public LiveData<Exhibition> getCurExposition() {
        return curExposition;
    }

    public void setCurExposition(Exhibition curExposition) {
        this.curExposition.postValue(curExposition);
    }

    public LiveData<Work> getCurWork() {return curWork; }

    public void setCurWork(Work curWork) {
        this.curWork.postValue(curWork);
    }

    public Fragment getmMuseoFragment() {
        return mMuseoFragment;
    }

    public void setmMuseoFragment(Fragment mMuseoFragment) {
        if (this.mMuseoFragment == null)
            this.mMuseoFragment = mMuseoFragment;
    }

    public Fragment getmExpositionFragment() {
        return mExpositionFragment;
    }

    public void setmExpositionFragment(Fragment mExpositionFragment) {
        if (this.mExpositionFragment == null)
            this.mExpositionFragment = mExpositionFragment;
    }

    public Fragment getActive() {
        if (active == null) active = mMuseoFragment;
        return active;
    }

    public void setActive(Fragment active) {
        this.active = active;
    }

    public Fragment getmCommentsFragment() {
        return mCommentsFragment;
    }

    public void setmCommentsFragment(Fragment mCommentsFragment) {
        if (this.mCommentsFragment == null)
            this.mCommentsFragment = mCommentsFragment;
    }

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        if (this.fm == null)
            this.fm = fm;
    }

    public void loadMuseum(String idMuseo){
        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseum(idMuseo);
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                Log.e("Code", "" + response.body().getMuseum().getName());
                AuxMuseo aux = response.body().getMuseum();
                Museo museum = new Museo();
                Call<LikesValue> callLikes = RetrofitClient.getInstance().getMyApi().getFavMuseums("RaulPes");
                callLikes.enqueue(new Callback<LikesValue>() {
                    @Override
                    public void onResponse(Call<LikesValue> call, Response<LikesValue> response) {
                        for (Likes l : response.body().getLikesList()){
                            if (l.getArtworkId().equals(museum.get_id())) {
                                museum.setLiked(true);
                                return;
                            }
                        }
                        museum.setLiked(false);
                    }

                    @Override
                    public void onFailure(Call<LikesValue> call, Throwable t) {

                    }
                });
                museum.set_id(aux.get_id());
                museum.setName(aux.getName());
                museum.setImage(aux.getImage());
                museum.setCountry(aux.getCountry());
                museum.setCity(aux.getCity());
                museum.setRestrictions(aux.getRestrictions());
                museum.setDescriptions(aux.getDescriptions());

                String nameM = museum.getName();
                String cityM = museum.getCity();

                //
                Call<InfoValue> callInfo = RetrofitClient.getInstance().getMyApi().getInfo(nameM, cityM);
                callInfo.enqueue(new Callback<InfoValue>() {
                    @Override
                    public void onResponse(@NotNull Call<InfoValue> call, @NotNull Response<InfoValue> response) {
                        InfoValue info = response.body();
                        if (info != null) {
                            museum.setCovidInformation(info.getInfo());
                            museum.setOpeningHour(parseOpeningHour(museum.getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                        }
                    }
                    @Override
                    public void onFailure(Call<InfoValue> call, Throwable t) {
                        Log.e("TAG1Info", t.getLocalizedMessage());
                        Log.e("TAG2Info", t.getMessage());

                        t.printStackTrace();
                    }
                });

                for (Exhibition e : aux.getExhibitions()){
                    museum.addExhibition(e);
                    // cache works
                    Call<WorksValue> call2 = RetrofitClient.getInstance().getMyApi().getExhibition(museum.get_id(), e.get_id());
                    call2.enqueue(new Callback<WorksValue>() {
                        @Override
                        public void onResponse(Call<WorksValue> call, Response<WorksValue> response) {
                            WorksValue exh = response.body();
                            if(exh != null)
                                for (Work w : exh.getExposition().getWorks()){
                                    w.setLoved(APIRequests.getInstance().checkLikes(w.get_id()));
                                    e.addWork(w);
                                    //getCommentsOfWork(w);
                                }
                            setMyMuseum(museum);
                            //e.addWorks(exh.getExposition().getWorks());
                        }

                        @Override
                        public void onFailure(Call<WorksValue> call, Throwable t) {
                            Log.e("TAG1Works", t.getLocalizedMessage());
                            Log.e("TAG2Works", t.getMessage());
                            t.printStackTrace();
                        }
                    });
                }
                setMyMuseum(museum);
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });
    }
    public void reloadMuseum(String idMuseo){

        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseum(idMuseo);
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                AuxMuseo aux = response.body().getMuseum();
                Museo museum = MuseuActivity.curMuseum;
                Call<LikesValue> callLikes = RetrofitClient.getInstance().getMyApi().getFavMuseums("RaulPes");
                callLikes.enqueue(new Callback<LikesValue>() {
                    @Override
                    public void onResponse(Call<LikesValue> call, Response<LikesValue> response) {
                        for (Likes l : response.body().getLikesList()){
                            if (l.getArtworkId().equals(museum.get_id())) {
                                museum.setLiked(true);
                                return;
                            }
                        }
                        museum.setLiked(false);
                    }

                    @Override
                    public void onFailure(Call<LikesValue> call, Throwable t) {

                    }
                });
                museum.set_id(aux.get_id());
                museum.setName(aux.getName());
                museum.setImage(aux.getImage());
                museum.setCountry(aux.getCountry());
                museum.setCity(aux.getCity());
                museum.setRestrictions(aux.getRestrictions());
                museum.setDescriptions(aux.getDescriptions());

                String nameM = museum.getName();
                String cityM = museum.getCity();

                //
                Call<InfoValue> callInfo = RetrofitClient.getInstance().getMyApi().getInfo(nameM, cityM);
                callInfo.enqueue(new Callback<InfoValue>() {
                    @Override
                    public void onResponse(@NotNull Call<InfoValue> call, @NotNull Response<InfoValue> response) {
                        InfoValue info = response.body();
                        if (info != null) {
                            museum.setCovidInformation(info.getInfo());
                            museum.setOpeningHour(parseOpeningHour(museum.getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                        }
                    }
                    @Override
                    public void onFailure(Call<InfoValue> call, Throwable t) {
                        Log.e("TAG1Info", t.getLocalizedMessage());
                        Log.e("TAG2Info", t.getMessage());

                        t.printStackTrace();
                    }
                });
                museum.setExhibitionObjects(Arrays.asList(aux.getExhibitions()));
                for (Exhibition e : aux.getExhibitions()){
                    // cache works
                    Call<WorksValue> call2 = RetrofitClient.getInstance().getMyApi().getExhibition(museum.get_id(), e.get_id());
                    call2.enqueue(new Callback<WorksValue>() {
                        @Override
                        public void onResponse(Call<WorksValue> call, Response<WorksValue> response) {
                            WorksValue exh = response.body();
                            if(exh != null)
                                for (Work w : exh.getExposition().getWorks()){
                                    w.setLoved(APIRequests.getInstance().checkLikes(w.get_id()));
                                    e.addWork(w);
                                    //getCommentsOfWork(w);
                                }
                            setMyMuseum(museum);
                            //e.addWorks(exh.getExposition().getWorks());
                        }

                        @Override
                        public void onFailure(Call<WorksValue> call, Throwable t) {
                            Log.e("TAG1Works", t.getLocalizedMessage());
                            Log.e("TAG2Works", t.getMessage());
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });
    }
    public void likeWork(String _id){
        Call<Void> call = RetrofitClient.getInstance().getMyApi().likeWork("RaulPes", _id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    public void likeMuseum(String _id){
        Call<Void> call = RetrofitClient.getInstance().getMyApi().favMuseum("RaulPes", _id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    private int parseOpeningHour(String time){
        if (time.contains("Closed")) return -1;
        int index = time.indexOf(':');
        String myTime = time.substring(index).replace(": ", "").replace(" ", ""); // Now we have 10:30AM-8:00PM
        String[] hours = myTime.split("–");
        String temp = hours[0].replace("AM", ""); // 10:30
        String[] h = temp.split(":"); // [10, 30]
        return Integer.parseInt(h[0]);
    }
}
