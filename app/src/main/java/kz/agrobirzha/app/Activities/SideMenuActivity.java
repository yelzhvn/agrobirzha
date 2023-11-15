package kz.agrobirzha.app.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import kz.agrobirzha.app.Fragments.AboutFragment;
import kz.agrobirzha.app.Fragments.ContactsFragment;
import kz.agrobirzha.app.Fragments.MyitemsFragment;
import kz.agrobirzha.app.Fragments.PostofferFragment;
import kz.agrobirzha.app.Fragments.SigninFragment;
import kz.agrobirzha.app.Fragments.SignupFragment;
import kz.agrobirzha.app.R;

public class SideMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();

        String page = intent.getStringExtra("page");
        if(page.equals("signin")){
            setFragment(new SigninFragment());
        }else if(page.equals("signup")){
            setFragment(new SignupFragment());
        }else if(page.equals("ploffer")){
            setFragment(new PostofferFragment());
        }else if(page.equals("myitems")){
            setFragment(new MyitemsFragment());
        }else if(page.equals("contacts")){
            setFragment(new ContactsFragment());
        }else if(page.equals("about")){
            setFragment(new AboutFragment());
        }
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setFragment(android.support.v4.app.Fragment Fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sideframe,Fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void onBackPressed(){
        finish();
    }

}
