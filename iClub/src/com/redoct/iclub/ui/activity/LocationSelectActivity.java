package com.redoct.iclub.ui.activity;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.mapcore.util.v;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.redoct.iclub.R;
import com.redoct.iclub.util.Constant;


public class LocationSelectActivity extends Activity implements LocationSource,
		AMapLocationListener,OnGeocodeSearchListener,OnClickListener  {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private Marker marker;
	
	private Marker marker2;
    
	private LatLng mLatLng;
	
	private String locDesc;
	
	private GeocodeSearch geocoderSearch;
	
	private boolean locationSuccess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position_select);
		
		initTitle();
        
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		
		init();
	}
	
	private void initTitle(){
		
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.position_select));
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
		
		Button rightBtn=(Button) findViewById(R.id.rightBtn);
		rightBtn.setBackgroundResource(R.drawable.icon_start_time);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);
	}
	
	private void init() {
		
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		
		aMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng latlng) {
				
				mLatLng=latlng;
				
				Log.e("zyf", "map long click: "+latlng.latitude);
				
				aMap.clear();
				
				marker2= aMap.addMarker(new MarkerOptions()
				.position(mLatLng)
				.title(getResources().getString(R.string.position_getting))
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		         marker2.showInfoWindow();
		         
		         LatLonPoint latLonPoint=new LatLonPoint(mLatLng.latitude, mLatLng.longitude);
		         RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
		 		geocoderSearch.getFromLocationAsyn(query);
			}
		});
 
	}

	
	private void setUpMap() {
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		
		marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icons(giflist).period(50));
		
		aMap.setLocationSource(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		
		if (mListener != null && aLocation != null) {
			
			if(!locationSuccess){
				locationSuccess=true;
			}else{
				return;
			}
			
			mLatLng=new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
			
			aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15.0f));
			
			marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation
					.getLongitude()));
			
			locDesc=aLocation.getAddress();
			
			marker.setTitle(locDesc);
	        marker.showInfoWindow();
	        
			float bearing = aMap.getCameraPosition().bearing;
			aMap.setMyLocationRotateAngle(bearing);
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				locDesc = result.getRegeocodeAddress().getFormatAddress();
				
				Log.e("zyf", "geocode address: "+locDesc);
		         
				marker2.setTitle(locDesc);
				marker2.showInfoWindow();
				
				//aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
				
			    return;
 				
			}
		}
		
		Log.e("zyf", "geocode address failed");
        
		marker2.setTitle(getResources().getString(R.string.position_desc_get_failed));
		marker2.showInfoWindow();
		
		//aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.leftBtn:
			
			finish();
			
			break;
		case R.id.rightBtn:
			
			Intent intent=new Intent();
			intent.putExtra("locDesc", locDesc);
			intent.putExtra("locX", mLatLng.longitude+"");
			intent.putExtra("locY", mLatLng.latitude+"");
			setResult(Constant.RESULT_CODE_LOCATION_SELECT, intent);
			
			finish();
			
			break;

		default:
			break;
		}
	}

}
