package com.example.comfortable_apartment_100;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ParkingSpace extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    static Handler mMainHandler;

    CarAsynctask CA;

    static ImageView car1;
    static ImageView car2;
    static ImageView car3;
    static TextView remaining;
    static TextView danger;
    private static int[] howManyCar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    public static String car_array[] = null;


    public ParkingSpace() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static ParkingSpace newInstance(String param1, String param2,String param3) {
        ParkingSpace fragment = new ParkingSpace();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parking_space, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        car1 = (ImageView)getView().findViewById(R.id.white_car);
        car2 = (ImageView)getView().findViewById(R.id.blue_car);
        car3 = (ImageView)getView().findViewById(R.id.red_car);
        remaining = (TextView)getView().findViewById(R.id.remaining_space);
        danger = (TextView)getView().findViewById(R.id.danger);
        mMainHandler = new Handler(Looper.getMainLooper());
        howManyCar= new int[3];
//        car_array[3] = "0";
//        car_array[4] = "0";
//        car_array[5] = "0";

        carThread ct = new carThread();
        ct.start();
    }

    // TODO: Rename method, update argument and hook method into UI event


    public static class carThread extends Thread{
        @Override
        public void run() {
            super.run();
           int car_count=0;

                if(!(car_array==null)) {
                    Looper.prepare();
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (car_array[3].equals("1")) {
                                car1.setVisibility(View.VISIBLE);
                                howManyCar[0] = 0;
                            } else {
                                howManyCar[0] = 1;
                                car1.setVisibility(View.INVISIBLE);
                            }

                            if (car_array[4].equals("1")) {
                                howManyCar[1] = 0;
                                car2.setVisibility(View.VISIBLE);
                            } else {
                                howManyCar[1] = 1;
                                car2.setVisibility(View.INVISIBLE);
                            }

                            if (car_array[5].equals("1")) {
                                howManyCar[2] = 0;
                                car3.setVisibility(View.VISIBLE);
                            } else {
                                howManyCar[2] = 1;
                                car3.setVisibility(View.INVISIBLE);
                            }
                            //car_count = howManyCar[0] + howManyCar[1] + howManyCar[2];
                            remaining.setText((howManyCar[0] + howManyCar[1] + howManyCar[2]) + "자리가 남았습니다.");

                            if(MainActivity.siren >=3)
                            {
                                danger.setVisibility(View.VISIBLE);
                                MainActivity.siren = 0;
                            }
                            if(MainActivity.siren == 2)
                            {
                                danger.setVisibility(View.INVISIBLE);
                            }
                            car1.invalidate();
                            car2.invalidate();
                            car3.invalidate();
                        }
                    });
                    Looper.loop();
                }

        }
    }

    public class CarAsynctask extends AsyncTask<String,String,Boolean> {

        int car_count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            car_count=0;
        }


        @Override
        protected Boolean doInBackground(String... strings) {

//            while(true)
//            {
//                car_count=0;
                publishProgress();
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(car_array[3].equals("1")) {
                car1.setVisibility(View.VISIBLE);
                howManyCar[0] = 1;
            }
            else {
                howManyCar[0] = 0;
                car1.setVisibility(View.INVISIBLE);
            }

            if(car_array[4].equals("1")) {
                howManyCar[1] = 1;
                car2.setVisibility(View.VISIBLE);
            }
            else {
                howManyCar[1] = 0;
                car2.setVisibility(View.INVISIBLE);
            }

            if(car_array[5].equals("1")) {
                howManyCar[2] = 1;
                car3.setVisibility(View.VISIBLE);
            }
            else {
                howManyCar[2] = 0;
                car3.setVisibility(View.INVISIBLE);
            }
            car_count = howManyCar[0] + howManyCar[1] + howManyCar[2];
            remaining.setText(car_count+"자리가 남았습니다.");

//            car1.invalidate();
//            car2.invalidate();
//            car3.invalidate();

        }
    }




}
