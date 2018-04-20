package com.example.roadin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment   {


    public CameraFragment() {
        // Required empty public constructor
    }

    //View view;
    View view;
    Button firstButton;
    Button secondbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);


       /* @Override
        public void onViewCreated (View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
           // Toast.makeText(getContext(), "one", Toast.LENGTH_SHORT).show();
           firstButton = (Button) view.findViewById(R.id.firstButton);
            firstButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), camActivity.class);
                    getActivity().startActivity(intent);

                }
            });
            return view;
        }
        else
        {
           // Toast.makeText(getContext(), "two", Toast.LENGTH_SHORT).show();
            secondbutton = (Button) view.findViewById(R.id.firstButton);
            secondbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "two", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new SignInFragment();

                    FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.mainLayout, fragment).commit();
                }
            });
            return view;
        }

    }

}



