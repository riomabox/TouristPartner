package com.example.putrosw.touristpartner.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.putrosw.touristpartner.R;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListNearDestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListNearDestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListNearDestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressDialog loading;
    private AlertDialog dialog;
    private View myFragmentView;

    private OnFragmentInteractionListener mListener;

    public ListNearDestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListNearDestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListNearDestFragment newInstance(String param1, String param2) {
        ListNearDestFragment fragment = new ListNearDestFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_list_near_dest, container, false);
        LinearLayout restaurant = (LinearLayout)myFragmentView.findViewById(R.id.lay_restaurant);
        LinearLayout spbu = (LinearLayout)myFragmentView.findViewById(R.id.lay_spbu);
        LinearLayout hotel = (LinearLayout)myFragmentView.findViewById(R.id.lay_hotel);
        LinearLayout hospital = (LinearLayout)myFragmentView.findViewById(R.id.lay_hospital);
        LinearLayout terminal = (LinearLayout)myFragmentView.findViewById(R.id.lay_bus);
        LinearLayout stasiun = (LinearLayout)myFragmentView.findViewById(R.id.lay_train);
        LinearLayout semua = (LinearLayout)myFragmentView.findViewById(R.id.lay_all);
        restaurant.setOnClickListener(operation);
        spbu.setOnClickListener(operation);
        hotel.setOnClickListener(operation);
        hospital.setOnClickListener(operation);
        terminal.setOnClickListener(operation);
        stasiun.setOnClickListener(operation);
        semua.setOnClickListener(operation);

        loading = new ProgressDialog(getContext());
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        return myFragmentView;
    }

    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.lay_restaurant:sembunyikanKeyBoard(v);
                    handleNearRestaurant();break;
                case R.id.lay_spbu:sembunyikanKeyBoard(v);
                    handleNearSpbu();break;
                case R.id.lay_hotel:sembunyikanKeyBoard(v);
                    handleNearHotel();break;
                case R.id.lay_hospital:sembunyikanKeyBoard(v);
                    handleNearHospital();break;
                case R.id.lay_bus:sembunyikanKeyBoard(v);
                    handleNearTerminal();break;
                case R.id.lay_train:sembunyikanKeyBoard(v);
                    handleNearStasiun();break;
                case R.id.lay_all:sembunyikanKeyBoard(v);
                    handleNearSemua();break;
            }
        }
    };

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button
                    HomeFragment homefragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).replace(R.id.frame, homefragment).commit();
                    return true;
                }

                return false;
            }
        });
    }

    public void handleNearRestaurant() {
        // update the main content by replacing fragments
        //Fragment fragment = getHomeFragment();

        //Ubah di sini//

        /*String item="food";
        NearDestFragment nearfragment = new NearDestFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args = new Bundle();
        args.putString("item", item);
        nearfragment.setArguments(args);
        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out).replace(R.id.frame, nearfragment).commit();*/
        // Clicking on items

        Intent intent = new Intent(getActivity().getApplication(), NearDestFragment.class);
        String item = "food";
        intent.putExtra("item",item);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleNearSpbu() {
        Intent intent = new Intent(getActivity().getApplication(), NearDestFragment.class);
        String item = "gas_station";
        intent.putExtra("item",item);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleNearHotel() {
        Intent intent = new Intent(getActivity().getApplication(), NearDestFragment.class);
        String item = "lodging";
        intent.putExtra("item",item);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleNearHospital() {
        Intent intent = new Intent(getActivity().getApplication(), NearDestFragment.class);
        String item = "hospital";
        intent.putExtra("item",item);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleNearTerminal() {
        Intent intent = new Intent(getActivity().getApplication(), NearDestFragment.class);
        String item = "bus_station";
        intent.putExtra("item",item);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleNearStasiun() {
        Intent intent = new Intent(getActivity().getApplication(), NearDestFragment.class);
        String item = "train_station";
        intent.putExtra("item",item);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleNearSemua() {
        Intent intent = new Intent(getActivity().getApplication(), NearDestFragment.class);
        String item = "food|gas_station|lodging|hospital|bus_station|train_station";
        intent.putExtra("item",item);
        System.out.println(item);
        startActivity(intent);
    }

    private void sembunyikanKeyBoard(View v){
        InputMethodManager a = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
