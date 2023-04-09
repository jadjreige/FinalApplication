package algonquin.cst2335.finalapplication.KittenPlaceholder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.databinding.ActivityKittenBinding;
import algonquin.cst2335.finalapplication.databinding.ActivityMainBinding;

public class KittenActivity extends AppCompatActivity {
    int width;
    int height;
    protected RequestQueue queue = null;
    KittenDAO kDAO;
    Bitmap image;
    ImageRequest imgReq;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);



        ActivityKittenBinding binding = ActivityKittenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        KittenDatabase db = Room.databaseBuilder(getApplicationContext(), KittenDatabase.class, "KittenDatabase").build();
        kDAO = db.kittenDAO();



        binding.submit.setOnClickListener( clk -> {
            width = Integer.parseInt(binding.widthEditText.getText().toString());
            height = Integer.parseInt(binding.heightEditText.getText().toString());


                URL = new StringBuilder().append("https://placekitten.com/").append(width).append("/").append(height).toString();
                imgReq = new ImageRequest(URL, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        try{
                            image = bitmap;
                            image.compress(Bitmap.CompressFormat.PNG, 100,
                                    KittenActivity.this.openFileOutput("kitten" + ".png", Activity.MODE_PRIVATE));
                            binding.imageButton.setImageBitmap(bitmap);
                            binding.imageButton.setVisibility(View.VISIBLE);
                            binding.saveButton.setVisibility(View.VISIBLE);


                            }catch (Exception e){e.printStackTrace();}


                    }
                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                    Toast.makeText(KittenActivity.this,"" + error , Toast.LENGTH_SHORT).show();
            });
            queue.add(imgReq);
        });

        binding.saveButton.setOnClickListener( clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = sdf.format(new Date());
            String name = new Random(5).toString();
            try {
                FileOutputStream fOut= openFileOutput("/storage/sdcard/kittens/" +name + ".png", Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG,100,fOut);
                fOut.flush();
                fOut.close();
            }catch(Exception e){e.printStackTrace();}

            Kitten kitten = new Kitten( width, height,name, currentDateTime );
            new Thread (() -> {
                kDAO.insertKitten(kitten);
            }).start();
            Toast.makeText(KittenActivity.this,"Image added to favourites", Toast.LENGTH_SHORT).show();
        });

        binding.imageButton.setOnClickListener( click -> {
            List<Kitten> kittens = kDAO.getAllKittens();
            String folderPath = "/storage/sdcard/kittens/";

            File folder = new File(folderPath);
            File[] kittenImages = folder.listFiles();
            for(int i=0 ; i< kittenImages.length; i++){
                File file = kittenImages[i];
                try{ FileInputStream inputStream = new FileInputStream(file);
                    Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();

                }catch(Exception e){}



            }



        });

    }

    class myImageHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        public myImageHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();

            });

        }
    }




}