package com.example.linkgame;

import android.util.SparseArray;

import java.util.ArrayList;

public class FadeData {

    private static final int MAX_COUNT = 10;

    /**
     * 最大为十个图片集合
     *
     * @param count 获取几个图片(结果会自动变成2倍的)
     * @return
     */
    public static ArrayList<LinkItem> getData(int count) {
        Integer[] imgArr =
                new Integer[]{R.mipmap.card1, R.mipmap.card2, R.mipmap.card3, R.mipmap.card4, R.mipmap.card5,
                        R.mipmap.card6, R.mipmap.card7, R.mipmap.card8, R.mipmap.card9, R.mipmap.card10};
        Integer[] imgRanIndexArr = randomNumber(0, imgArr.length, count);
        assert imgRanIndexArr != null;
        ArrayList<LinkItem> resultList = new ArrayList<>();
        ArrayList<LinkItem> linkItemList = new ArrayList<>();
        for (int i = 0; i < imgRanIndexArr.length; i++) {
            LinkItem linkItem = new LinkItem();
            linkItem.setKey(i);
            linkItem.setResId(imgArr[imgRanIndexArr[i]]);
            linkItemList.add(linkItem);
            linkItemList.add(linkItem);
        }
        for (int i = 0; i < (MAX_COUNT - count) * 2; i++) {
            LinkItem linkItem = new LinkItem();
            linkItem.setKey(-1);
            linkItemList.add(linkItem);
        }
        Integer[] resultImgRanIndexArr = randomNumber(0, linkItemList.size(), linkItemList.size());
        assert resultImgRanIndexArr != null;
        for (Integer integer : resultImgRanIndexArr) {
            resultList.add(linkItemList.get(integer));
        }
        return resultList;
    }


    public static Integer[] randomNumber(int min, int max, int n) {

        if (n > (max - min + 1) || max < min) {
            return null;
        }

        Integer[] result = new Integer[n];

        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < count; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    /**
     * Game level
     */
    private static final int[] LEVEL_TOTAL_TIME = new int[]{60, 45};
    private static final int[] LEVEL_VIEW_TIME = new int[]{45, 60};
    private static final int[] LEVEL_CARD_NUM = new int[]{8, 10};
    private static SparseArray<GameLevel> gameLevelSparseArray = new SparseArray<>();

    public static void initGameLevels() {
        for (int i = 0; i < LEVEL_TOTAL_TIME.length; i++) {
            GameLevel gameLevel = new GameLevel();
            int level = i + 1;
            gameLevel.setLevel(level);
            gameLevel.setIntervalTime(1000);
            gameLevel.setViewTime(LEVEL_VIEW_TIME[i]);
            gameLevel.setTotalTime(LEVEL_TOTAL_TIME[i]);
            gameLevel.setCardNum(LEVEL_CARD_NUM[i]);
            if (gameLevelSparseArray.indexOfKey(level) < 0)
                gameLevelSparseArray.put(level, gameLevel);
        }
    }

    public static GameLevel getGameLevelByLevel(int level) {
        return gameLevelSparseArray.get(level);
    }

}
