package algonquin.cst2335.finalapplication.KittenPlaceholder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import androidx.lifecycle.ViewModelProvider;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import algonquin.cst2335.finalapplication.R;

import algonquin.cst2335.finalapplication.databinding.ActivityKittenBinding;
import algonquin.cst2335.finalapplication.databinding.KittenImagesBinding;


public class KittenActivity extends AppCompatActivity {
    ActivityKittenBinding binding;
    int width;
    int height;
    protected RequestQueue queue = null;
    KittenDAO kDAO;
    Bitmap image;
    ImageRequest imgReq;
    String URL;
    private RecyclerView.Adapter myAdapter;
    List<Kitten> favKittens = new ArrayList<>();
    FileInputStream fOut = null;
    List<Bitmap> kittenImages ;
    KittenImageViewModel kittenModel;
    List<String> urls = null;
    KittenImagesBinding imageBinding;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.kitten_menu, menu);
        new Thread(() -> {

            favKittens = kDAO.getAllKittens();

        }).start();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(KittenActivity.this);
                builder.setMessage(R.string.helpKitten).setPositiveButton(null, null).
                        setNegativeButton("Okay", (dialog, cl) -> {
                        }).setTitle("Help").create().show();
                break;
            case R.id.favourite1:

                binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                kittenModel = new ViewModelProvider(this).get(KittenImageViewModel.class);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.heightEditText.setVisibility(View.INVISIBLE);
                binding.widthEditText.setVisibility(View.INVISIBLE);
                binding.saveButton.setVisibility(View.INVISIBLE);
                binding.imageButton.setVisibility(View.INVISIBLE);
                binding.width.setVisibility(View.INVISIBLE);
                binding.height.setVisibility(View.INVISIBLE);
                binding.submit.setVisibility(View.INVISIBLE);
                binding.recyclerView.setVisibility(View.VISIBLE);



                //for (int i = 0; i < favKittens.size(); i++) {

                //  URL = new StringBuilder().append("https://placekitten.com/").append(favKittens.get(i).getWidth()).append("/").append(favKittens.get(i).getHeight()).toString();
                //  urls.add(URL);

                    /*imgReq = new ImageRequest(URL, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            image = bitmap;
                            String name = width + "-" + height;

                            binding.imageButton.setImageBitmap(bitmap);
                            binding.imageButton.setVisibility(View.VISIBLE);
                            binding.saveButton.setVisibility(View.VISIBLE);

                        }
                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                        Toast.makeText(KittenActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    });
                    queue.add(imgReq);*/


                break;

            case R.id.newImages:

                binding.recyclerView.setVisibility(View.INVISIBLE);
                binding.heightEditText.setVisibility(View.VISIBLE);
                binding.widthEditText.setVisibility(View.VISIBLE);
                binding.saveButton.setVisibility(View.INVISIBLE);
                binding.imageButton.setVisibility(View.INVISIBLE);
                binding.width.setVisibility(View.VISIBLE);
                binding.height.setVisibility(View.VISIBLE);
                binding.submit.setVisibility(View.VISIBLE);


                break;
            //TO-DO: what happens when menu items are clicked (help,view all kittens)
        }
                return true;
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);


        binding = ActivityKittenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get a SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);

        // Store the strings
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString("width", "") == null || sharedPreferences.getString("height", "")== null) {


            editor.putString("width", "5");
            editor.putString("height", "5");
            editor.commit();
        }
        // Retrieve the strings
        binding.widthEditText.setText(sharedPreferences.getString("width", "").toString());
        binding.heightEditText.setText(sharedPreferences.getString("height", "").toString());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        kittenModel = new ViewModelProvider(this).get(KittenImageViewModel.class);


        setSupportActionBar(binding.myToolbar);
        invalidateOptionsMenu();

        KittenDatabase db = Room.databaseBuilder(getApplicationContext(), KittenDatabase.class, "KittenDatabase").build();
        kDAO = db.kittenDAO();


        binding.submit.setOnClickListener(clk -> {
            if (binding.widthEditText.getText().toString() != null && binding.heightEditText.getText().toString() != null ) {
                try {
                    width = Integer.parseInt(binding.widthEditText.getText().toString());
                    height = Integer.parseInt(binding.heightEditText.getText().toString());

                }catch (Exception e){}
            }
            editor.putString("width", String.valueOf(width));
            editor.putString("height", String.valueOf(height));
            editor.commit();

            URL = new StringBuilder().append("https://placekitten.com/").append(width).append("/").append(height).toString();
            imgReq = new ImageRequest(URL, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    image = bitmap;
                    String name = width + "-" + height;

                        binding.imageButton.setImageBitmap(bitmap);
                        binding.imageButton.setVisibility(View.VISIBLE);
                        binding.saveButton.setVisibility(View.VISIBLE);

                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                Toast.makeText(KittenActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            });
            queue.add(imgReq);
        });

        binding.saveButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = sdf.format(new Date());
            String path = "/data/user/0/algonquin.cst2335.finalapplication/files/kittens";
            String name = width + "-" + height;
        try {

            image.compress(Bitmap.CompressFormat.PNG, 100,
                    KittenActivity.this.openFileOutput(name + ".png", Activity.MODE_PRIVATE));
             } catch (Exception e) {
            e.printStackTrace();}
            name = name +".png";
            Kitten kitten = new Kitten(width, height, name, currentDateTime);
            String finalName = name;
            new Thread(() -> {
                favKittens = kDAO.getAllKittens();
            }).start();
                boolean exist = false;
                for(int i=0; i<favKittens.size();i++){
                    String name1 = favKittens.get(i).getFileName();
                    if(name1.equals(finalName)){
                        exist = true;
                        Toast.makeText(KittenActivity.this, R.string.existKitten, Toast.LENGTH_SHORT).show();break;
                    }
                }
                if(!exist) {
                    new Thread(() -> {
                        kDAO.insertKitten(kitten);
                    }).start();
                }
            Toast.makeText(KittenActivity.this, R.string.addedKitten, Toast.LENGTH_SHORT).show();
        });

        //binding.imageButton.setOnClickListener(click -> {
          //  new Thread(() -> {
            //    favKittens = kDAO.getAllKittens();
          //  }).start();
      //  });
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<myImageHolder>() {
            @NonNull
            @Override
            public myImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater li = LayoutInflater.from(parent.getContext());
                View thisRow = li.inflate(R.layout.kitten_images,parent,false);
                return new myImageHolder(thisRow);
            }

            @Override
            public void onBindViewHolder(@NonNull myImageHolder holder, int position) {
                Kitten obj = favKittens.get(position);

                URL = "https://placekitten.com/"+obj.getWidth()+"/"+obj.getHeight();
                    Picasso.get().load(URL).into(holder.imageButton);

            //holder.imageButton.setImageBitmap();
//            holder.kittenDetails.setText("height: " +obj.getHeight() + "width: " + width);
            }

            @Override
            public int getItemCount(){
                return favKittens.size();
            }

        });
    }
    class myImageHolder extends RecyclerView.ViewHolder {
        ImageView imageButton;
        TextView kittenDetails;
        Button removeImage;
        public myImageHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();

            });
            imageButton = itemView.findViewById(R.id.kittenImageButton);
            removeImage = itemView.findViewById(R.id.removeImage);

            removeImage.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                Kitten deletedKitten = favKittens.get(getAbsoluteAdapterPosition());
                new Thread(() -> {
                kDAO.deleteKitten(deletedKitten);
                }).start();
                myAdapter.notifyItemRemoved(getAbsoluteAdapterPosition());
                Snackbar.make(imageButton, R.string.deletedKitten, Snackbar.LENGTH_LONG)
                        .setAction("Undo", click -> {

                            new Thread(() -> {
                                favKittens.add(position, deletedKitten);
                                kDAO.insertKitten(deletedKitten);
                            }).start();

                            myAdapter.notifyItemInserted(position);
                        }).show();

            });
            imageButton.setOnClickListener(clk -> {

                int position = getAbsoluteAdapterPosition();
                Kitten fragKitten = favKittens.get(position);
                KittenDetailsFragment fragment = new KittenDetailsFragment(fragKitten);
                getSupportFragmentManager().beginTransaction().replace(R.id.KittenFragmentLocation, fragment)
                        .addToBackStack("").commit();

            });
        }
    }




}