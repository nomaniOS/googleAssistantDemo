package com.example.RM;

/*
* ****************************************************************************
* * Copyright © 2018 W3 Engineers Ltd., All rights reserved.
* *
* * Created by:
* * Name : Md. Nomanur Rashid
* * Date : 4/16/18
* * Email : nomanur@w3engineers.com
* *
* * Purpose :
* *
* * Last Edited by : Md. Nomanur Rashid on 4/16/18.
* * History:
* * 1:
* * 2:
* *  
* * Last Reviewed by : Md. Nomanur Rashid on 4/16/18.
* ****************************************************************************
*/

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.util.Arrays;

import io.left.rightmesh.android.AndroidMeshManager;
import io.left.rightmesh.android.MeshService;
import io.left.rightmesh.id.MeshID;
import io.left.rightmesh.mesh.MeshManager;
import io.left.rightmesh.mesh.MeshStateListener;
import io.left.rightmesh.util.RightMeshException;
import io.reactivex.functions.Consumer;

import static io.left.rightmesh.mesh.MeshManager.ADDED;
import static io.left.rightmesh.mesh.MeshManager.DATA_RECEIVED;
import static io.left.rightmesh.mesh.MeshManager.PEER_CHANGED;
import static io.left.rightmesh.mesh.MeshManager.REMOVED;

public class RMController implements MeshStateListener{

    private final String TAG = "RMController";
    private static final int MESH_PORT = 54321;
    private AndroidMeshManager meshManager = null;
    private static RMController rmController = null;

    public static RMController on() {

       if (rmController== null){
           rmController = new RMController();
       }
        return rmController;
    }

    @Override
    public void meshStateChanged(MeshID meshID, int state) {
        if (state == MeshStateListener.SUCCESS) {

            Log.v(TAG, "Mesh initialize");
            try {
                // Binds this app to MESH_PORT.
                // This app will now receive all events generated on that port.
                meshManager.bind(MESH_PORT);
                // Subscribes handlers to receive events from the mesh.
                meshManager.on(DATA_RECEIVED, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handleDataReceived((MeshManager.RightMeshEvent) o);
                    }
                });
                meshManager.on(PEER_CHANGED, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handlePeerChanged((MeshManager.RightMeshEvent) o);
                    }
                });

            } catch (RightMeshException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * Get a {@link AndroidMeshManager} instance, starting RightMesh if it isn't already running.
     *
     * @param context service context to bind to
     */
    public void connect(Context context) {
        String ssid = "GADemo";
        //meshManager = AndroidMeshManager.getInstance(context, RightMeshController.this, ssid);
        meshManager = AndroidMeshManager.getInstance(context, RMController.this);
    }

    /**
     * Close the RightMesh connection, stopping the service if no other apps are running.
     */
    public void disconnect() {
        try {
            meshManager.stop();
        } catch (MeshService.ServiceDisconnectedException e) {
            e.printStackTrace();
        }
    }
    private void send(MeshID meshID, byte[] data) {
        if (meshManager != null) {
            try {
                meshManager.sendDataReliable(meshID, MESH_PORT, data);
            } catch (RightMeshException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDataReceived(MeshManager.RightMeshEvent e) {
        Log.v(TAG,"handleDataReceived()");

    }

    private void handlePeerChanged(MeshManager.RightMeshEvent e) {

        // Update peer list.
        MeshManager.PeerChangedEvent event = (MeshManager.PeerChangedEvent) e;

        if (event.state == ADDED) {
            Log.v(TAG,"User added");

        } else if (event.state == REMOVED) {
            Log.v(TAG,"User removed");

        }
    }
}
