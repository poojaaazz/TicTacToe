package com.pooja.tictactoe;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.pooja.tictactoe.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0,0,0,0,0,0,0,0,0}; //9 zero
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;

    private InterstitialAd mInterstitialAd;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaPlayer = MediaPlayer.create(this, R.raw.click);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(100,100);

        AdView adview = findViewById(R.id.banner);
        AdView adview2 = findViewById(R.id.banner2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
        adview2.loadAd(adRequest);


        AdListener listener = new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d("TAG", "onAdFailedToLoad: " + adError.getMessage());
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.d("TAG", "onAdLoaded: ");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        };

        adview.setAdListener(listener);
        adview2.setAdListener(listener);


        InterstitialAd.load(this,getString(R.string.interstitialAdId), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        setCallback();
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", " failedToLoadInterstitial "+ loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });




        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});

        String  getPlayerOneName = getIntent().getStringExtra("playerOne");
        String  getPlayerTwoName = getIntent().getStringExtra("playerTwo");

        binding.playerOneName.setText(getPlayerOneName);
        binding.playerTwoName.setText(getPlayerTwoName);

        binding.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(0)){
                    performAction((ImageView) v, 0 );
                }
            }
        });

        binding.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(1)){
                    performAction((ImageView) v, 1 );
                }
            }
        });
        binding.image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(2)){
                    performAction((ImageView) v, 2 );
                }
            }
        });
        binding.image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(3)){
                    performAction((ImageView) v, 3 );
                }
            }
        });
        binding.image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(4)){
                    performAction((ImageView) v, 4 );
                }
            }
        });
        binding.image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(5)){
                    performAction((ImageView) v, 5 );
                }
            }
        });
        binding.image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(6)){
                    performAction((ImageView) v, 6 );
                }
            }
        });
        binding.image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(7)){
                    performAction((ImageView) v, 7 );
                }
            }
        });
        binding.image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(8)){
                    performAction((ImageView) v, 8 );
                }
            }
        });
    }
    private void performAction(ImageView imageView,int selectedBoxPosition){
        boxPositions[selectedBoxPosition] = playerTurn;
        playClickSound();



        if (playerTurn ==1){
            imageView.setImageResource(R.drawable.xx);
            if (checkResults()){
                showResultDialog(binding.playerOneName.getText().toString()+ " is a Winner!");
            } else if (totalSelectedBoxes == 9) {
                showResultDialog("Match Draw");

            }else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        }else {
            imageView.setImageResource(R.drawable.oo);
            if (checkResults()){
                showResultDialog((binding.playerTwoName.getText().toString()+ " is a Winner!"));
            } else if (totalSelectedBoxes == 9) {
                showResultDialog("Match Draw");

            }else {


                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }
    private  boolean  isBoxSelectable(int boxIndex){
        return boxPositions[boxIndex] == 0;
    }

    private void changePlayerTurn(int playerIndex){
        playerTurn = playerIndex;
    }

    private boolean checkResults(){

        for (int i = 0; i < combinationList.size(); i++){

            int[] possiblity = combinationList.get(i);
            int first = boxPositions[possiblity[0]];
            int second = boxPositions[possiblity[1]];
            int third = boxPositions[possiblity[2]];
            if ( (first == 1 && second == 1 && third == 1) || (first == 2 && second == 2 && third == 2)){
                return  true;
            }

        }

        return false;

    }

    private void showResultDialog(String message){
        ResultDialog resultDialog = new ResultDialog(MainActivity.this,message, MainActivity.this);
        resultDialog.setCancelable(false);
//        Button startAgainButton = resultDialog.findViewById(R.id.startAgainButton);
//        startAgainButton.setOnClickListener((v)-> {
//            this.recreate();
//        });
        resultDialog.show();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }


    private void setCallback(){


        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d("TAG", "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d("TAG", "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e("TAG", "Ad failed to show fullscreen content. "+adError.getMessage());
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d("TAG", "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d("TAG", "Ad showed fullscreen content.");
            }
        });


    }


    private void playClickSound() {
       if (mediaPlayer.isPlaying()){
           mediaPlayer.stop();
           try {
               mediaPlayer.prepare();
           } catch (IOException e) {
               Log.e("TAG", "playClickSound: ", e);
           }
       }
        mediaPlayer.start();
    }
}

