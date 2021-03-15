package com.example.museaapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.SingletonDataHolder;

public class MuseuActivity extends AppCompatActivity {

    boolean loved = false;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museu);
        //setTitle(getIntent().getExtras().getString("Name"));
        // Setting the action bar buttons
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setDisplayHomeAsUpEnabled(true);



        // Set the image we get from previous activity
        String image = SingletonDataHolder.getInstance().getCodedImage();
        ImageView imageView = findViewById(R.id.image_holder);
        imageView.setImageBitmap(stringToImage(image));

        TextView txtV = findViewById(R.id.text_view);
        Bundle b = getIntent().getExtras();
        Museo museum = (Museo)b.getSerializable("Museu");
        setTitle(museum.getName());

        /*txtV.setText(museum.getCountry() + "\n"
                +    museum.getCity() + "\n"
                +    museum.getAddress() + "\n"
                +    museum.getDescriptions().getEn());*/

        txtV.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas mattis velit felis, lobortis euismod sem iaculis in. Aenean imperdiet congue consectetur. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eleifend ipsum a enim porta, et mattis metus bibendum. Quisque eu ullamcorper ligula. Nunc dictum velit sit amet nunc suscipit, id sagittis dolor mattis. Proin in eros sodales, tincidunt justo vitae, consectetur ante. In quis libero leo. Cras consectetur sit amet eros eu sagittis. Duis metus sem, tempus id nulla vitae, convallis pretium tortor. Quisque varius tincidunt nisi, vel pulvinar nisl posuere sit amet. Quisque tortor neque, pretium ut varius quis, volutpat nec ipsum. Nam ultrices commodo ultricies. Aliquam rutrum id tellus eu placerat. Cras vehicula orci in facilisis condimentum.\n" +
                "\n" +
                "Proin ultrices augue molestie velit tincidunt, vitae bibendum metus vestibulum. Nullam ac odio congue, eleifend ex quis, ultricies purus. Vivamus ornare libero vitae facilisis sagittis. Sed gravida maximus libero, ut eleifend ante iaculis et. Phasellus accumsan id augue ac rhoncus. Integer non porttitor odio. Praesent rhoncus et sapien sed mollis.\n" +
                "\n" +
                "Vestibulum diam massa, dictum nec vehicula sit amet, tincidunt ut purus. Suspendisse vestibulum arcu velit, vel mattis velit cursus vitae. Donec varius viverra risus, congue dictum diam iaculis sed. Nulla tempus rhoncus ex, eget venenatis justo accumsan vestibulum. Donec tristique mi elit, nec mattis elit ultrices quis. Integer tempor tellus nec justo congue, in sollicitudin neque viverra. Duis blandit dui arcu, in rhoncus mauris maximus vitae. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.\n" +
                "\n" +
                "Cras dictum nulla sed dui mattis, eu interdum nisi ultrices. Pellentesque a purus nibh. Nunc ultricies erat at nibh eleifend placerat. Maecenas laoreet sodales arcu quis sagittis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum eu mauris vitae dui euismod hendrerit sed eu orci. Cras libero libero, pharetra vitae imperdiet id, iaculis sit amet odio. Pellentesque ornare convallis leo. Morbi hendrerit dolor non diam euismod, vel facilisis nisl euismod. Curabitur viverra dolor at cursus efficitur. Cras et maximus risus. Praesent eu felis nisi. In ligula nisi, mollis in nunc ut, scelerisque volutpat arcu. Vivamus malesuada molestie dui, eu commodo est.\n" +
                "\n" +
                "Maecenas bibendum diam et tellus facilisis semper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Etiam ut vulputate lorem. Mauris eu massa at elit consequat pellentesque. Ut sit amet faucibus dolor. Phasellus faucibus finibus diam, vel porttitor justo. Proin in justo sagittis dolor ullamcorper dictum in sit amet elit. Nulla justo nibh, dapibus id commodo vel, accumsan non nisl. Donec sollicitudin sapien justo, eu facilisis velit bibendum nec.");

    }


    public void goBack(View v) {
        finish();
    }
    void love() {
        loved = !loved;
    }

    Bitmap stringToImage(String codeImage){
        byte[] imageBytes = Base64.decode(codeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mybutton:
                love();
                if (loved) {
                    item.setIcon(R.drawable.filled_heart);
                }else {
                    item.setIcon(R.drawable.heart_empty);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}