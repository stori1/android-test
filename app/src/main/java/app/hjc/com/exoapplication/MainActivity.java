package app.hjc.com.exoapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private String uriAddress = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private String uriAddress1 = "https://v-cdn.zjol.com.cn/280443.mp4";
    private String hlsAddress = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.e("TAG", "AAA");
                    break;
                case 2:
                    Log.e("TAG", "BBB");
                    break;
                    default:
                        break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }


    public void initPlayer(){
        playerView = findViewById(R.id.player_view);
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        playerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = ((BaseApplication)getApplication()).buildDataSourceFactory();
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(uriAddress1));
//        HlsMediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(hlsAddress));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    private void requestPermission() {
        int checkInternet = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        String[] permissions = {Manifest.permission.INTERNET};
        if (checkInternet != 0){
            ActivityCompat.requestPermissions(this, permissions, 1);
        }else {
            initPlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "申请网络权限成功", Toast.LENGTH_SHORT).show();
                    initPlayer();
                }else {
                    Toast.makeText(this, "申请网络权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        playerView.setPlayer(null);
        player.release();
        player = null;
    }

}
