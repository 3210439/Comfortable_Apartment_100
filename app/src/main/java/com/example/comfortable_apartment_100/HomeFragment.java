package com.example.comfortable_apartment_100;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.graphics.Color.RED;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link HomeFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView temperture_text;
    TextView humidity_text;
    TextView noise_text;
    TextView tax;

    ImageView sad;
    ImageView soso;
    ImageView good;
    ImageView happy;

    TextView weekState;
    public static String string[] = null;
    MyAsynctask myAsynctask;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weekState = (TextView) getView().findViewById(R.id.td_state);
        temperture_text = (TextView)getView().findViewById(R.id.temperture);
        humidity_text= (TextView)getView().findViewById(R.id.humidity);
        noise_text = (TextView)getView().findViewById(R.id.noise);
        tax = (TextView)getView().findViewById(R.id.tax);
        tax.setText("저번달 요금은 40,000원 입니다.");
        Spannable span = (Spannable)tax.getText();
        span.setSpan(new ForegroundColorSpan(RED), 8,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        weekState.setText(MainActivity.getDateDay());
        weekState.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Howto.class);
                startActivity(intent);
            }
        });
        sad = (ImageView)getView().findViewById(R.id.sad_emo);
        soso = (ImageView)getView().findViewById(R.id.soso_emo);
        good = (ImageView)getView().findViewById(R.id.good_emo);
        happy = (ImageView)getView().findViewById(R.id.happy_emo);
        myAsynctask= new MyAsynctask();
        myAsynctask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //weekState = (TextView) getView().findViewById(R.id.td_state);
        //weekState.setText("");

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public class MyAsynctask extends AsyncTask<String,String,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Boolean doInBackground(String... strings) {
            while(true)
            {
                if(!(string==null) ) {
                    publishProgress();
                }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                 // if

            }
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            temperture_text.setText("온도: "+string[0]+" ℃");
            humidity_text.setText("습도: " +string[1]+" rh");
            noise_text.setText("소음: " + string[2] + " dB");
            int n1 = Integer.parseInt(string[0]);
            int n2 = Integer.parseInt(string[1]);
            double discomfort = 0.72*(n1+n2)+40.6;

            if(discomfort >= 86) {
                sad.setVisibility(View.VISIBLE);
                soso.setVisibility(View.INVISIBLE);
                good.setVisibility(View.INVISIBLE);
                happy.setVisibility(View.INVISIBLE);
            }
            else if(discomfort <86 && discomfort >= 80) {
                soso.setVisibility(View.VISIBLE);
                sad.setVisibility(View.INVISIBLE);
                good.setVisibility(View.INVISIBLE);
                happy.setVisibility(View.INVISIBLE);
            }
            else if(discomfort <80 && discomfort >= 70) {
                good.setVisibility(View.VISIBLE);
                soso.setVisibility(View.INVISIBLE);
                sad.setVisibility(View.INVISIBLE);
                happy.setVisibility(View.INVISIBLE);
            }
            else if(discomfort <70) {
                happy.setVisibility(View.VISIBLE);
                soso.setVisibility(View.INVISIBLE);
                good.setVisibility(View.INVISIBLE);
                sad.setVisibility(View.INVISIBLE);
            }

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
