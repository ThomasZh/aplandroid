package com.redoct.iclub.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class LocationShowActivity extends BaseActivity implements
		OnMapLoadedListener,OnClickListener{
	private AMap aMap;
	private MapView mapView;
	private LatLng latlng;
	private String locDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position_show);
		
		locDesc=getIntent().getStringExtra("locDesc");
		
		double lat=Double.parseDouble(getIntent().getStringExtra("locY"));
		double lng=Double.parseDouble(getIntent().getStringExtra("locX"));
		
		latlng = new LatLng(lat, lng);
		
		initTitle();
		
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		
		init();
	}
	
	private void initTitle(){
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.position_show));
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {

		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
			addMarkersToMap();// 往地图上添加marker
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		MarkerOptions options=new MarkerOptions();
		options.position(latlng)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
		.perspective(true)
		.draggable(true);
		
		if(locDesc!=null&&locDesc.length()>0){
			options.title(locDesc);
		}
		Marker marker = aMap.addMarker(options);
		
		marker.showInfoWindow();//设置默认显示一个infowinfow
		
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(latlng).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.leftBtn:
			
			finish();
			break;

		default:
			break;
		}
	}

}
