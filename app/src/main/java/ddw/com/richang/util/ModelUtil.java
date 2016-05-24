package ddw.com.richang.util;


import java.util.ArrayList;
import java.util.List;

import ddw.com.richang.model.filter.FilterEntity;
import ddw.com.richang.model.filter.FilterTwoEntity;

/**
 * 数据分类 测试数据
 */
public class ModelUtil {

    public static final String type_scenery = "风景";
    public static final String type_building = "建筑";
    public static final String type_animal = "动物";
    public static final String type_plant = "植物";


    // 分类数据
    public static List<FilterTwoEntity> getCategoryData() {
        List<FilterTwoEntity> list = new ArrayList<>();
        list.add(new FilterTwoEntity(type_scenery, getFilterData()));
        list.add(new FilterTwoEntity(type_building, getFilterData()));
        list.add(new FilterTwoEntity(type_animal, getFilterData()));
        list.add(new FilterTwoEntity(type_plant, getFilterData()));
        return list;
    }

    // 排序数据
    public static List<FilterEntity> getSortData() {
        List<FilterEntity> list = new ArrayList<>();
        list.add(new FilterEntity("排序从高到低", "1"));
        list.add(new FilterEntity("排序从低到高", "2"));
        return list;
    }

    // 筛选数据
    public static List<FilterEntity> getFilterData() {
        List<FilterEntity> list = new ArrayList<>();
        list.add(new FilterEntity("中国", "1"));
        list.add(new FilterEntity("美国", "2"));
        list.add(new FilterEntity("英国", "3"));
        list.add(new FilterEntity("德国", "4"));
        list.add(new FilterEntity("西班牙", "5"));
        list.add(new FilterEntity("意大利", "6"));
        return list;
    }

//    // ListView分类数据
//    public static List<TravelingEntity> getCategoryTravelingData(FilterTwoEntity entity) {
//        List<TravelingEntity> list = getTravelingData();
//        List<TravelingEntity> travelingList = new ArrayList<>();
//        int size = list.size();
//        for (int i = 0; i < size; i++) {
//            if (list.get(i).getType().equals(entity.getType()) &&
//                    list.get(i).getFrom().equals(entity.getSelectedFilterEntity().getKey())) {
//                travelingList.add(list.get(i));
//            }
//        }
//        return travelingList;
//    }

//    // ListView排序数据
//    public static List<TravelingEntity> getSortTravelingData(FilterEntity entity) {
//        List<TravelingEntity> list = getTravelingData();
//        Comparator<TravelingEntity> ascComparator = new TravelingEntityComparator();
//        if (entity.getKey().equals("排序从高到低")) {
//            Collections.sort(list);
//        } else {
//            Collections.sort(list, ascComparator);
//        }
//        return list;
//    }

    // ListView筛选数据
//    public static List<TravelingEntity> getFilterTravelingData(FilterEntity entity) {
//        List<TravelingEntity> list = getTravelingData();
//        List<TravelingEntity> travelingList = new ArrayList<>();
//        int size = list.size();
//        for (int i = 0; i < size; i++) {
//            if (list.get(i).getFrom().equals(entity.getKey())) {
//                travelingList.add(list.get(i));
//            }
//        }
//        return travelingList;
//    }

    // 暂无数据
//    public static List<TravelingEntity> getNoDataEntity(int height) {
//        List<TravelingEntity> list = new ArrayList<>();
//        TravelingEntity entity = new TravelingEntity();
//        entity.setNoData(true);
//        entity.setHeight(height);
//        list.add(entity);
//        return list;
//    }

}
