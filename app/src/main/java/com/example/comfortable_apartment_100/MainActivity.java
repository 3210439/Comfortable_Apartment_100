package com.example.comfortable_apartment_100;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ImageView sendBtn;
    EditText msgEdit;

    boolean flagConnection = true;
    boolean isConnected = false;
    boolean flagRead = true;
    TextView tempertureText;
    static ArrayList<Fragment> fragments;

    //Handler writeHandler;
    boolean isBinOK = false;
    Socket socket;
    BufferedInputStream bin;
    BufferedOutputStream bout;

    SocketThread st;
    ReadThread rt;

    String serverIp="192.168.0.120";
    int serverPort=9874;

    private final String dbName = "webnautes";
    private final String tableName = "boxState";

    private String names[];
    {
        names = new String[]{"input", "output", "input", "output", "input", "output"
                , "input", "output", "input"};
    }

    private final String reportDays[];
    {
        reportDays = new String[]{"19.08.04_12:00", "19.08.04_12:01", "19.08.04_12:02",
                "19.08.04_12:03", "19.08.04_12:04", "19.08.04_12:05", "19.08.04_12:06",
                "19.08.04_12:07", "19.08.04_12:08"};
    }

    ArrayList<HashMap<String, String>> boxStateList;
    private static final String TAG_NAME = "name";
    private static final String TAG_reportDay ="reportDay";

    SQLiteDatabase sampleDB = null;
    ViewPager pager;

    FragmentManager manager;
    //HomeFragment homeFragment;
    deliveryFragment df;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    pager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                     pager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    pager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==10){

                showToast("서버 연결 성공");

            }else if(msg.what==20){

                showToast("서버 연결 실패");
            }else if(msg.what==100){
                showToast((String) msg.obj);
                String str = (String)msg.obj;
                String[] arrayFromServer =  str.split("&");
                HomeFragment.string = arrayFromServer;
                ParkingSpace.car_array = arrayFromServer;
                ParkingSpace.carThread ct = new ParkingSpace.carThread();
                ct.start();

            }else if(msg.what==200){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        manager = getSupportFragmentManager();

        boxStateList = new ArrayList<HashMap<String,String>>();
        pager = (ViewPager)findViewById(R.id.pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        tempertureText = (TextView)findViewById(R.id.temperture);

        try {
            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            //테이블이 존재하지 않으면 새로 생성합니다.
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                    + " (name VARCHAR(20), reportDay VARCHAR(20) );");

            //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
            sampleDB.execSQL("DELETE FROM " + tableName  );

            //새로운 데이터를 테이블에 집어넣습니다..
            for (int i=0; i<names.length; i++ ) {

                if(names[i].equals("input")) {
                    sampleDB.execSQL("INSERT INTO " + tableName
                            + " (name, reportDay)  Values ('" + "택배 보관함에 택배가 있습니다." + "'" +
                            ", '" + reportDays[i] + "');");
                }
                else
                {
                    sampleDB.execSQL("INSERT INTO " + tableName
                            + " (name, reportDay)  Values ('" + "택배를 수령하셨습니다." + "'" +
                            ", '" + reportDays[i] + "');");
                }
            }
            sampleDB.close();

            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);


            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);

            if (c != null) {


                if (c.moveToFirst()) {
                    do {

                        //테이블에서 두개의 컬럼값을 가져와서
                        String Name = c.getString(c.getColumnIndex("name"));
                        String reportDay = c.getString(c.getColumnIndex("reportDay"));

                        //HashMap에 넣습니다.
                        HashMap<String,String> boxStates = new HashMap<String,String>();

                        boxStates.put(TAG_NAME,Name);
                        boxStates.put(TAG_reportDay,reportDay);

                        //ArrayList에 추가합니다..
                        boxStateList.add(boxStates);

                    } while (c.moveToNext());
                    Collections.reverse(boxStateList);
                }
            }

            ReadDB.close();

        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());


        }
        df = new deliveryFragment(boxStateList);

       // homeFragment = HomeFragment.newInstance(" 여긴 안사용중"," 여긴 안사용중");


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navView.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 1:
                        navView.setSelectedItemId(R.id.navigation_dashboard);
                        break;
                    case 2:
                        navView.setSelectedItemId(R.id.navigation_notifications);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    } // onCreate finish

    @Override
    protected void onStart() {
        super.onStart();
        st=new SocketThread();
        st.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flagConnection = false;
        isConnected = false;

        if (socket != null) {
            flagRead = false;
            try {
                bout.close();
                bin.close();
                socket.close();
                socket.close();

            } catch (Exception e) {
            }
        }
    }

    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    class SocketThread extends Thread {

        public void run() {


            while (flagConnection){
                try {
                    if(!isConnected) {
                        socket = new Socket();
                        SocketAddress remoteAddr = new InetSocketAddress(serverIp, serverPort);
                        socket.connect(remoteAddr, 10000);
                        Log.d("ClientThread","배룩스 시스템 성공");
                        isBinOK=true;

                        bin = new BufferedInputStream(socket.getInputStream());
                        if (rt != null) {
                            flagRead = false;
                        }

                        rt = new ReadThread();
                        rt.start();

                        isConnected = true;

                        Message msg = new Message();
                        msg.what = 10;
                        mainHandler.sendMessage(msg);
                        sleep(1000);
                    }else {
                        SystemClock.sleep(10000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    SystemClock.sleep(10000);
                }
            }

        }
    }



    class ReadThread extends Thread {

        @Override
        public void run() {

            byte[] buffer=null;
            while (flagRead){
                buffer=new byte[1024];
                try {
                    String message=null;
                    int size=bin.read(buffer);
                    if(size>0){
                        message=new String(buffer, 0, size, "utf-8");
                        if(message != null && !message.equals("")){
                            Message msg=new Message();
                            msg.what=100;
                            msg.obj=message;
                            mainHandler.sendMessage(msg);
                        }
                    }else {
                        flagRead=false;
                        isConnected=false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    flagRead=false;
                    isConnected=false;
                }
            }
            Message msg=new Message();
            msg.what=20;
            mainHandler.sendMessage(msg);
        }

    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments=new ArrayList<>();
            fragments.add(new HomeFragment());
            fragments.add(new ParkingSpace());
            fragments.add(new deliveryFragment(boxStateList));

        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);

        }
    }

    public static String getDateDay() {

        String day = "";

//        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
//        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
//        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "오늘은 종이류 버리는 날"; // 일
                break;
            case 2:
                day = "오늘은 폐식용유 버리는 날"; // 월
                break;
            case 3:
                day = "오늘은 아파트 분리수거 하는날"; // 화
                break;
            case 4:
                day = "오늘은 플라스틱 버리는 날"; // 수
                break;
            case 5:
                day = "오늘은 종이류 버리는 날"; // 목
                break;
            case 6:
                day = "오늘은 종이류 버리는 날";
                break;
            case 7:
                day = "오늘은 플라스틱 버리는 날";
                break;

        }

        return day;
    }

    class ClientThread extends Thread {
        public void run() {
            String host = "localhost";
            int port = 5001;

            try {
                Socket socket = new Socket(host, port);

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                final String input = (String) inputStream.readObject(); // Object로 받아도 무방
                Log.d("ClientThread","받은 데이터 : "+input);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}
