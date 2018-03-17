package com.example.roadin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);


       /* @Override
        public void onViewCreated (View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);*/

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

}