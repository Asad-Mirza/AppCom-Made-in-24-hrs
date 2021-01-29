package com.example.admin.appcom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link list_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link list_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class list_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SortedList<contactPOJO> data;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private OnFragmentInteractionListener mListener;
    private sharedPrefData prefData;

    public list_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment list_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static list_frag newInstance(String param1, String param2) {
        list_frag fragment = new list_frag();
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
   contact_adapter  adapter;
    void update(){
        SQLiteHelper helper  =new SQLiteHelper(getActivity(),new sharedPrefData().getUserData(getActivity()).getEmail());
        data = helper.getAllCotacts();
        adapter.notifyDataSetChanged();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_list_frag, container, false);
      recyclerView = view.findViewById(R.id.recyclerview);
      swipeRefreshLayout =view.findViewById(R.id.swiperef);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setSmoothScrollbarEnabled(true);
       final SQLiteHelper helper  =new SQLiteHelper(getActivity(),new sharedPrefData().getUserData(getActivity()).getEmail());
        data = helper.getAllCotacts();

        recyclerView.setLayoutManager(linearLayoutManager);
     final contact_adapter  adapter  = new contact_adapter(data,getActivity());

     recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                data = helper.getAllCotacts();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });



    return view;
    }

    @Override
    public void onResume() {
        contact_adapter  adapter  = new contact_adapter(data,getActivity());
        super.onResume();

        data = new SQLiteHelper(getActivity(),new sharedPrefData().getUserData(getActivity()).getEmail()).getAllCotacts();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

   public class contact_adapter extends RecyclerView.Adapter<contact_adapter.myviewholder> {
        LayoutInflater layoutInflater;


        private Context mContext;

        private SortedList<contactPOJO> data;



       contact_adapter(SortedList<contactPOJO> data, Context mContext) {
            this.mContext = mContext;
            this.data = data;
            this.layoutInflater  = LayoutInflater.from(this.mContext);

        }


        @Override
        public contact_adapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =layoutInflater.inflate(R.layout.contact_row_layout, null);
            contact_adapter.myviewholder viewHolder = new contact_adapter.myviewholder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(contact_adapter.myviewholder holder, int position) {

            final contactPOJO current=data.get(position);

            holder.name.setText(current.getName());
            holder.email.setText(current.getEmail());
            holder.phone.setText(current.getPhone());
            if(current.getImage()!=null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(current.getImage(), 0, current.getImage().length);
                holder.circleImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 60,
                        60, false));
            }






        }

        @Override
        public int getItemCount() {
            return (data.size());
        }

        class myviewholder extends RecyclerView.ViewHolder {


           private TextView name,email,phone;
           CircleImageView circleImageView;

            myviewholder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                email = view.findViewById(R.id.email);
                phone = view.findViewById(R.id.phone2);
               circleImageView = view.findViewById(R.id.pro_pic);





            }
        }
    }

}
