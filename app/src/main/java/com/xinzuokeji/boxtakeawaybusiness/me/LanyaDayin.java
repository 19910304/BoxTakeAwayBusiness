package com.xinzuokeji.boxtakeawaybusiness.me;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.me.bluetoothtest.PrintAdapter;
import com.xinzuokeji.boxtakeawaybusiness.me.bluetoothtest.PrintBean;

import java.util.ArrayList;

import static com.xinzuokeji.boxtakeawaybusiness.me.bluetoothtest.PrintBean.PRINT_TYPE;

public class LanyaDayin extends BaseActivity {
    private ImageButton header_back;
    ListViewAdapter<String> mAdapter;
    ArrayList<PrintBean> mBluetoothDevicesDatas = new ArrayList<PrintBean>();
    ListView listView;
    //蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    private PrintAdapter adapter;
    //请求的code
    public static final int REQUEST_ENABLE_BT = 1;
    private TextView searchHint;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_lanya);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("蓝牙打印机");
        searchHint = (TextView) findViewById(R.id.searchHint);
        listView = findViewById(R.id.lv_lanya);
        //广播注册
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        //初始化
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String printContent = getIntent().getStringExtra("printContent");
//        adapter = new PrintAdapter(this, mBluetoothDevicesDatas, TextUtils.isEmpty(printContent) ? "禾籽外卖少时诵诗书所所少时诵诗书所所所所所所所所所所所所所所所所所所所少时诵诗书所所所所所" : printContent);
        adapter = new PrintAdapter(this, mBluetoothDevicesDatas, "禾籽外卖少时诵诗书所所少时诵诗书所所所所所所所所所所所所所所所所所所所少时诵诗书所所所所所");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.clearSelection(position);
                mAdapter.notifyDataSetChanged();
            }
        });

        chechBluetooth();
        addViewListener();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            default:

                break;
        }
    }

    /**
     * 判断有没有开启蓝牙
     */
    private void chechBluetooth() {
        //没有开启蓝牙
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); // 设置蓝牙可见性，最多300秒
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
                setViewStatus(true);
                //开启蓝牙
            } else {
                searchDevices();
                setViewStatus(true);
            }
        }
    }

    /**
     * 搜索状态调整
     *
     * @param isSearch 是否开始搜索
     */
    private void setViewStatus(boolean isSearch) {

        if (isSearch) {
//            mFloatingActionButton.setVisibility(View.GONE);
            searchHint.setVisibility(View.VISIBLE);
//            mProgressBar.setVisibility(View.VISIBLE);
        } else {
//            mFloatingActionButton.setVisibility(View.VISIBLE);
//            mProgressBar.setVisibility(View.GONE);
            searchHint.setVisibility(View.GONE);
        }
    }


    /**
     * 添加View的监听
     */
    private void addViewListener() {
        //蓝牙的状态
//        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    openBluetooth();
//                    setViewStatus(true);
//                } else {
//                    closeBluetooth();
//                }
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
            Log.e("text", "开启蓝牙");
            searchDevices();
            mBluetoothDevicesDatas.clear();
            adapter.notifyDataSetChanged();
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_ENABLE_BT) {
            Log.e("text", "没有开启蓝牙");
            setViewStatus(false);
        }

    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); // 设置蓝牙可见性，最多300秒
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20);
        startActivityForResult(intent, REQUEST_ENABLE_BT);

    }

    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        mBluetoothAdapter.disable();
    }


    /**
     * 搜索蓝牙设备
     */
    public void searchDevices() {
        mBluetoothDevicesDatas.clear();
        adapter.notifyDataSetChanged();
        //开始搜索蓝牙设备
        mBluetoothAdapter.startDiscovery();
    }


    /**
     * 通过广播搜索蓝牙设备
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 把搜索的设置添加到集合中
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("device", "Name" + device.getName() + "--" + "Address" + device.getAddress() + "a" + device.getBluetoothClass().getDeviceClass());
                //已经匹配的设备
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    addBluetoothDevice(device);

                    //没有匹配的设备
                } else {
                    addBluetoothDevice(device);
                }
                adapter.notifyDataSetChanged();
                //搜索完成
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setViewStatus(false);
            }
        }

        /**
         * 添加数据
         * @param device 蓝牙设置对象
         */
        private void addBluetoothDevice(BluetoothDevice device) {
            for (int i = 0; i < mBluetoothDevicesDatas.size(); i++) {
                if (device.getAddress().equals(mBluetoothDevicesDatas.get(i).getAddress())) {
                    mBluetoothDevicesDatas.remove(i);
                }
            }
            if (device.getBondState() == BluetoothDevice.BOND_BONDED && device.getBluetoothClass().getDeviceClass() == PRINT_TYPE) {
                mBluetoothDevicesDatas.add(0, new PrintBean(device));
            } else {
                mBluetoothDevicesDatas.add(new PrintBean(device));
            }
        }
    };
}
