package com.foo.garosero.myUtil;

import com.foo.garosero.R;
import com.foo.garosero.data.TreeTipData;

import java.util.HashMap;

public class TreeTipHelper {
    public static String STRING_ginkgo = "ginkgo";
    public static String STRING_pine = "pine";
    public static String STRING_zelkova = "zelkova";
    public static String STRING_cherry = "cherry";
    public static String STRING_poplar = "poplar";
    public static String STRING_apricot = "apricot";

    public static HashMap<String, TreeTipData> TreeHashMap = new HashMap<String, TreeTipData>();

    TreeTipData zelkova = new TreeTipData(
            "느티나무 관리 TIP",
            "느티나무의 유래?",
            "느티나무는 어떤 나무일까요?",
            "우리나라 거의 모든 지역에서 무성하게 자라요. 주로 마을 어귀에 위치하여 시원한 나무그늘을 만들어줘서 정자나무로 많이 이용해요.",
            "느티나무 특징",
            "느티나무를 잘 키우기 위해선 어떻게 해야할까요?",
            "산기슭이나 골짜기 또는 마을 부근의 흙이 깊고 그늘진 땅에서 잘 자라요 느티나무는 내한성이 강한 수종으로 특별하게 동절기 월동작업을 진행하지 않아도 돼요. 그러나 어린 느티나무의 경우 동해 피해나 병충해 피해를 입을 수 있기 때문에 월동작업과 잠복소 설치작업을 진행하는 것이 좋아요 또한 가뭄이나 외부 환경요인으로 수분공급이 적절하게 이뤄지지 않을 경우 스트레스나 피해를 입을 수 있어 수분관리가 중요해요",
            R.drawable.tree_lv3_zelkova,
            R.color.banner_color_bluegreen
    );

    TreeTipData cherry = new TreeTipData(
            "왕벚나무 관리 TIP",
            "왕벚나무란?",
            "왕벚나무는 어떤 나무일까요?",
            "봄에 눈이 부실 듯 피어나는 화려한 꽃이 아름다운 왕벚나무는 대표적인 봄나무입니다. 전국 곳곳에 가로수, 공원수 등으로 식재되어 있어 봄꽃놀이를 하러 가기 좋아요.",
            "왕벚나무 특징",
            "왕벚나무를 잘 키우기 위해선 어떻게 해야할까요?",
            "왕벚나무는 양지바르고 비옥한 땅을 좋아하며 우리나라 전역에서 재배가 가능해요. 하지만 각종 해충이 많이 발생하는 편이기 때문에 적당한 살충제로 관리가 필요해요.",
            R.drawable.tree_lv3_cherry,
            R.color.banner_color_whitepink
    );

    TreeTipData pine = new TreeTipData(
            "소나무 관리 TIP",
            "소나무의 유래?",
            "소나무는 어떤 나무일까요?",
            "소나무라는 이름은 ‘으뜸’이라는 ‘수리’라는 말이 변한 우리말 ‘솔’에서 유래되었어요.",
            "소나무 특징",
            "소나무를 잘 키우기 위해선 어떻게 해야할까요?",
            "소나무는 성장이 더딘 편이고, 손이 많이 가요 이런 점이 오히려 관리만 잘하면 간판이 가려지거나 하는 불편함을 막을 수 있어 다시금 가로수로 많이 쓰이고 있어요 또한 미세먼지 절감에 아주 탁월한데, 이는 바늘로 되어있는 침엽수 덕분이에요",
            R.drawable.tree_lv3_pine,
            R.color.banner_color_blue
    );

    TreeTipData apricot = new TreeTipData(
            "살구나무 관리 TIP",
            "살구나무란?",
            "살구나무는 어떤 나무일까요?",
            "살구는 '살(솔)고'라고 표기했던 순수한 우리말에서 유래되었어요. 옛날부터 우리 주변 야산에서 흔히 볼 수 있는 나무였다고 합니다. 살구에서 나오는 살구씨는 약재로도 쓰인다고 해요.",
            "살구나무 특징",
            "살구나무를 잘 키우기 위해선 어떻게 해야할까요?",
            "살구나무는 한국, 중국 등 온대지방 북반구 온도에서 잘 자라요. 하지만 건조한 환경에는 약한 편이라 주의를 기울여주세요. 살구나무는 꽃이 아름다워서 조경수로 많이 사용돼요.",
            R.drawable.tree_lv3_apricot,
            R.color.banner_color_pink
    );

    TreeTipData poplar = new TreeTipData(
            "이팝나무 관리 TIP",
            "이팝나무의 유래?",
            "이팝나무는 어떤 나무일까요?",
            "나무 꽃이 밥알(이밥)을 닮았다고 하여 이팝나무라고 불러요",
            "이팝나무 특징",
            "이팝나무를 잘 키우기 위해선 어떻게 해야할까요?",
            "공해에 강하여 가로수로 많이 사용되고, 정원이나 학교에 많이 심어요 습한 곳에서도 잘 자라고 꽃이 아름다워 가로수나 정원수로 많이 사용되지만 제법 손이 많이 가요 가지가 튼튼해 보이지만 비바람에 약해 나뭇결을 따라 쉽게 갈라져 끊어지고, 태풍이 불면 그 끊어진 가지가 여기저기 날아다녀요 따라서 정원수로 심을 경우 너무 집 근처에 심지 않는 것이 좋아요",
            R.drawable.tree_lv3_popular,
            R.color.banner_color_green
    );

    TreeTipData ginkgo = new TreeTipData(
            "은행나무 관리 TIP",
            "은행나무란?",
            "은행나무는 어떤 나무일까요?",
            "은행나무에서 은행은 그 열매가 ‘은빛이 나는 살구’라는 뜻에서 은행나무라고 불리게 되었다고 합니다.",
            "관리방법",
            "은행나무를 잘 키우기 위해선 어떻게 해야할까요?",
            "은행나무는 모래가 많거나 건조한 곳에서 잘 자라지 않아요 또한 물이 고이거나 물이 나는 곳에서도 잘 자라지 않아요 그러므로 물이 고이지 않게하고 너무 건조하지 않게 은행나무를 관리하는 것이 중요합니다!",
            R.drawable.tree_lv3_ginkgo,
            R.color.banner_color_mint
    );

    public TreeTipHelper(){
        TreeHashMap.put(STRING_poplar, poplar);
        TreeHashMap.put(STRING_apricot, apricot);
        TreeHashMap.put(STRING_pine, pine);
        TreeHashMap.put(STRING_cherry, cherry);
        TreeHashMap.put(STRING_zelkova, zelkova);
        TreeHashMap.put(STRING_ginkgo, ginkgo);
    }
}