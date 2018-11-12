package com.xinzuo.qiaobashangjia.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/********************************************************************************************
 * {
 * Status:false|true, -- false-服务器内部错误，true-调用接口成功
 * Data:[null | {}], -- 服务器返回的数据
 * *******************************
 * -- 服务器返回的数据的描述
 * Data:{
 * Valid:false|true, -- 用于返回业务逻辑验证，false-失败，true-成功
 * Message:String, --Valid == false，业务逻辑验证失败的描述
 * Obj:[null | <T>] --Valid ==true，成功返回客户端需要的数据对象
 * }
 * *******************************
 * Code:String -- 当Status==false时，Code服务器内部错误的描述
 * }
 * ******************************************************************************************
 *
 */

public class JsonToObject<T extends Serializable> {

    // 泛型的具体类型
    private Class<T> mType;
    //需要转换的JSON对象
    private JSONObject mObject;

    private static final String INTEGER_NAME = Integer.class.getName();
    private static final String DATE_NAME = Date.class.getName();
    private static final String STRING_NAME = String.class.getName();
    private static final String DOUBLE_NAME = Double.class.getName();
    private static final String FLOAT_NAME = Float.class.getName();
    private static final String BOOLEAN_NAME = Boolean.class.getName();

    //构造函数
    public JsonToObject(Class<T> type, JSONObject object) {
        mType = type;
        mObject = object;
    }

    //得到对象集合
    public List<T> getObjects() throws Exception {
        List<T> objects = new ArrayList<T>();

        // 得到对象的所有属性
        Field[] fields = mType.getFields();

        // 判断Obj是否为空,获取对象
        String objString = mObject.getString("Obj");
        //判断是否为集合
        if (!objString.startsWith("[") && Valid.isNotNullOrEmpty(objString)) {
            throw new Exception("结果应该是个对象！");
        }
        // JSONArray array = mObject.getJSONArray("Obj");
        JSONArray array = new JSONArray(objString);
        Log.i("Services Status", objString);
        for (int i = 0; i < array.length(); i++) {
            // 通过反射实例化一个对象
            T object = mType.newInstance();
            JSONObject jsonObject = array.getJSONObject(i);
            //属性循环赋值
            for (Field f : fields) {

                // 业务逻辑验证信息赋值
                if (f.getName().equals("Valid")) {
                    f.set(object, mObject.getBoolean("Valid"));
                    continue;
                }
                if (f.getName().equals("Message")) {
                    f.set(object, mObject.getString("Message"));
                    continue;
                }
                // 其他属性赋值
                try {
                    setObjectValue(object, f, jsonObject);

                    continue;
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            objects.add(object);
        }
        // 返回对象
        return objects;
    }

    //得到单个对象
    public T getObject() throws Exception {
        // 通过反射实例化一个对象
        T object = mType.newInstance();
        // 得到对象的所有属性
        Field[] fields = mType.getFields();

        // 判断Obj是否为空,获取对象
        String objString = mObject.getString("Obj");
        //判断是否为集合
        if (objString.startsWith("[")) {
            throw new Exception("结果应该是个数组对象！");
        }
        //
        if (!mObject.isNull("Obj") && Valid.isNotNullOrEmpty(objString)) {
            JSONObject jsonObject = mObject.getJSONObject("Obj");
            //属性循环赋值
            for (Field f : fields) {
                // 业务逻辑验证信息赋值
                if (f.getName().equals("Valid")) {
                    f.set(object, mObject.getBoolean("Valid"));
                    continue;
                }
                if (f.getName().equals("Message")) {
                    f.set(object, mObject.getString("Message"));
                    continue;
                }
                // 其他属性赋值
                try {
                    setObjectValue(object, f, jsonObject);
                    continue;
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }

        // 返回对象
        return object;
    }

    //设置对象的值
    private void setObjectValue(Object object, Field field, JSONObject jsonObject) throws Exception {
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
                if (Valid.isNotNullOrEmpty(value)) {
                    field.set(object, Integer.valueOf(value));
                }
            }
            //日期
            else if (className.contains(DATE_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (Valid.isNotNullOrEmpty(value)) {
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
                if (Valid.isNotNullOrEmpty(value)) {
                    field.set(object, Double.valueOf(value));
                }
            }
            //布尔
            else if (className.contains(BOOLEAN_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (Valid.isNotNullOrEmpty(value)) {
                    field.set(object, Boolean.valueOf(value));
                }
            }
            //浮点数
            else if (className.contains(FLOAT_NAME)) {
                String value = jsonObject.getString(fieldName);
                if (Valid.isNotNullOrEmpty(value)) {
                    field.set(object, Float.valueOf(value));
                }
            } else {
                String subObjString = jsonObject.getString(fieldName);
                if (subObjString.contains("[")) {
                    JSONArray array = jsonObject.getJSONArray(fieldName);
                    List<Object> subObjs = new ArrayList<Object>();
                    for (int i = 0; i < array.length(); i++) {
                        subObjs.add(setSubObject(array.getJSONObject(i), field));
                    }
                    field.set(object, subObjs);
                } else {
                    field.set(object, setSubObject(jsonObject.getJSONObject(fieldName), field));
                }
            }
        }
    }

    //????/
    private Object setSubObject(JSONObject object, Field field) {
        try {
            //字段名定义必须和类名相同
            Class<?> subType = Class.forName("com.xinzuokeji.boxtakeawaybusiness.entities." + field.getName());
            Object obj = subType.newInstance();

            //调用设置属性值的方法？
            Field[] fields = obj.getClass().getFields();
            for (Field f : fields) {
                setObjectValue(obj, f, object);
            }
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
