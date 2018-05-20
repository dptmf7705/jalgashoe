package com.dankook.jalgashoe.profile.setting;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.databinding.ActivitySettingBinding;
import com.dankook.jalgashoe.service.BluetoothChatService;
import com.dankook.jalgashoe.data.vo.SettingItemVO;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {
    private ListView listBTSetting;
    private SettingListAdapter adapter;

    private static final String SHARED_PREFERENCE_NAME = "BPM";
    private static final String SHARED_PREFERENCE_KEY = "CURRENT";

    // 디버깅용
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // 서비스 BluetoothChatService로부터 핸들러를 통해 메시지를 받는데
    // 이 메시지 타입에 대한 규약
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // 서비스 BluetoothChatService로부터 핸들러를 통해 메시지를 받는데
    // 이때 사용되는 키 정의
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // startActivityForResult의 결과값을 구분하기 위한 요청 코드
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    // 연결된 디바이스의 이름
    private String mConnectedDeviceName = null;
    // 대화이력을 위한 어레이 어댑터
    private ArrayAdapter<String> mConversationArrayAdapter;
    // 출력 메시지용 버퍼
    private StringBuffer mOutStringBuffer;
    // 블루투스 어댑터
    private BluetoothAdapter mBluetoothAdapter = null;
    // 채팅 서비스
    private BluetoothChatService mChatService = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 블루투스 어댑터가 널이면 이 디바이스는 블루투스 미지원 디바이스
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "블루투스 미지원 단말입니다.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setupView();
    }

    private void setupView(){
        initBluetoothSetting();
    }

    private void initBluetoothSetting() {
        adapter = new SettingListAdapter(this, getItemList());
        listBTSetting = findViewById(R.id.list_bluetooth_setting);
        listBTSetting.setAdapter(adapter);
        listBTSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent;

                switch (position){
                    case 0:
                        intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                        startActivityForResult(intent, REQUEST_CONNECT_DEVICE_SECURE);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                        startActivityForResult(intent, REQUEST_CONNECT_DEVICE_INSECURE);
                        break;
                    case 2:
                        ensureDiscoverable();
                        Toast.makeText(SettingActivity.this, "서비스 준비중", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }

    private ArrayList<SettingItemVO> getItemList() {
        ArrayList<SettingItemVO> list = new ArrayList<>();

        list.add(new SettingItemVO(R.mipmap.ic_bluetooth_secure, "디바이스 보안 연결"));
        list.add(new SettingItemVO(R.mipmap.ic_bluetooth_insecure, "디바이스 비보안 연결"));
        list.add(new SettingItemVO(R.mipmap.ic_search, "디바이스 찾기"));

        return list;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // 블루투스가 꺼져 있으면 , 블루투스를 켤수 있는 액티비티를 요청한다.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // 블루투스 사용가능 상태이면 채팅을 위한 준비작업 시작
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        if (mChatService != null) {
            // 채팅 서비스의 상태가 STATE_NONE이면, 채팅 서비스 시작
            if (mChatService.getState() ==
                    BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // 대화이력을 위한 어레이 어댑터 준비
        mConversationArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.chat_message);

        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);

        // 리턴키를 위한 리스너와 함게 텍스트 필드 초기화
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // 클릭 이벤트시 메시지 전송 처리
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView view =
                        (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });

        // 블루투스 연결을 위해 블루투스 서비스 초기화
        mChatService = new BluetoothChatService(this, mHandler);

        // 출력 메시지를 위한 버퍼 초기화
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        // 블루투스 채팅 서비스 종료
        if (mChatService != null) mChatService.stop();
        */
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.
                    ACTION_REQUEST_DISCOVERABLE);

            discoverableIntent.putExtra(BluetoothAdapter.
                    EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * 채팅 서비스를 사용하여 메시지를 전송한다.
     */
    private void sendMessage(String message) {
        // 연결 상태인지를 먼저 체크한다.
        if (mChatService.getState() !=
                BluetoothChatService.STATE_CONNECTED) {

            Toast.makeText(this, "연결이 안됨",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 실제로 전송할 데이터가 있는지 검사 후 채팅 서비스에 요청
        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mChatService.write(send);

            // 전송을 위한 버퍼를 지우고, 전송 메시지 필드 초기화
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    // EditText에 대해 리턴키를 위한 리스너 등록
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {

                public boolean onEditorAction(
                        TextView view, int actionId, KeyEvent event) {

                    Log.d(TAG, "onEditorAction");
                    // 리턴키 입력이 완료됬을 때 메시지 전송
                    if (actionId == EditorInfo.IME_NULL &&
                            event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendMessage(message);
                    }
                    if(D) Log.i(TAG, "END onEditorAction");
                    return true;
                }
            };

    private final void setStatus(int resId) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(subTitle);
    }

    // 서비스 BluetoothChatService에서 이 액티비티로 정보를 전달하기 위한
    // 핸들러
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                // 채팅 서비스가 메시지 전송 시 MESSAGE_WRITE 전달
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                // 채팅 서비스가 메시지 수신 시 MESSAGE_READ 전달
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if(readMessage.contains(", ")) {
                        String[] messages = readMessage.split(",");
                        readMessage = messages[1];
                    }

                    mConversationArrayAdapter.add(
                            mConnectedDeviceName+":  " + readMessage);

                    SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(SHARED_PREFERENCE_KEY, readMessage);
                    editor.commit();

                    break;
                // 채팅 서비스가 디바이스 연결 시 MESSAGE_DEVICE_NAME 전달
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName =
                            msg.getData().getString(DEVICE_NAME);

                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                    break;
                // 채팅 서비스는 연결 에러시 MESSAGE_TOAST 전달
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // DeviceListActivity가 연결한 디바이스를 리턴함
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // DeviceListActivity가 연결한 디바이스를 리턴함 비보안 연결
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // 블루투스가 켜짐
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // 블루투스가 꺼지거나 에러 발생
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // 맥주소 획득
        String address = data.getExtras().getString(
                DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // BluetoothDevice 객체 획득
        BluetoothDevice device
                = mBluetoothAdapter.getRemoteDevice(address);
        // 디바이스 연결 시도
        mChatService.connect(device, secure);
    }


}
