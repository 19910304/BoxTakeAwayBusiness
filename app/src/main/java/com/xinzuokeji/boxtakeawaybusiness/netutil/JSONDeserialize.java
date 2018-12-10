package com.xinzuokeji.boxtakeawaybusiness.netutil;

import android.text.TextUtils;

import com.xinzuokeji.boxtakeawaybusiness.GSApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class JSONDeserialize<T extends Serializable> {

    //类所在的包名
    private static final String CLASS_PACKAGE_NAME = "com.xinzuo.qiaobashangjia.entities";

    //常规数据类型
    private static final String INTEGER_NAME = Integer.class.getName();
    private static final String DATE_NAME = Date.class.getName();
    private static final String STRING_NAME = String.class.getName();
    private static final String DOUBLE_NAME = Double.class.getName();
    private static final String FLOAT_NAME = Float.class.getName();
    private static final String BOOLEAN_NAME = Boolean.class.getName();

    // 泛型的具体类型
    private Class<T> mType;
    //
    private Field[] mFields;
    //需要转换的JSON对象
    private String mJsonString;

    // 构造函数
    public JSONDeserialize(Class<T> type, String jsonString) {

        mType = type;
        mFields = type.getFields();
        mJsonString = jsonString;
    }

    // 根据Json字符串判断是否是个对象？
    private boolean isArray(String jsonString) {
        if (!TextUtils.isEmpty(jsonString) && jsonString.startsWith("[")) {
            return true;
        } else {
            return false;
        }
    }

    // 获取对象集合
    public List<T> toObjects() throws Exception {

        if (!isArray(mJsonString)) {
            throw new Exception("Error Information:需要解析的JSON字符串不是有效的数组对象！");
        }

        List<T> objects = new ArrayList<>();
        JSONArray arrays = new JSONArray(mJsonString);
        for (int i = 0; i < arrays.length(); i++) {
            objects.add((T) deserialize(mFields, mType.newInstance(), arrays.getJSONObject(i)));
        }

        return objects;
    }

    // 获取对象
    public T toObject() throws Exception {

        if (isArray(mJsonString)) {
            throw new Exception("Error Information:需要解析的JSON字符串不是有效的对象！");
        }

        return (T) deserialize(mFields, mType.newInstance(), new JSONObject(mJsonString));
    }

    // 转换JSON对象为实体
    private Object deserialize(Field[] fields, Object instance, JSONObject jsonObject) {
        // 对象的属性集合
        for (Field f : fields) {
            try {
                setObjectValue(instance, f, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        // 返回赋值后的实体
        return instance;
    }

    //设置对象的值
    private void setObjectValue(Object object, Field field, JSONObject jsonObject) throws Exception {
        String PACKAGE_NAME = GSApplication.getInstance().getPackage() + ".entities";
        String className = field.getGenericType().toString();//.getName();
        String fieldName = field.getName();
        if (!jsonObject.isNull(fieldName)) {
            //字符串
            if (className.contains(STRING_NAME)) {
                field.set(object, jsonObject.getString(fieldName));
            }
            //整型
            else if (className.contains(INTEGER_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (!TextUtils.isEmpty(value)) {
                    field.set(object, Integer.valueOf(value));
                }
            }
            //日期
            else if (className.contains(DATE_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (!TextUtils.isEmpty(value)) {
                    //转换字符串为时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if (!value.contains(".")) {
                        value += ".000";
                    }
                    field.set(object, dateFormat.parse(value));
                }
            }
            //双精度浮点数
            else if (className.contains(DOUBLE_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (!TextUtils.isEmpty(value)) {
                    field.set(object, Double.valueOf(value));
                }
            }
            //布尔
            else if (className.contains(BOOLEAN_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (!TextUtils.isEmpty(value)) {
                    field.set(object, Boolean.valueOf(value));
                }
            }
            //浮点数
            else if (className.contains(FLOAT_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (!TextUtils.isEmpty(value)) {
                    field.set(object, Float.valueOf(value));
                }
            } else {
                // 除去基本属性，定义类时只应该用自定义的类，此处处理这种类型的属性
                String subObjString = jsonObject.getString(fieldName);

                //字段名定义必须和类名相同

                Class<?> subType = Class.forName(PACKAGE_NAME + "." + field.getName());
                Field[] subFields = subType.getFields();

                // 集合和对象
                if (isArray(subObjString)) {
                    JSONArray array = jsonObject.getJSONArray(fieldName);
                    List<Object> subObjects = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        subObjects.add(deserialize(subFields, subType.newInstance(), array.getJSONObject(i)));
                    }
                    field.set(object, subObjects);
                } else {
                    field.set(object, deserialize(subFields, subType.newInstance(), jsonObject.getJSONObject(fieldName)));
                }
            }
        }
    }
}
