package com.redoct.iclub.task;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.moment.QueryMomentPhotoFlowPaginationReq;
import com.oct.ga.comm.cmd.moment.QueryMomentPhotoFlowPaginationResp;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.redoct.iclub.iClubApplication;

public class GetMomentTask extends TemplateTask {
	
	private short pageNum;
	private short pageSize;
	private String activityId;
	
    private ArrayList<String> imageUrls;
    
    public ArrayList<String> getImageUrls(){
    	
    	return imageUrls;
    }
	
	
	public GetMomentTask(short pageNum, short pageSize, String activityId) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.activityId = activityId;
		imageUrls=new ArrayList<String>();
	}

	@Override
	protected boolean justTodo() {
		
		
		QueryMomentPhotoFlowPaginationReq req=new QueryMomentPhotoFlowPaginationReq();
		req.setSequence(DatetimeUtil.currentTimestamp());
		req.setPageNum(pageNum);
		req.setPageSize(pageSize);
		req.setTaskId(activityId);
		
		try {
			
			QueryMomentPhotoFlowPaginationResp resp=(QueryMomentPhotoFlowPaginationResp) iClubApplication.send(req);
			
			if(resp==null){
				
				Log.e("zyf", "GetMomentTask resp is null...... ");
				
				return false;
			}
			
			List<GaMomentPhotoObject> photoList=resp.getMomentPhotos();
			
			Log.e("zyf", "photos list size: "+photoList.size());
			
			for(int i=0;i<photoList.size();i++){
				imageUrls.add(photoList.get(i).getPhotoUrl());
			}
			
		} catch (Exception e) {
			
			Log.e("zyf", "GetMomentTask exception: "+e.toString());
			
			return false;
		}

		return true;
	}

}
