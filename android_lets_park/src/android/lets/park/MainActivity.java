package android.lets.park;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

	// 메시지 정수
	public static final int MSG_STATE_CHANGE = 1;
	public static final int MSG_READ = 2;

	// 요청 정수
	private static final int RQ_CONNECT_DEVICE = 1;
	private static final int RQ_ENABLE_BT = 2;

	// Bluetooth
	private BluetoothAdapter btAdapter;
	private Service BtService;

	// UI
	private RelativeLayout layout;
	private TextView title, bt_state, car_state_1, car_state_2, car_state_3, car_state_4, car_state_5, car_state_6,
			my_car;

	int[] parking_array = new int[7];

	int w, h;

	@Override
	// 액티비티가 처음 실행될때 호출
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 휴대폰 가로,세로 전체길이
		w = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		h = getWindow().getWindowManager().getDefaultDisplay().getHeight();

		// 레이아웃의 생성
		layout = new RelativeLayout(this);
		layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
		setContentView(layout);
		title = new TextView(this);
		title.setId(10);
		title.setBackgroundResource(R.drawable.corner_rounded);
		RelativeLayout.LayoutParams param10 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param10.addRule(RelativeLayout.ALIGN_PARENT_TOP, 10);
		param10.addRule(RelativeLayout.CENTER_HORIZONTAL, 10);
		param10.height = h / 10;
		param10.width = w - (w / 3);
		param10.setMargins(0, h / 20, 0, h / 40);
		title.setGravity(Gravity.CENTER);
		title.setText("Let's Park");
		title.setTextSize(25);
		title.setTextColor(Color.parseColor("#FFFFFF"));
		title.setLayoutParams(param10);
		layout.addView(title);

		car_state_1 = new TextView(this);
		car_state_1.setId(101);
		car_state_1.setBackgroundResource(R.drawable.car_x);
		RelativeLayout.LayoutParams param101 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param101.addRule(RelativeLayout.BELOW, 10);
		param101.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 101);
		param101.height = h / 9;
		param101.width = (w / 5) * 2;
		param101.setMargins(w / 20, h / 20, 0, 0);
		car_state_1.setText("주차구역: 1　\nnull　");
		car_state_1.setTextSize(13);
		car_state_1.setTextColor(Color.parseColor("#000000"));
		car_state_1.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		car_state_1.setLayoutParams(param101);
		car_state_1.setOnClickListener(this);
		layout.addView(car_state_1);

		car_state_2 = new TextView(this);
		car_state_2.setId(103);
		car_state_2.setBackgroundResource(R.drawable.car_x);
		RelativeLayout.LayoutParams param103 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param103.addRule(RelativeLayout.BELOW, 101);
		param103.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 103);
		param103.height = h / 9;
		param103.width = (w / 5) * 2;
		param103.setMargins(w / 20, h / 15, 0, 0);
		car_state_2.setText("주차구역: 2　\nnull　");
		car_state_2.setTextSize(13);
		car_state_2.setTextColor(Color.parseColor("#000000"));
		car_state_2.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		car_state_2.setLayoutParams(param103);
		car_state_2.setOnClickListener(this);
		layout.addView(car_state_2);

		car_state_3 = new TextView(this);
		car_state_3.setId(105);
		car_state_3.setBackgroundResource(R.drawable.car_x);
		RelativeLayout.LayoutParams param105 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param105.addRule(RelativeLayout.BELOW, 103);
		param105.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 105);
		param105.height = h / 9;
		param105.width = (w / 5) * 2;
		param105.setMargins(w / 20, h / 15, 0, 0);
		car_state_3.setText("주차구역: 3　\nnull　");
		car_state_3.setTextSize(13);
		car_state_3.setTextColor(Color.parseColor("#000000"));
		car_state_3.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		car_state_3.setLayoutParams(param105);
		car_state_3.setOnClickListener(this);
		layout.addView(car_state_3);

		car_state_4 = new TextView(this);
		car_state_4.setId(107);
		car_state_4.setBackgroundResource(R.drawable.car_x);
		RelativeLayout.LayoutParams param107 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param107.addRule(RelativeLayout.BELOW, 10);
		param107.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 107);
		param107.height = h / 9;
		param107.width = (w / 5) * 2;
		param107.setMargins(0, h / 20, w / 20, 0);
		car_state_4.setText("주차구역: 4　\nnull　");
		car_state_4.setTextSize(13);
		car_state_4.setTextColor(Color.parseColor("#000000"));
		car_state_4.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		car_state_4.setLayoutParams(param107);
		car_state_4.setOnClickListener(this);
		layout.addView(car_state_4);

		car_state_5 = new TextView(this);
		car_state_5.setId(109);
		car_state_5.setBackgroundResource(R.drawable.car_x);
		RelativeLayout.LayoutParams param109 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param109.addRule(RelativeLayout.BELOW, 107);
		param109.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 109);
		param109.height = h / 9;
		param109.width = (w / 5) * 2;
		param109.setMargins(0, h / 15, w / 20, 0);
		car_state_5.setText("주차구역: 5　\nnull　");
		car_state_5.setTextSize(13);
		car_state_5.setTextColor(Color.parseColor("#000000"));
		car_state_5.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		car_state_5.setLayoutParams(param109);
		car_state_5.setOnClickListener(this);
		layout.addView(car_state_5);

		car_state_6 = new TextView(this);
		car_state_6.setId(111);
		car_state_6.setBackgroundResource(R.drawable.car_x);
		RelativeLayout.LayoutParams param111 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param111.addRule(RelativeLayout.BELOW, 109);
		param111.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 111);
		param111.height = h / 9;
		param111.width = (w / 5) * 2;
		param111.setMargins(0, h / 15, w / 20, 0);
		car_state_6.setText("주차구역: 6　\nnull　");
		car_state_6.setTextSize(13);
		car_state_6.setTextColor(Color.parseColor("#000000"));
		car_state_6.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		car_state_6.setLayoutParams(param111);
		car_state_6.setOnClickListener(this);
		layout.addView(car_state_6);

		my_car = new TextView(this);
		my_car.setId(113);
		RelativeLayout.LayoutParams param113 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param113.addRule(RelativeLayout.ABOVE, 20);
		param113.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 113);
		param113.height = h / 9;
		param113.width = (w / 10) * 4;
		param113.setMargins(0, 0, w / 20, 0);
		my_car.setText("내차 위치 : [   ]");
		my_car.setTextSize(18);
		my_car.setTextColor(Color.parseColor("#000000"));
		my_car.setGravity(Gravity.CENTER);
		my_car.setLayoutParams(param113);
		my_car.setOnClickListener(this);
		layout.addView(my_car);

		bt_state = new TextView(this);
		bt_state.setId(20);
		bt_state.setBackgroundResource(R.drawable.corner_rounded);
		RelativeLayout.LayoutParams param20 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param20.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 20);
		param20.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 20);
		param20.height = h / 14;
		param20.setMargins(0, 0, w / 20, h / 60);
		bt_state.setPadding(w / 20, 0, w / 20, 0);
		bt_state.setGravity(Gravity.RIGHT | Gravity.CENTER);
		bt_state.setText("접속상태: 미접속");
		bt_state.setTextSize(20);
		bt_state.setTextColor(Color.parseColor("#FFFFFF"));
		bt_state.setLayoutParams(param20);
		bt_state.setOnClickListener(this);
		layout.addView(bt_state);

		// Bluetooth 어댑터
		btAdapter = BluetoothAdapter.getDefaultAdapter();

	}

	// onCreate다음(처음 시작시)이나 onRestart다음(재시작) 호출
	@Override
	public void onStart() {
		super.onStart();
		if (!btAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, RQ_ENABLE_BT);
		} else {
			if (BtService == null)
				BtService = new Service(this, handler);
		}
	}

	// 어플리케이션 재개 시 불린다. (액티비티가 포그라운드 상태가 되기 직전에 호출)
	@Override
	public synchronized void onResume() {
		super.onResume();
		if (BtService != null) {
			if (BtService.getState() == Service.STATE_NONE) {
				// Bluetooth의 접속 대기(서버)
				BtService.start();
			}
		}
	}

	// 액티비티가 백그라운드 상태(보여지지 않을때) 호출 (홈버튼, 다른 app실행, ...)
	@Override
	protected void onStop() {
		super.onStop();
	}

	// 액티비티가 종료 될때 호출, 액티비티가 종료되어도 프로세스는 남아있게 된다.
	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
		if (BtService != null)
			BtService.stop();
	}

	// 채팅 서버로부터 정보를 취득하는 핸들러
	private final Handler handler = new Handler() {
		// 핸들 메시지
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_STATE_CHANGE:
				switch (msg.arg1) {
				case Service.STATE_CONNECTED:
					bt_state.setText("접속상태: 접속 완료");
					break;
				case Service.STATE_CONNECTING:
					bt_state.setText("접속상태: 접속 중");
					break;
				case Service.STATE_LISTEN:
				case Service.STATE_NONE:
					bt_state.setText("접속상태: 미접속");
					break;
				}
				break;
			// 메시지 수신
			case MSG_READ:
				byte[] readBuf = (byte[]) msg.obj;
				String readMessage = new String(readBuf, 0, msg.arg1);
				int l = readMessage.length();

				try {
					if (l == 7 && readMessage.charAt(6) == '/') {

						if (readMessage.charAt(0) == 'x') {
							car_state_1.setBackgroundResource(R.drawable.car_x);
							car_state_1.setText("주차구역: 1　\n[ 주차가능 ]　");
							car_state_1.setTextColor(Color.parseColor("#ADE35E"));

							parking_array[1] = 0;
							
							if(my_car_number == 1){
								my_car_number = 0;
								my_car.setText("내차 위치 : [   ]");
							}
							
						} else {
							car_state_1.setBackgroundResource(R.drawable.car_o);
							car_state_1.setText("주차구역: 1　\n[ 주차중 ]　");
							car_state_1.setTextColor(Color.parseColor("#E24646"));

							parking_array[1] = 1;
						}

						if (readMessage.charAt(1) == 'x') {
							car_state_2.setBackgroundResource(R.drawable.car_x);
							car_state_2.setText("주차구역: 2　\n[ 주차가능 ]　");
							car_state_2.setTextColor(Color.parseColor("#ADE35E"));

							parking_array[2] = 0;
							
							if(my_car_number == 2){
								my_car_number = 0;
								my_car.setText("내차 위치 : [   ]");
							}
							
						} else {
							car_state_2.setBackgroundResource(R.drawable.car_o);
							car_state_2.setText("주차구역: 2　\n[ 주차중 ]　");
							car_state_2.setTextColor(Color.parseColor("#E24646"));

							parking_array[2] = 1;
						}

						if (readMessage.charAt(2) == 'x') {
							car_state_3.setBackgroundResource(R.drawable.car_x);
							car_state_3.setText("주차구역: 3　\n[ 주차가능 ]　");
							car_state_3.setTextColor(Color.parseColor("#ADE35E"));

							parking_array[3] = 0;
							
							if(my_car_number == 3){
								my_car_number = 0;
								my_car.setText("내차 위치 : [   ]");
							}
							
						} else {
							car_state_3.setBackgroundResource(R.drawable.car_o);
							car_state_3.setText("주차구역: 3　\n[ 주차중 ]　");
							car_state_3.setTextColor(Color.parseColor("#E24646"));

							parking_array[3] = 1;
						}

						if (readMessage.charAt(3) == 'x') {
							car_state_4.setBackgroundResource(R.drawable.car_x);
							car_state_4.setText("주차구역: 4　\n[ 주차가능 ]　");
							car_state_4.setTextColor(Color.parseColor("#ADE35E"));

							parking_array[4] = 0;
							
							if(my_car_number == 4){
								my_car_number = 0;
								my_car.setText("내차 위치 : [   ]");
							}
							
						} else {
							car_state_4.setBackgroundResource(R.drawable.car_o);
							car_state_4.setText("주차구역: 4　\n[ 주차중 ]　");
							car_state_4.setTextColor(Color.parseColor("#E24646"));

							parking_array[4] = 1;
						}

						if (readMessage.charAt(4) == 'x') {
							car_state_5.setBackgroundResource(R.drawable.car_x);
							car_state_5.setText("주차구역: 5　\n[ 주차가능 ]　");
							car_state_5.setTextColor(Color.parseColor("#ADE35E"));

							parking_array[5] = 0;
							
							if(my_car_number == 5){
								my_car_number = 0;
								my_car.setText("내차 위치 : [   ]");
							}
							
						} else {
							car_state_5.setBackgroundResource(R.drawable.car_o);
							car_state_5.setText("주차구역: 5　\n[ 주차중 ]　");
							car_state_5.setTextColor(Color.parseColor("#E24646"));

							parking_array[5] = 1;
						}

						if (readMessage.charAt(5) == 'x') {
							car_state_6.setBackgroundResource(R.drawable.car_x);
							car_state_6.setText("주차구역: 6　\n[ 주차가능 ]　");
							car_state_6.setTextColor(Color.parseColor("#ADE35E"));

							parking_array[6] = 0;
							
							if(my_car_number == 6){
								my_car_number = 0;
								my_car.setText("내차 위치 : [   ]");
							}
							
						} else {
							car_state_6.setBackgroundResource(R.drawable.car_o);
							car_state_6.setText("주차구역: 6　\n[ 주차중 ]　");
							car_state_6.setTextColor(Color.parseColor("#E24646"));

							parking_array[6] = 1;
						}

					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				break;
			}
		}

	};

	// 어플리케이션 복귀 시 불린다.
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 단말기 검색
		case RQ_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

				// Bluetooth의 접속 요구(클라이언트)
				BluetoothDevice device = btAdapter.getRemoteDevice(address);
				BtService.connect(device);
			}
			break;
		// 검색 유효
		case RQ_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				BtService = new Service(this, handler);
			} else {
				Toast.makeText(this, "Bluetooth가 유효하지 않습니다", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	// 백 버튼을 누르면
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			TextView tv = new TextView(MainActivity.this);
			tv.setHeight(h / 10);
			tv.setGravity(Gravity.CENTER);
			tv.setText("[Let's Park] 종료 하시겠습니까?");
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			tv.setTextColor(Color.parseColor("#FFFFFF"));
			tv.setPadding((int) (w * 0), (int) (h * 0), (int) (w * 0), (int) (h * 0));

			new AlertDialog.Builder(this).setCustomTitle(tv).setCancelable(false)
					.setPositiveButton("예", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					}).setNegativeButton("아니오", null).show();

			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	int my_car_number = 0;

	public void onClick(View v) {

		if (v == bt_state) {
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, RQ_CONNECT_DEVICE);
		}

		if (v.getId() == 101 || v.getId() == 103 || v.getId() == 105 || v.getId() == 107 || v.getId() == 109
				|| v.getId() == 111) {

			if (v.getId() == 101) {
				my_car_number = 1;
			} else if (v.getId() == 103) {
				my_car_number = 2;
			} else if (v.getId() == 105) {
				my_car_number = 3;
			} else if (v.getId() == 107) {
				my_car_number = 4;
			} else if (v.getId() == 109) {
				my_car_number = 5;
			} else if (v.getId() == 111) {
				my_car_number = 6;
			}

			// 클릭한 위치가 주차가 된 상태라면
			if (parking_array[my_car_number] == 1) {
				new AlertDialog.Builder(this).setMessage("\n" + my_car_number + "번 위치에 내차를 저장 하시겠습니까	?\n")
						.setPositiveButton("예", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								my_car.setText("내차 위치 : [ " + my_car_number + " ]");

							}
						}).setNegativeButton("아니오", null).show();
			} else {// 클릭한 위치가 주차가 안 된 상태라면
				Toast.makeText(this, "주차를 하신뒤에 저장해주세요", Toast.LENGTH_SHORT).show();
			}

		}

	}
}
