package com.example.maurice.menmeindopdr;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.maurice.menmeindopdr.NSData.Station;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

//public class MapsViewModel extends ViewModel implements StationAvailableListener, SensorEventListener {
//    private final static String TAG = MapsViewModel.class.getSimpleName();
//    private MutableLiveData<List<Station>> stations;
//    private SensorManager mSensorManager;
//    private boolean saveNotFound;
//    private float[] mGravity = new float[3];
//    private float[] mGeomagnetic = new float[3];
//    private float azimuth;
//    private float correctAzimuth;
//    private RotationDeviceListener rotationDeviceListener;
//
////    public LiveData<List<Station>> getPointOfInterests(Integer selectedRoute) {
////        saveNotFound = false;
////        if (stations == null) {
////            stations = new MutableLiveData<>();
////            readRouteFromFile(selectedRoute);
////            switch(selectedRoute){
////                case SelectedRoute.BLIND_WALLS:
////                    if(saveNotFound || stations.getValue() == null)
////                        loadBlindWalls();
////                    break;
////                case SelectedRoute.HISTORIC_KM:
////                    if(saveNotFound || stations.getValue() == null)
////                        loadHistorics();
////                    break;
////                case SelectedRoute.ALL:
////                    loadAll();
////                    break;
////            }
////        }
////        return stations;
////    }
//
//    private void readRouteFromFile(int selectedRoute) {
//        String fileName = "";
//        if(selectedRoute == SelectedRoute.BLIND_WALLS){
//            fileName = "saved_blindwalls";
//        } else if(selectedRoute == SelectedRoute.HISTORIC_KM){
//            fileName = "saved_historics";
//        }
//        try {
//            FileInputStream fis = App.getContext().openFileInput(fileName);
//            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
//            List<PointOfInterest> pointsOfInterest = (ArrayList<PointOfInterest>) objectInputStream.readObject();
//            if(pointsOfInterest != null)
//                this.stations.setValue(pointsOfInterest);
//        } catch (IOException e) {
//            saveNotFound = true;
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private VolleyHandler handler;
//
//    public MapsViewModel() {
//        handler = new VolleyHandler(this);
//        mSensorManager = (SensorManager) App.getContext().getSystemService(Context.SENSOR_SERVICE);
//    }
//
//    private void loadHistorics(){
//        this.stations.setValue(JsonUtil.loadHistorischeKilometer());
//    }
//
//    private void loadBlindWalls() {
//        handler.connectToBlindwalls(RequestState.ONE);
//    }
//
//    public void onResume(){
//        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (accelerometer != null) {
//            mSensorManager.registerListener(this, accelerometer,
//                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
//        }
//        Sensor magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        if (magneticField != null) {
//            mSensorManager.registerListener(this, magneticField,
//                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
//        }
//    }
//
//    public void onPause(){
//        mSensorManager.unregisterListener(this);
//    }
//
//    private void loadAll() {
//        handler.connectToBlindwalls(RequestState.ALL);
//    }
//
//    public void setRotationDeviceListener(RotationDeviceListener rotationDeviceListener) {
//        this.rotationDeviceListener = rotationDeviceListener;
//    }
//
//    @Override
//    public void onPointOfInterestsAvailable(List<PointOfInterest> pointOfInterests, RequestState state) {
//        switch (state) {
//            case ALL:
//                List<PointOfInterest> points = new ArrayList<>();
//                points.addAll(JsonUtil.loadHistorischeKilometer());
//                points.addAll(pointOfInterests);
//
//                this.stations.setValue(points);
//                break;
//            case ONE:
//                this.stations.setValue(pointOfInterests);
//                break;
//        }
//    }
//
//    // Get readings from accelerometer and magnetometer. To simplify calculations,
//    // consider storing these readings as unit vectors.
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        int type = event.sensor.getType();
//
//        if(type == Sensor.TYPE_ACCELEROMETER){
//            mGravity = event.values.clone();
//        }
//
//        if(type == Sensor.TYPE_MAGNETIC_FIELD){
//            mGeomagnetic = event.values.clone();
//        }
//
//        if (mGravity != null && mGeomagnetic != null) {
//            float[] R = new float[9];
//            float[] I = new float[9];
//            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//            if (success) {
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//                azimuth = (float) Math.toDegrees(orientation[0]);
//                azimuth = (azimuth + 360) % 360;
//
//                if(rotationDeviceListener != null)
//                    rotationDeviceListener.onScreenRotate(correctAzimuth, azimuth);
//
//                correctAzimuth = azimuth;
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
//}