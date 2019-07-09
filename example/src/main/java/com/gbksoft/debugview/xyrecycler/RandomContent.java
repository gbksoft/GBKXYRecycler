package com.gbksoft.debugview.xyrecycler;

import android.annotation.SuppressLint;

import com.gbksoft.debugview.xyrecycler.template.models.CellModel;
import com.gbksoft.debugview.xyrecycler.template.models.ColumnHeaderModel;
import com.gbksoft.debugview.xyrecycler.template.models.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomContent {

    private static final int COLUMN_COUNT = 40;
    private static final int ROW_COUNT = 100;

    @SuppressLint("DefaultLocale")
    public static List<ColumnHeaderModel> getRandomColumnHeaderList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        for (int i = 0; i < COLUMN_COUNT; i++) {
            String title = String.format("column %d", i);
            int random = new Random().nextInt();

            if (random % 5 == 0 || random % 3 == 0 || random == i) {
                title = String.format("large column %d", i);
            }
            list.add(new ColumnHeaderModel(title));
        }

        return list;
    }

    @SuppressLint("DefaultLocale")
    public static List<RowHeaderModel> getRandomRowHeaderList() {
        List<RowHeaderModel> list = new ArrayList<>();

        for (int i = 0; i < ROW_COUNT; i++) {
            list.add(new RowHeaderModel(String.format("row %d", i)));
        }

        return list;
    }

    @SuppressLint("DefaultLocale")
    public static List<List<CellModel>> getRandomCellList() {
        List<List<CellModel>> list = new ArrayList<>();

        for (int i = 0; i < ROW_COUNT; i++) {
            List<CellModel> cellList = new ArrayList<>();

            for (int j = 0; j < COLUMN_COUNT; j++) {
                StringBuilder stringBuilder = new StringBuilder(String.format("cell %d : %d", j, i));
                int random = new Random().nextInt();

                if (random % 5 == 0 || random % 3 == 0 || random == j) {
                    stringBuilder.append(String.format(" random content: %s", getRandomValue()));
                }

                cellList.add(new CellModel(stringBuilder.toString()));
            }

            list.add(cellList);
        }

        return list;
    }

    private static String getRandomValue() {
        Random random = new Random();
        String[] strings = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < random.nextInt(strings.length); i++) {
            stringBuilder.append(strings[i]);
        }

        return stringBuilder.toString();
    }
}
