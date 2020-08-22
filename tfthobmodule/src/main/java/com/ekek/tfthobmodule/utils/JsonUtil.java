package com.ekek.tfthobmodule.utils;


import android.content.Context;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.entity.CookingTable;
import com.ekek.tfthobmodule.entity.FishTable;
import com.ekek.tfthobmodule.entity.FruitTable;
import com.ekek.tfthobmodule.entity.LegumesAndCerealsTable;
import com.ekek.tfthobmodule.entity.MeatTable;
import com.ekek.tfthobmodule.entity.ShellfishTable;
import com.ekek.tfthobmodule.entity.VegetablesTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class JsonUtil {

    public static Observable<Boolean> parseCookingTableJson(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                LogUtil.d("Enter:: parseCookingTableJson");
                Type type = new TypeToken<List<CookingTable>>(){}.getType();
                Gson gson = new Gson();
                String content = context.getResources().getString(R.string.tfthobmodule_cooking_table_json);
                List<CookingTable> cookingTables = gson.fromJson(content, type);

                CataTFTHobApplication.getInstance().getMeatTables().clear();
                CataTFTHobApplication.getInstance().getLegumesAndCerealsTables().clear();
                CataTFTHobApplication.getInstance().getFruitTables().clear();
                CataTFTHobApplication.getInstance().getShellfishTables().clear();
                CataTFTHobApplication.getInstance().getVegetablesTables().clear();
                CataTFTHobApplication.getInstance().getFishTables().clear();

                for (CookingTable cookingTable : cookingTables) {
                    List<CookingTable.Item> items = cookingTable.getItems();
                    for (CookingTable.Item item : items) {
                        List<CookingTable.Item.CookDetail> cookDetails = item.getCookDetail();
                        List<String> cookDatas = new ArrayList<>();
                        for (CookingTable.Item.CookDetail cookDetail : cookDetails) {
                            String str = cookDetail.getCookTemp() + "-" + cookDetail.getCookTime();
                            cookDatas.add(str);

                        }
                        if (cookingTable.getType().equals("Meat")) {
                            MeatTable meatTable = new MeatTable(null,item.getName(),item.getWeight(),cookDatas);
                            CataTFTHobApplication.getInstance().getMeatTables().add(meatTable);
                            CataTFTHobApplication.getInstance().setMeatTablesDesciption(cookingTable.getDescription());
                        }else if (cookingTable.getType().equals("Legumes and Cereals")) {
                            LegumesAndCerealsTable legumesAndCerealsTable = new LegumesAndCerealsTable(null,item.getName(),item.getWeight(),cookDatas);
                            CataTFTHobApplication.getInstance().getLegumesAndCerealsTables().add(legumesAndCerealsTable);
                            CataTFTHobApplication.getInstance().setLegumesAndCerealsTablesDesciption(cookingTable.getDescription());
                        }else if (cookingTable.getType().equals("Fruit")) {
                            FruitTable fruitTable = new FruitTable(null,item.getName(),item.getWeight(),cookDatas);
                            CataTFTHobApplication.getInstance().getFruitTables().add(fruitTable);
                            CataTFTHobApplication.getInstance().setFruitTablesDesciption(cookingTable.getDescription());
                        }else if (cookingTable.getType().equals("Shellfish")) {
                            ShellfishTable shellfishTable = new ShellfishTable(null,item.getName(),item.getWeight(),cookDatas);
                            CataTFTHobApplication.getInstance().getShellfishTables().add(shellfishTable);
                            CataTFTHobApplication.getInstance().setShellfishTablesDesciption(cookingTable.getDescription());
                        }else if (cookingTable.getType().equals("Vegetables")) {
                            VegetablesTable vegetablesTable = new VegetablesTable(null,item.getName(),item.getWeight(),cookDatas);
                            CataTFTHobApplication.getInstance().getVegetablesTables().add(vegetablesTable);
                            CataTFTHobApplication.getInstance().setVegetablesTablesDesciption(cookingTable.getDescription());
                        }else if (cookingTable.getType().equals("Fish")) {
                            FishTable fishTable = new FishTable(null,item.getName(),item.getWeight(),cookDatas);
                            CataTFTHobApplication.getInstance().getFishTables().add(fishTable);
                            CataTFTHobApplication.getInstance().setFishTablesDesciption(cookingTable.getDescription());
                        }
                    }
                }

                emitter.onComplete();
            }
        });

    }
}
