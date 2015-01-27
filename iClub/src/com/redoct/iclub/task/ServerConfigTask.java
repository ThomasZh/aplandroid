package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.cmd.gatekeeper.GK_ACF;
import com.oct.ga.comm.cmd.gatekeeper.GK_ARQ;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.config.ServiceConfig;
import com.redoct.iclub.param.GatewayParams;

import java.io.UnsupportedEncodingException;

/**
 * Created by liwenzhi on 14-9-23.
 */
public class ServerConfigTask extends TemplateTask {

    private String gatewayIP;
    private int gatewayPort;


    public ServerConfigTask(){
        gatewayIP = ServiceConfig.mina_server_ip;
        gatewayPort = ServiceConfig.mina_server_port;
    }

    /**
     * login task needed!
     *
     * @return
     */
    @Override
    public boolean justTodo() {
        connectGateway();
        return connectStpServer();
    }

    private boolean connectGateway(){
    	
        if (ServiceConfig.gateway != null) return true;
        
        Log.e("zyf", "connect gate gate gate.......");

        GK_ARQ arq = new GK_ARQ();
        arq.setDeviceId(AppConfig.DEVICE_ID);
        arq.setVersion(AppConfig.APP_VERSION);//
        arq.setVendorId(AppConfig.VENDOR);//use default..
        arq.setAppId(AppConfig.APP_ID);//use default...

        Log.d("sima", "connect to remote gateway...");
        iClubApplication.connect(gatewayIP, gatewayPort);
        Log.d("sima", "gateway connected!");

        Log.d("sima","fetching app server config...");
        GK_ACF acf = null;
        try{
            acf = (GK_ACF) iClubApplication.send(arq);
        }catch(InterruptedException e){
            e.printStackTrace();
            return false;
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return false;
        }

        if(acf==null){
            Log.e("sima", "fetching gateway config is null!");
            return false;//not decoded...
        }else {
            Log.e("zyf","fetching app server config...");
            
            GatewayParams server = new GatewayParams();
            server.setAppServerIp(acf.getServerIp());
            server.setAppServerPort(acf.getPort());
            server.setAppToken(acf.getGateToken());

            Log.e("zyf", acf.getGateToken()+"@"+acf.getServerIp()+":"+acf.getPort());

            //save it to global
            ServiceConfig.gateway = server;

            Log.d("sima", "gateway obtained!");
        }
        //close connection
        iClubApplication.close();

        return true;

    }

    private boolean connectStpServer(){
        if (ServiceConfig.gateway == null) return false; //init api client

        String ip = ServiceConfig.gateway.getAppServerIp();
        int port = ServiceConfig.gateway.getAppServerPort();
        try {
            Log.d("sima","setup sima session...");
            iClubApplication.connect(ip, port);//start a new session
            Log.d("sima", "api client obtained!");
        }catch (Exception e){
            Log.e("sima", e.getMessage());
            return false;
        }

        Log.d("sima", "api client started!");

        return true;
    }

}
